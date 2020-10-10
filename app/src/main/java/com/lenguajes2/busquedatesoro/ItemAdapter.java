package com.lenguajes2.busquedatesoro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lenguajes2.busquedatesoro.model.MatrixItem;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {

    private List<MatrixItem> data = new ArrayList();

    private MatrixItemClick itemClick;

    public ItemAdapter(MatrixItemClick itemClick){
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_matrix_button, parent, false);
        return new ItemViewHolder(view, itemClick, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        MatrixItem item = data.get(position);
        holder.bindData(item);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addItems(List<MatrixItem> itemsList){
        data = itemsList;
    }

    public List<MatrixItem> getData(){
        return data;
    }

    public void cleanData(){
        data.clear();
        notifyDataSetChanged();
    }
}
