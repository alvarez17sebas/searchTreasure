package com.lenguajes2.busquedatesoro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.lenguajes2.busquedatesoro.database.AppDatabase;
import com.lenguajes2.busquedatesoro.database.QuestionDao;
import com.lenguajes2.busquedatesoro.model.Answer;
import com.lenguajes2.busquedatesoro.model.MatrixItem;
import com.lenguajes2.busquedatesoro.model.Question;
import com.lenguajes2.busquedatesoro.model.QuestionWithAnswers;
import com.lenguajes2.busquedatesoro.model.Score;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MatrixItemClick, AnswerItemClick {

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    private RecyclerView matrixItemsRecyclerView;
    private ItemAdapter itemAdapter;

    private RecyclerView answersRecyclerView;
    private AnswerAdapter answerAdapter;

    private TextView tvQuestion;
    private TextView tvScore;
    private Button btnStartGame;
    private View view;

    private AppDatabase appDatabase;
    private QuestionDao questionDao;

    private List<QuestionWithAnswers> questionWithAnswersList = new ArrayList<>();
    private List<Question> questionList = new ArrayList<>();

    private Chronometer chronometer;
    private Boolean running = false;

    private String dificulty = "";
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        initVariables();
        initComponents();
        fillMatrixItemsAdapter();
        setupMatrixItemsRecyclerView();

        populateLocalDataBase();

        questionWithAnswersList = questionDao.getQuestions();

        setupAnswersRecyclerView();

        startGameButtonClick();

        matrixItemsRecyclerView.setVisibility(View.INVISIBLE);
    }

    private void initVariables(){

        appDatabase  = AppDatabase.getDatabase(getApplicationContext());
        questionDao = appDatabase.questionDao();

        itemAdapter = new ItemAdapter(this);
        answerAdapter = new AnswerAdapter(this);
    }

    private void initComponents(){
        view = findViewById(R.id.view);
        chronometer = findViewById(R.id.chronometer);
        matrixItemsRecyclerView = findViewById(R.id.rvMatrixItems);
        answersRecyclerView = findViewById(R.id.rvAnswers);
        tvQuestion = findViewById(R.id.tvQuestion);
        tvScore = findViewById(R.id.tvScore);
        btnStartGame = findViewById(R.id.btnStartGame);
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

         answerAdapter = new AnswerAdapter(this);
         setupAnswersRecyclerView();

        if(questionWithAnswersList.size() > 0) {

            Question question = selectRandomQuestion();

            tvQuestion.setText(question.question);
            List<Answer> answers = question.answers;
            answerAdapter.addAnswers(answers);
        }else{
            tvQuestion.setText("No existen mas preguntas para mostrar");
            tvQuestion.setTextColor(Color.RED);
        }
    }

    private Question selectRandomQuestion(){

        int randomQuestionPosition;
        Question question;

        randomQuestionPosition = randomID();
        QuestionWithAnswers questionWithAnswers = questionWithAnswersList.get(randomQuestionPosition);
        question = questionWithAnswers.question;
        question.answers = questionWithAnswers.answers;
        dificulty = question.level;

        questionWithAnswersList.remove(randomQuestionPosition);

        return question;
    }

    private void populateLocalDataBase(){

        Question question = new Question();
        question.id = 1;
        question.question = "Cuantos paises hay en el mundo?";
        question.level = LevelQuestion.MEDIUM_LEVEL;
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

        //--------------------------------------

        Question question2 = new Question();
        question2.id = 2;
        question2.question = "Cuanto es 2 + 2 ?";
        question2.level = LevelQuestion.EASY_LEVEL;
        question2.displayed = false;

        Answer answer5 = new Answer();
        answer5.id =5;
        answer5.questionId = 2;
        answer5.answer = "4";
        answer5.isCorrect = true;

        Answer answer6 = new Answer();
        answer6.id = 6;
        answer6.questionId = 2;
        answer6.answer = "5";

        Answer answer7 = new Answer();
        answer7.id = 7;
        answer7.questionId = 2;
        answer7.answer = "30";

        Answer answer8 = new Answer();
        answer8.id = 8;
        answer8.questionId = 2;
        answer8.answer = "10";

        questionDao.insertQuestion(question2);

        questionDao.insertAnswer(answer5);
        questionDao.insertAnswer(answer6);
        questionDao.insertAnswer(answer7);
        questionDao.insertAnswer(answer8);

        //------------------------------------

        Question question3 = new Question();
        question3.id = 3;
        question3.question = "Capital de colombia?";
        question3.level = LevelQuestion.EASY_LEVEL;
        question3.displayed = false;

        Answer answer9 = new Answer();
        answer9.id =9;
        answer9.questionId = 3;
        answer9.answer = "Medellin";

        Answer answer10 = new Answer();
        answer10.id = 10;
        answer10.questionId = 3;
        answer10.answer = "Bogota";
        answer10.isCorrect = true;

        Answer answer11 = new Answer();
        answer11.id = 11;
        answer11.questionId = 3;
        answer11.answer = "Barranquilla";

        Answer answer12 = new Answer();
        answer12.id = 12;
        answer12.questionId = 3;
        answer12.answer = "Cartagena";

        questionDao.insertQuestion(question3);

        questionDao.insertAnswer(answer9);
        questionDao.insertAnswer(answer10);
        questionDao.insertAnswer(answer11);
        questionDao.insertAnswer(answer12);
    }

    private int randomID(){
        int numero = (int) (Math.random() * questionWithAnswersList.size());
        return numero;
    }



    @Override
    public void clickItem(MatrixItem item) {

        if (item.hasVictory == true) {
            pauseChronometer();
            TypedValue typedValue = new TypedValue();
            getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
            int color = typedValue.data;
            btnStartGame.setBackgroundColor(color);
            btnStartGame.setEnabled(true);
            btnStartGame.setText("Iniciar nuevamente juego");

            Score score = new Score();
            score.score = Integer.parseInt(tvScore.getText().toString());
            score.time = chronometer.getText().toString();

            firestore.collection("scores").add(score);

            tvQuestion.setText("JUEGO TERMINADO !!!");
            tvQuestion.setTextColor(Color.BLUE);

            answerAdapter.cleanData();
        } else{
            fillAnswerAdapter();
        }

        view.setVisibility(View.INVISIBLE);
    }

    @Override
    public void answerClick(Answer answer) {
        if(answer.isCorrect){
            switch (dificulty){
                case LevelQuestion.EASY_LEVEL:
                    score = score + ScoreQuestion.SCORE_EASY;
                    break;
                case LevelQuestion.MEDIUM_LEVEL:
                    score = score + ScoreQuestion.SCORE_MEDIUM;
                    break;
                case LevelQuestion.HARD_LEVEL:
                    score = score + ScoreQuestion.SCORE_HARD;
                    break;
            }
            tvScore.setText(String.valueOf(score));
        }else{
            Toast.makeText(this, "Respuesta incorrecta", Toast.LENGTH_SHORT).show();

        }

        view.setVisibility(View.VISIBLE);
    }


    public void startChronometer(){
        if(!running){
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
            running = true;
        }
    }

    public void pauseChronometer(){
        if(running){
            chronometer.stop();
            running = false;
        }
    }

    public  void startGameButtonClick(){
        btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemAdapter.cleanData();
                fillMatrixItemsAdapter();
                tvScore.setText("0");
                matrixItemsRecyclerView.setVisibility(View.VISIBLE);
                btnStartGame.setBackgroundColor(Color.GREEN);
                btnStartGame.setText("Juego iniciado");
                tvQuestion.setText("Preguntas");
                tvQuestion.setTextColor(Color.BLACK);
                btnStartGame.setEnabled(false);
                startChronometer();
            }
        });
    }
}