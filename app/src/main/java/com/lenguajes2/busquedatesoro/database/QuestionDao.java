package com.lenguajes2.busquedatesoro.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.lenguajes2.busquedatesoro.model.Answer;
import com.lenguajes2.busquedatesoro.model.Question;
import com.lenguajes2.busquedatesoro.model.QuestionWithAnswers;

@Dao
public interface QuestionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertQuestion(Question question);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAnswer(Answer answer);

    @Transaction
    @Query("SELECT * FROM question WHERE id = :id")
    QuestionWithAnswers getQuestion(int id);
}
