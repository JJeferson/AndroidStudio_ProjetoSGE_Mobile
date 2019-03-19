package com.sge_mobileandroid.sge_mobileandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Seleciona_Lista_Preco extends AppCompatActivity {




    private Button Cancelar_ID;
    private TextView textView7;
    private TextView Exibe_Preco_01;
    private TextView Exibe_Preco_02;
    private TextView Exibe_Preco_03;
    private TextView Exibe_Preco_04;

    private Button seleciona_01;
    private Button seleciona_02;
    private Button seleciona_03;
    private Button seleciona_04;

    //Firebase autenticação
    private FirebaseAuth firebaseAuth;

    //abre conexao com o banco de dados voltando sempre pro nó raiz
    private DatabaseReference databaseReferencia = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference BancoReferencia = databaseReferencia.child("FirebaseBancoSGE");

    //classe com os getter e setter
    insere_Produtos insere_Produtos = new insere_Produtos();


    public static final String ARQUIVO_REFERENCIA = "ArquivoReferencia";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seleciona_lista_preco);



        Cancelar_ID = (Button) findViewById(R.id.Cancelar_ID);
        Exibe_Preco_01= (TextView) findViewById(R.id.Exibe_Preco_01);
        Exibe_Preco_02 = (TextView) findViewById(R.id.Exibe_Preco_02);
        Exibe_Preco_03 = (TextView) findViewById(R.id.Exibe_Preco_03);
        Exibe_Preco_04 = (TextView) findViewById(R.id.Exibe_Preco_04);
        textView7      = (TextView) findViewById(R.id.textView7);

        seleciona_01     = (Button) findViewById(R.id.seleciona_01);
        seleciona_02     = (Button) findViewById(R.id.seleciona_02);
        seleciona_03     = (Button) findViewById(R.id.seleciona_03);
        seleciona_04     = (Button) findViewById(R.id.seleciona_04);




        //Gerar um shared preference para pegar um ID direitinho
        final SharedPreferences sharedPreferences = getSharedPreferences(ARQUIVO_REFERENCIA, MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        //Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        //pega o id do usuário logado
        final String IdLogado =firebaseAuth.getCurrentUser().getUid();


        String Codigo_Corrente_Prod = "";
        String Descricao_Corrente_Prod = "";
        String P01="";
        String P02="";
        String P03="";
        String P04="";

        Codigo_Corrente_Prod = sharedPreferences.getString("Produto_ID","");
        Descricao_Corrente_Prod = sharedPreferences.getString("Descricao_Prod","");
        P01 = sharedPreferences.getString("Preco_prod_01","");
        P02 = sharedPreferences.getString("Preco_prod_02","");
        P03 = sharedPreferences.getString("Preco_prod_03","");
        P04 = sharedPreferences.getString("Preco_prod_04","");

        Exibe_Preco_01.setText(P01);
        Exibe_Preco_02.setText(P02);
        Exibe_Preco_03.setText(P03);
        Exibe_Preco_04.setText(P04);








        final String finalP01 = P01;
        seleciona_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                editor.putString("Preco_prod_Corrente", finalP01);
                editor.commit();
                startActivity( new Intent(Seleciona_Lista_Preco.this,Novo_Pedido.class));

            }//fim da ação
        });//fim do botão Voltar


        final String finalP02 = P02;
        seleciona_02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editor.putString("Preco_prod_Corrente", finalP02);
                editor.commit();
                startActivity( new Intent(Seleciona_Lista_Preco.this,Novo_Pedido.class));

            }//fim da ação
        });//fim do botão Voltar

        final String finalP03 = P03;
        seleciona_03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                editor.putString("Preco_prod_Corrente", finalP03);
                editor.commit();
                startActivity( new Intent(Seleciona_Lista_Preco.this,Novo_Pedido.class));

            }//fim da ação
        });//fim do botão Volt



        final String finalP04 = P04;
        seleciona_04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                editor.putString("Preco_prod_Corrente", finalP04);
                editor.commit();
                startActivity( new Intent(Seleciona_Lista_Preco.this,Novo_Pedido.class));

            }//fim da ação
        });//fim do botão Volt
// *******************************************************************************//


        Cancelar_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //limpa os campos do Shared preferences para evitar ficar carregando dados não necessarios na memoria

                editor.putString("Preco_prod_Corrente","");
                editor.putString("Preco_prod_01","");
                editor.putString("Preco_prod_02","");
                editor.putString("Preco_prod_03","");
                editor.putString("Preco_prod_04","");
                editor.commit();
                startActivity( new Intent(Seleciona_Lista_Preco.this,Novo_Pedido.class));

            }//fim da ação
        });//fim do botão Voltar




//*******************************************************************************//




    }//fim do oncreate
}//fim da classe java
