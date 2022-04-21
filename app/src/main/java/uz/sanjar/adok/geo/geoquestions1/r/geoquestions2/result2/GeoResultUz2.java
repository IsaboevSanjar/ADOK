package uz.sanjar.adok.geo.geoquestions1.r.geoquestions2.result2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

import uz.sanjar.adok.R;
import uz.sanjar.adok.geo.geoquestions1.r.geoquestions2.GeoUzbekActivity2;
import uz.sanjar.adok.start.StartActivityUzbek;

public class GeoResultUz2 extends AppCompatActivity {
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
        setContentView(R.layout.activity_geo_result_uz2);

        loadViews();
        Bundle bundle = getIntent().getExtras();

        int trues = bundle.getInt(KEY_TRUES);
        int mistakes = bundle.getInt(KEY_MISTAKES);
        String type = bundle.getString(KEY_TYPE);


        textView.setText("To'g'ri javoblar: " + trues + "\n" + "Xato javoblar: " + mistakes + "\n" + "O'yin turi: " + type + " 2");


        if (mistakes >= 1) {
            frameLayoutTryAgain.setVisibility(View.VISIBLE);
            frameLayoutNextStep.setVisibility(View.INVISIBLE);
            textViewTryAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(GeoResultUz2.this, GeoUzbekActivity2.class);
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
                    showAlert();
                }
            });
        }


    }

    public void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Uzur\uD83D\uDE15")
                .setMessage("Bizda geografiyadan boshqa savollar yo'q \uD83D\uDE15. " +
                        "In Sha Alloh bu dastur hali yangilanadi va yangi savollar qo'shiladi. " +
                        "Raxmat\uD83D\uDE0A.");

        builder.setPositiveButton("Tugatish", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(GeoResultUz2.this, StartActivityUzbek.class);
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
        startActivity(new Intent(GeoResultUz2.this, StartActivityUzbek.class));
        finish();
    }

}