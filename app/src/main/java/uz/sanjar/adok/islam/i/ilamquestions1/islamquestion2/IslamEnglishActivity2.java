package uz.sanjar.adok.islam.i.ilamquestions1.islamquestion2;

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
import uz.sanjar.adok.islam.i.ilamquestions1.islamquestion2.result2.IslamResultEng2;
import uz.sanjar.adok.start.StartActivityEnglish;

public class IslamEnglishActivity2 extends AppCompatActivity {
    private final long START_TIME_IN_MILES = 240000;
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
        setContentView(R.layout.activity_islam_english_ectivity2);
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
                String key = "Islam deen";

                Bundle bundle = new Bundle();
                bundle.putInt(IslamResultEng2.KEY_TRUES, trueCount);
                bundle.putString(IslamResultEng2.KEY_TYPE, key);
                bundle.putInt(IslamResultEng2.KEY_MISTAKES, falseCount);

                Intent intent = new Intent(IslamEnglishActivity2.this, IslamResultEng2.class);
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
                            Toast.makeText(IslamEnglishActivity2.this, "Choose the answer!!!", Toast.LENGTH_SHORT).show();
                        }

                    }

                }
            });


            comeBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(IslamEnglishActivity2.this, StartActivityEnglish.class);
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
                Intent intent = new Intent(IslamEnglishActivity2.this, StartActivityEnglish.class);
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
                "Who killed Jaloot?",
                "Dawood (as)",
                "Isa (as)",
                "Musa (as)",
                "Dawood (as)"
        ));
        data.add(new QuestionData(
                "Where is Madyan situated?",
                "In Hijas near Syria",
                "In Lebanon",
                "In Hijas near Syria",
                "In Israel"
        ));
        data.add(new QuestionData(
                "The Arabian Peninsula is enclosed in the west by the.",
                "Red Sea and Sinai",
                "Dead Sea",
                "Red Sea and Sinai",
                "Arabian Sea"
        ));
        data.add(new QuestionData(
                "According to the majority of the scholars saying ‘A’oodhu Billah’ before recitation in the prayer is?",
                "Sunnah",
                "Waajib",
                "Permissible",
                "Sunnah"
        ));
        data.add(new QuestionData(
                "What is Barzakh?",
                "Al-Barzakh is the period between a person's dead and his resurrection on the Day of Resurrection",
                "It is the world before worthly life",
                "It is a valley in Hell (Jahannam)",
                "Al-Barzakh is the period between a person's dead and his resurrection on the Day of Resurrection"
        ));
        data.add(new QuestionData(
                "What does “Subhana Rabbiyal Azim” (a dua said in Ruku’) mean?",
                "Glorifying the One Who possesses ultimate greatness",
                "Allah is the greatest",
                "Glorifying the One Who possesses ultimate greatness",
                "There is no deity except Allah"
        ));
        data.add(new QuestionData(
                "Is it permissible to accept a gift from a kaafir on a day of festival for him? (E.g. In Christmas)",
                "It is permissible",
                "It is Haraam",
                "It is permissible",
                "It is Makrooh"
        ));
        data.add(new QuestionData(
                "Who was the first person to be given the title Ameerul Mu'umineen (Commander of the Believers) ?",
                "Umar (ra)",
                "Umar (ra)",
                "Aboo Bakr (ra)",
                "Ali (ra)"
        ));
        data.add(new QuestionData(
                "What was the first pledge in Islam called?",
                "Pledge of Aqaba",
                "Pledge of handaq",
                "Pledge of Hijra",
                "Pledge of Aqaba"
        ));
        data.add(new QuestionData(
                "Who among the sahabah married daughters of the prophet ﷺ  ?",
                "Uthman (ra) and Ali (ra)",
                "Aboo Bakr (ra) and Umar(ra)",
                "Uthman (ra) and Ali (ra)",
                "Ali (ra) and Khalid Bin Waleed (ra)"
        ));
        data.add(new QuestionData(
                "Which companion searched for years and years for the truth and travelled many lands to finally arrive in Madinah to meet the Prophet (ﷺ) ?",
                "Salman Al Farisi",
                "Salaahudheen al Ayyubi",
                "Salman Al Farisi",
                "Abdulla bin Sufyon"
        ));
        data.add(new QuestionData(
                "The ruling of Funeral Prayer (Salaathul Janaazaa) is...",
                "Fard Kiyafayah",
                "Fard Ain",
                "Permissible",
                "Fard Kiyafayah"
        ));
        data.add(new QuestionData(
                "What is the ruling on moving the deceased corpse from one country to another?",
                "It is permissible if it is valid reason",
                "Makrooh",
                "It is Haram generally",
                "It is permissible if it is valid reason"
        ));
        data.add(new QuestionData(
                "If someone believes a person must be obeyed more than Allah, then it is...",
                "Shirk",
                "Shirk",
                "Permissible",
                "Haraam"
        ));
        data.add(new QuestionData(
                "What if someone eats pork without knowing it was pork?",
                "The person should stop eating it, and be more cautious in the future",
                "The person should vomit it and repent",
                "Do sadaqah",
                "The person should stop eating it, and be more cautious in the future"
        ));
        data.add(new QuestionData(
                "When is the time for reciting Soorat al-Kahf on Friday?",
                "Both",
                "Night time (Thursday night)",
                "Day time ",
                "Both"
        ));
        data.add(new QuestionData(
                "According to the hadith wailing (crying) over the dead person will...",
                "Be a punishment for the dead person",
                "Be a punishment for the dead person",
                "Increase in reward for the dead person",
                "Result in forgiveness of the sins of the dead person"
        ));
        data.add(new QuestionData(
                "Removing hair and nails during the menstrual period is...",
                "Permissible",
                "Haraam",
                "Permissible",
                "Mustahab"
        ));
        data.add(new QuestionData(
                "According to the majority of the scholars, how many times the person who leaves Islam should/ apostisizes be asked to repent?",
                "3 times",
                "5 times",
                "3 times",
                "Limitless"
        ));
        data.add(new QuestionData(
                "How frequently do muslims have to pay Zakath?",
                "Yearly",
                "Quarterly",
                "Monthly",
                "Yearly"
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
        startActivity(new Intent(IslamEnglishActivity2.this, StartActivityEnglish.class));
        finish();
    }
}