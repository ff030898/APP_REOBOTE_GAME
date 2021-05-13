package com.reobotetechnology.reobotegame.utils;

import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.reobotetechnology.reobotegame.config.ConfigurationFireBase;


@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class VerificyFollowUser {

    //Configurações do banco de dados
    private static FirebaseAuth autenticacao = ConfigurationFireBase.getFirebaseAutenticacao();
    private static DatabaseReference firebaseRef = ConfigurationFireBase.getFirebaseDataBase();

    private static boolean follow = true;

    public static boolean followUser(String idFollow){

        if (autenticacao.getCurrentUser() != null) {

            firebaseRef.child("follow").child(idFollow).addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    try {
                        follow = true;
                    }catch(Exception e){
                        follow = false;
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        return follow;
    }
}
