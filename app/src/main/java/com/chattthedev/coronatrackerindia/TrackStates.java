package com.chattthedev.coronatrackerindia;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Process;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TrackStates extends AppCompatActivity {
    final static String url = "https://api.covidindiatracker.com/state_data.json";
    private LinearLayoutManager linearLayoutManager;
    private List<ItemModel> statelist;
    //private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private EditText editText;
    ItemViewHolder itemViewHolder;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_states);

        getSupportActionBar().setTitle("All States");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerview);
        editText = findViewById(R.id.searchStates);

        itemViewHolder = new ItemViewHolder(this, statelist);

        statelist = new ArrayList<>();
        itemViewHolder = new ItemViewHolder(getApplicationContext(), statelist);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(itemViewHolder);
        getData();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString().toLowerCase();
                List<ItemModel> temp = new ArrayList();
                for(ItemModel d: statelist){
                    if(d.getStates().toLowerCase().contains(text)){
                        temp.add(d);
                    }
                }
                itemViewHolder.updatelist(temp);




            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



    }

    private void getData() {
        progressBar = findViewById(R.id.spinkit);
        Sprite doubleBounce = new Wave();
        progressBar.setIndeterminateDrawable(doubleBounce);
        progressBar.setVisibility(View.VISIBLE);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        ItemModel itemModel = new ItemModel();
                        itemModel.setActive(jsonObject.getString("active"));
                        itemModel.setConfirmed(jsonObject.getString("confirmed"));
                        itemModel.setDeaths(jsonObject.getString("deaths"));
                        itemModel.setRecovered(jsonObject.getString("recovered"));
                        itemModel.setStates(jsonObject.getString("state"));
                        statelist.add(itemModel);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                    }
                }
                itemViewHolder.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                progressBar.setVisibility(View.GONE);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
}