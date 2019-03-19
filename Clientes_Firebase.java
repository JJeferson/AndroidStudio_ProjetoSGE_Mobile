package com.sge_mobileandroid.sge_mobileandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

public class Clientes_Firebase extends AppCompatActivity {


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

    //montagem da lista do listview
    private ArrayAdapter<String> ClientesAdaptador;
    private ArrayList<String> ClientesFirebase;
    //Para recuperar as informações no Clik individualmente
    private ArrayList<String> IDFirebase;
    private ArrayAdapter<String> IDAdaptador;
    //Nome
    private ArrayList<String> NomeFirebase;
    private ArrayAdapter<String> NomeAdaptador;
    //Fone
    private ArrayList<String> FoneFirebase;
    private ArrayAdapter<String> FoneAdaptador;
    //Email
    private ArrayList<String> EmailFirebase;
    private ArrayAdapter<String> EmailAdaptador;
    //endereco
    private ArrayList<String> EnderecoFirebase;
    private ArrayAdapter<String> EnderecoAdaptador;



    //Firebase autenticação
    private FirebaseAuth firebaseAuth;
    //abre conexao com o banco de dados voltando sempre pro nó raiz
   private DatabaseReference databaseReferencia = FirebaseDatabase.getInstance().getReference();
   private DatabaseReference BancoReferencia = databaseReferencia.child("FirebaseBancoSGE");




    String Id_Externo="";
    insere_Clientes insere_Clientes = new insere_Clientes();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clientes__firebase);


       // FirebaseDatabase.getInstance().setPersistenceEnabled(true);



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



//*******************************************************************************//
        //Inicio da cadeia de string do clientes
        ClientesFirebase = new ArrayList<>();
        IDFirebase       = new ArrayList<>();
        NomeFirebase     = new ArrayList<>();
        FoneFirebase     = new ArrayList<>();
        EmailFirebase    = new ArrayList<>();
        EnderecoFirebase = new ArrayList<>();

      //--------------------------------------------------//
        //adaptadores

        ClientesAdaptador = new ArrayAdapter(
                getApplicationContext(),
                android.R.layout.simple_list_item_2,
                android.R.id.text2,
                ClientesFirebase);

        IDAdaptador = new ArrayAdapter(
                getApplicationContext(),
                android.R.layout.simple_list_item_2,
                android.R.id.text2,
                IDFirebase);

        NomeAdaptador = new ArrayAdapter(
                getApplicationContext(),
                android.R.layout.simple_list_item_2,
                android.R.id.text2,
                NomeFirebase);
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
        EnderecoAdaptador = new ArrayAdapter(
                getApplicationContext(),
                android.R.layout.simple_list_item_2,
                android.R.id.text2,
                EnderecoFirebase);


        //Link do listview
        listView_ID.setAdapter(ClientesAdaptador);

        //*******************************************************************************/
        // declarando o ouvinte, aquele que vai ler o banco na nuvem


        BancoReferencia.child("00642057036").child(IdLogado).child("Clientes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ClientesFirebase.clear();
                IDFirebase.clear();
                NomeFirebase.clear();
                FoneFirebase.clear();
                EmailFirebase.clear();
                EnderecoFirebase.clear();

                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    insere_Clientes Clientes = dados.getValue(insere_Clientes.class);
                    ClientesFirebase.add("NOME : "+Clientes.getNome()+" | FONE : "+Clientes.getFone()+" |  E-MAIL: "+Clientes.getEmail()+"  |  ENDEREÇO: "+Clientes.getEndereco());
                    IDFirebase.add(Clientes.getCodigoExterno());
                    NomeFirebase.add(Clientes.getNome());
                    FoneFirebase.add(Clientes.getFone());
                    EmailFirebase.add(Clientes.getEmail());
                    EnderecoFirebase.add(Clientes.getEndereco());



                }//fim do dataSpnapshot
                ClientesAdaptador.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });//fim do ouvinte

//*******************************************************************************//
//testes com o equalls to+

//*******************************************************************************//

       listView_ID.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //String Clientes = ClientesFirebase.get(position);
              Id_Externo = IDFirebase.get(position);

              nome_ID.setText(NomeFirebase.get(position));
              fone_ID.setText(FoneFirebase.get(position));
              email_ID.setText(EmailFirebase.get(position));
              endereco_ID.setText(EnderecoFirebase.get(position));


            }//Fim da classe do onclivk
        });  //Fim da classe do onclick



        //-------------------------------------------------------------------------------------------------------------------------//

        grava_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nome_ID.getText().toString().length() == 0) {
                    Toast.makeText(Clientes_Firebase.this, "Nome em branco, preencha", Toast.LENGTH_SHORT).show();

                }else {
                    String recebeNOME = nome_ID.getText().toString();

                    //----

                if (email_ID.getText().toString().length() == 0) {
                        Toast.makeText(Clientes_Firebase.this, "E-mail em branco, preencha", Toast.LENGTH_SHORT).show();
                }else {
                String recebeEMAIL = email_ID.getText().toString();

                   //----

                if (fone_ID.getText().toString().length() == 0) {
                      Toast.makeText(Clientes_Firebase.this, "Telefone em branco, preencha", Toast.LENGTH_SHORT).show();
                }else {
                String recebeFONE = fone_ID.getText().toString();

                   //----

                if (endereco_ID.getText().toString().length() == 0) {
                       Toast.makeText(Clientes_Firebase.this, "Nome em branco, preencha", Toast.LENGTH_SHORT).show();
                }else {
                String recebeENDERECO = endereco_ID.getText().toString();
                              if(Id_Externo.length()>0){

                                  //Isso é umn update
                                   insere_ClientesFirebase(Id_Externo, IdLogado);
                                  //GravaCodigoClienteFirebase="";
                                  Id_Externo="";
                                  Limpa_Campos();

                                  Toast.makeText(Clientes_Firebase.this, "Cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                              }
                              else {
                                  //insert
                                  UUID uuid = UUID.randomUUID();
                                  String GravaCodigoClienteFirebase = uuid.toString();
                                  insere_ClientesFirebase(GravaCodigoClienteFirebase, IdLogado);
                                  //GravaCodigoClienteFirebase="";
                                  Limpa_Campos();

                                  Toast.makeText(Clientes_Firebase.this, "Cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                              }


                            }}}} // fim dos else das validaçes de campo
            }//fim da ação
        });//fim do botão Grava

        //-------------------------------------------------------------------------------------------------------------------------//

        exclui_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Exclui(Id_Externo);
                Id_Externo="";
                Limpa_Campos();

            }//fim da ação
        });//fim do botão Apaga

        //-------------------------------------------------------------------------------------------------------------------------//

        voltar_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Limpa_Campos();
                Id_Externo="";
                startActivity( new Intent(Clientes_Firebase.this,Menu_Principal.class));


            }//fim da ação
        });//fim do botão Apaga

        //-------------------------------------------------------------------------------------------------------------------------//



    }//fim do oncreate





    //***************************************************//

    public void Exclui(String IdExterno)
    {

        if (Id_Externo.length()>0) {
            BancoReferencia.child("00642057036").child(firebaseAuth.getCurrentUser().getUid()).child("Clientes").child(IdExterno).removeValue();
            Limpa_Campos();
        }else{
            Toast.makeText(Clientes_Firebase.this, "Selecione um cliente!", Toast.LENGTH_SHORT).show();
        }

    }

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


}//fim da classe java
