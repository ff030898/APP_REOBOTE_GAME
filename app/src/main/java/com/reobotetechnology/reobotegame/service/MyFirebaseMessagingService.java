package com.reobotetechnology.reobotegame.service;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.reobotetechnology.reobotegame.config.ConfigurationFireBase;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private DatabaseReference firebaseRef = ConfigurationFireBase.getFirebaseDataBase();
    private FirebaseAuth autenticacao = ConfigurationFireBase.getFirebaseAutenticacao();
    private FirebaseUser user = autenticacao.getCurrentUser();
    String token;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage notificacao) {
        super.onMessageReceived(notificacao);


        if (notificacao.getNotification() != null) {
            String titulo = notificacao.getNotification().getTitle();
            String corpo = notificacao.getNotification().getBody();

            Log.i("Notificacao", "recebida titulo: " + titulo + "\ncorpo: " + corpo);
        }

    }


    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }
}
