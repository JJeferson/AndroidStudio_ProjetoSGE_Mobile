package com.sge_mobileandroid.sge_mobileandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Seleciona_Produto extends AppCompatActivity {

    private Button Cancelar_ID;
    private ListView listView_ID;


    //montando os adaptadores

    //montagem da lista do listview
    private ArrayAdapter<String> ProdutosAdaptador;
    private ArrayList<String> ProdutosFirebase;
    //Para recuperar as informações no Clik individualmente

    //ID  -> um para cada textfield, um para cada item que pode precisar ser identificado individualmente
    private ArrayList<String> IDFirebase;
    private ArrayAdapter<String> IDAdaptador;

    private ArrayList<String> DescFirebase;
    private ArrayAdapter<String> DescAdaptador;

    private ArrayList<String> P01Firebase;
    private ArrayAdapter<String> P01Adaptador;

    private ArrayList<String> P02Firebase;
    private ArrayAdapter<String> P02Adaptador;

    private ArrayList<String> P03Firebase;
    private ArrayAdapter<String> P03Adaptador;

    private ArrayList<String> P04Firebase;
    private ArrayAdapter<String> P04Adaptador;
    private String Id_Externo="";

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
        setContentView(R.layout.seleciona_produto);

        Cancelar_ID = (Button) findViewById(R.id.Cancelar_ID);
        listView_ID = (ListView) findViewById(R.id.listView_ID);





        //Gerar um shared preference para pegar um ID direitinho
        final SharedPreferences sharedPreferences = getSharedPreferences(ARQUIVO_REFERENCIA, MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        //Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        //pega o id do usuário logado
        final String IdLogado =firebaseAuth.getCurrentUser().getUid();

        //Declarando os campos individuais
        ProdutosFirebase  = new ArrayList<>();
        IDFirebase        = new ArrayList<>();
        DescFirebase      = new ArrayList<>();
        P01Firebase       = new ArrayList<>();
        P02Firebase       = new ArrayList<>();
        P03Firebase       = new ArrayList<>();
        P04Firebase       = new ArrayList<>();





        //--------------------------------------------------//
        //adaptadores

        ProdutosAdaptador = new ArrayAdapter(
                getApplicationContext(),
                android.R.layout.simple_list_item_2,
                android.R.id.text2,
                ProdutosFirebase);

        IDAdaptador = new ArrayAdapter(
                getApplicationContext(),
                android.R.layout.simple_list_item_2,
                android.R.id.text2,
                IDFirebase);

        DescAdaptador = new ArrayAdapter(
                getApplicationContext(),
                android.R.layout.simple_list_item_2,
                android.R.id.text2,
                DescFirebase);

        P01Adaptador = new ArrayAdapter(
                getApplicationContext(),
                android.R.layout.simple_list_item_2,
                android.R.id.text2,
                P01Firebase);
        P02Adaptador = new ArrayAdapter(
                getApplicationContext(),
                android.R.layout.simple_list_item_2,
                android.R.id.text2,
                P02Firebase);
        P03Adaptador = new ArrayAdapter(
                getApplicationContext(),
                android.R.layout.simple_list_item_2,
                android.R.id.text2,
                P03Firebase);
        P04Adaptador = new ArrayAdapter(
                getApplicationContext(),
                android.R.layout.simple_list_item_2,
                android.R.id.text2,
                P04Firebase);



        //Link do listview
        //Faz com que apareça os dados do produtos no ListView
        listView_ID.setAdapter(ProdutosAdaptador);





        //*******************************************************************************/
        // declarando o ouvinte, aquele que vai ler o banco na nuvem
        //O famoso Cherador


        BancoReferencia.child("00642057036").child(IdLogado).child("Produtos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ProdutosFirebase.clear();
                IDFirebase.clear();
                DescFirebase.clear();
                P01Firebase.clear();
                P02Firebase.clear();
                P03Firebase.clear();
                P04Firebase.clear();

                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    insere_Produtos Produtos = dados.getValue(insere_Produtos.class);
                    ProdutosFirebase.add("Descrição :  "+Produtos.getDescricao()+"" +
                            " |  Preço 1: "+Produtos.getP01()+"  |  Preço 2: "+Produtos.getP02()+" | Preço 3: "+Produtos.getP03()+"| Preço 4: "+Produtos.getP04());
                    IDFirebase.add(Produtos.getCodigoExterno());
                    DescFirebase.add(Produtos.getDescricao());
                    P01Firebase.add(Produtos.getP01());
                    P02Firebase.add(Produtos.getP02());
                    P03Firebase.add(Produtos.getP03());
                    P04Firebase.add(Produtos.getP04());
                }//fim do dataSpnapshot
                ProdutosAdaptador.notifyDataSetChanged();

            }//fim do data snapshot


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });//fim do ouvinte

        //***********************************************************************************************//


//*******************************************************************************//



        listView_ID.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                editor.putString("Produto_ID",IDFirebase.get(position));
                editor.putString("Descricao_Prod",DescFirebase.get(position));
                editor.putString("Preco_prod_01",P01Firebase.get(position));
                editor.putString("Preco_prod_02",P02Firebase.get(position));
                editor.putString("Preco_prod_03",P03Firebase.get(position));
                editor.putString("Preco_prod_04",P04Firebase.get(position));

                editor.commit();


                startActivity( new Intent(Seleciona_Produto.this,Novo_Pedido.class));
                /*
                preco_01_ID.setText(P01Firebase.get(position));
                preco_02_ID.setText(P02Firebase.get(position));
                preco_03_ID.setText(P03Firebase.get(position));
                preco_04_ID.setText(P04Firebase.get(position));
                */

            }//Fim da classe do onclivk
        });  //Fim da classe do onclick


//*******************************************************************************//


        Cancelar_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //limpa os campos do Shared preferences para evitar ficar carregando dados não necessarios na memoria
                editor.putString("Produto_ID","");
                editor.putString("Descricao_Prod","");
                editor.putString("Preco_prod_Corrente","");
                editor.putString("Preco_prod_01","");
                editor.putString("Preco_prod_02","");
                editor.putString("Preco_prod_03","");
                editor.putString("Preco_prod_04","");
                editor.commit();
                startActivity( new Intent(Seleciona_Produto.this,Novo_Pedido.class));

            }//fim da ação
        });//fim do botão Voltar



    }//fim do oncreate
}//fim da classe java
