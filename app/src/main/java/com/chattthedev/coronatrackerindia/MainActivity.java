package com.chattthedev.coronatrackerindia;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.WanderingCubes;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    final static String URL = "https://api.covidindiatracker.com/total.json";
    PieChart chart;
    TextView activeCases, recoveredCases, deaths, confirmedCases;
    Button viewByStates;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chart = findViewById(R.id.piechart);
        activeCases = findViewById(R.id.activecases);
        recoveredCases = findViewById(R.id.recovered);
        deaths = findViewById(R.id.deaths);
        confirmedCases = findViewById(R.id.confirmedcases);
        viewByStates = findViewById(R.id.viewByStates);

        new BackgroundTask().execute();

        viewByStates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TrackStates.class);
                startActivity(intent);
            }
        });
    }

    public void getAllStatus() {
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar = findViewById(R.id.mainspin);
                Sprite doubleBounce = new WanderingCubes();
                progressBar.setIndeterminateDrawable(doubleBounce);
                progressBar.setVisibility(View.VISIBLE);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.toString());
                    activeCases.setText(jsonObject.getString("active"));
                    recoveredCases.setText(jsonObject.getString("recovered"));
                    deaths.setText(jsonObject.getString("deaths"));
                    confirmedCases.setText(jsonObject.getString("confirmed"));

                    chart.addPieSlice(new PieModel("Active Cases", Integer.parseInt(activeCases.getText().toString()), Color.parseColor("#FF9800")));
                    chart.addPieSlice(new PieModel("Confirmed Cases", Integer.parseInt(confirmedCases.getText().toString()), Color.parseColor("#670424")));
                    chart.addPieSlice(new PieModel("Recovered", Integer.parseInt(recoveredCases.getText().toString()), Color.parseColor("#1AAA00")));
                    chart.addPieSlice(new PieModel("Deaths", Integer.parseInt(deaths.getText().toString()), Color.parseColor("#FF0101")));
                    chart.startAnimation();
                    progressBar.setVisibility(View.GONE);


                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }

    public class BackgroundTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            getAllStatus();
            return null;
        }
    }
}