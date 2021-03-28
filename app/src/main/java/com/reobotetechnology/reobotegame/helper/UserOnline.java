package com.reobotetechnology.reobotegame.helper;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.reobotetechnology.reobotegame.config.ConfigurationFireBase;

import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class UserOnline extends Application implements Application.ActivityLifecycleCallbacks {



    private void setOnline(boolean enabled){
        DatabaseReference firebaseRef = ConfigurationFireBase.getFirebaseDataBase();
        FirebaseAuth autenticacao = ConfigurationFireBase.getFirebaseAutenticacao();
        FirebaseUser user = autenticacao.getCurrentUser();
        if(user != null) {
            String idUsuario = Base64Custom.codificarBase64((Objects.requireNonNull(user.getEmail())));
            DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);
            usuarioRef.child("online").setValue(enabled);
        }
    }


    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
       setOnline(true);
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        setOnline(false);
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        setOnline(false);
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        setOnline(false);
    }
}
