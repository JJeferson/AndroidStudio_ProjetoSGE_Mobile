package com.sge_mobileandroid.sge_mobileandroid;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;




public class MainActivity extends AppCompatActivity {



    //private DatabaseReference databaseReferencia = FirebaseDatabase.getInstance().getReference();
   // private DatabaseReference BancoReferencia = databaseReferencia.child("FirebaseBancoSGE");



    private Button logar_ID;



    //Firebase autenticação
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //habilita o persistent teste
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);


        //Firebase
        firebaseAuth = FirebaseAuth.getInstance();

        logar_ID = (Button) findViewById(R.id.logar_ID);



        logar_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                firebaseAuth.signInWithEmailAndPassword("jteste@gmail.com","123654").addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //TEstando se a criação do login deu certo
                        if (task.isSuccessful()){
                         //vou usar depois, armazenar num shared pref
                         //   Pega_login_ID = task.getResult().getUser().getUid();

                            startActivity( new Intent(MainActivity.this,Menu_Principal.class));
                            Toast.makeText(MainActivity.this,"Login Efetuado.",Toast.LENGTH_SHORT).show();


                        }else{

                            String erroLogar = task.getException().getMessage(); //toString();
                            Toast.makeText(MainActivity.this,"ERRO AO LOGAR"+erroLogar,Toast.LENGTH_SHORT).show();

                        }

                    }
                });



            }//fim da ação
        });//fim





    }//fim do oncreate
}//fim da classe java
