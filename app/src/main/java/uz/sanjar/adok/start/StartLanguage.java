package uz.sanjar.adok.start;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import uz.sanjar.adok.R;

public class StartLanguage extends AppCompatActivity {

    private TextView englishChosen, uzbekChosen;
    private ImageView exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_language);

        loadViews();

        // TODO: 4/7/2022 Cache da saqlab olishda problemalar bor/ Result settings with loadData

    }

    private void loadViews() {
        exitButton = findViewById(R.id.exit_whole_game);
        englishChosen = findViewById(R.id.english_chosen);
        uzbekChosen = findViewById(R.id.uzbek_chosen);
    }

    public void OnClickLanguage(View view) {
        switch (view.getId()) {
            case R.id.english_chosen: {
                Intent intent = new Intent(StartLanguage.this, StartActivityEnglish.class);
                startActivity(intent);
                break;
            }
            case R.id.uzbek_chosen: {
                Intent intent = new Intent(StartLanguage.this, StartActivityUzbek.class);
                startActivity(intent);
                break;
            }
            case R.id.exit_whole_game: {
                finish();
                break;
            }

        }
    }

    public void onClickExit(View view) {
        finish();
    }
}