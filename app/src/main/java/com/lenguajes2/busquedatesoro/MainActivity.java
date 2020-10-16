package com.lenguajes2.busquedatesoro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
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

        int randomPosition = (int) (Math.random() * 37);

        for(int i = 1; i < 37; i++){
            Boolean hasVictory = false;
            if(i == randomPosition){
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
        question.question = "Cuales son los tipos de cable de una red?";
        question.level = LevelQuestion.MEDIUM_LEVEL;
        question.displayed = false;

        Answer answer = new Answer();
        answer.id = 1;
        answer.questionId = 1;
        answer.answer = "FTP, Cable COAXIAL, Cable PAR TRENZADO";

        Answer answer2 = new Answer();
        answer2.id = 2;
        answer2.questionId = 1;
        answer2.answer = " STP, cable COAXIAL, FIBRA OPTICA";

        Answer answer3 = new Answer();
        answer3.id = 3;
        answer3.questionId = 1;
        answer3.answer = "Cable PAR TRENZADO, UTP, STP";

        Answer answer4 = new Answer();
        answer4.id = 4;
        answer4.questionId = 1;
        answer4.answer = "Cable COAXIAL, cable PAR TRENZADO, FIBRA ÓPTICA";
        answer4.isCorrect = true;

        questionDao.insertQuestion(question);

        questionDao.insertAnswer(answer);
        questionDao.insertAnswer(answer2);
        questionDao.insertAnswer(answer3);
        questionDao.insertAnswer(answer4);

        questionWithAnswersList = questionDao.getQuestions();

        //--------------------------------------

        Question question2 = new Question();
        question2.id = 2;
        question2.question = "CUALES SON LOS TIPOS DE CABLE PAR TRENZADO?";
        question2.level = LevelQuestion.EASY_LEVEL;
        question2.displayed = false;

        Answer answer5 = new Answer();
        answer5.id =5;
        answer5.questionId = 2;
        answer5.answer = "STP, UTP, FTP";
        answer5.isCorrect = true;

        Answer answer6 = new Answer();
        answer6.id = 6;
        answer6.questionId = 2;
        answer6.answer = "STP, COAXIAL, UTM";

        Answer answer7 = new Answer();
        answer7.id = 7;
        answer7.questionId = 2;
        answer7.answer = "UTM,FTO, TRE";

        Answer answer8 = new Answer();
        answer8.id = 8;
        answer8.questionId = 2;
        answer8.answer = "TRENZADO, UTM, UTP";

        questionDao.insertQuestion(question2);

        questionDao.insertAnswer(answer5);
        questionDao.insertAnswer(answer6);
        questionDao.insertAnswer(answer7);
        questionDao.insertAnswer(answer8);

        //------------------------------------

        Question question3 = new Question();
        question3.id = 3;
        question3.question = "QUE SIGNIFICAN MAN?";
        question3.level = LevelQuestion.EASY_LEVEL;
        question3.displayed = false;

        Answer answer9 = new Answer();
        answer9.id =9;
        answer9.questionId = 3;
        answer9.answer = " Las redes de área local (Local Area Network)";

        Answer answer10 = new Answer();
        answer10.id = 10;
        answer10.questionId = 3;
        answer10.answer = "Las redes de área metropolitana (Metropolitan Area Network)";
        answer10.isCorrect = true;

        Answer answer11 = new Answer();
        answer11.id = 11;
        answer11.questionId = 3;
        answer11.answer = "Las redes de área amplia (Wide Area Network)";

        Answer answer12 = new Answer();
        answer12.id = 12;
        answer12.questionId = 3;
        answer12.answer = "Se encarga de identificar el enrutamiento existente entre una o más redes";

        questionDao.insertQuestion(question3);

        questionDao.insertAnswer(answer9);
        questionDao.insertAnswer(answer10);
        questionDao.insertAnswer(answer11);
        questionDao.insertAnswer(answer12);

        //-------------------

        Question question4 = new Question();
        question4.id = 4;
        question4.question = "Como se clasifican las redes por su tamaño?";
        question4.level = LevelQuestion.MEDIUM_LEVEL;
        question4.displayed = false;

        Answer answer13 = new Answer();
        answer13.id =13;
        answer13.questionId = 4;
        answer13.answer = "WAN, LAM, MAN";
        answer13.isCorrect = true;

        Answer answer14 = new Answer();
        answer14.id = 14;
        answer14.questionId = 4;
        answer14.answer = "WAN, GRP, LAM";

        Answer answer15 = new Answer();
        answer15.id = 15;
        answer15.questionId = 4;
        answer15.answer = "MAN, OSPF, GRP";

        Answer answer16 = new Answer();
        answer16.id = 16;
        answer16.questionId = 4;
        answer16.answer = "LAM, WAN, STP";

        questionDao.insertQuestion(question4);
        questionDao.insertAnswer(answer13);
        questionDao.insertAnswer(answer14);
        questionDao.insertAnswer(answer15);
        questionDao.insertAnswer(answer16);

        //--------------------------------

        Question question5 = new Question();
        question5.id = 5;
        question5.question = "Como se clasifican las redes Según su topologia?";
        question5.level = LevelQuestion.HARD_LEVEL;
        question5.displayed = false;

        Answer answer17 = new Answer();
        answer17.id =17;
        answer17.questionId = 5;
        answer17.answer = "Anillo y estrella";

        Answer answer18 = new Answer();
        answer18.id = 18;
        answer18.questionId = 5;
        answer18.answer = "Bus y avión";

        Answer answer19 = new Answer();
        answer19.id = 19;
        answer19.questionId = 5;
        answer19.answer = "Anillo, bus y estrella";
        answer19.isCorrect = true;

        Answer answer20 = new Answer();
        answer20.id = 20;
        answer20.questionId = 5;
        answer20.answer = "Anillo , bus y triangulo";

        questionDao.insertQuestion(question5);
        questionDao.insertAnswer(answer17);
        questionDao.insertAnswer(answer18);
        questionDao.insertAnswer(answer19);
        questionDao.insertAnswer(answer20);

        //--------------------------------

        Question question6 = new Question();
        question6.id = 6;
        question6.question = "Como se clasifican las redes por el numero de ordenadores?";
        question6.level = LevelQuestion.MEDIUM_LEVEL;
        question6.displayed = false;

        Answer answer21 = new Answer();
        answer21.id =21;
        answer21.questionId = 6;
        answer21.answer = "Clase A, Clase B, Clase C";
        answer21.isCorrect = true;

        Answer answer22 = new Answer();
        answer22.id = 22;
        answer22.questionId = 6;
        answer22.answer = "Clase D, Clase E, Clase F";

        Answer answer23 = new Answer();
        answer23.id = 23;
        answer23.questionId = 6;
        answer23.answer = "Todas las anteriores";

        Answer answer24 = new Answer();
        answer24.id = 24;
        answer24.questionId = 6;
        answer24.answer = "Ninguna de las anteriores";

        questionDao.insertQuestion(question6);
        questionDao.insertAnswer(answer21);
        questionDao.insertAnswer(answer22);
        questionDao.insertAnswer(answer23);
        questionDao.insertAnswer(answer24);


        //-------------------------------

        Question question7 = new Question();
        question7.id = 7;
        question7.question = "Que significa LAN?";
        question7.level = LevelQuestion.MEDIUM_LEVEL;
        question7.displayed = false;

        Answer answer25 = new Answer();
        answer25.id =25;
        answer25.questionId = 7;
        answer25.answer = "Las redes de àrea local (Local area network)";
        answer25.isCorrect = true;

        Answer answer26 = new Answer();
        answer26.id = 26;
        answer26.questionId = 7;
        answer26.answer = "Tarjeta network interface card";

        Answer answer27 = new Answer();
        answer27.id = 27;
        answer27.questionId = 7;
        answer27.answer = "Las redes de área amplia (Wide Area Network)";

        Answer answer28 = new Answer();
        answer28.id = 28;
        answer28.questionId = 7;
        answer28.answer = "Las redes de área metropolitana (Metropolitan Area Network)";

        questionDao.insertQuestion(question7);
        questionDao.insertAnswer(answer25);
        questionDao.insertAnswer(answer26);
        questionDao.insertAnswer(answer27);
        questionDao.insertAnswer(answer28);

        //-------------------------------

        Question question8 = new Question();
        question8.id = 8;
        question8.question = "Cuántas capas tiene el modelo OSI?";
        question8.level = LevelQuestion.MEDIUM_LEVEL;
        question8.displayed = false;

        Answer answer29 = new Answer();//Check check check check
        answer29.id =29;
        answer29.questionId = 8;
        answer29.answer = "8";

        Answer answer30 = new Answer();
        answer30.id = 30;
        answer30.questionId = 8;
        answer30.answer = "6";

        Answer answer31 = new Answer();
        answer31.id = 31;
        answer31.questionId = 8;
        answer31.answer = "7";
        answer31.isCorrect = true;

        Answer answer32 = new Answer();
        answer32.id = 32;
        answer32.questionId = 8;
        answer32.answer = "9";

        questionDao.insertQuestion(question8);
        questionDao.insertAnswer(answer29);
        questionDao.insertAnswer(answer30);
        questionDao.insertAnswer(answer31);
        questionDao.insertAnswer(answer32);

        //-------------------------------

        Question question9 = new Question();
        question9.id = 9;
        question9.question = "Cuántas capas tiene el modelo TCP/IP?";
        question9.level = LevelQuestion.EASY_LEVEL;
        question9.displayed = false;

        Answer answer33 = new Answer();
        answer33.id =33;
        answer33.questionId = 9;
        answer33.answer = "5";

        Answer answer34 = new Answer();
        answer34.id = 34;
        answer34.questionId = 9;
        answer34.answer = "7";

        Answer answer35 = new Answer();
        answer35.id = 35;
        answer35.questionId = 9;
        answer35.answer = "4";
        answer35.isCorrect = true;

        Answer answer36 = new Answer();
        answer36.id = 36;
        answer36.questionId = 9;
        answer36.answer = "8";

        questionDao.insertQuestion(question9);
        questionDao.insertAnswer(answer33);
        questionDao.insertAnswer(answer34);
        questionDao.insertAnswer(answer35);
        questionDao.insertAnswer(answer36);

        //-----------------------

        Question question10 = new Question();
        question10.id = 10;
        question10.question = "¿CUAL ES EL PROTOCOLO QUE PERMITE DIRECCIONAR UNA RED?";
        question10.level = LevelQuestion.MEDIUM_LEVEL;
        question10.displayed = false;

        Answer answer37 = new Answer();
        answer37.id =37;
        answer37.questionId = 10;
        answer37.answer = "STP/UTP";

        Answer answer38 = new Answer();
        answer38.id = 38;
        answer38.questionId = 10;
        answer38.answer = "TCP/IP";
        answer38.isCorrect = true;

        Answer answer39 = new Answer();
        answer39.id = 39;
        answer39.questionId = 10;
        answer39.answer = "UTP/TCP";

        Answer answer40 = new Answer();
        answer40.id = 40;
        answer40.questionId = 10;
        answer40.answer = "Ninguna de las anteriores";

        questionDao.insertQuestion(question10);
        questionDao.insertAnswer(answer37);
        questionDao.insertAnswer(answer38);
        questionDao.insertAnswer(answer39);
        questionDao.insertAnswer(answer40);

        //-----------------------

        Question question11 = new Question();
        question11.id = 11;
        question11.question = "Qué formato tiene el direccionamiento lógico IP?";
        question11.level = LevelQuestion.MEDIUM_LEVEL;
        question11.displayed = false;

        Answer answer41 = new Answer();
        answer41.id =41;
        answer41.questionId = 11;
        answer41.answer = "32 bits en un bloque";

        Answer answer42 = new Answer();
        answer42.id = 42;
        answer42.questionId = 11;
        answer42.answer = "32 bits agrupados en 4 bloques de 1 byte";
        answer42.isCorrect = true;

        Answer answer43 = new Answer();
        answer43.id = 43;
        answer43.questionId = 11;
        answer43.answer = "48 bits agrupados en 6 bytes, expresados normalmente en hexadecimal";

        Answer answer44 = new Answer();
        answer44.id = 44;
        answer44.questionId = 11;
        answer44.answer = "48 bits agrupados en 6 bytes, expresados normalmente en binario o decimal";

        questionDao.insertQuestion(question11);
        questionDao.insertAnswer(answer41);
        questionDao.insertAnswer(answer42);
        questionDao.insertAnswer(answer43);
        questionDao.insertAnswer(answer44);

        //-----------------------

        Question question12 = new Question();
        question12.id = 12;
        question12.question = "Sobre las redes de computadores podemos decir";
        question12.level = LevelQuestion.HARD_LEVEL;
        question12.displayed = false;

        Answer answer45 = new Answer();
        answer45.id =45;
        answer45.questionId = 12;
        answer45.answer = " Según su ámbito, las redes se clasifican en LAN, MAN y WAN";

        Answer answer46 = new Answer();
        answer46.id = 46;
        answer46.questionId = 12;
        answer46.answer = "Según su tecnología las redes se clasifican en broadcast o punto a punto";

        Answer answer47 = new Answer();
        answer47.id = 47;
        answer47.questionId = 12;
        answer47.answer = "Casi todas las redes LAN son broadcast";

        Answer answer48 = new Answer();
        answer48.id = 48;
        answer48.questionId = 12;
        answer48.answer = "Todas las afirmaciones son correctas";
        answer48.isCorrect = true;

        questionDao.insertQuestion(question12);
        questionDao.insertAnswer(answer45);
        questionDao.insertAnswer(answer46);
        questionDao.insertAnswer(answer47);
        questionDao.insertAnswer(answer48);

        //-----------------------

        Question question13 = new Question();
        question13.id = 13;
        question13.question = "Sobre las arquitecturas de red (modelos de capas), podemos afirmar";
        question13.level = LevelQuestion.MEDIUM_LEVEL;
        question13.displayed = false;

        Answer answer49 = new Answer();
        answer49.id =49;
        answer49.questionId = 13;
        answer49.answer = "El modelo OSI de ISO funde los niveles de presentación, sesión y aplicación en un único nivel: el de aplicación";

        Answer answer50 = new Answer();
        answer50.id = 50;
        answer50.questionId = 13;
        answer50.answer = "El modelo OSI de ISO funde los niveles físico y de enlace de datos en un único llamado host-red";

        Answer answer51 = new Answer();
        answer51.id = 51;
        answer51.questionId = 13;
        answer51.answer = "El modelo híbrido parte el nivel de enlace de datos en dos subniveles, para las LAN: El MAC y el LLC";
        answer51.isCorrect = true;

        Answer answer52 = new Answer();
        answer52.id = 52;
        answer52.questionId = 13;
        answer52.answer = "El modelo TCP/IP divide la arquitectura en 7 capas";

        questionDao.insertQuestion(question13);
        questionDao.insertAnswer(answer49);
        questionDao.insertAnswer(answer50);
        questionDao.insertAnswer(answer51);
        questionDao.insertAnswer(answer52);

        //-----------------------

        Question question14 = new Question();
        question14.id = 14;
        question14.question = "Cuál es la PDU (Protocol Data Unit) del Nivel de Red?";
        question14.level = LevelQuestion.MEDIUM_LEVEL;
        question14.displayed = false;

        Answer answer53 = new Answer();
        answer53.id =53;
        answer53.questionId = 14;
        answer53.answer = "Segmento";

        Answer answer54 = new Answer();
        answer54.id = 54;
        answer54.questionId = 14;
        answer54.answer = "Paquete";
        answer54.isCorrect = true;

        Answer answer55 = new Answer();
        answer55.id = 55;
        answer55.questionId = 14;
        answer55.answer = "Datagrama";

        Answer answer56 = new Answer();
        answer56.id = 56;
        answer56.questionId = 14;
        answer56.answer = "Trama";

        questionDao.insertQuestion(question14);
        questionDao.insertAnswer(answer53);
        questionDao.insertAnswer(answer54);
        questionDao.insertAnswer(answer55);
        questionDao.insertAnswer(answer56);

        //-----------------------

        Question question15 = new Question();
        question15.id = 15;
        question15.question = "Los principales problemas que presentan los códigos de línea son la transmisión de contínua y la falta de sincronización. Sobre estos problemas podemos afirmar.";
        question15.level = LevelQuestion.MEDIUM_LEVEL;
        question15.displayed = false;

        Answer answer57 = new Answer();
        answer57.id =57;
        answer57.questionId = 15;
        answer57.answer = "Los códigos NRZ eliminan la continua";

        Answer answer58 = new Answer();
        answer58.id = 58;
        answer58.questionId = 15;
        answer58.answer = "Los códigos multinivel (AMI Bipolar y pseudos ternario) consiguen la sincronización";

        Answer answer59 = new Answer();
        answer59.id = 59;
        answer59.questionId = 15;
        answer59.answer = "El código MANCHESTER sincroniza.";
        answer59.isCorrect = true;

        Answer answer60 = new Answer();
        answer60.id = 60;
        answer60.questionId = 15;
        answer60.answer = "Las técnicas de inserción de bits (Bit Stuffing) ayudan a eliminar la componente continua";

        questionDao.insertQuestion(question15);
        questionDao.insertAnswer(answer57);
        questionDao.insertAnswer(answer58);
        questionDao.insertAnswer(answer59);
        questionDao.insertAnswer(answer60);

        //-----------------------

        Question question16 = new Question();
        question16.id = 16;
        question16.question = "Aplicando el teorema de Muestreo de Nyquist, ¿a qué frecuencia mínima debemos muestrear una señal para poder reconstruirla en recepción?";
        question16.level = LevelQuestion.EASY_LEVEL;
        question16.displayed = false;

        Answer answer61 = new Answer();
        answer61.id = 61;
        answer61.questionId = 16;
        answer61.answer = "Al doble del ancho de banda de la señal";
        answer61.isCorrect = true;

        Answer answer62 = new Answer();
        answer62.id = 62;
        answer62.questionId = 16;
        answer62.answer = "A la misma que el ancho de banda de la señal";

        Answer answer63 = new Answer();
        answer63.id = 63;
        answer63.questionId = 16;
        answer63.answer = " Depende de si la señal se transmite en banda base o modulada";

        Answer answer64 = new Answer();
        answer64.id = 64;
        answer64.questionId = 16;
        answer64.answer = "A la mitad del ancho de banda de la señal";

        questionDao.insertQuestion(question16);
        questionDao.insertAnswer(answer61);
        questionDao.insertAnswer(answer62);
        questionDao.insertAnswer(answer63);
        questionDao.insertAnswer(answer64);

        //-----------------------

        Question question17 = new Question();
        question17.id = 17;
        question17.question = "¿Cuándo utilizaremos cable directo (Conectores RJ45 tipo A- tipo A) para conectar dos dispositivos?";
        question17.level = LevelQuestion.MEDIUM_LEVEL;
        question17.displayed = false;

        Answer answer65 = new Answer();
        answer65.id = 65;
        answer65.questionId = 17;
        answer65.answer = "a. Para unir un Switch con un Router";

        Answer answer66 = new Answer();
        answer66.id = 66;
        answer66.questionId = 17;
        answer66.answer = "b. Para unir un PC con un Switch";

        Answer answer67 = new Answer();
        answer67.id = 67;
        answer67.questionId = 17;
        answer67.answer = "c. Para unir un PC con un Router";

        Answer answer68 = new Answer();
        answer68.id = 68;
        answer68.questionId = 17;
        answer68.answer = "d. a y b son correctas";
        answer68.isCorrect = true;

        questionDao.insertQuestion(question17);
        questionDao.insertAnswer(answer65);
        questionDao.insertAnswer(answer66);
        questionDao.insertAnswer(answer67);
        questionDao.insertAnswer(answer68);

        //-----------------------

        Question question18 = new Question();
        question18.id = 18;
        question18.question = "Respecto a la telefonía digital PDH podemos afirmar";
        question18.level = LevelQuestion.MEDIUM_LEVEL;
        question18.displayed = false;

        Answer answer69 = new Answer();
        answer69.id = 69;
        answer69.questionId = 18;
        answer69.answer = "a. Agrupa canales utilizando técnicas de multiplexación en frecuencia FDM";

        Answer answer70 = new Answer();
        answer70.id = 70;
        answer70.questionId = 18;
        answer70.answer = "b. Las jerarquías americana, europea y japonesa son compatibles";

        Answer answer71 = new Answer();
        answer71.id = 71;
        answer71.questionId = 18;
        answer71.answer = "c. El nivel de Jerarquía T1 presenta 22 canales se datos multiplexados";

        Answer answer72 = new Answer();
        answer72.id = 72;
        answer72.questionId = 18;
        answer72.answer = "d. El nivel de Jerarquía E1 presenta 30 canales de datos multiplexados";

        questionDao.insertQuestion(question18);
        questionDao.insertAnswer(answer69);
        questionDao.insertAnswer(answer70);
        questionDao.insertAnswer(answer71);
        questionDao.insertAnswer(answer72);

        //-----------------------

        Question question19 = new Question();
        question19.id = 19;
        question19.question = "Cuál de las siguientes afirmaciones sobre los modems es FALSA?";
        question19.level = LevelQuestion.MEDIUM_LEVEL;
        question19.displayed = false;

        Answer answer73 = new Answer();
        answer73.id = 73;
        answer73.questionId = 19;
        answer73.answer = "a. La velocidad de bajada siempre suele ser menor que la de subida";
        answer63.isCorrect = true;

        Answer answer74 = new Answer();
        answer74.id = 74;
        answer74.questionId = 19;
        answer74.answer = "b. Para transmitir datos con modems es necesario deshabilitar los supresores de eco    con tonos especiales o utilizar canceladores de eco en lugar de supresores.";

        Answer answer75 = new Answer();
        answer75.id = 75;
        answer75.questionId = 19;
        answer75.answer = "c. Las técnicas de compresión consiguen aumentar la velocidad de transmisión";

        Answer answer76 = new Answer();
        answer76.id = 76;
        answer76.questionId = 19;
        answer76.answer = "d. La configuración de los modems se realiza en modo local mediante comandos HAYES";

        questionDao.insertQuestion(question19);
        questionDao.insertAnswer(answer73);
        questionDao.insertAnswer(answer74);
        questionDao.insertAnswer(answer75);
        questionDao.insertAnswer(answer76);

        //-----------------------

        Question question20 = new Question();
        question20.id = 20;
        question20.question = "Cuáles de las siguientes funciones de la Capa de Enlace son opcionales";
        question20.level = LevelQuestion.MEDIUM_LEVEL;
        question20.displayed = false;

        Answer answer77 = new Answer();
        answer77.id = 77;
        answer77.questionId = 20;
        answer77.answer = "a. Corrección de errores";

        Answer answer78 = new Answer();
        answer78.id = 78;
        answer78.questionId = 20;
        answer78.answer = "b. Control de Flujo";

        Answer answer79 = new Answer();
        answer79.id = 79;
        answer79.questionId = 20;
        answer79.answer = "c. Entramado";

        Answer answer80 = new Answer();
        answer80.id = 80;
        answer80.questionId = 20;
        answer80.answer = "d.  a y b";
        answer80.isCorrect = true;

        questionDao.insertQuestion(question20);
        questionDao.insertAnswer(answer77);
        questionDao.insertAnswer(answer78);
        questionDao.insertAnswer(answer79);
        questionDao.insertAnswer(answer80);

        //-----------------------

        Question question21 = new Question();
        question21.id = 21;
        question21.question = "Cuál de las siguientes afirmaciones sobre el Protocolo de Enlace de Datos PPP es FALSA?";
        question21.level = LevelQuestion.MEDIUM_LEVEL;
        question21.displayed = false;

        Answer answer81 = new Answer();
        answer81.id = 81;
        answer81.questionId = 21;
        answer81.answer = "a. Sólo trabaja con IP";
        answer81.isCorrect = true;

        Answer answer82 = new Answer();
        answer82.id = 82;
        answer82.questionId = 21;
        answer82.answer = "b. PPP consta de varios protocolos, definiendo una arquitectura de tres niveles";

        Answer answer83 = new Answer();
        answer83.id = 83;
        answer83.questionId = 21;
        answer83.answer = "c. Su estructura de tramas sigue la de las tramas HDLC";

        Answer answer84 = new Answer();
        answer84.id = 84;
        answer84.questionId = 21;
        answer84.answer = "d. Soporta la compresión de cabeceras";

        questionDao.insertQuestion(question21);
        questionDao.insertAnswer(answer81);
        questionDao.insertAnswer(answer82);
        questionDao.insertAnswer(answer83);
        questionDao.insertAnswer(answer84);

        //-----------------------

        Question question22 = new Question();
        question22.id = 22;
        question22.question = "Sobre los mecanismos de autenticidad PAP y CHAP de PPP podemos afirmar";
        question22.level = LevelQuestion.MEDIUM_LEVEL;
        question22.displayed = false;

        Answer answer85 = new Answer();
        answer85.id = 85;
        answer85.questionId = 22;
        answer85.answer = "a. El login y el password, con CHAP viajan por la red";

        Answer answer86 = new Answer();
        answer86.id = 86;
        answer86.questionId = 22;
        answer86.answer = "b. CHAP no constituye un método fuerte o rígido";

        Answer answer87 = new Answer();
        answer87.id = 87;
        answer87.questionId = 22;
        answer87.answer = "c. CHAP ofrece características como la verificación periódica para mejorar la seguridad";
        answer87.isCorrect = true;

        Answer answer88 = new Answer();
        answer88.id = 88;
        answer88.questionId = 22;
        answer88.answer = "d. PAP no permite al que realiza la llamada, intentar la autenticación sin un desafío o reto (challenge) previo";

        questionDao.insertQuestion(question22);
        questionDao.insertAnswer(answer85);
        questionDao.insertAnswer(answer86);
        questionDao.insertAnswer(answer87);
        questionDao.insertAnswer(answer88);

        //-----------------------

        Question question23 = new Question();
        question23.id = 23;
        question23.question = "¿Cuál de las siguientes afirmaciones sobre el control de flujo es FALSA";
        question23.level = LevelQuestion.MEDIUM_LEVEL;
        question23.displayed = false;

        Answer answer89 = new Answer();
        answer89.id = 89;
        answer89.questionId = 23;
        answer89.answer = "a. El control de flujo controla los errores de la línea de transmisión";
        answer89.isCorrect = true;

        Answer answer90 = new Answer();
        answer90.id = 90;
        answer90.questionId = 23;
        answer90.answer = "b. Lo utiliza el receptor para indicarle al emisor cuándo está preparado para recibir y procesar tramas";

        Answer answer91 = new Answer();
        answer91.id = 91;
        answer91.questionId = 23;
        answer91.answer = "c. Utiliza diferentes mecanismos de retroalimentación para mandar señales de control de flujo, y por tanto requiere un canal semi-duplex o full-duplex.";

        Answer answer92 = new Answer();
        answer92.id = 92;
        answer92.questionId = 23;
        answer92.answer = "d. El control de flujo con ACK permite controlar al transmisor, de forma que si no se le reconocen las tramas enviadas, éste espera hasta que se le reconozcan.";

        questionDao.insertQuestion(question23);
        questionDao.insertAnswer(answer89);
        questionDao.insertAnswer(answer90);
        questionDao.insertAnswer(answer91);
        questionDao.insertAnswer(answer92);

        //-----------------------

        Question question24 = new Question();
        question24.id = 24;
        question24.question = "La norma de IEEE que estandariza las redes inalámbricas es:";
        question24.level = LevelQuestion.HARD_LEVEL;
        question24.displayed = false;

        Answer answer93 = new Answer();
        answer93.id = 93;
        answer93.questionId = 24;
        answer93.answer = "a. IEEE 802.3";

        Answer answer94 = new Answer();
        answer94.id = 94;
        answer94.questionId = 24;
        answer94.answer = "b. IEEE 802.4";

        Answer answer95 = new Answer();
        answer95.id = 95;
        answer95.questionId = 24;
        answer95.answer = "c. IEEE 802.5";

        Answer answer96 = new Answer();
        answer96.id = 96;
        answer96.questionId = 24;
        answer96.answer = "d. IEEE 802.11";
        answer96.isCorrect = true;

        questionDao.insertQuestion(question24);
        questionDao.insertAnswer(answer93);
        questionDao.insertAnswer(answer94);
        questionDao.insertAnswer(answer95);
        questionDao.insertAnswer(answer96);

        //-----------------------

        Question question25 = new Question();
        question24.id = 25;
        question24.question = "¿Cuál es una de las características principales del protocolo IEEE 802.2 LLC?";
        question24.level = LevelQuestion.MEDIUM_LEVEL;
        question24.displayed = false;

        Answer answer97 = new Answer();
        answer93.id = 97;
        answer93.questionId = 25;
        answer93.answer = "a. Es común para todos los subniveles MAC de IEEE 802";
        answer97.isCorrect = true;

        Answer answer98 = new Answer();
        answer94.id = 98;
        answer94.questionId = 25;
        answer94.answer = "b. Es el protocolo MAC de acceso al medio que siguen las redes Ethernet DIX";

        Answer answer99 = new Answer();
        answer95.id = 99;
        answer95.questionId = 25;
        answer95.answer = "c. Es el protocolo MAC de acceso al medio que siguen las redes Token Ring";

        Answer answer100 = new Answer();
        answer96.id = 100;
        answer96.questionId = 25;
        answer96.answer = "d. Es el protocolo MAC de acceso al medio que siguen las redes FDDI";

        questionDao.insertQuestion(question25);
        questionDao.insertAnswer(answer97);
        questionDao.insertAnswer(answer98);
        questionDao.insertAnswer(answer99);
        questionDao.insertAnswer(answer100);

        //-----------------------

        Question question26 = new Question();
        question24.id = 26;
        question24.question = "Cómo se consigue la capacidad de 1Gbps con los cables UTP cat5e en redes Ethernet?";
        question24.level = LevelQuestion.MEDIUM_LEVEL;
        question24.displayed = false;

        Answer answer101 = new Answer();
        answer101.id = 101;
        answer101.questionId = 26;
        answer101.answer = "a. Se reparte el tráfico entre los cuatro pares (250 Mb/s cada uno)";

        Answer answer102 = new Answer();
        answer102.id = 102;
        answer102.questionId = 26;
        answer102.answer = "b. Se emplean circuitos híbridos para conseguir transmisión simultánea por cada par en cada sentido.";

        Answer answer103 = new Answer();
        answer103.id = 103;
        answer103.questionId = 26;
        answer103.answer = "c. Se usa una codificación multinivel PAM5x5";

        Answer answer104 = new Answer();
        answer104.id = 104;
        answer104.questionId = 26;
        answer104.answer = "d. Todas las respuestas anteriores son correctas";
        answer104.isCorrect = true;

        questionDao.insertQuestion(question26);
        questionDao.insertAnswer(answer101);
        questionDao.insertAnswer(answer102);
        questionDao.insertAnswer(answer103);
        questionDao.insertAnswer(answer104);

        //-----------------------

        Question question27 = new Question();
        question27.id = 27;
        question27.question = "Sobre el protocolo Ethernet 802.3 podemos afirmar:";
        question27.level = LevelQuestion.EASY_LEVEL;
        question27.displayed = false;

        Answer answer105 = new Answer();
        answer105.id = 105;
        answer105.questionId = 27;
        answer105.answer = "a. Protocolo sin contención (sin colisiones)";

        Answer answer106 = new Answer();
        answer106.id = 106;
        answer106.questionId = 27;
        answer106.answer = "b. Cable coaxial, UTP-3, UTP-5, UTP-5e y F. O";
        answer106.isCorrect = true;

        Answer answer107 = new Answer();
        answer107.id = 107;
        answer107.questionId = 27;
        answer107.answer = "c. Velocidades: 4, 16 Mbps";

        Answer answer108 = new Answer();
        answer108.id = 108;
        answer108.questionId = 27;
        answer108.answer = "d. Codificación: Manchester Diferencial";

        questionDao.insertQuestion(question27);
        questionDao.insertAnswer(answer105);
        questionDao.insertAnswer(answer106);
        questionDao.insertAnswer(answer107);
        questionDao.insertAnswer(answer108);


        //-----------------------

        Question question28 = new Question();
        question28.id = 28;
        question28.question = "¿Cuál de las siguientes afirmaciones sobre Ethernet y Fast Ethernet es FALSA?";
        question28.level = LevelQuestion.MEDIUM_LEVEL;
        question28.displayed = false;

        Answer answer109 = new Answer();
        answer109.id = 109;
        answer109.questionId = 28;
        answer109.answer = "a. Ambas utilizan únicamente coaxial como medio de transmisión";
        answer109.isCorrect = true;

        Answer answer110 = new Answer();
        answer110.id = 110;
        answer110.questionId = 28;
        answer110.answer = "b. El estandar fija el tamaño mínimo de trama para que pueda detectar la colisión";

        Answer answer111 = new Answer();
        answer111.id = 111;
        answer111.questionId = 28;
        answer111.answer = "c. El tamaño mínimo de tramas es en ambos casos 64 bytes";

        Answer answer112 = new Answer();
        answer112.id = 112;
        answer112.questionId = 28;
        answer112.answer = "d. El tiempo de transmisión de la trama mínima es en Ethernet 51,2 µs";

        questionDao.insertQuestion(question28);
        questionDao.insertAnswer(answer109);
        questionDao.insertAnswer(answer110);
        questionDao.insertAnswer(answer111);
        questionDao.insertAnswer(answer112);

        //-----------------------

        Question question29 = new Question();
        question29.id = 29;
        question29.question = "La señal de un par de cables que crea una interferencia sobre la señal de otro par, se llama";
        question29.level = LevelQuestion.MEDIUM_LEVEL;
        question29.displayed = false;

        Answer answer113 = new Answer();
        answer113.id = 113;
        answer113.questionId = 29;
        answer113.answer = "a. NEXT.";

        Answer answer114 = new Answer();
        answer114.id = 114;
        answer114.questionId = 29;
        answer114.answer = "b. Cross Talk";
        answer114.isCorrect = true;

        Answer answer115 = new Answer();
        answer115.id = 115;
        answer115.questionId = 29;
        answer115.answer = "c. Perdidas de retorno o RL (Return Loss).";

        Answer answer116 = new Answer();
        answer116.id = 116;
        answer116.questionId = 29;
        answer116.answer = "d. Atenuación";

        questionDao.insertQuestion(question29);
        questionDao.insertAnswer(answer113);
        questionDao.insertAnswer(answer114);
        questionDao.insertAnswer(answer115);
        questionDao.insertAnswer(answer116);

        //-----------------------

        Question question30 = new Question();
        question30.id = 30;
        question30.question = "Los tipos de conectores que se utilizaron en las prácticas para los servicios POTS Y y RDSI fueron:";
        question30.level = LevelQuestion.MEDIUM_LEVEL;
        question30.displayed = false;

        Answer answer117 = new Answer();
        answer117.id = 117;
        answer117.questionId = 30;
        answer117.answer = "a. RJ10  y RJ45 respectivamente";

        Answer answer118 = new Answer();
        answer118.id = 118;
        answer118.questionId = 30;
        answer118.answer = "b. V.35  y RJ45 respectivamente";

        Answer answer119 = new Answer();
        answer119.id = 119;
        answer119.questionId = 30;
        answer119.answer = "c. RJ11 y V.42 respectivamente";

        Answer answer120 = new Answer();
        answer120.id = 120;
        answer120.questionId = 30;
        answer120.answer = "d. RJ11 y RJ45 respectivamente";
        answer120.isCorrect = true;

        questionDao.insertQuestion(question30);
        questionDao.insertAnswer(answer117);
        questionDao.insertAnswer(answer118);
        questionDao.insertAnswer(answer119);
        questionDao.insertAnswer(answer120);

        //-----------------------

        Question question31 = new Question();
        question24.id = 31;
        question24.question = "Para conectar un PC a un MODEM:";
        question24.level = LevelQuestion.MEDIUM_LEVEL;
        question24.displayed = false;

        Answer answer121 = new Answer();
        answer121.id = 121;
        answer121.questionId = 31;
        answer121.answer = "a. El DCE es el PC y el DTE es el MODEM";

        Answer answer122 = new Answer();
        answer122.id = 122;
        answer122.questionId = 31;
        answer122.answer = "b. El DTE es el PC y el DCE es el MODEM";
        answer122.isCorrect = true;

        Answer answer123 = new Answer();
        answer123.id = 123;
        answer123.questionId = 31;
        answer123.answer = "c. Ambos son DTE";

        Answer answer124 = new Answer();
        answer124.id = 124;
        answer124.questionId = 31;
        answer124.answer = "d. El DTA es el MODEM y el DCA es el PC";

        questionDao.insertQuestion(question31);
        questionDao.insertAnswer(answer121);
        questionDao.insertAnswer(answer122);
        questionDao.insertAnswer(answer123);
        questionDao.insertAnswer(answer124);

        //-----------------------

        Question question32 = new Question();
        question32.id = 32;
        question32.question = "Las capas del modelo OSI que gestiona un router, switch y hub son";
        question32.level = LevelQuestion.MEDIUM_LEVEL;
        question32.displayed = false;

        Answer answer125 = new Answer();
        answer125.id = 125;
        answer125.questionId = 32;
        answer125.answer = "a.  1)Router= capa 1-2 ;    2)Switch= capa 1-3      ; 3)Hub= capa 2";

        Answer answer126 = new Answer();
        answer126.id = 126;
        answer126.questionId = 32;
        answer126.answer = "b.  1)Router= capa 1 ;       2)Switch= capa 1          ; 3)Hub= capa 1 ";

        Answer answer127 = new Answer();
        answer127.id = 127;
        answer127.questionId = 32;
        answer127.answer = "c.  1)Router= capa 1-3 ;    2)Switch= capa 1-2      ; 3)Hub= capa 1";
        answer127.isCorrect = true;

        Answer answer128 = new Answer();
        answer128.id = 128;
        answer128.questionId = 32;
        answer128.answer = "d.  1)Router= capa 3 ;      2)Switch= capa 2           ; 3)Hub= capa 1";

        questionDao.insertQuestion(question32);
        questionDao.insertAnswer(answer125);
        questionDao.insertAnswer(answer126);
        questionDao.insertAnswer(answer127);
        questionDao.insertAnswer(answer128);

        //-----------------------

        Question question33 = new Question();
        question33.id = 33;
        question33.question = "protocolo que operan en la capa de aplicación del modelo OSI";
        question33.level = LevelQuestion.MEDIUM_LEVEL;
        question33.displayed = false;

        Answer answer129 = new Answer();
        answer129.id = 129;
        answer129.questionId = 33;
        answer129.answer = "a. DHCP,FTP,UDP";

        Answer answer130 = new Answer();
        answer130.id = 130;
        answer130.questionId = 33;
        answer130.answer = "b. TCP,ARP,UDP";

        Answer answer131 = new Answer();
        answer131.id = 131;
        answer131.questionId = 33;
        answer131.answer = "c. POP3,DHCP,FTP";
        answer131.isCorrect = true;

        Answer answer132 = new Answer();
        answer132.id = 132;
        answer132.questionId = 33;
        answer132.answer = "d. TCP,ARP,FTP";

        questionDao.insertQuestion(question33);
        questionDao.insertAnswer(answer129);
        questionDao.insertAnswer(answer130);
        questionDao.insertAnswer(answer131);
        questionDao.insertAnswer(answer132);

        //-----------------------

        Question question34 = new Question();
        question34.id = 34;
        question34.question = "En la provisión de conexión del nivel de transporte al nivel de sesión del modelo OSI";
        question34.level = LevelQuestion.MEDIUM_LEVEL;
        question34.displayed = false;

        Answer answer133 = new Answer();
        answer133.id = 133;
        answer133.questionId = 34;
        answer133.answer = "a. Es posible el soporte simultáneo de varias conexiones de sesión por una sola conexión de transporte";

        Answer answer134 = new Answer();
        answer134.id = 134;
        answer134.questionId = 34;
        answer134.answer = "b. Una conexión de sesión no puede ser soportada por varias conexiones de transporte secuencialmente";

        Answer answer135 = new Answer();
        answer135.id = 135;
        answer135.questionId = 34;
        answer135.answer = "c. No es posible el soporte secuencial de varias conexiones de sesión por una de transporte";

        Answer answer136 = new Answer();
        answer136.id = 136;
        answer136.questionId = 34;
        answer136.answer = "d. Una sola conexión de sesión puede ser soportada por varias conexiones de transporte secuencialmente";
        answer136.isCorrect = true;

        questionDao.insertQuestion(question34);
        questionDao.insertAnswer(answer133);
        questionDao.insertAnswer(answer134);
        questionDao.insertAnswer(answer135);
        questionDao.insertAnswer(answer136);

        //-----------------------

        Question question35 = new Question();
        question35.id = 35;
        question35.question = "En un desplazamiento cíclico a la izquierda de un bit, el contenido de {100101} se modifica en";
        question35.level = LevelQuestion.MEDIUM_LEVEL;
        question35.displayed = false;

        Answer answer137 = new Answer();
        answer137.id = 137;
        answer137.questionId = 35;
        answer137.answer = "a) 110010";

        Answer answer138 = new Answer();
        answer138.id = 138;
        answer138.questionId = 35;
        answer138.answer = "b) 1010.";

        Answer answer139 = new Answer();
        answer139.id = 139;
        answer139.questionId = 35;
        answer139.answer = "c) 110011";

        Answer answer140 = new Answer();
        answer140.id = 140;
        answer140.questionId = 35;
        answer140.answer = "d) 1011";
        answer140.isCorrect = true;

        questionDao.insertQuestion(question35);
        questionDao.insertAnswer(answer137);
        questionDao.insertAnswer(answer138);
        questionDao.insertAnswer(answer139);
        questionDao.insertAnswer(answer140);


        //-----------------------

        Question question36 = new Question();
        question36.id = 36;
        question36.question = "Dentro del modelo OSI la función de 'compatibilización de ficheros de distintos formatos', corresponde al nivel";
        question36.level = LevelQuestion.MEDIUM_LEVEL;
        question36.displayed = false;

        Answer answer141 = new Answer();
        answer141.id = 141;
        answer141.questionId = 36;
        answer141.answer = "a) Nivel de red";

        Answer answer142 = new Answer();
        answer142.id = 142;
        answer142.questionId = 36;
        answer142.answer = "b) Nivel de transporte";

        Answer answer143 = new Answer();
        answer143.id = 143;
        answer143.questionId = 36;
        answer143.answer = "c) Nivel de sesión";

        Answer answer144 = new Answer();
        answer144.id = 144;
        answer144.questionId = 36;
        answer144.answer = "d) Nivel de presentación";
        answer144.isCorrect = true;

        questionDao.insertQuestion(question36);
        questionDao.insertAnswer(answer141);
        questionDao.insertAnswer(answer142);
        questionDao.insertAnswer(answer143);
        questionDao.insertAnswer(answer144);

        //-----------------------

        Question question37 = new Question();
        question37.id = 37;
        question37.question = "En qué nivel OSI actúan los repetidores?";
        question37.level = LevelQuestion.MEDIUM_LEVEL;
        question37.displayed = false;

        Answer answer145 = new Answer();
        answer145.id = 145;
        answer145.questionId = 37;
        answer145.answer = "a) Enlace";

        Answer answer146 = new Answer();
        answer146.id = 146;
        answer146.questionId = 37;
        answer146.answer = "b) Red";

        Answer answer147 = new Answer();
        answer147.id = 147;
        answer147.questionId = 37;
        answer147.answer = "c) Transporte";

        Answer answer148 = new Answer();
        answer148.id = 148;
        answer148.questionId = 37;
        answer148.answer = "d) Físico";
        answer148.isCorrect = true;

        questionDao.insertQuestion(question37);
        questionDao.insertAnswer(answer145);
        questionDao.insertAnswer(answer146);
        questionDao.insertAnswer(answer147);
        questionDao.insertAnswer(answer148);

        //-----------------------

        Question question38 = new Question();
        question38.id = 38;
        question38.question = "La capa de Red del modelo OSI equivale a la capa siguiente del modelo TCP";
        question38.level = LevelQuestion.EASY_LEVEL;
        question38.displayed = false;

        Answer answer149 = new Answer();
        answer149.id = 149;
        answer149.questionId = 38;
        answer149.answer = "a) Internet";
        answer149.isCorrect = true;

        Answer answer150 = new Answer();
        answer150.id = 150;
        answer150.questionId = 38;
        answer150.answer = "b) Application";

        Answer answer151 = new Answer();
        answer151.id = 151;
        answer151.questionId = 38;
        answer151.answer = "c) Red";

        Answer answer152 = new Answer();
        answer152.id = 152;
        answer152.questionId = 38;
        answer152.answer = "d) Data Link";

        questionDao.insertQuestion(question38);
        questionDao.insertAnswer(answer149);
        questionDao.insertAnswer(answer150);
        questionDao.insertAnswer(answer151);
        questionDao.insertAnswer(answer152);

        //-----------------------

        Question question39 = new Question();
        question39.id = 39;
        question39.question = "La unidad de datos intercambiada en la capa de transporte, según el modelo OSI de ISO, es";
        question39.level = LevelQuestion.MEDIUM_LEVEL;
        question39.displayed = false;

        Answer answer153 = new Answer();
        answer153.id = 153;
        answer153.questionId = 39;
        answer153.answer = "a) TPDU.";
        answer153.isCorrect = true;

        Answer answer154 = new Answer();
        answer154.id = 154;
        answer154.questionId = 39;
        answer154.answer = "b) Paquete";

        Answer answer155 = new Answer();
        answer155.id = 155;
        answer155.questionId = 39;
        answer155.answer = "c) Trama";

        Answer answer156 = new Answer();
        answer156.id = 156;
        answer156.questionId = 39;
        answer156.answer = "d) TCDU";

        questionDao.insertQuestion(question39);
        questionDao.insertAnswer(answer153);
        questionDao.insertAnswer(answer154);
        questionDao.insertAnswer(answer155);
        questionDao.insertAnswer(answer156);

        //-----------------------
        //--

        Question question40 = new Question();
        question40.id = 40;
        question40.question = "La dirección IP 192.168.42.127";
        question40.level = LevelQuestion.HARD_LEVEL;
        question40.displayed = false;

        Answer answer157 = new Answer();
        answer157.id = 157;
        answer157.questionId = 40;
        answer157.answer = "a) Es una dirección de la red Internet";

        Answer answer158 = new Answer();
        answer158.id = 158;
        answer158.questionId = 40;
        answer158.answer = "b) Es una dirección de la red Internet, clase C";

        Answer answer159 = new Answer();
        answer159.id = 159;
        answer159.questionId = 40;
        answer159.answer = "c) Es una dirección de una red IP privada";
        answer159.isCorrect = true;

        Answer answer160 = new Answer();
        answer160.id = 160;
        answer160.questionId = 40;
        answer160.answer = "d) Es una dirección de la red Internet, clase B";

        questionDao.insertQuestion(question40);
        questionDao.insertAnswer(answer157);
        questionDao.insertAnswer(answer158);
        questionDao.insertAnswer(answer159);
        questionDao.insertAnswer(answer160);


        //-----------------------

        Question question41 = new Question();
        question41.id = 41;
        question41.question = "Las unidades de información entre redes contienen una o más cabeceras que se usan para:";
        question41.level = LevelQuestion.MEDIUM_LEVEL;
        question41.displayed = false;

        Answer answer161 = new Answer();
        answer161.id = 161;
        answer161.questionId = 41;
        answer161.answer = "a) Transportar datos a la aplicación software receptora";

        Answer answer162 = new Answer();
        answer162.id = 162;
        answer162.questionId = 41;
        answer162.answer = "b) Evitar que los datos transmitidos por una estación interfieran con los de otra";

        Answer answer163 = new Answer();
        answer163.id = 163;
        answer163.questionId = 41;
        answer163.answer = "c) Asegurar un acceso ordenado al medio físico";

        Answer answer164 = new Answer();
        answer164.id = 164;
        answer164.questionId = 41;
        answer164.answer = "d) Pasar información de control a los niveles OSI (o equivalentes) en el sistema de destino";
        answer164.isCorrect = true;

        questionDao.insertQuestion(question41);
        questionDao.insertAnswer(answer161);
        questionDao.insertAnswer(answer162);
        questionDao.insertAnswer(answer163);
        questionDao.insertAnswer(answer164);

        //-----------------------

        Question question42 = new Question();
        question42.id = 42;
        question42.question = "En el modelo de referencia OSI, una interfaz:";
        question42.level = LevelQuestion.MEDIUM_LEVEL;
        question42.displayed = false;

        Answer answer165 = new Answer();
        answer165.id = 165;
        answer165.questionId = 42;
        answer165.answer = "a) Define las primitivas y los servicios que una capa ofrece a todas las que están sobre ella";

        Answer answer166 = new Answer();
        answer166.id = 166;
        answer166.questionId = 42;
        answer166.answer = "b) Define los servicios y las primitivas que una capa ofrece a la inmediatamente superior";
        answer166.isCorrect = true;

        Answer answer167 = new Answer();
        answer167.id = 167;
        answer167.questionId = 42;
        answer167.answer = "c) Define la funcionalidad final que el sistema ofrece al usuario";

        Answer answer168 = new Answer();
        answer168.id = 168;
        answer168.questionId = 42;
        answer168.answer = "d) Define las reglas y convenios para que dos procesos en diferentes máquinas comuniquen entre sí";

        questionDao.insertQuestion(question42);
        questionDao.insertAnswer(answer165);
        questionDao.insertAnswer(answer166);
        questionDao.insertAnswer(answer167);
        questionDao.insertAnswer(answer168);

        //-----------------------
        // Review D option

        Question question43 = new Question();
        question43.id = 43;
        question43.question = "A qué nivel del modelo OSI se realiza el encapsulamiento Frame Relay y HDLC?";
        question43.level = LevelQuestion.MEDIUM_LEVEL;
        question43.displayed = false;

        Answer answer169 = new Answer();
        answer169.id = 169;
        answer169.questionId = 43;
        answer169.answer = "a) Red";

        Answer answer170 = new Answer();
        answer170.id = 170;
        answer170.questionId = 43;
        answer170.answer = "b) Sesión";

        Answer answer171 = new Answer();
        answer171.id = 171;
        answer171.questionId = 43;
        answer171.answer = "c) Enlace de datos";
        answer171.isCorrect = true;

        Answer answer172 = new Answer();
        answer172.id = 172;
        answer172.questionId = 43;
        answer172.answer = "d) Transporte";

        questionDao.insertQuestion(question43);
        questionDao.insertAnswer(answer169);
        questionDao.insertAnswer(answer170);
        questionDao.insertAnswer(answer171);
        questionDao.insertAnswer(answer172);

        //-----------------------
        //Review A option

        Question question44 = new Question();
        question44.id = 44;
        question44.question = "El router es un dispositivo que se utiliza en la interconexión de redes y opera según el modelo OSI en el nivel de";
        question44.level = LevelQuestion.MEDIUM_LEVEL;
        question44.displayed = false;

        Answer answer173 = new Answer();
        answer173.id = 173;
        answer173.questionId = 44;
        answer173.answer = "a) Transporte";

        Answer answer174 = new Answer();
        answer174.id = 172;
        answer174.questionId = 44;
        answer174.answer = "b) Red";
        answer174.isCorrect = true;

        Answer answer175 = new Answer();
        answer175.id = 175;
        answer175.questionId = 44;
        answer175.answer = "c) Físico";

        Answer answer176 = new Answer();
        answer176.id = 176;
        answer176.questionId = 44;
        answer176.answer = "d) Enlace";

        questionDao.insertQuestion(question44);
        questionDao.insertAnswer(answer173);
        questionDao.insertAnswer(answer174);
        questionDao.insertAnswer(answer175);
        questionDao.insertAnswer(answer176);

        //-----------------------

        Question question45 = new Question();
        question45.id = 45;
        question45.question = "Cuál de los siguientes dispositivos es el que trabaja como máximo a nivel 3 - Red - del sistema OSI?";
        question45.level = LevelQuestion.MEDIUM_LEVEL;
        question45.displayed = false;

        Answer answer177 = new Answer();
        answer177.id = 177;
        answer177.questionId = 45;
        answer177.answer = "a) Repetidores";

        Answer answer178 = new Answer();
        answer178.id = 178;
        answer178.questionId = 45;
        answer178.answer = "b) Puentes (Bridges)";

        Answer answer179 = new Answer();
        answer179.id = 179;
        answer179.questionId = 45;
        answer179.answer = "c) Enrutadores (routers)";
        answer179.isCorrect = true;

        Answer answer180 = new Answer();
        answer180.id = 180;
        answer180.questionId = 45;
        answer180.answer = "d) Pasarelas (Gateways)";

        questionDao.insertQuestion(question45);
        questionDao.insertAnswer(answer177);
        questionDao.insertAnswer(answer178);
        questionDao.insertAnswer(answer179);
        questionDao.insertAnswer(answer180);

        //-----------------------

        Question question46 = new Question();
        question46.id = 46;
        question46.question = "Dentro del modelo OSI la función de 'proporcionar testigos para que las dos partes de un protocolo no utilicen la misma operación al mismo tiempo', corresponde al nivel";
        question46.level = LevelQuestion.MEDIUM_LEVEL;
        question46.displayed = false;

        Answer answer181 = new Answer();
        answer181.id = 181;
        answer181.questionId = 46;
        answer181.answer = "a) Presentación";

        Answer answer182 = new Answer();
        answer182.id = 182;
        answer182.questionId = 46;
        answer182.answer = "b) Sesión";
        answer182.isCorrect = true;

        Answer answer183 = new Answer();
        answer183.id = 183;
        answer183.questionId = 46;
        answer183.answer = "c) Transporte";

        Answer answer184 = new Answer();
        answer184.id = 184;
        answer184.questionId = 46;
        answer184.answer = "d) Red";

        questionDao.insertQuestion(question46);
        questionDao.insertAnswer(answer181);
        questionDao.insertAnswer(answer182);
        questionDao.insertAnswer(answer183);
        questionDao.insertAnswer(answer184);

        //-----------------------

        Question question47 = new Question();
        question47.id = 47;
        question47.question = "Las direcciones de tipo B en IPv4";
        question47.level = LevelQuestion.MEDIUM_LEVEL;
        question47.displayed = false;

        Answer answer185 = new Answer();
        answer185.id = 185;
        answer185.questionId = 47;
        answer185.answer = "a) Comienzan por '110'";

        Answer answer186 = new Answer();
        answer186.id = 186;
        answer186.questionId = 47;
        answer186.answer = "b) incluyen entre ellas a la dirección 193.168.25.73";

        Answer answer187 = new Answer();
        answer187.id = 187;
        answer187.questionId = 47;
        answer187.answer = "c) destina 16 bits para los sistemas";
        answer187.isCorrect = true;

        Answer answer188 = new Answer();
        answer188.id = 188;
        answer188.questionId = 47;
        answer188.answer = "d) es utilizada para direcciones multicast";

        questionDao.insertQuestion(question47);
        questionDao.insertAnswer(answer185);
        questionDao.insertAnswer(answer186);
        questionDao.insertAnswer(answer187);
        questionDao.insertAnswer(answer188);

        //-----------------------

        Question question48 = new Question();
        question48.id = 48;
        question48.question = "El protocolo de comunicaciones denominado HDLC, es un protocolo orientado a";
        question48.level = LevelQuestion.MEDIUM_LEVEL;
        question48.displayed = false;

        Answer answer189 = new Answer();
        answer189.id = 189;
        answer189.questionId = 48;
        answer189.answer = "a) Carácter, de modos: ARM, ABM y SRM";

        Answer answer190 = new Answer();
        answer190.id = 190;
        answer190.questionId = 48;
        answer190.answer = "b) Bit , de modos: ARM, ABM y SRM";
        answer190.isCorrect = true;

        Answer answer191 = new Answer();
        answer191.id = 191;
        answer191.questionId = 48;
        answer191.answer = "c) Carácter, con cadencia secuencial";

        Answer answer192 = new Answer();
        answer192.id = 192;
        answer192.questionId = 48;
        answer192.answer = "d) Bit, con cadencia secuencial";

        questionDao.insertQuestion(question48);
        questionDao.insertAnswer(answer189);
        questionDao.insertAnswer(answer190);
        questionDao.insertAnswer(answer191);
        questionDao.insertAnswer(answer192);

        //-----------------------

        Question question49 = new Question();
        question49.id = 49;
        question49.question = " A qué nivel de OSI es equivalente el protocolo IP (Internet Protocol)?:";
        question49.level = LevelQuestion.MEDIUM_LEVEL;
        question49.displayed = false;

        Answer answer193 = new Answer();
        answer193.id = 193;
        answer193.questionId = 49;
        answer193.answer = "a) Enlace";

        Answer answer194 = new Answer();
        answer194.id = 194;
        answer194.questionId = 49;
        answer194.answer = "b) Red";
        answer194.isCorrect = true;

        Answer answer195 = new Answer();
        answer195.id = 195;
        answer195.questionId = 49;
        answer195.answer = "c) Transporte";

        Answer answer196 = new Answer();
        answer196.id = 196;
        answer196.questionId = 49;
        answer196.answer = "d) Comunicación";

        questionDao.insertQuestion(question49);
        questionDao.insertAnswer(answer193);
        questionDao.insertAnswer(answer194);
        questionDao.insertAnswer(answer195);
        questionDao.insertAnswer(answer196);

        //-----------------------
        //Review

        Question question50 = new Question();
        question50.id = 50;
        question50.question = "Cuál de las siguientes asociaciones de protocolos y niveles OSI es correcta?";
        question50.level = LevelQuestion.MEDIUM_LEVEL;
        question50.displayed = false;

        Answer answer197 = new Answer();
        answer197.id = 197;
        answer197.questionId = 50;
        answer197.answer = "a) Nivel 7 - MPLS";

        Answer answer198 = new Answer();
        answer198.id = 198;
        answer198.questionId = 50;
        answer198.answer = "b) Nivel 3 - RARP";

        Answer answer199 = new Answer();
        answer199.id = 183;
        answer199.questionId = 50;
        answer199.answer = "c) Nivel 2 - HDLC";
        answer199.isCorrect = true;

        Answer answer200 = new Answer();
        answer200.id = 200;
        answer200.questionId = 50;
        answer200.answer = "d) Nivel 1 – CSMA/CD";

        questionDao.insertQuestion(question50);
        questionDao.insertAnswer(answer197);
        questionDao.insertAnswer(answer198);
        questionDao.insertAnswer(answer199);
        questionDao.insertAnswer(answer200);
    }

    private int randomID(){
        int numero = 1 + (int) (Math.random() * questionWithAnswersList.size() - 1);
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

                questionWithAnswersList =  new ArrayList<>();
                questionWithAnswersList = questionDao.getQuestions();
                setupMatrixItemsRecyclerView();
            }
        });
    }
}