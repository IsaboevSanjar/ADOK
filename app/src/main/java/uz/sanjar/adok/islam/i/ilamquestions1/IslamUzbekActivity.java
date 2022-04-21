package uz.sanjar.adok.islam.i.ilamquestions1;

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
import uz.sanjar.adok.islam.i.ilamquestions1.result.IslamResultUz;
import uz.sanjar.adok.start.StartActivityUzbek;

public class IslamUzbekActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_islam_uzbek);
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
                String key = "Islom dini";

                Bundle bundle = new Bundle();
                bundle.putInt(IslamResultUz.KEY_TRUES, trueCount);
                bundle.putString(IslamResultUz.KEY_TYPE, key);
                bundle.putInt(IslamResultUz.KEY_MISTAKES, falseCount);

                Intent intent = new Intent(IslamUzbekActivity.this, IslamResultUz.class);
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
                            Toast.makeText(IslamUzbekActivity.this, "Javob tanlang!!!", Toast.LENGTH_SHORT).show();
                        }

                    }

                }
            });


            comeBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(IslamUzbekActivity.this, StartActivityUzbek.class);
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
                Intent intent = new Intent(IslamUzbekActivity.this, StartActivityUzbek.class);
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
                "Qaysi Payg'ambar (as) jinlarni boshqargan va hayvonlar bilan gaplasha olgan?",
                "Sulayman (as)",
                "Yunus (as)",
                "Dovud (as)",
                "Sulayman (as)"
        ));
        data.add(new QuestionData(
                "Zam Zam so'zining ma'nosi nima?",
                "To'xta",
                "Ich",
                "To'xta",
                "Muqaddas suv"
        ));
        data.add(new QuestionData(
                "Az-Zaqqum nima?",
                "Do'zax ahli uchun ovqat",
                "Do'zaxiylar uchun ichimlik",
                "Do'zax ahli uchun ovqat",
                "Do'zax ahli uchun uy"
        ));
        data.add(new QuestionData(
                "Muso (as) bilan Horun (as) o'rtasida qanday munosabat bor edi?",
                "Aka-ukalik",
                "Qo'shnilik",
                "Aka-ukalik",
                "Do'stlik"
        ));
        data.add(new QuestionData(
                "Qaysi 2 sura yomon ko'z va jodudan Allohdan panoh so'rash uchun?",
                "Falaq,Naas",
                "Falaq,Naas",
                "Niso,Yaseen",
                "Falaq,Ixlos"
        ));

        data.add(new QuestionData(
                "Quyidagilardan qaysi biri katta Shirkga misol emas?",
                "O'zini ko'rsatish",
                "Allohni va Uning dinini inkor qilish",
                "O'zini ko'rsatish",
                "Solih kishining qabriga ziyorat uchun borish"
        ));
        data.add(new QuestionData(
                "Allohning Al-Muizz ismi nimani anglatadi?",
                "Ulug'lovchi",
                "Hamma narsani ko'rguvchi",
                "Jazolovchi",
                "Ulug'lovchi"
        ));
        data.add(new QuestionData(
                "Payg'ambarimiz Muhammad SAVning birinchi amakivachchalari kim edi?",
                "Abdulloh ibn al-Abbos (ra) ",
                "Abdulloh ibn al-Abbos (ra) ",
                "Abdulloh ibn Umar (ra)",
                "Xolid ibn Waleed (ra)"
        ));
        data.add(new QuestionData(
                "Kim qiyomat kuni amal daftarini o'ng qo'liga oladi?",
                "Mo'minlar",
                "Munofiqlar",
                "Yetakchilar",
                "Mo'minlar"
        ));
        data.add(new QuestionData(
                "Imom al-Buxoriy qaysi muammo bilan tug'ilgan va u qanday hal qilingan?",
                "Ko'r va ona duosi bilan shifo topgan",
                "Kar va onasining duosi bilan shifo topgan",
                "Ko'r va ona duosi bilan shifo topgan",
                "Kar va kasalxonada davolangan"
        ));
        data.add(new QuestionData(
                "Jannatdagi Tuba nima?",
                "Daraxt",
                "Meva",
                "Daraxt",
                "Daryo"
        ));
        data.add(new QuestionData(
                "Allohning Al-G'ani ismi nimani anglatadi?",
                "Mustaqil",
                "Mustaqil",
                "Donishmand",
                "Himoyachi"
        ));
        data.add(new QuestionData(
                "Qaysi sura Qur'onning qolgan qismidan ikki sajda bilan afzal etildi?",
                "Hajj ",
                "Tavba",
                "Yaseen",
                "Hajj "
        ));
        data.add(new QuestionData(
                "Qaysi farishta qiyomat kunini bildirish uchun sur chaladi?",
                "Isrofil (as)",
                "Isrofil (as)",
                "Mikoil (as)",
                "Jibroil (as)"
        ));
        data.add(new QuestionData(
                "Qaysi Payg'ambar (as) hukmdor podshoh Namrudga xitob qilgan?",
                "Ibrohim (as)",
                "Iso (as)",
                "Ibrohim (as)",
                "Yusuf (as)"
        ));
        data.add(new QuestionData(
                "Payg'ambarimiz Muhammad SAV qaysi amakilari Islomni qabul qilmaganlar?",
                "Abu Lahab",
                "Hamza",
                "Abdul Muttalib",
                "Abu Lahab"
        ));
        data.add(new QuestionData(
                "Qaysi sura basmala bilan boshlanmaydi?",
                "Tavba",
                "Rahman",
                "Tavba",
                "Hajj"
        ));
        data.add(new QuestionData(
                "Qanday qilib Abu Hurayra (ra) unutishni to'xtatdi va ajoyib xotiraga ega bo'ldi?",
                "Rasululloh SAV kiyimlariga duo qildilar",
                "Rasululloh SAV kiyimlariga duo qildilar",
                "Ko'p sut ichdi",
                "Rasululloh SAV  U (ra) uchun duo qildi"
        ));
        data.add(new QuestionData(
                "Abu Bakr (ra) ning to'liq ismi nima?",
                "Abdulloh ibn Uthman",
                "Abdulloh ibn Abdur Rahman",
                "Abdulloh ibn Uthman",
                "Abdur Rahman ibn Uthman"
        ));
        data.add(new QuestionData(
                "Nega Alloh azob-uqubatlarga ruxsat beradi?",
                "Chunki hayot bu test",
                "Chunki biz azob chekish uchun tug'ilganmiz",
                "Chunki hayot bu test",
                "Chunki biz yaxshilikni yanada qadrlaymiz"
        ));
        data.add(new QuestionData(
                "Jannatning nechta darvozasi bor?",
                "8",
                "9",
                "8",
                "99"
        ));
        data.add(new QuestionData(
                "Qur'onda qaysi meva zikr qilingan?",
                "Uzum",
                "Uzum",
                "Xurmo",
                "Olma"
        ));
        data.add(new QuestionData(
                "Qur'onga yana qanday nom berilgan?",
                "Al-Furqan",
                "Ar-Rahman",
                "Al-Furqan",
                "Al-Sahih"
        ));
        data.add(new QuestionData(
                "Payg'ambarimiz Muhammad SAV Isro val Me'rojda turli osmonlarda nechta payg'ambar bilan uchrashgan?",
                "8",
                "8",
                "7",
                "5"
        ));
        data.add(new QuestionData(
                "Qaysi jangda musulmon kamonchilar Payg'ambarimiz Muhammad SAV ning amrlariga bo'ysunmadilar?",
                "Uhud jangi",
                "Badr jangi",
                "Uhud jangi",
                "Xaybar jangi"
        ));
        data.add(new QuestionData(
                "Islomda inson necha yoshdan voyaga yetadi?",
                "Balog'at yoshi",
                "15 yosh",
                "10 yosh",
                "Balog'at yoshi"
        ));
        data.add(new QuestionData(
                "Jannatning eng yaxshi 4 ayolidan biri kim?",
                "Osiyo (Firavnning ayoli)",
                "Oisha (Muhammad SAV ning ayoli) ",
                "Hajar (Ibrahim (as)ning ayoli)",
                "Osiyo (Firavnning ayoli)"
        ));
        data.add(new QuestionData(
                "Qur'onda qaysi Payg'ambar (as)ning ismi ko'proq tilga olingan?",
                "Muso (as)",
                "Muso (as)",
                "Odam (as)",
                "Muhammad ﷺ"
        ));
        data.add(new QuestionData(
                "Tarovehning lingvistik ma'nosi nima?",
                "Dam olish",
                "Nomoz o'qish",
                "Tik turish",
                "Dam olish"
        ));
        data.add(new QuestionData(
                "Ramazonning lingvistik ma'nosi nima?",
                "Gunohlarni yoqish",
                "Gunohlarni yoqish",
                "Och yurish",
                "Poyga qilish"
        ));
        data.add(new QuestionData(
                "Payg'ambarimiz Muhammad SAV qaysi jangda bir marta katta toshga duch kelgan va uni parchalagan?",
                "Xandoq jangi",
                "Xandoq jangi",
                "Tabuk jangi",
                "Uhud jangi"
        ));
        data.add(new QuestionData(
                "Allohning Al-Musavvir ismi nimani anglatadi?",
                "Modachi",
                "Eng oliy",
                "Boshqaruvchi",
                "Modachi"
        ));
        data.add(new QuestionData(
                "Alloh taoloning o'zi unga ruhini puflagan yagona inson kim edi?",
                "Odam (as)",
                "Muhammad (ﷺ)",
                "Odam (as)",
                "Muso (as)"
        ));
        data.add(new QuestionData(
                "Payg'ambarimiz Muhammad SAV bizni qaysi taraflama uxlamaslikka undaganlar?",
                "Qorin tomon bilan",
                "Orqa tomon bilan",
                "Chap tomon bilan",
                "Qorin tomon bilan"
        ));
        data.add(new QuestionData(
                "Alloh taoloning Payg'ambarga payg'ambarlikni bildiruvchi arabcha so'z nima deb ataladi?",
                "Vahiy",
                "Sirah",
                "Vahiy",
                "Dalil"
        ));
        data.add(new QuestionData(
                "Alloh qachon 'Innā lillāhi wa innā ilayhi rāji‘ūn' deb aytishimiz kerakligini eytdi?",
                "Har qanday falokat sodir bo'lganda",
                "Har qanday falokat sodir bo'lganda",
                "Faqat birovning o'limida",
                "Qur'onda o'qiganingizda"
        ));
        data.add(new QuestionData(
                "Qaysi Payg'ambar (as) Alloh tomonidan og'ir kasallik bilan sinovdan o'tgan?",
                "Ayub (as)",
                "Muso (as)",
                "Ayub (as)",
                "Idris (as)"
        ));
        data.add(new QuestionData(
                "Alloh taolo O'z ilmi bilan bizga ________dan yaqinroqdir, deydi?",
                "Yuguler Vena",
                "Yuguler Vena",
                "Yurak",
                "O'y-xayollar"
        ));
        data.add(new QuestionData(
                "Masjidul Aqsoda namoz o'qigan kishiga necha barobar ko'p savob beriladi?",
                "500",
                "500",
                "1,000",
                "50,000"
        ));
        data.add(new QuestionData(
                "Ramazon oyida qaysi jang bo'ldi?",
                "Badr",
                "Hunayn",
                "Xandaq",
                "Badr"
        ));
        data.add(new QuestionData(
                "Quyidagilardan qaysi biri Allohning ismlaridan - eng mehribondir?",
                "Al-Vadud",
                "Al-Vadud",
                "Al-Majid",
                "Al-Muqtadir"
        ));
        data.add(new QuestionData(
                "Rasululloh SAV ning qaysi xotiniga uylanganida u beva edi va farzandi bor edi?",
                "Ummu Salama (ra)",
                "Ummu Salama (ra)",
                "Zaynab bint Jahsh (ra)",
                "Hafsa (ra)"
        ));
        data.add(new QuestionData(
                "Qur'onda Madinaning nomi nima?",
                "Yasrib",
                "Yasrib",
                "Quddus",
                "Bakka"
        ));
        data.add(new QuestionData(
                "Nega biz vaqtni la'natlamasligimiz kerak?",
                "Chunki Alloh O'zini Vaqtdir, deydi",
                "Chunki Alloh O'zini Vaqtdir, deydi",
                "Chunki Alloh taolo bizning vaqtimizni qisqartiraman, deydi",
                "Chunki bu omad keltiradi"
        ));
        data.add(new QuestionData(
                "Rasululloh SAV qachon bizga shafoat qiladilar?",
                "Qiyomat kunida",
                "Duosida",
                "Sirotda",
                "Qiyomat kunida"
        ));
        data.add(new QuestionData(
                "Qur'onda nechta payg'ambar (as)ning nomlari zikr qilingan?",
                "25",
                "25",
                "313",
                "124000"
        ));
        data.add(new QuestionData(
                "Qaysi biri islomning 5 ta ustunidan biri emas?",
                "Jannat",
                "Jannat",
                "Zakot",
                "Ramzon"
        ));
        data.add(new QuestionData(
                "Qur’onda boylik haqida nima aytilgan?",
                "Odam imononining sinovi",
                "Allohning rahmati",
                "Odam imononining sinovi",
                "Kofirlikka qadam"
        ));
        data.add(new QuestionData(
                "Qur’oni Karimni qancha vaqtda nozil bo‘ldi?",
                "22 yilu,5 oy, 14 kun",
                "22 yilu,5 oy, 14 kun",
                "23 yil",
                "22 yilu,6 oy, 24 kun"
        ));
        data.add(new QuestionData(
                "Qur'on oyatlarini dastlab kim sanab chiqqan?",
                "Halima onamiz",
                "Oisha onamiz",
                "Abu Bakir siddiq (ra)",
                "Halima onamiz"
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
        startActivity(new Intent(IslamUzbekActivity.this, StartActivityUzbek.class));
        finish();
    }
}