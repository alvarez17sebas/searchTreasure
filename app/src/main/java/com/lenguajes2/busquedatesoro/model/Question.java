package com.lenguajes2.busquedatesoro.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "question")
public class Question {
    @PrimaryKey
    public int id;
    public String question;
    public Boolean displayed;
    public String level;

}
