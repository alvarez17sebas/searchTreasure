package com.lenguajes2.busquedatesoro.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class QuestionWithAnswers {
    @Embedded public Question question;

    @Relation(parentColumn = "id",
              entityColumn = "question_id")
    public List<Answer> answers;

}
