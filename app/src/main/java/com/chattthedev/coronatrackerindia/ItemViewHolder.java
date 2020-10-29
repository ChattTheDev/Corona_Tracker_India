package com.chattthedev.coronatrackerindia;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ItemViewHolder extends RecyclerView.Adapter<ItemViewHolder.ViewHolder> {

    private Context context;
    private List<ItemModel> list;

    public ItemViewHolder(Context context, List<ItemModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.singlelayout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemModel itemModel = list.get(position);
        holder.acase.setText(itemModel.getActive());
        holder.reccase.setText(itemModel.getRecovered());
        holder.dcase.setText(itemModel.getDeaths());
        holder.ccase.setText(itemModel.getConfirmed());
        holder.snames.setText(itemModel.getStates());

    }



    @Override
    public int getItemCount() {
        return list.size();
    }
    public void updatelist(List<ItemModel> models){

        list = new ArrayList<>();
        list.addAll(models);
        notifyDataSetChanged();
    }




    public Filter getFilter(){
        return new Filter() {
            private List<ItemModel> filtered = new ArrayList<ItemModel>();
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString().toLowerCase();

                filtered.clear();
                if(charString.isEmpty()){
                    filtered = list;
                    //filteredCUG = CUG;
                }
                else{
                    for (ItemModel cug : list){
                        if( cug.getStates().toLowerCase().contains(charString)){
                            filtered.add(cug);
                        }
                    }
                    //filteredCUG = filtered;
                }
                FilterResults filterResults = new FilterResults();

                filterResults.values = filtered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list = (List<ItemModel>) results.values;
                notifyDataSetChanged();

            }
        };
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView acase, reccase, dcase, ccase, snames;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            acase = itemView.findViewById(R.id.acases);
            reccase = itemView.findViewById(R.id.reccase);
            dcase = itemView.findViewById(R.id.dcases);
            ccase = itemView.findViewById(R.id.ccases);
            snames = itemView.findViewById(R.id.sname);
        }
    }


//    public TextView acase, reccase, dcase, ccase, snames;
//    public ItemViewHolder(@NonNull View itemView) {
//        super(itemView);
//
//        acase = itemView.findViewById(R.id.acases);
//        reccase = itemView.findViewById(R.id.reccase);
//        dcase = itemView.findViewById(R.id.dcases);
//        ccase = itemView.findViewById(R.id.ccases);
//        snames = itemView.findViewById(R.id.sname);
//    }
}
