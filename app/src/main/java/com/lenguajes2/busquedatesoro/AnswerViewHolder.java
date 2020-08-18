package com.lenguajes2.busquedatesoro;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lenguajes2.busquedatesoro.model.Answer;

public class AnswerViewHolder extends RecyclerView.ViewHolder {
    private TextView tvAnswerItem;
    public AnswerViewHolder(@NonNull View itemView) {
        super(itemView);

        tvAnswerItem = itemView.findViewById(R.id.tvAnswerItem);
    }

    public void bindData(Answer answer){
        tvAnswerItem.setText(answer.answer);
    }
}
