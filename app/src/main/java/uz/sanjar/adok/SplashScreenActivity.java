package uz.sanjar.adok;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import uz.sanjar.adok.start.StartLanguage;


public class SplashScreenActivity extends AppCompatActivity {
    private static int Splash_timeout = 2500;
    TextView wel, knowledge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        wel = findViewById(R.id.splash_screen);
        knowledge = findViewById(R.id.splash_text);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent splashIntent = new Intent(SplashScreenActivity.this, StartLanguage.class);
                startActivity(splashIntent);
                finish();
            }
        }, Splash_timeout);
        Animation myAnimation = AnimationUtils.loadAnimation(SplashScreenActivity.this, R.anim.animation2);
        wel.startAnimation(myAnimation);

        Animation myAnimation1 = AnimationUtils.loadAnimation(SplashScreenActivity.this, R.anim.animation1);
        knowledge.startAnimation(myAnimation1);

    }
}