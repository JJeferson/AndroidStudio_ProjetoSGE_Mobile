package com.sge_mobileandroid.sge_mobileandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class Tela_Pedidos extends AppCompatActivity {


    private Button   novo_ID;
    private Button   voltar_ID;
    private ListView listView_ID;

    private EditText pesquisa_ID;
    private TextView exibe_ID;

    private String text;

    //Para montar na tela a tabela com os dados
    private ArrayAdapter<String> PedidosAdaptador;
    private ArrayList<String> Pedidos;
    private ArrayList<Integer> IDS;

    //montagem individual
    private ArrayAdapter<String> ClienteAdaptador;
    private ArrayList<String> ClienteFirebase;
   //Para numero do pedido
    private ArrayList<String> NumeroPedidoFirebase;
    private ArrayAdapter<String> NumeroPedidoAdaptador;
    //Valor total
    private ArrayList<String> ValorTotalFirebase;
    private ArrayAdapter<String> ValorTotalAdaptador;
    //endereco
    private ArrayList<String> EnderecoFirebase;
    private ArrayAdapter<String> EnderecoAdaptador;
    //Fone
    private ArrayList<String> FoneFirebase;
    private ArrayAdapter<String> FoneAdaptador;
    //Email
    private ArrayList<String> EmailFirebase;
    private ArrayAdapter<String> EmailAdaptador;


    //Firebase autenticação
    private FirebaseAuth firebaseAuth;
    //abre conexao com o banco de dados voltando sempre pro nó raiz
    private DatabaseReference databaseReferencia = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference BancoReferencia = databaseReferencia.child("FirebaseBancoSGE");



    public static final String ARQUIVO_REFERENCIA = "ArquivoReferencia";
    grava_Pedido_BotaoGrava grava_pedido_botaoGrava = new grava_Pedido_BotaoGrava();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_pedidos);



        //Gerar um shared preference para pegar um ID direitinho
        final SharedPreferences sharedPreferences = getSharedPreferences(ARQUIVO_REFERENCIA, MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();


       novo_ID= (Button) findViewById(R.id.novo_ID);
       voltar_ID= (Button) findViewById(R.id.voltar_ID);
       listView_ID = (ListView) findViewById(R.id.listView_ID);

        pesquisa_ID   = (EditText) findViewById(R.id.pesquisa_ID);
        exibe_ID      = (TextView) findViewById(R.id.exibe_ID);

        listView_ID.requestFocus();


        //Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        //pega o id do usuário logado
        final String IdLogado =firebaseAuth.getCurrentUser().getUid();

        //limpa os campos do Shared preferences para evitar ficar carregando dados não necessarios na memoria
        //usados pea busca de cliente
        editor.putString("ID_ClienteFB","");
        editor.putString("Nome_ClienteFB","");
        editor.putString("Fone_ClienteFB","");
        editor.putString("Email_ClienteFB","");
        editor.putString("Endereco_ClienteFB","");
        //usados para editar pedidos
        editor.putString("Numero_Pedido","");
        editor.putString("Cliente_Pedido","");
        editor.putString("Fone_Pedido","");
        editor.putString("Email_Pedido","");
        editor.putString("Endereco_Pedido","");

        editor.commit();


      //Inicio da cadeia de string do clientes
        Pedidos                 = new ArrayList<>();
        NumeroPedidoFirebase    = new ArrayList<>();
        ClienteFirebase         = new ArrayList<>();
        FoneFirebase            = new ArrayList<>();
        EmailFirebase           = new ArrayList<>();
        EnderecoFirebase        = new ArrayList<>();

        //--------------------------------------------------//

        //--------------------------------------------------//
        //adaptadores

        PedidosAdaptador = new ArrayAdapter(
                getApplicationContext(),
                android.R.layout.simple_list_item_2,
                android.R.id.text2,
                Pedidos);


        ClienteAdaptador = new ArrayAdapter(
                getApplicationContext(),
                android.R.layout.simple_list_item_2,
                android.R.id.text2,
                ClienteFirebase);

        NumeroPedidoAdaptador = new ArrayAdapter(
                getApplicationContext(),
                android.R.layout.simple_list_item_2,
                android.R.id.text2,
                NumeroPedidoFirebase);

        ValorTotalAdaptador = new ArrayAdapter(
                getApplicationContext(),
                android.R.layout.simple_list_item_2,
                android.R.id.text2,
                ValorTotalFirebase);


        EnderecoAdaptador = new ArrayAdapter(
                getApplicationContext(),
                android.R.layout.simple_list_item_2,
                android.R.id.text2,
                EnderecoFirebase);

        FoneAdaptador = new ArrayAdapter(
                getApplicationContext(),
                android.R.layout.simple_list_item_2,
                android.R.id.text2,
                FoneFirebase);


        EmailAdaptador = new ArrayAdapter(
                getApplicationContext(),
                android.R.layout.simple_list_item_2,
                android.R.id.text2,
                EmailFirebase);







        //Link do listview
        listView_ID.setAdapter(PedidosAdaptador);


//***********************************************************************************************************************************//


        pesquisa_ID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                text = pesquisa_ID.getText().toString().toLowerCase(Locale.getDefault());
                exibe_ID.setText(text);


            }
        });




