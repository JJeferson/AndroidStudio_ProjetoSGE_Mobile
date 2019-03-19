package com.sge_mobileandroid.sge_mobileandroid;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Menu_Principal extends AppCompatActivity {
   // private Button clientes_ID;
    private Button clientes_ID_firebase;
    private Button prod_ID;
    private Button ped_ID;
    private Button rel_ID;
    private Button logoff_ID;



    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu__principal);

        //Firebase
        firebaseAuth = FirebaseAuth.getInstance();



        clientes_ID_firebase = (Button) findViewById(R.id.clientes_ID_firebase);
        prod_ID    = (Button) findViewById(R.id.prod_ID);
        ped_ID     = (Button) findViewById(R.id.ped_ID);
        rel_ID     = (Button) findViewById(R.id.rel_ID);
        logoff_ID  = (Button) findViewById(R.id.logoff_ID);




        clientes_ID_firebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity( new Intent(Menu_Principal.this,Clientes_Firebase.class));



            }//fim da ação
        });//fim
        prod_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity( new Intent(Menu_Principal.this,Cadastro_Produtos.class));


            }//fim da ação
        });//fim
        ped_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity( new Intent(Menu_Principal.this,Tela_Pedidos.class));


            }//fim da ação
        });//fim
        rel_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }//fim da ação
        });//fim
        logoff_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseAuth.signOut();
                startActivity( new Intent(Menu_Principal.this,MainActivity.class));



            }//fim da ação
        });//fim

    }//fim do oncreate
}//fim da classe java
