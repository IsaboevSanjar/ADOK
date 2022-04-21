package uz.sanjar.adok.start;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import uz.sanjar.adok.R;
import uz.sanjar.adok.geo.geoquestions1.r.GeoUzbekActivity;
import uz.sanjar.adok.info.InfoActivityEnglish;
import uz.sanjar.adok.islam.i.ilamquestions1.IslamUzbekActivity;
import uz.sanjar.adok.math.mathquestions1.MathActivityUzbek;

public class StartActivityUzbek extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout geographyTest, islamTest, mathTest, exitGame;
    private ImageView infoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_uzbek);
        loadViews();
        infoView.setOnClickListener(this);
        geographyTest.setOnClickListener(this);
        islamTest.setOnClickListener(this);
        mathTest.setOnClickListener(this);
        exitGame.setOnClickListener(this);
    }

    private void loadViews() {

        geographyTest = findViewById(R.id.geography_test);
        islamTest = findViewById(R.id.islam_test);
        mathTest = findViewById(R.id.math_test);
        exitGame = findViewById(R.id.exit_game);
        infoView = findViewById(R.id.info_view_uzbek);


    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.geography_test: {
                Intent intent = new Intent(StartActivityUzbek.this, GeoUzbekActivity.class);
                startActivity(intent);
                finish();
                break;
            }
            case R.id.islam_test: {
                Intent intent = new Intent(StartActivityUzbek.this, IslamUzbekActivity.class);
                startActivity(intent);
                finish();
                break;
            }
            case R.id.math_test: {
                Intent intent = new Intent(StartActivityUzbek.this, MathActivityUzbek.class);
                startActivity(intent);
                finish();
                break;
            }
            case R.id.exit_game: {
                finish();
                break;
            }
            case R.id.info_view_uzbek: {
                Intent intent = new Intent(StartActivityUzbek.this, InfoActivityEnglish.class);
                startActivity(intent);
                finish();
                break;
            }
        }
    }

}