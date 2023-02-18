package com.verbanek.organize.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfiguracaoFirebase {
    private static FirebaseAuth autenticacao;
    private static DatabaseReference firebase;
    //retorna a instanca do firabaseDatabase
    public static DatabaseReference getFirebase(){
        if (firebase == null) {
            firebase = FirebaseDatabase.getInstance().getReference();
        }
        return firebase;
    }

    //retorna a instanca do firebaseAuth
    public static FirebaseAuth getFirebaseAutenticacao(){
        if (autenticacao == null) {
            autenticacao = FirebaseAuth.getInstance();
        }
        return autenticacao;
    }


}
