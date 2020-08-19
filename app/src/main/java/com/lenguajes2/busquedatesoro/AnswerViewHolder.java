package com.lenguajes2.busquedatesoro;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.lenguajes2.busquedatesoro.model.Answer;

public class AnswerViewHolder extends RecyclerView.ViewHolder {

    private TextView tvAnswerItem;
    private CardView cardView;
    private AnswerItemClick answerItemClick;
    private AnswerAdapter answerAdapter;
    private Answer answer;

    public AnswerViewHolder(@NonNull View itemView, AnswerItemClick answerItemClick, AnswerAdapter answerAdapter) {
        super(itemView);

        this.answerItemClick = answerItemClick;
        this.answerAdapter = answerAdapter;

        tvAnswerItem = itemView.findViewById(R.id.tvAnswerItem);
        cardView = itemView.findViewById(R.id.cardViewContainer);

        itemClick();
    }

    public void bindData(Answer answer){
        this.answer = answer;
        tvAnswerItem.setText(answer.answer);
    }

    private void itemClick(){
        tvAnswerItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(answer.isCorrect){
                    cardView.setCardBackgroundColor(Color.GREEN);
                }else{
                    cardView.setCardBackgroundColor(Color.GRAY);
                }
                answerItemClick.answerClick(answerAdapter.getData().get(getLayoutPosition()));
            }
        });
    }
}
