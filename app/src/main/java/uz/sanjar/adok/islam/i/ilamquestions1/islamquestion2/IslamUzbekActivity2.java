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
import uz.sanjar.adok.islam.i.ilamquestions1.islamquestion2.result2.IslamResultUz2;
import uz.sanjar.adok.islam.i.ilamquestions1.result.IslamResultUz;
import uz.sanjar.adok.start.StartActivityUzbek;

public class IslamUzbekActivity2 extends AppCompatActivity {
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
        setContentView(R.layout.activity_islam_uzbek2);
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
                String key = "Islom dini";

                Bundle bundle = new Bundle();
                bundle.putInt(IslamResultUz.KEY_TRUES, trueCount);
                bundle.putString(IslamResultUz.KEY_TYPE, key);
                bundle.putInt(IslamResultUz.KEY_MISTAKES, falseCount);

                Intent intent = new Intent(IslamUzbekActivity2.this, IslamResultUz2.class);
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
                                checkButton.setText("Keyingisi");
                                isAnswered = true;
                            }
                        } else {
                            Toast.makeText(IslamUzbekActivity2.this, "Javob tanlang!!!", Toast.LENGTH_SHORT).show();
                        }

                    }

                }
            });


            comeBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(IslamUzbekActivity2.this, StartActivityUzbek.class);
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
                .setMessage("Agar hozir tugatsangiz natijangiz saqlanmaydi!!!");

        builder.setPositiveButton("Tugatish", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(IslamUzbekActivity2.this, StartActivityUzbek.class);
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
                "O'qiganni shafoat qiluvchi sura qaysi?",
                "Sahih hadisda kelgandek, Mulk surasi",
                "Sahih hadisda kelgandek, Mulk surasi",
                "Sahih hadisda kelgandek, Yaseen surasi",
                "Sahih hadisda kelgandek, Tavba surasi"
        ));
        data.add(new QuestionData(
                "Insonni dajjoldan saqlab qoladigan 10 oyat qaysi?",
                "Kahf surasining dastlabki 10 oyati",
                "Ar Rohman surasining dastlabki 10 oyati",
                "Baqara surasining dastlabki 10 oyati",
                "Kahf surasining dastlabki 10 oyati"
        ));
        data.add(new QuestionData(
                "Qaysi sura bir martada to'liq nozil bo'lgan?",
                "Mudassir surasi",
                "Kavsar surasi",
                "Mudassir surasi",
                "Xumaza surasi"
        ));
        data.add(new QuestionData(
                "Qaysi sura Qur'onning o'rkachi deb ataladi?",
                "Baqara surasi",
                "Ixlos surasi",
                "Fotiha surasi",
                "Baqara surasi"
        ));
        data.add(new QuestionData(
                "Rasululloh SAV qaysi surani ayollar o'rgansin deganlar?",
                "Nur surasi",
                "Nur surasi",
                "Niso surasi",
                "Kafirun surasi"
        ));
        data.add(new QuestionData(
                "Vidolashuv surasi qaysi?",
                "Nasr surasi",
                "Nasr surasi",
                "Quroysh surasi",
                "Toha surasi"
        ));
        data.add(new QuestionData(
                "Arab alifbosidagi barcha harflarni o'zida jamlagan oyat qaysi?",
                "Fath surasining ohirgi oyati",
                "Fath surasining ohirgi oyati",
                "Mulk surasining ohirgi oyati",
                "Taloq surasining ohirgi oyati"
        ));
        data.add(new QuestionData(
                "Qaysi sura kichik Niso surasi deb ataladi?",
                "Taloq surasi",
                "Taloq surasi",
                "Nas surasi",
                "Fotiha surasi"
        ));
        data.add(new QuestionData(
                "Qur'onning kelinchagi qaysi sura?",
                "Ar Rohman surasi",
                "Ar Rohman surasi",
                "Niso surasi",
                "Fotiha surasi"
        ));
        data.add(new QuestionData(
                "Qur’onni har bir harfini to qiyomatga qadar qo‘riqlovchi kim?",
                "Alloh",
                "Alloh",
                "Jabroil (as)",
                "Mo'minlar"
        ));
        data.add(new QuestionData(
                "Qur’onda Qu’ronni nechta nomlar bilan zikr etilgan?",
                "5",
                "5",
                "4",
                "2"
        ));
        data.add(new QuestionData(
                "Makkada nechta sura tushgan?",
                "86",
                "86",
                "80",
                "82"
        ));
        data.add(new QuestionData(
                "Madinada nechta sura tushgan?",
                "28",
                "34",
                "28",
                "32"
        ));
        data.add(new QuestionData(
                "Qur'onda nechta oyat bor?",
                "6666",
                "6363",
                "6666",
                "6636"
        ));
        data.add(new QuestionData(
                "Qur’onda Allohning nomi nechta marta berilgan?",
                "2698",
                "2698",
                "2689",
                "2690"
        ));
        data.add(new QuestionData(
                "Namoz haqida necha marta aytilgan?",
                "700 marta",
                "700 marta",
                "760 marta",
                "734 marta"
        ));
        data.add(new QuestionData(
                "Payg‘ambarimiz Makkada necha yil payg‘ambar bo‘lganlar?",
                "13 yil",
                "10 yil",
                "12 yil",
                "13 yil"
        ));
        data.add(new QuestionData(
                "Qur’onda nomi aytilgan yolg‘iz ayol?",
                "Mariyam (a.s)",
                "Mariyam (a.s)",
                "Osiyo Firavnning xotini",
                "Halima onamiz"
        ));
        data.add(new QuestionData(
                "Alloh taoloning qaysi goʻzal ismi maʼbud – ibodat qilingan Zot – maʼnosini anglatadi?",
                "Alloh",
                "Alloh",
                "Quddus",
                "Vadud"
        ));
        data.add(new QuestionData(
                "Moida surasing o'zbekcha ma'nosi nima?",
                "Dasturhon",
                "O'ljalar",
                "Dasturhon",
                "Chorvalar"
        ));
        data.add(new QuestionData(
                "Abu Bakr Siddiq (ra) Rasululloh SAV dan necha yosh katta edilar",
                "2",
                "1",
                "3",
                "2"
        ));
        data.add(new QuestionData(
                "Abdulloh qaysi ulug' sahobaning asl ismi edi?",
                "Abu Bakr Siddiq (ra)",
                "Abu Bakr Siddiq (ra)",
                "Usmon (ra)",
                "Muoz ibn Jabal (ra)"
        ));
        data.add(new QuestionData(
                "Ovozlari juda yoqimli bolgan va temirchilikni ixtiro qilgan payg'ambar (as)",
                "Dovud (as)",
                "Dovud (as)",
                "Yusuf (as)",
                "Ayyub (as)"
        ));
        data.add(new QuestionData(
                "SubhanAlloh so'zining ma'nosi?",
                "Alloh Pok",
                "Alloh Rahmli",
                "Alloh Pok",
                "Alloh Mehribon"
        ));
        data.add(new QuestionData(
                "Eng ko'p hadis rivoyat qilgan sahoba?",
                "Abu Hurayra (ra)",
                "Anas ibn Molik (ra)",
                "Abdulloh ibn Umar (ra)",
                "Abu Hurayra (ra)"
        ));
        data.add(new QuestionData(
                "Taxoratning farzi nechta?",
                "4",
                "5",
                "4",
                "6"
        ));
        data.add(new QuestionData(
                "Qaysi payg'ambar (as) ayollari sochlarini sotib, yegulik olib kelganlar?",
                "Ayyub (as)",
                "Solih (as)",
                "Ayyub (as)",
                "Lut (as)"
        ));
        data.add(new QuestionData(
                "Odam (as) bilan Havvo onamiz yerga tushirilganlaridan keyin qayerda uchrashganlar?",
                "Arofat tog'ida",
                "Savr tog'ida",
                "Minoda",
                "Arofat tog'ida"
        ));
        data.add(new QuestionData(
                "Qur'oni Karimning qalbi hisoblangan sura qaysi?",
                "Yasin",
                "Baqara",
                "Yasin",
                "Fotiha"
        ));
        data.add(new QuestionData(
                "Rasululloh SAV tug'ilgan yillari qanday nomlangan?",
                "Fil yili",
                "Fil yili",
                "Qo'y yili",
                "Abobil yili"
        ));
        data.add(new QuestionData(
                "Vafotidan song Usmon ibn Maz'un (ra) bilan qabrda ham yonma-yon bolishni va'dalashgan sahoba kim edi?",
                "Abdurahmon ibn Avf (ra)",
                "Abdurahmon ibn Avf (ra)",
                "Sa'd ibn Abu Vaqqos (ra)",
                "Muoz ibn Jab'al (ra)"
        ));
        data.add(new QuestionData(
                "Kahf surasida aytilgan qissalardan biri?",
                "Ikki bog' egasi qissasi",
                "Ikki bog' egasi qissasi",
                "Namrud qissasi",
                "Iso (as) qissasi"
        ));
        data.add(new QuestionData(
                "Qur'onda ismi aytilgan sahoba kim?",
                "Zayd ibn Horisa (ra)",
                "Abu Bakr (ra)",
                "Umar ibn Hattob (ra)",
                "Zayd ibn Horisa (ra)"
        ));
        data.add(new QuestionData(
                "'Oli Imron' surasi nechta oyatdan iborat?",
                "227",
                "227",
                "220",
                "286"
        ));
        data.add(new QuestionData(
                "Dajjolni qaysi payg'ambar (as) o'ldiradi?",
                "Iso (as)",
                "Muso (as)",
                "Muhammad SAV",
                "Iso (as)"
        ));
        data.add(new QuestionData(
                "Qaysi sura Allohga tasbeh aytish bilan boshlanib, hamd aytish bilan tugaydi?",
                "Isro",
                "Soffat",
                "Zumar",
                "Isro"
        ));
        data.add(new QuestionData(
                "Qur'onning 1/4 qismiga teng ekani aytilgan sura qaysi?",
                "Kofirun",
                "Ixlos",
                "Kofirun",
                "Yasin"
        ));
        data.add(new QuestionData(
                "Salomga alik olish qanday amal?",
                "Vojib",
                "Sunnat",
                "Vojib",
                "Farz"
        ));
        data.add(new QuestionData(
                "Salom berish qanday amal?",
                "Sunnat",
                "Sunnat",
                "Vojib",
                "Farz"
        ));
        data.add(new QuestionData(
                "'Gunohi sag'ira' deb qanday gunohlar aytiladi?",
                "Kichik gunohlar",
                "Katta gunohlar",
                "Kichik gunohlar",
                "Shirk"
        ));
        data.add(new QuestionData(
                "Abu Bakr Siddiq Usoma jangga ketayotganda kimni qoldirib ketishini iltimos qiladi?",
                "Umar ibn Hattobni",
                "Umar ibn Hattobni",
                "O'g'lini",
                "Ali ibn Abu Tolibni"
        ));
        data.add(new QuestionData(
                "Rasululloh SAV o'lganlarida 'U zot o'lmagan' deb kim baqirdi?",
                "Umar ibn Hattob (ra)",
                "Ali ibn Abu Tolib (ra)",
                "Umar ibn Hattob (ra)",
                "Abu Bakr (ra)"
        ));
        data.add(new QuestionData(
                "Nomoz qachon farz bo'lgan?",
                "Isro kechasida",
                "Handaq g'azotida",
                "Isro kechasida",
                "Makka fathida"
        ));
        data.add(new QuestionData(
                "Islom tarixida 1-elchi qaysi sahoba bo'lgan?",
                "Mus'ab ibn Umayr (ra)",
                "Mus'ab ibn Umayr (ra)",
                "Holid ibn Valid (ra)",
                "Abbos ibn Abdulmutallib (ra)"
        ));
        data.add(new QuestionData(
                "Bilol ibn Ravoh (ra) ni kim qullikdan ozod qilgan?",
                "Abu Bakr Siddiq (ra)",
                "Umar ibn Hattob (ra)",
                "Abu Bakr Siddiq (ra)",
                "Rasululloh SAV"
        ));
        data.add(new QuestionData(
                "Rasululloh SAV qaysi qizlari Badr jangida vafot etgan?",
                "Rukaya (ra)",
                "Ummu Kulsum (ra)",
                "Fotima (ra)",
                "Rukaya (ra)"
        ));
        data.add(new QuestionData(
                "Mahr berish qanday amal?",
                "Farz",
                "Vojib",
                "Sunnat",
                "Farz"
        ));
        data.add(new QuestionData(
                "Eri vafot etgan ayolning iddada o'tirish muddati qancha?",
                "4 oy 10 kun",
                "6 oy",
                "6 oy 10 kun",
                "4 oy 10 kun"
        ));
        data.add(new QuestionData(
                "Safiy so'zining ma'nosi nima?",
                "Tanlab olingan",
                "Tanlab olingan",
                "O'xshash",
                "Poklangan"
        ));
        data.add(new QuestionData(
                "Zakot so'zining ma'nosi nima?",
                "Soflik, Ko'payish",
                "Soflik, Ko'payish",
                "Rizq, G'amxorlik",
                "Poklik, O'sish"
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
        startActivity(new Intent(IslamUzbekActivity2.this, StartActivityUzbek.class));
        finish();
    }
}