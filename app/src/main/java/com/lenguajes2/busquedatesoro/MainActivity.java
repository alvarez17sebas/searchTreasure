package com.lenguajes2.busquedatesoro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.lenguajes2.busquedatesoro.database.AppDatabase;
import com.lenguajes2.busquedatesoro.database.QuestionDao;
import com.lenguajes2.busquedatesoro.model.Answer;
import com.lenguajes2.busquedatesoro.model.MatrixItem;
import com.lenguajes2.busquedatesoro.model.Question;
import com.lenguajes2.busquedatesoro.model.QuestionWithAnswers;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MatrixItemClick {

    private RecyclerView matrixItemsRecyclerView;
    private ItemAdapter itemAdapter;

    private RecyclerView answersRecyclerView;
    private AnswerAdapter answerAdapter;

    private TextView tvQuestion;

    private AppDatabase appDatabase;
    private QuestionDao questionDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initVariables();
        initComponents();
        fillMatrixItemsAdapter();
        setupMatrixItemsRecyclerView();

        populateLocalDataBase();
         fillAnswerAdapter();
        setupAnswersRecyclerView();

    }

    private void initVariables(){

        appDatabase  = AppDatabase.getDatabase(getApplicationContext());
        questionDao = appDatabase.questionDao();

        itemAdapter = new ItemAdapter(this);
        answerAdapter = new AnswerAdapter();
    }

    private void initComponents(){
        matrixItemsRecyclerView = findViewById(R.id.rvMatrixItems);
        answersRecyclerView = findViewById(R.id.rvAnswers);
        tvQuestion = findViewById(R.id.tvQuestion);
    }

    private void setupMatrixItemsRecyclerView(){
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 6);
        matrixItemsRecyclerView.setLayoutManager(layoutManager);
        matrixItemsRecyclerView.setAdapter(itemAdapter);
    }

    private void setupAnswersRecyclerView(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        answersRecyclerView.setLayoutManager(layoutManager);
        answersRecyclerView.setAdapter(answerAdapter);
    }

    private void fillMatrixItemsAdapter(){
        List<MatrixItem> itemsList = new ArrayList<>();

        for(int i = 1; i < 37; i++){
            Boolean hasVictory = false;
            if(i == 15){
                hasVictory = true;
            }

            String titleItem = "Item # " + i;
            MatrixItem item = new MatrixItem();
            item.titleItem = titleItem;
            item.hasVictory = hasVictory;
            itemsList.add(item);
        }

        itemAdapter.addItems(itemsList);
    }

    private void fillAnswerAdapter(){
        QuestionWithAnswers questionWithAnswers = questionDao.getQuestion(1);
        Question question = questionWithAnswers.question;
        tvQuestion.setText(question.question);

        List<Answer> answers = questionWithAnswers.answers;
        answerAdapter.addAnswers(answers);
    }

    private void populateLocalDataBase(){

        Question question = new Question();
        question.id = 1;
        question.question = "Cuantos paises hay en el mundo?";
        question.level = "Intermedia";
        question.displayed = false;

        Answer answer = new Answer();
        answer.id = 1;
        answer.questionId = 1;
        answer.answer = "200";

        Answer answer2 = new Answer();
        answer2.id = 2;
        answer2.questionId = 1;
        answer2.answer = "20";

        Answer answer3 = new Answer();
        answer3.id = 3;
        answer3.questionId = 1;
        answer3.answer = "30";

        Answer answer4 = new Answer();
        answer4.id = 4;
        answer4.questionId = 1;
        answer4.answer = "194";
        answer4.isCorrect = true;

        questionDao.insertQuestion(question);

        questionDao.insertAnswer(answer);
        questionDao.insertAnswer(answer2);
        questionDao.insertAnswer(answer3);
        questionDao.insertAnswer(answer4);
    }



    @Override
    public void clickItem(MatrixItem item) {

        if (item.hasVictory == true) {
            Toast.makeText(this, "Click True", Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(this, "Click False", Toast.LENGTH_SHORT).show();
        }
    }
}