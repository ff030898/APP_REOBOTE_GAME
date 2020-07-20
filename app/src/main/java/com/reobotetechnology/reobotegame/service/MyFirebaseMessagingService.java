package com.reobotetechnology.reobotegame.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.config.ConfiguracaoFireBase;
import com.reobotetechnology.reobotegame.ui.partida.AmigosOnlineActivity;
import com.reobotetechnology.reobotegame.ui.partida.CarregarPartidaActivity;

import java.util.Objects;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private DatabaseReference firebaseRef = ConfiguracaoFireBase.getFirebaseDataBase();
    private FirebaseAuth autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();
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