//*********************************************************************************************************************/
        ouvinte(IdLogado);
        // declarando o ouvinte, aquele que vai ler o banco na nuvem
/*
        BancoReferencia.child("00642057036").child(IdLogado).child("Pedidos").equalTo("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Pedidos.clear();
                NumeroPedidoFirebase.clear();
                ClienteFirebase.clear();
                FoneFirebase.clear();
                EmailFirebase.clear();
                EnderecoFirebase.clear();


                for (DataSnapshot dados: dataSnapshot.getChildren()) {
                grava_Pedido_BotaoGrava PedidosBuscados =  dados.getValue(grava_Pedido_BotaoGrava.class);
                Pedidos.add("Cliente : "+PedidosBuscados.getCliente()+" | Valor : "+PedidosBuscados.getValor_Total()+" | Fone : "+PedidosBuscados.getFone()+" | Email : "+PedidosBuscados.getEmail());
                NumeroPedidoFirebase.add(PedidosBuscados.getNumeroPedido());
                ClienteFirebase.add(PedidosBuscados.getCliente());
                FoneFirebase.add(PedidosBuscados.getFone());
                EmailFirebase.add(PedidosBuscados.getEmail());
                EnderecoFirebase.add(PedidosBuscados.getEndereco());


                }
                //fim do dataSpnapshot
                PedidosAdaptador.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });//fim do ouvinte
        */

//***********************************************************************************************************************************//
        listView_ID.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                editor.putString("Numero_Pedido",NumeroPedidoFirebase.get(position));
                editor.putString("Cliente_Pedido",ClienteFirebase.get(position));
                editor.putString("Fone_Pedido",FoneFirebase.get(position));
                editor.putString("Email_Pedido",EmailFirebase.get(position));
                editor.putString("Endereco_Pedido",EnderecoFirebase.get(position));
                editor.putString("Produto_ID","");
                editor.putString("Descricao_Prod","");
                editor.putString("Preco_prod_Corrente","");
                editor.putString("Preco_prod_01","");
                editor.putString("Preco_prod_02","");
                editor.putString("Preco_prod_03","");
                editor.putString("Preco_prod_04","");
                editor.commit();



                startActivity( new Intent(Tela_Pedidos.this,Novo_Pedido.class));




            }//Fim da classe do onclivk
        });  //Fim da classe do onclick



//***********************************************************************************************************************************//





        novo_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //usados para editar pedidos
                editor.putString("Numero_Pedido","");
                editor.putString("Cliente_Pedido","");
                editor.putString("Fone_Pedido","");
                editor.putString("Email_Pedido","");
                editor.putString("Endereco_Pedido","");
                editor.putString("Produto_ID","");
                editor.putString("Descricao_Prod","");
                editor.putString("Preco_prod_01","");
                editor.putString("Preco_prod_02","");
                editor.putString("Preco_prod_03","");
                editor.putString("Preco_prod_04","");
                editor.putString("Preco_prod_Corrente","");
                editor.commit();
                startActivity( new Intent(Tela_Pedidos.this,Novo_Pedido.class));


            }//fim da ação
        });//fim do botão Apaga


        voltar_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity( new Intent(Tela_Pedidos.this,Menu_Principal.class));


            }//fim da ação
        });//fim do botão Apaga


    }//fim do oncreate



    public void ouvinte(String ID_Logado){

        // declarando o ouvinte, aquele que vai ler o banco na nuvem

        BancoReferencia.child("00642057036").child(ID_Logado).child("Pedidos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Pedidos.clear();
                NumeroPedidoFirebase.clear();
                ClienteFirebase.clear();
                FoneFirebase.clear();
                EmailFirebase.clear();
                EnderecoFirebase.clear();


                for (DataSnapshot dados: dataSnapshot.getChildren()) {
                    grava_Pedido_BotaoGrava PedidosBuscados =  dados.getValue(grava_Pedido_BotaoGrava.class);
                    Pedidos.add("Cliente : "+PedidosBuscados.getCliente()+" | Valor : "+PedidosBuscados.getValor_Total()+" | Fone : "+PedidosBuscados.getFone()+" | Email : "+PedidosBuscados.getEmail());
                    NumeroPedidoFirebase.add(PedidosBuscados.getNumeroPedido());
                    ClienteFirebase.add(PedidosBuscados.getCliente());
                    FoneFirebase.add(PedidosBuscados.getFone());
                    EmailFirebase.add(PedidosBuscados.getEmail());
                    EnderecoFirebase.add(PedidosBuscados.getEndereco());


                }
                //fim do dataSpnapshot
                PedidosAdaptador.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });//fim do ouvinte
    }
}//fim da classe java
