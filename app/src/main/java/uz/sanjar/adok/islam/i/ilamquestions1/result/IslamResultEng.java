package uz.sanjar.adok.islam.i.ilamquestions1.result;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

import uz.sanjar.adok.R;
import uz.sanjar.adok.islam.i.ilamquestions1.IslamEnglishActivity;
import uz.sanjar.adok.islam.i.ilamquestions1.islamquestion2.IslamEnglishActivity2;
import uz.sanjar.adok.start.StartActivityEnglish;

public class IslamResultEng extends AppCompatActivity {
    public static final String KEY_TRUES = "true";
    public static final String KEY_MISTAKES = "mistakes";
    public static final String KEY_TYPE = "type";

    private FrameLayout frameLayoutNextStep;
    private TextView textViewNextStep;
    private FrameLayout frameLayoutTryAgain;
    private TextView textViewTryAgain;
    private LottieAnimationView brainMathAnimation;

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_islam_result_eng);

        loadViews();
        Bundle bundle = getIntent().getExtras();

        int trues = bundle.getInt(KEY_TRUES);
        int mistakes = bundle.getInt(KEY_MISTAKES);
        String type = bundle.getString(KEY_TYPE);


        textView.setText("Trues: " + trues + "\n" + "Mistakes: " + mistakes + "\n" + "Game type: " + type + " 1");


        if (trues < 46) {
            frameLayoutTryAgain.setVisibility(View.VISIBLE);
            frameLayoutNextStep.setVisibility(View.INVISIBLE);
            textViewTryAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(IslamResultEng.this, IslamEnglishActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        } else {
            frameLayoutTryAgain.setVisibility(View.INVISIBLE);
            brainMathAnimation.setVisibility(View.INVISIBLE);
            frameLayoutNextStep.setVisibility(View.VISIBLE);
            textViewNextStep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(IslamResultEng.this, IslamEnglishActivity2.class);
                    startActivity(intent);
                    finish();
                }
            });
        }


    }


    private void loadViews() {
        frameLayoutNextStep = findViewById(R.id.frame_next_step);
        frameLayoutTryAgain = findViewById(R.id.frame_try_again);

        textViewNextStep = findViewById(R.id.next_step_textview);
        textViewTryAgain = findViewById(R.id.try_again_textview);

        brainMathAnimation = findViewById(R.id.math_brain_result);

        textView = findViewById(R.id.result_date);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(IslamResultEng.this, StartActivityEnglish.class));
        finish();
    }

}