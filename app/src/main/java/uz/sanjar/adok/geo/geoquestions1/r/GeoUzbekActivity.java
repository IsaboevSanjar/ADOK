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
import uz.sanjar.adok.geo.geoquestions1.r.result.GeoResultUz;
import uz.sanjar.adok.math.mathquestions1.result.MathResultUz;
import uz.sanjar.adok.start.StartActivityUzbek;

public class GeoUzbekActivity extends AppCompatActivity {
    private final long START_TIME_IN_MILES = 360000;
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
        setContentView(R.layout.activity_geo_uzbek);
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
                String key = "Geografiya";

                Bundle bundle = new Bundle();
                bundle.putInt(MathResultUz.KEY_TRUES, trueCount);
                bundle.putString(MathResultUz.KEY_TYPE, key);
                bundle.putInt(MathResultUz.KEY_MISTAKES, falseCount);

                Intent intent = new Intent(GeoUzbekActivity.this, GeoResultUz.class);
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
                            Toast.makeText(GeoUzbekActivity.this, "Javobni tanlang!!!", Toast.LENGTH_SHORT).show();
                        }

                    }

                }
            });


            comeBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(GeoUzbekActivity.this, StartActivityUzbek.class);
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
                Intent intent = new Intent(GeoUzbekActivity.this, StartActivityUzbek.class);
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
                "Dunyodagi eng baland choqqi qaysi? ",
                "Everest",
                "Hazrati Sulton",
                "Namuli",
                "Everest"
        ));
        data.add(new QuestionData(
                "Dunyodagi eng kop aholi qayerda istiqomat qiladi? ",
                "Xitoy",
                "Xitoy",
                "Hindiston",
                "AQSh"
        ));
        data.add(new QuestionData(
                "Afrikadagi eng uzun daryo?",
                "Nil",
                "Ganga",
                "Nil",
                "Amazon"
        ));
        data.add(new QuestionData(
                "Oltin darvoza ko'prigi Amerikaning qaysi shtatida joylashgan?  ",
                "San Fransisco",
                "Los Angeles",
                "San Fransisco",
                "New York"
        ));
        data.add(new QuestionData(
                "Kanadaning poytaxti?  ",
                "Ottava",
                "Alyaska",
                "Varshava",
                "Ottava"
        ));
        data.add(new QuestionData(
                "Peramidalar qaysi davalatda joylashgan? ",
                "Misr",
                "Misr",
                "Jazoir",
                "Tunis"
        ));
        data.add(new QuestionData(
                "Tailandning poytaxti? ",
                "Bangkok",
                "Bangkok",
                "Tbilisi",
                "Budapesht"
        ));
        data.add(new QuestionData(

                "Amerikaning Kaliforniya shtatining markazi qayer?",
                "Sakramento",
                "New Mexiko",
                "Sakramento",
                "Vashington"
        ));
        data.add(new QuestionData(
                "Qaysi mamlakatda eng ko'p tabiiy ko'llar mavjud?",
                "Kanada",
                "AQSh",
                "Kanada",
                "Grenlandiya"
        ));
        data.add(new QuestionData(
                "AQShda nechta shtat bor?",
                "50",
                "50",
                "51",
                "44"
        ));
        data.add(new QuestionData(
                "Qaysi sayyora Yerga eng yaqin joylashgan?",
                "Venera",
                "Venera",
                "Mars",
                "Merkury"
        ));
        data.add(new QuestionData(
                "Braziliya tropik oʻrmonlaridan oqib oʻtuvchi daryo qanday nomlanadi?",
                "Amazon",
                "Bern",
                "Amazon",
                "Ren"
        ));
        data.add(new QuestionData(
                "Qaysi davlat Nederlandiya deb ham ataladi?",
                "Gollandiya",
                "Norvagiya",
                "Gollandiya",
                "Daniya"
        ));
        data.add(new QuestionData(
                "Buyuk Britaniyaning rasmiy pul birligi nima?",
                "Funt sterling",
                "Funt sterling",
                "Dollar",
                "Yevro"
        ));
        data.add(new QuestionData(
                "Buyuk Britaniyada nechta davlat bor?",
                "4",
                "5",
                "3",
                "4"
        ));
        data.add(new QuestionData(
                "Senegalning poytaxti nima deb nomlanadi?",
                "Dakar",
                "Senegal",
                "Kuba",
                "Dakar"
        ));
        data.add(new QuestionData(
                "Rossiyada nechta vaqt zonalari mavjud?",
                "11",
                "1",
                "9",
                "11"
        ));
        data.add(new QuestionData(
                "Kanadadagi eng baland tog'ning nomi nima?",
                "Logan",
                "Logan",
                "Everest",
                "Mauna Kea"
        ));
        data.add(new QuestionData(
                "Avstraliyaning eng katta shahri qanday nomlanadi?",
                "Brisben",
                "Brisben",
                "Melburn",
                "Kanberra"
        ));
        data.add(new QuestionData(
                "Kanada bayrog'ida qaysi daraxtning bargi tasvirlangan?",
                "Chinor",
                "Terak",
                "Chinor",
                "Qayin"
        ));
        data.add(new QuestionData(
                "Yerdagi eng qurg'oqchil qit'a nima deb ataladi?",
                "Antraktika",
                "Afrika",
                "Osiyo",
                "Antraktika"
        ));
        data.add(new QuestionData(
                "Avvallari Islandiyani qaysi davlat boshqargan?",
                "Daniya",
                "Daniya",
                "Anglya",
                "Irlandiya"
        ));
        data.add(new QuestionData(
                "Qaysi davlat eng sifatli musluk suviga ega?",
                "Shvetsariya",
                "AQSh",
                "Shvetsariya",
                "Yaponiya"
        ));
        data.add(new QuestionData(
                "Bermud uchburchagi qaysi okeanda joylashgan?",
                "Atlantika",
                "Atlantika",
                "Tinch",
                "Hind"
        ));
        data.add(new QuestionData(
                "Qaysi shahar dunyoning shisha poytaxti sifatida tanilgan?",
                "Toledo",
                "Toledo",
                "Tokyo",
                "London"
        ));
        data.add(new QuestionData(
                "Avstraliya bayrog'ida nechta yulduz bor?",
                "6",
                "6",
                "5",
                "4"
        ));
        data.add(new QuestionData(
                "AQShning eng kichik shtatining nomi nima?",
                "Rode orollari",
                "Rode orollari",
                "Alyaska",
                "New Mexiko"
        ));
        data.add(new QuestionData(
                "1867 yilda Rossiyadan qaysi shtat sotib olingan?",
                "Alyaska",
                "Alyaska",
                "San Fransisko",
                "Los Angeles"
        ));
        data.add(new QuestionData(
                "“Uzuklar hukmdori” qaysi davlatda suratga olingan?",
                "Yangi Zellandiya",
                "Yangi Zellandiya",
                "Buyuk Britaniya",
                "AQSh"
        ));
        data.add(new QuestionData(
                "Yamayka poytaxti nima?",
                "Kingston",
                "Kingston",
                "Yamayka",
                "Peru"
        ));
        data.add(new QuestionData(
                "Qaysi joy eng katta mikro-materik deb nomlanadi?",
                "Madagaskar",
                "Buyuk Britaniya",
                "Norvegiya",
                "Madagaskar"
        ));
        data.add(new QuestionData(
                "Buyuk Britaniya bilan chegaradosh yagona davlat qaysi?",
                "Irlandiya",
                "Irlandiya",
                "Ispaniya",
                "Shotlandiya"
        ));
        data.add(new QuestionData(
                "Avstraliyada nechta vaqt zonalari mavjud?",
                "3",
                "4",
                "3",
                "2"
        ));
        data.add(new QuestionData(
                "Janubiy yarim sharda nechta davlat joylashgan?",
                "32",
                "22",
                "14",
                "32"
        ));
        data.add(new QuestionData(
                "Janubiy Amerikadagi eng katta davlat qaysi?",
                "Braziliya",
                "Braziliya",
                "Argentina",
                "Ekuvador"
        ));
        data.add(new QuestionData(
                "Dunyodagi eng katta orolning nomi nima?",
                "Grenlandiya",
                "Madagaskar",
                "Grenlandiya",
                "Filippin orollari"
        ));
        data.add(new QuestionData(
                "Afrikada nechta davlat bor?",
                "54",
                "64",
                "40",
                "54"
        ));
        data.add(new QuestionData(
                "Qaysi mamlakatda eng ko'p vulqonlar joylashgan?",
                "Indonesiya",
                "Maldiv orollari",
                "Indonesiya ",
                "Xitoy"
        ));
        data.add(new QuestionData(
                "Dunyodagi eng katta sharsharaning nomi nima?",
                "Victoriya",
                "Victoriya",
                "Niagara ",
                "Farishta sharsharasi"
        ));
        data.add(new QuestionData(
                "Sudan fuqarolari nima deb ataladi?",
                "Sudanis",
                "Sudan",
                "Sudanis",
                "Sudanlik"
        ));
        data.add(new QuestionData(
                "Piza minorasi qaysi davlatda joylashgan?",
                "Italiya",
                "Rim",
                "Italiya",
                "Vatikan"
        ));
        data.add(new QuestionData(
                "Belgiyaning laqabi nima?",
                "Yevropaning kokpiti",
                "Ko'rshapalak",
                "Belgik",
                "Yevropaning kokpiti"
        ));
        data.add(new QuestionData(
                "Malta davlatining poytaxti nima?",
                "Valletta",
                "Malta",
                "Valletta",
                "Malmo"
        ));
        data.add(new QuestionData(
                "Alyaskadan keyin AQShning qaysi shtatlari eng uzun qirg'oqqa ega?",
                "Florida",
                "Florida",
                "Los Angeles",
                "Vashington"
        ));
        data.add(new QuestionData(
                "Dunyodagi eng baland tog' qaysi davlatda joylashgan?",
                "Nepal",
                "Xitoy",
                "Tayland",
                "Nepal"
        ));
        data.add(new QuestionData(
                "Drezden shahrini qaysi davlatda topasiz?",
                "Germaniya",
                "Germaniya",
                "Fransiya",
                "Italiya"
        ));

        data.add(new QuestionData(
                "Yer yuzidagi eng katta vulqon qayerda joylashgan?",
                "Gavayi orollarida",
                "Indoneziya",
                "Gavayi orollarida",
                "Yamayka"
        ));
        data.add(new QuestionData(
                "Yerning eng katta qit'asi qaysi?",
                "Osiyo",
                "Osiyo",
                "Yevropa",
                "Afrika"
        ));
        data.add(new QuestionData(
                "Janubiy Amerikaning g'arbiy qirg'oq chizig'ining yarmidan ko'pi qaysi jilodek yupqa davlatga to'g'ri keladi?",
                "Chili",
                "Urugvay",
                "Chili",
                "Braziliya"
        ));
        data.add(new QuestionData(
                "Nil daryosining necha foizi Misrda joylashgan?",
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
        startActivity(new Intent(GeoUzbekActivity.this, StartActivityUzbek.class));
        finish();
    }
}