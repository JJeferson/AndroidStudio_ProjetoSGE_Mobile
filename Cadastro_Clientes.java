package com.sge_mobileandroid.sge_mobileandroid;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

public class Cadastro_Clientes extends AppCompatActivity {

    private EditText nome_ID;
    private EditText email_ID;
    private EditText fone_ID;
    private EditText endereco_ID;




    private Button grava_ID;
    private Button exclui_ID;
    private Button voltar_ID;
    private ListView listView_ID;

    //Para montar na tela a tabela com os dados
    private ArrayAdapter<String> itensAdaptador;
    private ArrayList<String> itens;
    private ArrayList<Integer> IDS;
    public int IDClickCliente =0;

    //Firebase autenticação
    private FirebaseAuth firebaseAuth;

    //abre conexao com o banco de dados voltando sempre pro nó raiz
    private DatabaseReference databaseReferencia = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference BancoReferencia = databaseReferencia.child("FirebaseBancoSGE");


    insere_Clientes insere_Clientes = new insere_Clientes();







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_clientes);



        nome_ID    = (EditText) findViewById(R.id.nome_ID);
        email_ID    = (EditText) findViewById(R.id.email_ID);
        fone_ID    = (EditText) findViewById(R.id.fone_ID);
        endereco_ID    = (EditText) findViewById(R.id.endereco_ID);

        //*************/
        grava_ID = (Button) findViewById(R.id.grava_ID);
        exclui_ID= (Button) findViewById(R.id.exclui_ID);
        voltar_ID= (Button) findViewById(R.id.voltar_ID);
        listView_ID = (ListView) findViewById(R.id.listView_ID);

        //Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        //pega o id do usuário logado
        final String IdLogado =firebaseAuth.getCurrentUser().getUid();


        //*****************************************//

        //declarando o ouvinte, aquele que vai er o banco na nuvem
        BancoReferencia.child("00642057036").child(IdLogado).child("Clientes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });//fim do ouvinte



        Recupera_Clientes();
        //*****************************************//



        //*****************************************//



        //adicionando função no clique no listview
        listView_ID.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //pega o ID
                IDClickCliente = IDS.get(position);
                Preenche_Clientes(IDClickCliente);
                Recupera_Clientes();


            }//Fim da classe do onclivk
        });  //Fim da classe do onclick



        //-------------------------------------------------------------------------------------------------------------------------//

        grava_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (nome_ID.getText().toString().length() == 0) {
                    Toast.makeText(Cadastro_Clientes.this, "Nome em branco, preencha", Toast.LENGTH_SHORT).show();

                    }else {
                    String recebeNOME = nome_ID.getText().toString();

                    //----

                    if (email_ID.getText().toString().length() == 0) {
                    Toast.makeText(Cadastro_Clientes.this, "E-mail em branco, preencha", Toast.LENGTH_SHORT).show();
                    }else {
                    String recebeEMAIL = email_ID.getText().toString();

                        //----

                    if (fone_ID.getText().toString().length() == 0) {
                    Toast.makeText(Cadastro_Clientes.this, "Telefone em branco, preencha", Toast.LENGTH_SHORT).show();
                    }else {
                    String recebeFONE = fone_ID.getText().toString();

                            //----

                    if (endereco_ID.getText().toString().length() == 0) {
                    Toast.makeText(Cadastro_Clientes.this, "Nome em branco, preencha", Toast.LENGTH_SHORT).show();
                    }else {
                    String recebeENDERECO = endereco_ID.getText().toString();


                                //************************//
                                if (IDClickCliente > 0) {
                                    try {
                                        SQLiteDatabase bancoDados = openOrCreateDatabase("SGE_LOCAL_SQLITE", MODE_PRIVATE, null);
                                        //Update
                                        bancoDados.execSQL("UPDATE clientes set nome='" + recebeNOME + "',fone='" + recebeFONE + "' ,email='" + recebeEMAIL + "' ,endereco='" + recebeENDERECO + "' WHERE ID=" + IDClickCliente);
                                        Toast.makeText(Cadastro_Clientes.this, "Alterações Concluidas!", Toast.LENGTH_SHORT).show();
                                        IDClickCliente = 0;
                                        Limpa_Campos();
                                        Recupera_Clientes();


                                    } catch (Exception e) {
                                        e.printStackTrace();

                                    }    //fim do try


                                } else {
                                    try {
                                        //abre o banco
                                        SQLiteDatabase bancoDados = openOrCreateDatabase("SGE_LOCAL_SQLITE", MODE_PRIVATE, null);
                                        //insert
                                        bancoDados.execSQL("CREATE TABLE IF NOT EXISTS clientes (ID INTEGER PRIMARY KEY AUTOINCREMENT, nome VARCHAR,fone VARCHAR, email VARCHAR , endereco VARCHAR,cod_externo VARCHAR );");

                                        UUID uuid = UUID.randomUUID();
                                        String  GravaCodigoClienteFirebase = uuid.toString();

                                        bancoDados.execSQL("INSERT INTO clientes (nome,fone,email,endereco,cod_externo) VALUES ('" + recebeNOME + "','" + recebeFONE + "','" + recebeEMAIL + "','" + recebeENDERECO + "','"+GravaCodigoClienteFirebase+"' )");
                                        //Fim das transações com SQLITE inicio das com firebase

                                        insere_ClientesFirebase(GravaCodigoClienteFirebase, IdLogado);
                                        GravaCodigoClienteFirebase="";


                                        Toast.makeText(Cadastro_Clientes.this, "Cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                                        Recupera_Clientes();
                                        Limpa_Campos();

                                    } catch (Exception e) {
                                        e.printStackTrace();

                                    }  //fim do try
                                } //fim do

                    }}}} // fim dos else das validaçes de campo
            }//fim da ação
        });//fim do botão Grava

        //-------------------------------------------------------------------------------------------------------------------------//

        exclui_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    //abre o banco
                    SQLiteDatabase bancoDados = openOrCreateDatabase("SGE_LOCAL_SQLITE", MODE_PRIVATE, null);
                    bancoDados.execSQL("DELETE FROM clientes  WHERE ID="+IDClickCliente);

                } catch (Exception e) {
                    e.printStackTrace();
                }  //fim do try
                Toast.makeText(Cadastro_Clientes.this, "Cliente Apagado", Toast.LENGTH_SHORT).show();
                Limpa_Campos();
                Recupera_Clientes();
                IDClickCliente=0;


            }//fim da ação
        });//fim do botão Apaga

        //-------------------------------------------------------------------------------------------------------------------------//

        voltar_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Limpa_Campos();
                startActivity( new Intent(Cadastro_Clientes.this,Menu_Principal.class));


            }//fim da ação
        });//fim do botão Apaga

        //-------------------------------------------------------------------------------------------------------------------------//



    }//fim do oncreate



    //***************************************************//

    public void Limpa_Campos() {
        nome_ID.setText("");
        email_ID.setText("");
        fone_ID.setText("");
        endereco_ID.setText("");

    }


    public void insere_ClientesFirebase(String Recebe_GravaCodigoClienteFirebase, String RecebeID_Logado ) {



        //Tentativa com firebase aqui na leitura da tabela com cursor
        insere_Clientes.setFone(fone_ID.getText().toString());
        insere_Clientes.setNome(nome_ID.getText().toString());
        insere_Clientes.setEmail(email_ID.getText().toString());
        insere_Clientes.setEndereco(endereco_ID.getText().toString());
        insere_Clientes.setCodigoExterno(Recebe_GravaCodigoClienteFirebase);

        BancoReferencia.child("00642057036").child(RecebeID_Logado).child("Clientes").child(Recebe_GravaCodigoClienteFirebase).setValue(insere_Clientes);

    }


    // -------------------------------------------------------------------------------------------------------------------------//

    public void Recupera_Clientes() {
        try {
            SQLiteDatabase bancoDados = openOrCreateDatabase("SGE_LOCAL_SQLITE", MODE_PRIVATE, null);
            //Recuperandoos contatos
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS clientes (ID INTEGER PRIMARY KEY AUTOINCREMENT, nome VARCHAR,fone VARCHAR, email VARCHAR , endereco VARCHAR,cod_externo VARCHAR );");


            /*--------------------------------------------------------------------------------------------------------*/
            Cursor cursor = bancoDados.rawQuery("SELECT * FROM clientes " , null);
            //Recuperando os ids das colunas
            int indiceColunaID = cursor.getColumnIndex("ID");
            int indiceColunaFone = cursor.getColumnIndex("fone");
            int indiceColunaNome = cursor.getColumnIndex("nome");
            int indiceColunaEmail= cursor.getColumnIndex("email");
            int indiceColunaEndereco = cursor.getColumnIndex("endereco");

            //Criar adaptador
            itens = new ArrayList<String>();
            IDS = new ArrayList<Integer>();

            itensAdaptador = new ArrayAdapter<String>(getApplicationContext(),
                    android.R.layout.simple_list_item_2,
                    android.R.id.text2, itens);
            listView_ID.setAdapter(itensAdaptador);
            cursor.moveToFirst();
            //Listar as itens
            while (cursor != null) {


                itens.add("Nome:"+cursor.getString(indiceColunaNome)+" |Fone:"+cursor.getString(indiceColunaFone)+" |E-mail:"+cursor.getString(indiceColunaEmail)+" |Endereço:"+cursor.getString(indiceColunaEndereco));

                IDS.add(Integer.parseInt(cursor.getString(indiceColunaID)));


                cursor.moveToNext();
            }

        } catch (Exception e) {
            e.printStackTrace();

        }  //fim do try
    }  //fim do recupera dados

    public void Preenche_Clientes(int Recebe_IDClickCliente) {
        try {
            SQLiteDatabase bancoDados = openOrCreateDatabase("SGE_LOCAL_SQLITE", MODE_PRIVATE, null);
            //Recuperandoos contatos
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS clientes (ID INTEGER PRIMARY KEY AUTOINCREMENT, nome VARCHAR,fone VARCHAR, email VARCHAR , endereco VARCHAR,cod_externo VARCHAR );");


            /*--------------------------------------------------------------------------------------------------------*/
            Cursor cursor = bancoDados.rawQuery("SELECT * FROM clientes where ID="+Recebe_IDClickCliente , null);
            //Recuperando os ids das colunas
            int indiceColunaIDOnClick        = cursor.getColumnIndex("ID");
            int indiceColunaFoneOnClick      = cursor.getColumnIndex("fone");
            int indiceColunaNomeOnClick      = cursor.getColumnIndex("nome");
            int indiceColunaEmailOnClick     = cursor.getColumnIndex("email");
            int indiceColunaEnderecoOnClick  = cursor.getColumnIndex("endereco");

            //Criar adaptador
            itens = new ArrayList<String>();
            cursor.moveToFirst();
            //Listar as itens
            while (cursor != null) {

                //monta a tela
                nome_ID.setText(cursor.getString(indiceColunaNomeOnClick));
                fone_ID.setText(cursor.getString(indiceColunaFoneOnClick));
                email_ID.setText(cursor.getString(indiceColunaEmailOnClick));
                endereco_ID.setText(cursor.getString(indiceColunaEnderecoOnClick));
                cursor.moveToNext();


            }

        } catch (Exception e) {
            e.printStackTrace();

        }  //fim do try
    }  //fim do recupera clientes
}//fim da classe java
