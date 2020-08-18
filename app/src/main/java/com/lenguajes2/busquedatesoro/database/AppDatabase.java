package com.lenguajes2.busquedatesoro.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.lenguajes2.busquedatesoro.model.Answer;
import com.lenguajes2.busquedatesoro.model.Question;

@Database(entities = {Question.class, Answer.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract QuestionDao questionDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "question_database").allowMainThreadQueries().build();
        }
        return INSTANCE;
    }

}
