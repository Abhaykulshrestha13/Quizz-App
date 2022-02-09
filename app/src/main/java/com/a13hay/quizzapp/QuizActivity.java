package com.a13hay.quizzapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androchef.happytimer.countdowntimer.CircularCountDownView;
import com.androchef.happytimer.countdowntimer.HappyTimer;
import com.github.sshadkany.CircleButton;
import com.github.sshadkany.shapes.CircleView;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class QuizActivity extends AppCompatActivity {
    private QuestionsViewModel questionsViewModelob;
    List<Questions> list;
    Questions currentQuestion;
    Button btnOption1, btnOption2, btnOption3, btnOption4, btnNo, btnYes;
    CircleButton btnNext, btnPrevious;
    TextView txtQuestion, txtNumberQ, txtResult;
    CircularCountDownView countDown;
    CardView btnSubmit;
    CircleView countDown_cv;
    Dialog dialog;
    int qid = 0, submitted = 0;
    HashMap<Integer, Integer> selectedO = new HashMap<>();
    HashMap<Integer, Boolean> correctO = new HashMap<>();
    int DARK_GREEN = 0xFF245E00;
    int RED = 0xFF730000;
    int GREEN = 0xFF8BC34A;
    LinearLayout resultLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        countDown = findViewById(R.id.circularCountDownView);
        btnOption1 = findViewById(R.id.option_1_btn);
        btnOption2 = findViewById(R.id.option_2_btn);
        btnOption3 = findViewById(R.id.option_3_btn);
        btnOption4 = findViewById(R.id.option_4_btn);
        txtQuestion = findViewById(R.id.question_txt);
        txtNumberQ = findViewById(R.id.numberQ_txt);
        btnNext = findViewById(R.id.next_button);
        btnPrevious = findViewById(R.id.previous_button);
        btnSubmit = findViewById(R.id.submit_btn);
        resultLL = findViewById(R.id.result_ll);
        countDown_cv = findViewById(R.id.circleView);
        txtResult = findViewById(R.id.result_txt);

        for (int i = 0; i < 10; i++) {
            correctO.put(i, false);
        }

        dialog = new Dialog(QuizActivity.this);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setCancelable(false);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                btnNo = dialog.findViewById(R.id.no_btn);
                btnYes = dialog.findViewById(R.id.yes_btn);

                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.hide();
                    }
                });
                btnYes.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(View v) {
                        dialog.hide();
                        afterSubmission();
                    }
                });
            }
        });

        if (qid == 0) {
            btnPrevious.setVisibility(View.GONE);
        }

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qid++;
                if (qid == 9) {
                    btnNext.setVisibility(View.GONE);
                }
                if (qid < list.size()) {
                    btnPrevious.setVisibility(View.VISIBLE);
                    currentQuestion = list.get(qid);
                    updateQueAnsOptions();
                } else {
                    btnNext.setVisibility(View.GONE);
                    qid = list.size();
                    Toast.makeText(QuizActivity.this, "Last", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qid = qid - 1;
                if (qid == 0) {
                    btnPrevious.setVisibility(View.GONE);
                }
                if (qid >= 0) {
                    btnNext.setVisibility(View.VISIBLE);
                    currentQuestion = list.get(qid);
                    updateQueAnsOptions();
                } else {
                    qid = 0;
                    btnPrevious.setVisibility(View.GONE);
                    Toast.makeText(QuizActivity.this, "First", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnOption1.setOnClickListener(v -> {
            int id = currentQuestion.getId();
            selectedO.put(id, 1);

            if (currentQuestion.getAnswer().equals("1")) {
                correctO.put(id, true);
            } else {
                correctO.put(id, false);
            }

            btnOption1.setTextColor(Color.BLACK);
            btnOption2.setTextColor(Color.WHITE);
            btnOption3.setTextColor(Color.WHITE);
            btnOption4.setTextColor(Color.WHITE);
            btnOption1.setBackgroundColor(Color.YELLOW);
            btnOption2.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
            btnOption3.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
            btnOption4.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
        });
        btnOption2.setOnClickListener(v -> {
            int id = currentQuestion.getId();
            selectedO.put(id, 2);

            if (currentQuestion.getAnswer().equals("2")) {
                correctO.put(id, true);
            } else {
                correctO.put(id, false);
            }

            btnOption2.setTextColor(Color.BLACK);
            btnOption1.setTextColor(Color.WHITE);
            btnOption3.setTextColor(Color.WHITE);
            btnOption4.setTextColor(Color.WHITE);
            btnOption2.setBackgroundColor(Color.YELLOW);
            btnOption1.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
            btnOption3.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
            btnOption4.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));

        });
        btnOption3.setOnClickListener(v -> {
            int id = currentQuestion.getId();
            selectedO.put(id, 3);
            if (currentQuestion.getAnswer().equals("3")) {
                correctO.put(id, true);
            } else {
                correctO.put(id, false);
            }
            btnOption3.setTextColor(Color.BLACK);
            btnOption2.setTextColor(Color.WHITE);
            btnOption1.setTextColor(Color.WHITE);
            btnOption4.setTextColor(Color.WHITE);
            btnOption3.setBackgroundColor(Color.YELLOW);
            btnOption2.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
            btnOption1.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
            btnOption4.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));

        });
        btnOption4.setOnClickListener(v -> {
            int id = currentQuestion.getId();
            selectedO.put(id, 4);
            if (currentQuestion.getAnswer().equals("4")) {
                correctO.put(id, true);
            } else {
                correctO.put(id, false);
            }
            btnOption4.setTextColor(Color.BLACK);
            btnOption2.setTextColor(Color.WHITE);
            btnOption3.setTextColor(Color.WHITE);
            btnOption1.setTextColor(Color.WHITE);
            btnOption4.setBackgroundColor(Color.YELLOW);
            btnOption2.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
            btnOption3.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
            btnOption1.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
        });


        countDown.initTimer(600);
        countDown.startTimer();

        countDown.setOnTickListener(new HappyTimer.OnTickListener() {
            @Override
            public void onTick(int i, int i1) {

            }

            @Override
            public void onTimeUp() {
                afterSubmission();
                Toast.makeText(QuizActivity.this, "Time's over", Toast.LENGTH_LONG).show();
            }
        });

        questionsViewModelob = ViewModelProviders.of(this).get(QuestionsViewModel.class);
        questionsViewModelob.getAllQuestions().observe(this, this::fetchQuestions);
    }

    private void fetchQuestions(List<Questions> questions) {
        list = questions;
        Collections.shuffle(list);
        currentQuestion = list.get(qid);
        updateQueAnsOptions();
    }

    private void afterSubmission() {
        submitted = 1;
        btnSubmit.setVisibility(View.GONE);
        countDown_cv.setVisibility(View.GONE);
        resultLL.setVisibility(View.VISIBLE);
        countDown.stopTimer();
        int sum = 0;
        for (int id = 0; id < correctO.size(); id++) {
            if(correctO.get(id)){
                sum = sum + 1;
            }
        }
        txtResult.setText("Number of correct answers = " + sum);

//                        afterSubmission();
        updateQueAnsOptions();
    }

    private void updateQueAnsOptions() {
        txtQuestion.setText(currentQuestion.getQuestion());
        btnOption1.setText(currentQuestion.getOptA());
        btnOption2.setText(currentQuestion.getOptB());
        btnOption3.setText(currentQuestion.getOptC());
        btnOption4.setText(currentQuestion.getOptD());
        txtNumberQ.setText((qid + 1) + "/" + (list.size()));
        int id = currentQuestion.getId();
        boolean isPresent = false;
        // Iterate over the HashMap
        for (Map.Entry<Integer, Integer> entry : selectedO.entrySet()) {

            // Get the entry at this iteration
            // Check if this key is the required key
            if (id == entry.getKey()) {
                isPresent = true;
            }

        }
        if (submitted == 0) {
            if (isPresent) {
                switch (selectedO.get(id)) {
                    case 1: {
                        btnOption1.setTextColor(Color.BLACK);
                        btnOption2.setTextColor(Color.WHITE);
                        btnOption3.setTextColor(Color.WHITE);
                        btnOption4.setTextColor(Color.WHITE);
                        btnOption1.setBackgroundColor(Color.YELLOW);
                        btnOption2.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
                        btnOption3.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
                        btnOption4.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
                        break;
                    }
                    case 2: {
                        btnOption2.setTextColor(Color.BLACK);
                        btnOption1.setTextColor(Color.WHITE);
                        btnOption3.setTextColor(Color.WHITE);
                        btnOption4.setTextColor(Color.WHITE);
                        btnOption2.setBackgroundColor(Color.YELLOW);
                        btnOption1.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
                        btnOption3.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
                        btnOption4.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
                        break;
                    }
                    case 3: {
                        btnOption3.setTextColor(Color.BLACK);
                        btnOption2.setTextColor(Color.WHITE);
                        btnOption1.setTextColor(Color.WHITE);
                        btnOption4.setTextColor(Color.WHITE);
                        btnOption3.setBackgroundColor(Color.YELLOW);
                        btnOption2.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
                        btnOption1.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
                        btnOption4.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
                        break;
                    }
                    case 4: {
                        btnOption4.setTextColor(Color.BLACK);
                        btnOption2.setTextColor(Color.WHITE);
                        btnOption3.setTextColor(Color.WHITE);
                        btnOption1.setTextColor(Color.WHITE);
                        btnOption4.setBackgroundColor(Color.YELLOW);
                        btnOption2.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
                        btnOption3.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
                        btnOption1.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
                        break;
                    }
                    default:
                        throw new IllegalStateException("Unexpected value: " + selectedO.get(id));
                }
            } else {
                btnOption4.setTextColor(Color.WHITE);
                btnOption2.setTextColor(Color.WHITE);
                btnOption3.setTextColor(Color.WHITE);
                btnOption1.setTextColor(Color.WHITE);
                btnOption2.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
                btnOption3.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
                btnOption1.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
                btnOption4.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
            }
        } else {

            btnOption4.setClickable(false);
            btnOption3.setClickable(false);
            btnOption2.setClickable(false);
            btnOption1.setClickable(false);

            if (isPresent) {
                if (Objects.requireNonNull(selectedO.get(id)).toString().equals(currentQuestion.getAnswer())) {
                    switch (selectedO.get(id)) {
                        case 1: {
                            btnOption1.setTextColor(Color.WHITE);
                            btnOption2.setTextColor(Color.WHITE);
                            btnOption3.setTextColor(Color.WHITE);
                            btnOption4.setTextColor(Color.WHITE);
                            btnOption1.setBackgroundColor(DARK_GREEN);
                            btnOption2.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
                            btnOption3.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
                            btnOption4.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
                            break;
                        }
                        case 2: {
                            btnOption2.setTextColor(Color.WHITE);
                            btnOption1.setTextColor(Color.WHITE);
                            btnOption3.setTextColor(Color.WHITE);
                            btnOption4.setTextColor(Color.WHITE);
                            btnOption2.setBackgroundColor(DARK_GREEN);
                            btnOption1.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
                            btnOption3.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
                            btnOption4.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
                            break;
                        }
                        case 3: {
                            btnOption3.setTextColor(Color.WHITE);
                            btnOption2.setTextColor(Color.WHITE);
                            btnOption1.setTextColor(Color.WHITE);
                            btnOption4.setTextColor(Color.WHITE);
                            btnOption3.setBackgroundColor(DARK_GREEN);
                            btnOption2.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
                            btnOption1.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
                            btnOption4.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
                            break;
                        }
                        case 4: {
                            btnOption4.setTextColor(Color.WHITE);
                            btnOption2.setTextColor(Color.WHITE);
                            btnOption3.setTextColor(Color.WHITE);
                            btnOption1.setTextColor(Color.WHITE);
                            btnOption4.setBackgroundColor(DARK_GREEN);
                            btnOption2.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
                            btnOption3.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
                            btnOption1.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
                            break;
                        }
                        default:
                            throw new IllegalStateException("Unexpected value: " + selectedO.get(id));
                    }
                } else {
                    switch (selectedO.get(id)) {
                        case 1: {
                            btnOption1.setTextColor(Color.WHITE);
                            btnOption2.setTextColor(Color.WHITE);
                            btnOption3.setTextColor(Color.WHITE);
                            btnOption4.setTextColor(Color.WHITE);
                            btnOption1.setBackgroundColor(RED);
                            btnOption2.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
                            btnOption3.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
                            btnOption4.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
                            break;
                        }
                        case 2: {
                            btnOption2.setTextColor(Color.WHITE);
                            btnOption1.setTextColor(Color.WHITE);
                            btnOption3.setTextColor(Color.WHITE);
                            btnOption4.setTextColor(Color.WHITE);
                            btnOption2.setBackgroundColor(RED);
                            btnOption1.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
                            btnOption3.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
                            btnOption4.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
                            break;
                        }
                        case 3: {
                            btnOption3.setTextColor(Color.WHITE);
                            btnOption2.setTextColor(Color.WHITE);
                            btnOption1.setTextColor(Color.WHITE);
                            btnOption4.setTextColor(Color.WHITE);
                            btnOption3.setBackgroundColor(RED);
                            btnOption2.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
                            btnOption1.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
                            btnOption4.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
                            break;
                        }
                        case 4: {
                            btnOption4.setTextColor(Color.WHITE);
                            btnOption2.setTextColor(Color.WHITE);
                            btnOption3.setTextColor(Color.WHITE);
                            btnOption1.setTextColor(Color.WHITE);
                            btnOption4.setBackgroundColor(RED);
                            btnOption2.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
                            btnOption3.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
                            btnOption1.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
                            break;
                        }
                        default:
                            throw new IllegalStateException("Unexpected value: " + selectedO.get(id));
                    }
                }
            }
            else{

                switch (currentQuestion.getAnswer()) {
                    case "1": {
                        btnOption1.setTextColor(Color.BLACK);
                        btnOption2.setTextColor(Color.WHITE);
                        btnOption3.setTextColor(Color.WHITE);
                        btnOption4.setTextColor(Color.WHITE);
                        btnOption1.setBackgroundColor(GREEN);
                        btnOption2.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
                        btnOption3.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
                        btnOption4.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
                        break;
                    }
                    case "2": {
                        btnOption2.setTextColor(Color.BLACK);
                        btnOption1.setTextColor(Color.WHITE);
                        btnOption3.setTextColor(Color.WHITE);
                        btnOption4.setTextColor(Color.WHITE);
                        btnOption2.setBackgroundColor(GREEN);
                        btnOption1.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
                        btnOption3.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
                        btnOption4.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
                        break;
                    }
                    case "3": {
                        btnOption3.setTextColor(Color.BLACK);
                        btnOption2.setTextColor(Color.WHITE);
                        btnOption1.setTextColor(Color.WHITE);
                        btnOption4.setTextColor(Color.WHITE);
                        btnOption3.setBackgroundColor(GREEN);
                        btnOption2.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
                        btnOption1.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
                        btnOption4.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
                        break;
                    }
                    case "4": {
                        btnOption4.setTextColor(Color.BLACK);
                        btnOption2.setTextColor(Color.WHITE);
                        btnOption3.setTextColor(Color.WHITE);
                        btnOption1.setTextColor(Color.WHITE);
                        btnOption4.setBackgroundColor(GREEN);
                        btnOption2.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
                        btnOption3.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
                        btnOption1.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.quiz_back_bg));
                        break;
                    }
                    default:
                        throw new IllegalStateException("Unexpected value: " + selectedO.get(id));
                }
            }
        }
    }
    }