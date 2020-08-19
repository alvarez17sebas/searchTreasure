package com.lenguajes2.busquedatesoro.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.lenguajes2.busquedatesoro.model.Answer;
import com.lenguajes2.busquedatesoro.model.Question;
import com.lenguajes2.busquedatesoro.model.QuestionWithAnswers;

import java.util.List;

@Dao
public interface QuestionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertQuestion(Question question);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAnswer(Answer answer);

    @Update
    void updateQuestion(Question question);

    @Transaction
    @Query("SELECT * FROM question")
    List<QuestionWithAnswers> getQuestions();
}
