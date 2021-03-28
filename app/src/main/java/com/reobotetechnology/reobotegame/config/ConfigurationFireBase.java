package com.reobotetechnology.reobotegame.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfigurationFireBase {

    private static FirebaseAuth autentication;
    private static DatabaseReference firebase;

    //Retorna a instancia do FireBaseDataBase para base 64

    public static DatabaseReference getFirebaseDataBase() {

        if(firebase == null){

            firebase = FirebaseDatabase.getInstance().getReference();
        }

        return firebase;
    }

    //Retorna a instancia do FireBaseAuth

    public static FirebaseAuth getFirebaseAutenticacao(){

          if(autentication == null){
              autentication = FirebaseAuth.getInstance();
          }

          return autentication;
    }


}
