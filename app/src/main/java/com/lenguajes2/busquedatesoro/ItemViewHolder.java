package com.lenguajes2.busquedatesoro;

import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lenguajes2.busquedatesoro.model.MatrixItem;

public class ItemViewHolder extends RecyclerView.ViewHolder {

    private Button button;
    private MatrixItemClick itemClick;
    private ItemAdapter itemAdapter;

    public ItemViewHolder(@NonNull View itemView, MatrixItemClick itemClick, ItemAdapter itemAdapter) {
        super(itemView);
        this.itemClick = itemClick;
        this.itemAdapter = itemAdapter;
        button = itemView.findViewById(R.id.matrixButton);

        clickListener();
    }

    public void bindData(MatrixItem item){
        button.setText(item.titleItem);
    }

    private void clickListener(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MatrixItem item = itemAdapter.getData().get(getLayoutPosition());
                itemClick.clickItem(item);
            }
        });
    }


}
