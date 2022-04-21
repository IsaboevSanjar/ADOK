package uz.sanjar.adok.geo.geoquestions1.r;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

import uz.sanjar.adok.QuestionData;
import uz.sanjar.adok.QuestionManager;
import uz.sanjar.adok.R;
import uz.sanjar.adok.geo.geoquestions1.r.result.GeoResultEng;
import uz.sanjar.adok.start.StartActivityEnglish;

public class GeoEnglishActivity extends AppCompatActivity {
    private final long START_TIME_IN_MILES = 420000;
    private TextView mTextViewCountDown;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMiles = START_TIME_IN_MILES;


    private SeekBar seekBar;
    private TextView currentView, totalView, finishButton, checkButton, questionView;
    private RadioGroup answerGroup;
    private RadioButton variantA;
    private RadioButton variantB;
    private RadioButton variantC;
    private ArrayList<QuestionData> data = new ArrayList<>();
    private QuestionManager manager;
    private boolean isAnswered = false;
    private ImageView comeBack;
    private Button resultButton;
    private FrameLayout frameLayout;

    private int durationTime = 10_000;
    private int durationMinTime = 1_000;

    private int countDownInterval = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_english);
        startTimer();
        loadViews();
        setStateViews();
        loadData();
        manager = new QuestionManager(data);
        startQuiz();
    }

    private void loadViews() {

        frameLayout = findViewById(R.id.frame_layout);
        resultButton = findViewById(R.id.result_btn);
        comeBack = findViewById(R.id.come_back);
        seekBar = findViewById(R.id.state_view);
        currentView = findViewById(R.id.current_question);
        totalView = findViewById(R.id.total_question);

        finishButton = findViewById(R.id.finish_test);
        checkButton = findViewById(R.id.check_answer);
        questionView = findViewById(R.id.question_view);

        mTextViewCountDown = findViewById(R.id.text_view_countdown);

        variantA = findViewById(R.id.variant_a);
        variantB = findViewById(R.id.variant_b);
        variantC = findViewById(R.id.variant_c);
        answerGroup = findViewById(R.id.answer_group);

       /* progressView.setDuration(20_000);
        progressView.setMax(100);*/
    }

    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMiles, countDownInterval) {
            @Override
            public void onTick(long l) {
                mTimerRunning = true;
                mTimeLeftInMiles = l;
                upCountDownText();
                setListener();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                frameLayout.setVisibility(View.VISIBLE);
                checkButton.setVisibility(View.GONE);
                finishButton.setVisibility(View.GONE);
                answerGroup.setVisibility(View.GONE);
                comeBack.setVisibility(View.GONE);
            }
        }.start();
        mTimerRunning = true;

    }

    private void upCountDownText() {
        int minute = (int) (mTimeLeftInMiles / 1000) / 60;
        int second = (int) ((mTimeLeftInMiles / 1000) % 60);
        int milliSecond = (int) (mTimeLeftInMiles / 10) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "⌛%02d:%02d:%02d", minute, second, milliSecond);
        mTextViewCountDown.setText(timeLeftFormatted);
    }


    private void startQuiz() {
        questionView.setText(manager.getQuestion());
        variantA.setText(manager.variantA());
        variantB.setText(manager.variantB());
        variantC.setText(manager.variantC());

        currentView.setText(String.valueOf(manager.getCurrentLevel()));
        totalView.setText(String.valueOf(manager.getTotal()));
        seekBar.setProgress(manager.getCurrentLevel() * 100 / manager.getTotal());
    }

    public void clearView() {
        variantA.setBackgroundResource(R.drawable.radio_back);
        variantA.setEnabled(true);
        variantB.setBackgroundResource(R.drawable.radio_back);
        variantB.setEnabled(true);
        variantC.setBackgroundResource(R.drawable.radio_back);
        variantC.setEnabled(true);
        answerGroup.clearCheck();
    }


    private void setListener() {
        resultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int trueCount = manager.getTotalTrues();
                int falseCount = manager.getTotalFalse();
                String key = "Geography";

                Bundle bundle = new Bundle();
                bundle.putInt(GeoResultEng.KEY_TRUES, trueCount);
                bundle.putString(GeoResultEng.KEY_TYPE, key);
                bundle.putInt(GeoResultEng.KEY_MISTAKES, falseCount);

                Intent intent = new Intent(GeoEnglishActivity.this, GeoResultEng.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });


        if (mTimerRunning) {

            // TODO: 1/30/2022  soatga etibor berish kerak
            checkButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    boolean isFinished = false;

                    if (isFinished) {
                        finish();
                    } else {
                        // TODO: 1/30/2022 vaqt tugaganda savolni change qilish
                        /* if(durationTime>durationMinTime){*/
                        boolean hasPressed = variantA.isChecked() || variantB.isChecked() || variantC.isChecked();

                        if (hasPressed) {

                            if (isAnswered) {
                                if (manager.hasQuestion()) {
                                    clearView();
                                    startQuiz();
                                    checkButton.setText("Check");
                                } else {
                                    isFinished = true;
                                    checkButton.setText("Results");
                                    frameLayout.setVisibility(View.VISIBLE);
                                    checkButton.setVisibility(View.GONE);
                                    finishButton.setVisibility(View.GONE);
                                    answerGroup.setVisibility(View.GONE);
                                    comeBack.setVisibility(View.GONE);
                                }
                                isAnswered = false;
                            } else {
                                RadioButton button = findViewById(answerGroup.getCheckedRadioButtonId());
                                String answer = button.getText().toString();
                                boolean isTrue = manager.checkAnswer(answer);
                                if (isTrue) {
                                    button.setBackgroundResource(R.drawable.radio_back_green);
                                } else {
                                    button.setBackgroundResource(R.drawable.radio_back_red);
                                }
                                variantA.setEnabled(variantA.isChecked());
                                variantB.setEnabled(variantB.isChecked());
                                variantC.setEnabled(variantC.isChecked());
                                checkButton.setText("Next");
                                isAnswered = true;
                            }
                        } else {
                            Toast.makeText(GeoEnglishActivity.this, "Choose the answer!!!", Toast.LENGTH_SHORT).show();
                        }

                    }

                }
            });


            comeBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(GeoEnglishActivity.this, StartActivityEnglish.class);
                    startActivity(intent);
                    finish();
                }
            });
            finishButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showAlert();
                }
            });
            mTimerRunning = false;
        } else {
            frameLayout.setVisibility(View.VISIBLE);
        }
    }


    public void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Warning!!!")
                .setMessage("If you finish now the results will not save!!!");

        builder.setPositiveButton("Finish", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(GeoEnglishActivity.this, StartActivityEnglish.class);
                startActivity(intent);
                finish();
            }

        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.create().show();

    }


    private void loadData() {
        data.add(new QuestionData(
                "What is the name of the tallest mountain in the world? ",
                "Mount Everest",
                "Hazrati Sulton",
                "Namulli",
                "Mount Everest"
        ));
        data.add(new QuestionData(
                "What is the name of the smallest country in the world? ",
                "The Vatican City ",
                "Monaco",
                "The Vatican City ",
                "Nepal"
        ));
        data.add(new QuestionData(
                "What is the name of the longest river in Africa?",
                "The Nile River",
                "Ganges River",
                "The Nile River",
                "Amazon"
        ));
        data.add(new QuestionData(
                "What American city is the Golden Gate Bridge located in?  ",
                "San Francisco",
                "Los Angeles",
                "San Francisco",
                "New York"
        ));
        data.add(new QuestionData(
                "What is the capital of Canada?  ",
                "Ottawa",
                "Alaska",
                "Warsaw",
                "Ottawa"
        ));
        data.add(new QuestionData(
                "What country are the Great Pyramids of Giza located in? ",
                "Egypt",
                "Egypt",
                "Algeria",
                "Tunis"
        ));
        data.add(new QuestionData(
                "What is the capital of the American State of California? ",
                "Sacramento",
                "New Mexico",
                "Sacramento",
                "Washington"
        ));
        data.add(new QuestionData(
                "What country has the most natural lakes? ",
                "Canada",
                "USA",
                "Uzbekistan",
                "Canada"
        ));
        data.add(new QuestionData(
                "How many States does the United States consist of? ",
                "50",
                "50",
                "51",
                "44"
        ));
        data.add(new QuestionData(
                "What planet is closest to Earth? ",
                "Venus",
                "Mars",
                "Venus",
                "Mercury"
        ));
        data.add(new QuestionData(
                "What is the name of the river that flows through the Brazil rainforest? ",
                "The Amazon",
                "Bern",
                "Rhine",
                "The Amazon"
        ));
        data.add(new QuestionData(
                "Which country is also called The Netherlands? ",
                "Holland",
                "Norway",
                "Holland",
                "Denmark"
        ));
        data.add(new QuestionData(
                "What is the official currency of the United Kingdom?",
                "Pound sterling",
                "Dollar",
                "Pound sterling",
                "Euro"
        ));
        data.add(new QuestionData(
                "How many countries are there in the United Kingdom?",
                "4",
                "5",
                "3",
                "4"
        ));
        data.add(new QuestionData(
                "What is the capital of Senegal?",
                "Dakar",
                "Senegal",
                "Cuba",
                "Dakar"
        ));
        data.add(new QuestionData(
                "How many time zones does Russia have?",
                "11",
                "1",
                "9",
                "11"
        ));
        data.add(new QuestionData(
                "What is the name of the tallest mountain in Canada?",
                "Mount Logan",
                "Mount Logan",
                "Mount Everest",
                "Mount Mauna Kea"
        ));
        data.add(new QuestionData(
                "What is the name of the largest city in Australia?",
                "Brisbane",
                "Brisbane",
                "Melbourne",
                "Canberra"
        ));
        data.add(new QuestionData(
                "What type of leaf is on the Canadian flag?",
                "Maple",
                "Tree",
                "Maple",
                "Oak"
        ));
        data.add(new QuestionData(
                "What is the name of the driest continent on Earth?",
                "Antarctica",
                "Africa",
                "Asia",
                "Antarctica"
        ));
        data.add(new QuestionData(
                "What country formerly ruled Iceland?",
                "Denmark",
                "Denmark",
                "England",
                "Ireland"
        ));
        data.add(new QuestionData(
                "What country is known to have the best quality tap water?",
                "Switzerland",
                "Switzerland",
                "USA",
                "Japan"
        ));
        data.add(new QuestionData(
                "In what ocean is the Bermuda Triangle located?",
                "Atlantic Ocean",
                "Pacific Ocean",
                "Atlantic Ocean",
                "Indian Ocean"
        ));
        data.add(new QuestionData(
                "What city is known as the Glass Capital of the World?",
                "Toledo",
                "Toledo",
                "Tokyo",
                "London"
        ));
        data.add(new QuestionData(
                "How many stars are on the Australian flag?",
                "6",
                "6",
                "5",
                "4"
        ));
        data.add(new QuestionData(
                "What is the name of the smallest US state?",
                "Rhode Island",
                "Rhode Island",
                "Alaska",
                "New Mexico"
        ));
        data.add(new QuestionData(
                "Which state was purchased from Russia in 1867?",
                "Alaska",
                "Alaska",
                "San Fransisco",
                "Los Angeles"
        ));
        data.add(new QuestionData(
                "The Lord of The Rings trilogy was filmed in what country? ",
                "New Zealand",
                "Italy",
                "UK",
                "New Zealand"
        ));
        data.add(new QuestionData(
                "What is the capital of Jamaica?",
                "Kingston",
                "Kingston",
                "Jamaica",
                "Peru"
        ));
        data.add(new QuestionData(
                "What place is known as the largest micro-continent?",
                "Madagascar",
                "UK",
                "Norway",
                "Madagascar"
        ));
        data.add(new QuestionData(
                "What is the only country that borders the United Kingdom?",
                "Ireland",
                "Ireland",
                "Spain",
                "Scotland"
        ));
        data.add(new QuestionData(
                "How many time zones does Australia have? ",
                "3",
                "4",
                "3",
                "2"
        ));
        data.add(new QuestionData(
                "How many countries are located in the Southern Hemisphere?",
                "32",
                "22",
                "14",
                "32"
        ));
        data.add(new QuestionData(
                "What is the largest country in South America?",
                "Brazil",
                "Brazil",
                "Argentina",
                "Ecuador"
        ));
        data.add(new QuestionData(
                "What is the name of the world’s largest island?",
                "Greenland",
                "Madagascar",
                "Greenland",
                "Philippines"
        ));
        data.add(new QuestionData(
                "How many countries are there in Africa?",
                "54",
                "64",
                "40",
                "54"
        ));
        data.add(new QuestionData(
                "Which country is home to the most volcanoes?",
                "Indonesia",
                "Maldive",
                "Indonesia",
                "China"
        ));
        data.add(new QuestionData(
                "What is the name of the largest waterfall in the world?",
                "Victoria Falls",
                "Victoria Falls",
                "Niagara ",
                "Angel Falls"
        ));
        data.add(new QuestionData(
                "What are citizens of Sudan called?",
                "Sudanese",
                "Sudan",
                "Sudanese",
                "Sudanian"
        ));
        data.add(new QuestionData(
                "In which country is the Leaning Tower of Pisa located?",
                "Italy",
                "Rome",
                "Italy",
                "Vatican"
        ));
        data.add(new QuestionData(
                "What is Belgium’s nickname?",
                "The Cockpit of Europe",
                "Bat",
                "Belgik",
                "The Cockpit of Europe"
        ));
        data.add(new QuestionData(
                "What is the capital of Malta?",
                "Valletta",
                "Malta",
                "Valletta",
                "Malmo"
        ));
        data.add(new QuestionData(
                "After Alaska, which U.S. state has the longest coastline?",
                "Florida",
                "Florida",
                "Los Angels",
                "Washington"
        ));
        data.add(new QuestionData(
                "What country is home to the tallest mountain in the world?",
                "Nepal",
                "China",
                "Thailand",
                "Nepal"
        ));
        data.add(new QuestionData(
                "In which country would you find the city of Dresden?",
                "Germany",
                "France",
                "Germany",
                "Italy"
        ));
        data.add(new QuestionData(
                "What is the state capital of New York?",
                "Albany",
                "New York",
                "Albany",
                "Capitan"
        ));
        data.add(new QuestionData(
                "Where is the largest volcano on Earth located?",
                "Hawaii",
                "Indonesia",
                "Hawaii",
                "Jamaica"
        ));
        data.add(new QuestionData(
                "What is Earth's largest continent?",
                "Asia",
                "Asia",
                "Europe",
                "South America"
        ));
        data.add(new QuestionData(
                "What razor-thin country accounts for more than half of the western coastline of South America?",
                "Chile",
                "Uruguay",
                "Chile",
                "Brazil"
        ));
        data.add(new QuestionData(
                "What percentage of the River Nile is located in Egypt?",
                "22%",
                "50%",
                "30%",
                "22%"
        ));
    }

    private void setStateViews() {
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(GeoEnglishActivity.this, StartActivityEnglish.class));
        finish();
    }
}