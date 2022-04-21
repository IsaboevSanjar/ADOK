package uz.sanjar.adok.math.mathquestions1;

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
import uz.sanjar.adok.math.mathquestions1.result.MathResultEng;
import uz.sanjar.adok.start.StartActivityEnglish;

public class MathActivityEng extends AppCompatActivity {
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
        setContentView(R.layout.activity_math_eng);
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
                String key = "Math";

                Bundle bundle = new Bundle();
                bundle.putInt(MathResultEng.KEY_TRUES, trueCount);
                bundle.putString(MathResultEng.KEY_TYPE, key);
                bundle.putInt(MathResultEng.KEY_MISTAKES, falseCount);

                Intent intent = new Intent(MathActivityEng.this, MathResultEng.class);
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
                            Toast.makeText(MathActivityEng.this, "Choose the answer!!!", Toast.LENGTH_SHORT).show();
                        }

                    }

                }
            });


            comeBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MathActivityEng.this, StartActivityEnglish.class);
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
                Intent intent = new Intent(MathActivityEng.this, StartActivityEnglish.class);
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
                "2+2*4=",
                "10",
                "10",
                "11",
                "16"
        ));
        data.add(new QuestionData(
                "(2+2)*4=",
                "16",
                "14",
                "16",
                "10"
        ));
        data.add(new QuestionData(
                "Which of the following numbers gives 240 when added to its own square?",
                "15",
                "15",
                "16",
                "17"
        ));
        data.add(new QuestionData(
                "The least number of two digits is ….. .",
                "None of these",
                "None of these",
                "11",
                "99"
        ));
        data.add(new QuestionData(
                "The difference between the smallest number of four digits and the largest number of three digits is ……. .",
                "1",
                "2",
                "1",
                "100"
        ));
        data.add(new QuestionData(
                "The sum of the least number of three digits and largest number of two digits is ….. .",
                "199",
                "101",
                "100",
                "199"
        ));
        data.add(new QuestionData(
                "If a number has an even number or zero at its unit place; the number is always divisible by ……. .",
                "2",
                "5",
                "2",
                "3"
        ));
        data.add(new QuestionData(
                "A number is divisible by 3 if the sum of its digits is divisible by …….. .",
                "3",
                "5",
                "7",
                "3"
        ));
        data.add(new QuestionData(
                "What is three fifth of 100?",
                "60",
                "60",
                "65",
                "50"
        ));
        data.add(new QuestionData(
                "If David’s age is 27 years old in 2011. What was his age in 2003?",
                "19",
                "18",
                "20",
                "19"
        ));
        data.add(new QuestionData(
                "I am a number. I have 7 in the ones place. I am less than 80 but greater than 70. What is my number?",
                "77",
                "71",
                "75",
                "76"
        ));
        data.add(new QuestionData(
                "How many years are there in a decade?",
                "10",
                "10",
                "100",
                "50"
        ));
        data.add(new QuestionData(
                "If 1=3, 2=3, 3=5,4=4, 5=4 Then 6=?",
                "3",
                "3",
                "7",
                "4"
        ));
        data.add(new QuestionData(
                "I am an odd number. Take away one letter and I become even. What number am I?",
                "7",
                "9",
                "11",
                "7"
        ));
        data.add(new QuestionData(
                "Sally is 54 years old and her mother is 80, how many years ago was Sally’s mother times her age?",
                "41 years ago",
                "26 years ago",
                "41 years ago",
                "54 years ago"
        ));
        data.add(new QuestionData(
                "There is a three-digit number. The second digit is four times as big as the third digit, while the first digit is three less than the second digit. What is the number?",
                "141",
                "141",
                "282",
                "333"
        ));
        data.add(new QuestionData(
                "The day before yesterday I was 25. The next year I will be 28. This is true only one day in a year. What day is my Birthday?",
                "December 31",
                "January 1",
                "February 29",
                "December 31"
        ));
        data.add(new QuestionData(
                "How many feet are in a mile?",
                "5280",
                "5280",
                "1000",
                "1200"
        ));
        data.add(new QuestionData(
                "Solve  - 15+ (-5x) =0",
                "-3",
                "5",
                "-3",
                "0"
        ));
        data.add(new QuestionData(
                "What is 1.92÷3",
                "0.64",
                "1",
                "0.75",
                "0.64"
        ));
        data.add(new QuestionData(
                "Look at this series: 36, 34, 30, 28, 24, … What number should come next?",
                "22",
                "22",
                "20",
                "18"
        ));
        data.add(new QuestionData(
                "Look at this series: 22, 21, 23, 22, 24, 23, … What number should come next?",
                "25",
                "25",
                "24",
                "21"
        ));
        data.add(new QuestionData(
                "121 Divided by 11 is ",
                "11",
                "12",
                "11",
                "10"
        ));
        data.add(new QuestionData(
                "60 Times of 8 Equals to",
                "480",
                "300",
                "400",
                "480"
        ));
        data.add(new QuestionData(
                "What is the Next Prime Number after 7 ?",
                "11",
                "11",
                "9",
                "8"
        ));
        data.add(new QuestionData(
                "What is 6% Equals to",
                "0.06",
                "0.006",
                "0.06",
                "0.6"
        ));
        data.add(new QuestionData(
                "How Many Months Make a Century?",
                "1200",
                "12000",
                "100",
                "1200"
        ));
        data.add(new QuestionData(
                "How Many Months Have 120 Days?",
                "4 months",
                "2 months",
                "6 months",
                "4 months"
        ));
        data.add(new QuestionData(
                "How Many Sides are there in a Decagon?",
                "10",
                "4",
                "8",
                "10"
        ));
        data.add(new QuestionData(
                "27 is a perfect cube. If true then what is the perfect cube of 27?",
                "3",
                "7",
                "3",
                "27 is not perfect cube"
        ));
        data.add(new QuestionData(
                "In 1882, Whiz Ferdinand Von Lindemann discovered which Mathematical Symbol?",
                "Pi",
                "Rectangle",
                "Pi",
                "Division sign"
        ));
        data.add(new QuestionData(
                "How many digits does the value of Pi have?",
                "Infinite",
                "Infinite",
                "3.14",
                "3.142"
        ));
        data.add(new QuestionData(
                "How many sides does an “enneadecagon” have?",
                "19",
                "11",
                "19",
                "7"
        ));
        data.add(new QuestionData(
                "What is the sum of all the interior angles of a triangle? ",
                "180",
                "180",
                "90",
                "360"
        ));
        data.add(new QuestionData(
                "What is the name of this sequence: 0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55…?",
                "Fibonacci numbers",
                "Arithmetic numbers",
                "Fibonacci numbers",
                "Prime numbers"
        ));
        data.add(new QuestionData(
                "Calculate the quotient of 150 and 3. ",
                "50",
                "15",
                "50",
                "60"
        ));
        data.add(new QuestionData(
                "How many times can a sheet of paper be folded in half?",
                "7",
                "6",
                "infinite",
                "7"
        ));
        data.add(new QuestionData(
                "What is the world’s biggest known number? ",
                "Googolplex",
                "Hypotenuse",
                "Googolplex",
                "1000000000000"
        ));
        data.add(new QuestionData(
                "What is the only prime number that is even?",
                "2",
                "2",
                "1",
                "4"
        ));
        data.add(new QuestionData(
                "What is the prime number closest to 100?",
                "101",
                "99",
                "101",
                "99.9"
        ));
        data.add(new QuestionData(
                "How many vertices are there on a cube?",
                "8",
                "8",
                "4",
                "6"
        ));
        data.add(new QuestionData(
                "At a party, everyone shook hands with everyone else. In total, there were 66 handshakes. How many people were at the party?",
                "12",
                "15",
                "16",
                "12"
        ));
        data.add(new QuestionData(
                "What is the only number that is twice the sum of its digits?",
                "18",
                "9",
                "18",
                "22"
        ));
        data.add(new QuestionData(
                "How many feet are equal to a meter?",
                "3.28 feet",
                "3.28 feet",
                "10 feet",
                "3.90 feet"
        ));
        data.add(new QuestionData(
                "How many right angles are there in a square?",
                "4",
                "4",
                "2",
                "8"
        ));
        data.add(new QuestionData(
                "If you had 785 dogs, how many would you have if I took 524?",
                "261",
                "271",
                "101",
                "261"
        ));
        data.add(new QuestionData(
                "What number doesn't have its own Roman numeral?",
                "0",
                "Billion",
                "0",
                "1"
        ));
        data.add(new QuestionData(
                "What is the smallest perfect number?",
                "6",
                "1",
                "0",
                "6"
        ));
        data.add(new QuestionData(
                "Which number is the sum of its multiples when you add the single digits together?",
                "9",
                "2",
                "9",
                "18"
        ));
        data.add(new QuestionData(
                "What letter does every odd number have in it?",
                "E",
                "E",
                "A",
                "N"
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
        startActivity(new Intent(MathActivityEng.this, StartActivityEnglish.class));
        finish();
    }
}