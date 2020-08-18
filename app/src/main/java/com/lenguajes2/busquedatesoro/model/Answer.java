package com.lenguajes2.busquedatesoro.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "answer")
public class Answer {
    @PrimaryKey
    public int id;
    @ColumnInfo(name = "question_id")
    public int questionId;
    public String answer;
    public Boolean isCorrect = false;
}
