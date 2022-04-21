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
import uz.sanjar.adok.islam.i.ilamquestions1.result.IslamResultEng;
import uz.sanjar.adok.start.StartActivityEnglish;

public class IslamEnglishActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_islam_english);
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
                String key = "Islam deen";

                Bundle bundle = new Bundle();
                bundle.putInt(IslamResultEng.KEY_TRUES, trueCount);
                bundle.putString(IslamResultEng.KEY_TYPE, key);
                bundle.putInt(IslamResultEng.KEY_MISTAKES, falseCount);

                Intent intent = new Intent(IslamEnglishActivity.this, IslamResultEng.class);
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
                            Toast.makeText(IslamEnglishActivity.this, "Choose the answer!!!", Toast.LENGTH_SHORT).show();
                        }

                    }

                }
            });


            comeBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(IslamEnglishActivity.this, StartActivityEnglish.class);
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
                Intent intent = new Intent(IslamEnglishActivity.this, StartActivityEnglish.class);
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
                "Which sahaba did Prophet Muhammad (ﷺ) help to become free from being a slave by planting 300+ date palm trees? ",
                "Salman al Farisi (ra)",
                "Bilal ibn Rabah (ra)",
                "Zayd ibn Harisa (ra)",
                "Salman al Farisi (ra)"
        ));
        data.add(new QuestionData(
                "What is the virtue of reciting Ayatul Kursi before going to bed at night to sleep?",
                "You are protected from harm till sunrise",
                "Takes away hunger",
                "You are protected from harm till sunrise",
                "Gives you strength"
        ));
        data.add(new QuestionData(
                "Which Prophet (as) had control of the Jinn and was able to talk to animals?",
                "Sulaiman (as)",
                "Yunus (as)",
                "Dawud (as)",
                "Sulaiman (as)"
        ));
        data.add(new QuestionData(
                "What does Zam Zam mean?",
                "Stop",
                "Stop",
                "Drink",
                "Holy water"
        ));
        data.add(new QuestionData(
                "What is Az-Zaqqum?",
                "Food for the people of hellfire",
                "Drink for the people of hellfire",
                "Food for the people of hellfire",
                "Home for the people of hellfire"
        ));
        data.add(new QuestionData(
                "What was the relation between Prophet Musa (alayhi as-salaam) & Prophet Haroon (alayhi as-salaam)?",
                "Brothers ",
                "Cousins ",
                "Brothers",
                "Friends"
        ));
        data.add(new QuestionData(
                "Which 2 surahs are for seeking protection in Allah from evil-eye & witchcraft?",
                "Surah Falaq,Surah Naas",
                "Surah Falaq,Surah Naas",
                "Surah Niso,Surah Yaseen",
                "Surah Falaq,Surah Ikhlas"
        ));

        data.add(new QuestionData(
                "Which of the following is not an example of Major Shirk?",
                "Showing off",
                "Denying Allah & His religion",
                "Showing off",
                "Going to a pious person’s grave for blessings"
        ));
        data.add(new QuestionData(
                "What does Allah’s name Al-Mu’izz mean?",
                "The One who honours ",
                "The One who sees it all",
                "The One who will judge",
                "The One who honours "
        ));
        data.add(new QuestionData(
                "Who was a first cousin of Prophet Muhammad (peace be upon him)?",
                "Abdullah ibn al-Abbas (ra) ",
                "Abdullah ibn al-Abbas (ra) ",
                "Abdullah ibn Umar (ra)",
                "Khalid ibn Waleed (ra)"
        ));
        data.add(new QuestionData(
                "Who will get their book of deeds in the right hand on the Day of Judgment?",
                "The believers ",
                "The hypocrites",
                "The leaders",
                "The believers "
        ));
        data.add(new QuestionData(
                "What issue was Imam al-Bukhari (rh) born with and how as it fixed?",
                "Blind & fixed with mother’s dua",
                "Deaf & fixed with mother’s dua",
                "Blind & fixed with mother’s dua",
                "Deaf & fixed with medicine"
        ));
        data.add(new QuestionData(
                "In Jannah, what is Tuba?",
                "A tree",
                "A fruit",
                "A tree",
                "A river"
        ));
        data.add(new QuestionData(
                "What does Allah’s name Al-Ghani mean?",
                "The Independent",
                "The Independent",
                "The Wise",
                "The Protector"
        ));
        data.add(new QuestionData(
                "Which Surah was favoured over the rest of the Quran with two prostrations?",
                "Surah Hajj ",
                "Surah Tawbah",
                "Surah Yaseen",
                "Surah Hajj "
        ));

        data.add(new QuestionData(
                "Which Angel will blow the horn to signal the Day of Judgement?",
                "Israfeel (as)",
                "Israfeel (as)",
                "Mikaeel (as)",
                "Jibraeel (as)"
        ));
        data.add(new QuestionData(
                "Which Prophet (as) addressed the ruler King Nimrod?",
                "Prophet Ibrahim (as)",
                "Prophet Isa (as)",
                "Prophet Ibrahim (as)",
                "Prophet Yusuf (as)"
        ));
        data.add(new QuestionData(
                "Which uncle of Prophet Muhammad (peace be upon him) didn’t accept Islam?",
                "Abu Lahab",
                "Abdul Muttalib",
                "Abdul Muttalib",
                "Abu Lahab"
        ));
        data.add(new QuestionData(
                "Which Surah does not begin with the basmallah?",
                "Surah Tawbah",
                "Surah Rahman",
                "Surah Tawbah",
                "Surah Hajj"
        ));
        data.add(new QuestionData(
                "How did Abu Hurairah (ra) stop forgetting and had an amazing memory?",
                "The Prophet ﷺ blessed his (ra) clothing sheet ",
                "The Prophet ﷺ blessed his (ra) clothing sheet ",
                "He (ra) drank a lot of milk",
                "The Prophet ﷺ made dua for him (ra)"
        ));
        data.add(new QuestionData(
                "What is Aboo Bakr (r)’s full name?",
                "Abdullah ibn Uthman",
                "Abdullah ibn Abdur Rahman",
                "Abdullah ibn Uthman",
                "Abdur Rahman ibn Uthman"
        ));
        data.add(new QuestionData(
                "Why does Allah allow suffering to happen? 2 answers.",
                "Because life is a test",
                "Because we were born to suffer",
                "Because life is a test",
                "Because we appreciate the good even more"
        ));
        data.add(new QuestionData(
                "How many gates of Jannah are there?",
                "8",
                "9",
                "8",
                "99"
        ));
        data.add(new QuestionData(
                "Which fruit is mentioned in the Quran?",
                "Grapes",
                "Apples",
                "Grapes",
                "Mangoes"
        ));
        data.add(new QuestionData(
                "What is another name given to the Quran?",
                "Al-Furqan",
                "Ar-Rahmaan",
                "Al-Furqan",
                "Al-Sahih"
        ));
        data.add(new QuestionData(
                "How many Prophets did Prophet Muhammad ﷺ meet on the different heavens during Isra wal Miraaj?",
                "8",
                "8",
                "7",
                "5"
        ));
        data.add(new QuestionData(
                "In which battle did some Muslim archers disobey the order of Prophet Muhammad ﷺ ?",
                "Battle of Uhud",
                "Battle of Badr",
                "Battle of Uhud",
                "Battle of Khaybar"
        ));
        data.add(new QuestionData(
                "At what age does a person become an adult in Islam?",
                "Puberty",
                "15 years old",
                "10 years old",
                "Puberty"
        ));
        data.add(new QuestionData(
                "Who is one of the 4 best women of Jannah?",
                "Aasiya (wife of Firawn)",
                "Aisha (wife of Muhammad ﷺ)",
                "Hajar (wife of Ibrahim (as))",
                "Aasiya (wife of Firawn)"
        ));
        data.add(new QuestionData(
                "Which Prophet (as) is mentioned the most in the Quran by name?",
                "Prophet Musa (as)",
                "Prophet Musa (as)",
                "Prophet Adam (as)",
                "Prophet Muhammad ﷺ"
        ));
        data.add(new QuestionData(
                "What is the linguistic meaning of Taraweeh?",
                "To rest",
                "To pray",
                "To stay up",
                "To rest"
        ));
        data.add(new QuestionData(
                "What is the linguistic meaning of Ramadan?",
                "To burn the sins",
                "To fast",
                "To burn the sins",
                "To race"
        ));
        data.add(new QuestionData(
                "During which battle did Prophet Muhammad (s) strIke a massive rock once and it got smashed into pieces?",
                "Battle of Khandaq",
                "Battle of Khandaq",
                "Battle of Tabuk",
                "Battle of Uhud"
        ));
        data.add(new QuestionData(
                "What does Allah’s name Al-Musawwir mean?",
                "The Fashioner",
                "The Most High",
                "The Provider",
                "The Fashioner"
        ));
        data.add(new QuestionData(
                "Who was the only human to whom Allah blew the soul into him by Himself?",
                "Prophet Adam (as)",
                "Prophet Muhammad (ﷺ)",
                "Prophet Adam (as)",
                "Prophet Musa (as)"
        ));
        data.add(new QuestionData(
                "Which side did Prophet Muhammad ﷺ encourage us not to sleep on?",
                "Stomach side",
                "Back side",
                "Left side",
                "Stomach side"
        ));
        data.add(new QuestionData(
                "What is the Arabic word for Allah’s inspiration or revelation to a Prophet (as) called?",
                "Wahyi",
                "Seerah",
                "Wahyi",
                "Daleel"
        ));
        data.add(new QuestionData(
                "When does Allah say we should say Innā lillāhi wa innā ilayhi rāji‘ūn?",
                "When any calamity happens",
                "When any calamity happens",
                "Only at someone’s death",
                "When you read it in the Quran"
        ));
        data.add(new QuestionData(
                "Which Prophet (as) was tested with a severe illness by Allah?",
                "Prophet Ayub (as)",
                "Prophet Musa (as)",
                "Prophet Ayub (as)",
                "Prophet Idris (as)"
        ));
        data.add(new QuestionData(
                "Allah says, with His knowledge, He is more closer to us than our ________?",
                "Jugular Vein",
                "Jugular Vein",
                "Heart",
                "Thoughts"
        ));
        data.add(new QuestionData(
                "How many times more reward does one gets for praying salah inside Masjid al Aqsa?",
                "500 times more",
                "500 times more",
                "1,000 times more",
                "50,000 times more"
        ));
        data.add(new QuestionData(
                "Which battle happened in Ramadan?",
                "Battle of Badr",
                "Battle of Hunayn",
                "Battle of Khandaq",
                "Battle of Badr"
        ));
        data.add(new QuestionData(
                "Which of the following is the name of Allah with the meaning – The Most Loving One?",
                "Al-Wadud",
                "Al-Wadud",
                "Al-Majeed",
                "Al-Muqtadir"
        ));
        data.add(new QuestionData(
                "Which wife of the Prophet ﷺ had children and was a widow when he ﷺ married her?",
                "Umm Salamah (r)",
                "Umm Salamah (r)",
                "Zaynab bint Jahsh (r)",
                "Hafsa (r)"
        ));
        data.add(new QuestionData(
                "What is the name of Madinah in the Quran?",
                "Yathrib",
                "Yathrib",
                "Quds",
                "Bakkah"
        ));
        data.add(new QuestionData(
                "Why we should not curse time?",
                "Because Allah says He is Time",
                "Because Allah says He is Time",
                "Because Allah says He will cut our time",
                "Because it will bring good luck"
        ));
        data.add(new QuestionData(
                "The angels at the Battle of Badr copied which sahabi’s yellow turban?",
                "Zubayr ibn Awwam (r)",
                "Zubayr ibn Awwam (r)",
                "Sa’ad ibn Abi Waqqas (r)",
                "Ali ibn Abi Taalib (r)"
        ));
        data.add(new QuestionData(
                "When will the Prophet ﷺ intercede for us?",
                "On the Day of Resurrection",
                "In his ﷺ grave",
                "On the Sirat",
                "On the Day of Resurrection"
        ));
        data.add(new QuestionData(
                "How many Prophets (as) are mentioned by name in the Quran?",
                "25",
                "25",
                "313",
                "124000"
        ));
        data.add(new QuestionData(
                "Which is not one of the 5 pillars of Islam?",
                "Paradise",
                "Paradise",
                "Zakat",
                "Ramadan"
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
        startActivity(new Intent(IslamEnglishActivity.this, StartActivityEnglish.class));
        finish();
    }
}