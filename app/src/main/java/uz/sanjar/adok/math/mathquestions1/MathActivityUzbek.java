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
import uz.sanjar.adok.math.mathquestions1.result.MathResultUz;
import uz.sanjar.adok.start.StartActivityUzbek;

public class MathActivityUzbek extends AppCompatActivity {
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
        setContentView(R.layout.activity_math_uzbek);
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
                String key = "Matematika";

                Bundle bundle = new Bundle();
                bundle.putInt(MathResultUz.KEY_TRUES, trueCount);
                bundle.putString(MathResultUz.KEY_TYPE, key);
                bundle.putInt(MathResultUz.KEY_MISTAKES, falseCount);

                Intent intent = new Intent(MathActivityUzbek.this, MathResultUz.class);
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
                                    checkButton.setText("Tekshirish");
                                } else {
                                    isFinished = true;
                                    checkButton.setText("Natija");
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
                                checkButton.setText("keyingisi");
                                isAnswered = true;
                            }
                        } else {
                            Toast.makeText(MathActivityUzbek.this, "Javobni tanlang!!!", Toast.LENGTH_SHORT).show();
                        }

                    }

                }
            });


            comeBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MathActivityUzbek.this, StartActivityUzbek.class);
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

        builder.setTitle("Ogohlantirish!!!")
                .setMessage("Agar hozir o'yinni tugatsangiz natijangiz saqlanmaydi!!!");

        builder.setPositiveButton("Tugatish", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MathActivityUzbek.this, StartActivityUzbek.class);
                startActivity(intent);
                finish();
            }

        });

        builder.setNegativeButton("Yoq", new DialogInterface.OnClickListener() {
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
                "Sonlarni yozishda nechta raqam ishlatiladi ? ",
                "10 ta",
                "Cheksiz ko'p",
                "10 ta",
                "9 ta"
        ));
        data.add(new QuestionData(
                "12 dm  necha  sm?",
                "120sm",
                "120sm",
                "1200sm",
                "100sm"
        ));
        data.add(new QuestionData(
                "Ko’pburchaklarning perimetri qanday  topiladi ? ",
                "Barcha tomonlari yig’indisi",
                "Barcha tomonlari yig’indisi",
                "Bir tomoni yig’indisi",
                "Barcha tomonlari  kopaytmasi "
        ));
        data.add(new QuestionData(
                "Ikki nuqtadan nechta to’g’ri chiziq o’tkazish mumkin ?",
                "1 ta",
                "4 ta",
                "2 ta",
                "1 ta"
        ));
        data.add(new QuestionData(
                "Ikki million besh yuz olti ming uch yuz yetmish yetti soni necha xonali",
                "7",
                "6",
                "7",
                "5"
        ));
        data.add(new QuestionData(
                "Kesma bir uchi tomonga cheksiz davom ettirilsa qanday shakl hosil bo’ladi ?",
                "Nur",
                "Kesma",
                "To'g'ri chiziq",
                "Nur"
        ));
        data.add(new QuestionData(
                "Qiymati  noma’lum harf qatnashgan tenglik qanday nomlanadi ?",
                "Tenglama",
                "Tenglama",
                "Harfli ifoda",
                "Ildiz"
        ));
        data.add(new QuestionData(
                "Eng kichik to'rt raqamli son bilan eng katta uchta raqamli o'rtasidagi farq …… ga teng. .",
                "1",
                "1",
                "2",
                "99"
        ));
        data.add(new QuestionData(
                "Bir dekada necha yilga teng?",
                "10",
                "22",
                "100",
                "10"
        ));
        data.add(new QuestionData(
                "Agar 1=3,2=4,3=2,5=3 bo'lsa 6=?",
                "4",
                "4",
                "5",
                "3"
        ));
        data.add(new QuestionData(
                "- 15+ (-5x) =0  x=?",
                "-3",
                "15",
                "0",
                "-3"
        ));
        data.add(new QuestionData(
                "1.92÷3=?",
                "0.64",
                "1",
                "0.64",
                "0.75"
        ));
        data.add(new QuestionData(
                "7 dan keyin keladigan tub son qaysi?",
                "11",
                "11",
                "5",
                "13"
        ));
        data.add(new QuestionData(
                "6% nechiga teng?",
                "0.06",
                "6",
                "0.6",
                "0.06"
        ));
        data.add(new QuestionData(
                "1 asrda necha oy mavjud?",
                "1200",
                "1200",
                "3560",
                "12000"
        ));
        data.add(new QuestionData(
                "Dekagonning nechta tomoni bor?",
                "10",
                "11",
                "7",
                "10"
        ));
        data.add(new QuestionData(
                "Pi qiymati nechta raqamdan iborat?",
                "Aniqlanmagan",
                "Aniqlanmagan",
                "3.14",
                "3.142"
        ));
        data.add(new QuestionData(
                "0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55... bular qanday sonlar?",
                "Fibonachchi sonlar",
                "Tub sonlar",
                "Mukammal sonlar",
                "Fibonachchi sonlar"
        ));
        data.add(new QuestionData(
                "1 ta varoqni necha marta teng yarmidan buklash mumkin?",
                "7",
                "6",
                "7",
                "8"
        ));
        data.add(new QuestionData(
                "Juft son bolgan yagona tub son?",
                "2",
                "1",
                "2",
                "4"
        ));
        data.add(new QuestionData(
                "100 ga eng yaqin tub son?",
                "101",
                "101",
                "99",
                "9.99"
        ));
        data.add(new QuestionData(
                "Kubning nechta uchi bor?",
                "8",
                "8",
                "4",
                "6"
        ));
        data.add(new QuestionData(
                "Eng kichkina mukammal son?",
                "6",
                "7",
                "6",
                "1"
        ));
        data.add(new QuestionData(
                "3/10 qismi 240 ga teng bo'lgan sonni toping.",
                "800",
                "400",
                "800",
                "1"
        ));
        data.add(new QuestionData(
                "Uchta ketma-ket kelgan natural sonlarning yig'indisi 72 ga teng. Shu sonlardan eng kattasini toping.",
                "25",
                "25",
                "24",
                "30"
        ));
        data.add(new QuestionData(
                "Surati mahrajidan katta bolgan kasr _______ deyiladi?",
                "noto'g'ri kasr",
                "noto'g'ri kasr",
                "to'g'ri kasr",
                "aralash kars"
        ));
        data.add(new QuestionData(
                "Agar 100 ni x ga kopaytirsa 400 hosil boladi. Shu x ni 70 ga kopaytirsa nechi soni chiqadi?",
                "280",
                "380",
                "280",
                "300"
        ));
        data.add(new QuestionData(
                "100 ni 0.5 ga bolganda nechi chiqadi?",
                "200",
                "200",
                "50",
                "150"
        ));
        data.add(new QuestionData(
                "Eng kichik natural son?",
                "1",
                "0",
                "1",
                "2"
        ));
        data.add(new QuestionData(
                "Muntazam 5 burchakning ichki burchaklari yig'indisi nechiga teng?",
                "580",
                "360",
                "580",
                "500"
        ));
        data.add(new QuestionData(
                "Kub nechta simmetriya tekisligiga ega?",
                "9",
                "8",
                "4",
                "9"
        ));
        data.add(new QuestionData(
                "Muntazam 6 burchak diogannalari soni  nechta?",
                "9",
                "9",
                "6",
                "12"
        ));
        data.add(new QuestionData(
                "Quyidagi sonlar to'plamidan qaysi biri 24 ning ko'paytmalari hisoblanadi?",
                "2,3,4,6,8",
                "2,3,4,6,8",
                "2,8,18,6",
                "2,3,18,9,4"
        ));
        data.add(new QuestionData(
                "Nechta oyda 30 kun mavjud?",
                "4",
                "4",
                "5",
                "11"
        ));
        data.add(new QuestionData(
                "37 qanday raqam",
                "Tub ham toq ham",
                "Tub",
                "Tub ham toq ham",
                "Toq"
        ));
        data.add(new QuestionData(
                "Oyna orqali soat 8:00 korinsa, ayni soat nechi?",
                "4:00",
                "4:00",
                "8:00",
                "12:00"
        ));
        data.add(new QuestionData(
                "Soat vaqtni 12.20 deb ko'rsatadi. Soat qo'li daqiqalar qo'li bilan qanday burchak hosil qiladi?",
                "110",
                "110",
                "90",
                "150"
        ));
        data.add(new QuestionData(
                "1,9,25,49,(?)",
                "81",
                "81",
                "64",
                "16"
        ));
        data.add(new QuestionData(
                "To'liq kvadrat bo'lishi uchun 2600 ga qaysi eng kichik sonni qo'shish kerak?",
                "1",
                "100",
                "1",
                "5"
        ));
        data.add(new QuestionData(
                "10001-101=?",
                "9900",
                "9900",
                "9990",
                "1001"
        ));
        data.add(new QuestionData(
                "50 bo'lishi uchun 40 ga necha % qoshilishi kerak?",
                "25",
                "25",
                "10",
                "40"
        ));
        data.add(new QuestionData(
                "Agar 5 ta o'g'il bola 35 ta multfilm yig'ish uchun 7 soat vaqt sarflasa, 3 soatda nechta o'g'il bola 65 ta multfilm yig'a oladi?",
                "Javob berilmagan",
                "Javob berilmagan",
                "45",
                "22"
        ));
        data.add(new QuestionData(
                "248*36-36*148=?",
                "3600",
                "3600",
                "2400",
                "4000"
        ));
        data.add(new QuestionData(
                "Poyezd 1020 km masofani  12 soatda  bosib  o’tadi. Poyezdning tezligi qancha?",
                "85 km/soat",
                "75 km/soat",
                "85 km/soat",
                "55 km/soat"
        ));
        data.add(new QuestionData(
                "To’g’ri   to’rtburchakning  kengligi  7 sm bo’lib ,  uzunligi kengligidan  3sm ortiq  bo’lsa, to’g’ri  to’rburchakning  perimetrini toping.",
                "34sm",
                "25sm",
                "24sm",
                "34sm"
        ));
        data.add(new QuestionData(
                "Yer maydonining   uzunligi 1 km 150 m  bo’lsa, u necha metr bo'ladi.",
                "1150 m",
                "1150 m",
                "10150m",
                "150m"
        ));
        data.add(new QuestionData(
                "36 sonining  natural  bo’luvchisi  nechta?",
                "9ta",
                "7ta",
                "9ta",
                "6ta"
        ));
        data.add(new QuestionData(
                "Yulduzcha o’rniga qanday raqam qo’ysak 23*5 soni 9 ga bo’linadi?",
                "8",
                "4",
                "5",
                "8"
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
        startActivity(new Intent(MathActivityUzbek.this, StartActivityUzbek.class));
        finish();
    }
}