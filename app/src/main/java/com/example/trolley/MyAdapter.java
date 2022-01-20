package com.example.trolley;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<ListItem> listItems;
    private Context context;


    public MyAdapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListItem listItem = listItems.get(position);

        holder.prodName.setText(listItem.getProdName());
        holder.prodQuantity.setText(listItem.getProdQuantity());
        holder.prodPrice.setText(listItem.getProdPrice());


    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView prodName, prodPrice, prodQuantity;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            prodName = (TextView) itemView.findViewById(R.id.prodName);
            prodPrice = (TextView) itemView.findViewById(R.id.prodPrice);
            prodQuantity = (TextView) itemView.findViewById(R.id.prodQuantity);


        }
    }
}
