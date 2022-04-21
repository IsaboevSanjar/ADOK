package uz.sanjar.adok.info;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import uz.sanjar.adok.R;
import uz.sanjar.adok.start.StartActivityEnglish;

public class InfoActivityEnglish extends AppCompatActivity {
    private TextView telegram;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_english);
        telegram = findViewById(R.id.telegram_info);
        telegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoUrl("https://t.me/sanjar_isaboev");

            }
        });


    }

    private void gotoUrl(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    public void onClickInfo(View view) {
        Intent intent = new Intent(InfoActivityEnglish.this, StartActivityEnglish.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(InfoActivityEnglish.this, StartActivityEnglish.class);
        startActivity(intent);
        finish();
    }


}