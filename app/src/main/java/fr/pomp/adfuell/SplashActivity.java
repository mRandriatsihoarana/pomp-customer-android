package fr.pomp.adfuell;


import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;

import fr.pomp.adfuell.ws.IWSDelegate;

public class SplashActivity extends BaseActivity {
    /**
     * Nombre de secondes pour quitter l'ecran Splash
     */
    public static int SECOND_TO_LEAVE_SPLASH = 2*1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new CountDownTimer(SECOND_TO_LEAVE_SPLASH, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                getTokenOuth();
            }
        }.start();


    }

    void getTokenOuth(){
        getToken(new IWSDelegate() {
            @Override
            public void onFaillure(Object obj) {
                startActivityMain();
            }

            @Override
            public void onSuccess(Object obj) {
                startActivityMain();
            }
        });
    }

    /**
     * Fonction pour lancer l'ecran principal
     */
    void startActivityMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }




}
