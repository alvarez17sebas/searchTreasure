package com.lenguajes2.busquedatesoro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lenguajes2.busquedatesoro.model.Answer;

import java.util.ArrayList;
import java.util.List;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerViewHolder> {
    private List<Answer> data = new ArrayList();

    @NonNull
    @Override
    public AnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_answer, parent, false);
        return new AnswerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerViewHolder holder, int position) {
        Answer answer =  data.get(position);
        holder.bindData(answer);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addAnswers(List<Answer> answersList){
        data = answersList;
    }
}
