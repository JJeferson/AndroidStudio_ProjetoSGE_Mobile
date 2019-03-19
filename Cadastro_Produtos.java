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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

public class Cadastro_Produtos extends AppCompatActivity {
    //Os EditTExt
    private EditText prod_descricao_ID;
    private EditText preco_01_ID;
    private EditText preco_02_ID;
    private EditText preco_03_ID;
    private EditText preco_04_ID;
    //os botoes e listview
    private Button grava_ID;
    private Button apaga_ID;
    private Button voltar_ID;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro__produtos);



        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        //declarando os objetos

        prod_descricao_ID   = (EditText) findViewById(R.id.prod_descricao_ID);
        preco_01_ID         = (EditText) findViewById(R.id.preco_01_ID);
        preco_02_ID         = (EditText) findViewById(R.id.preco_02_ID);
        preco_03_ID         = (EditText) findViewById(R.id.preco_03_ID);
        preco_04_ID         = (EditText) findViewById(R.id.preco_04_ID);

        //*************/

        grava_ID = (Button) findViewById(R.id.grava_ID);
        apaga_ID= (Button) findViewById(R.id.apaga_ID);
        voltar_ID= (Button) findViewById(R.id.voltar_ID);
        listView_ID = (ListView) findViewById(R.id.listView_ID);


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

//*******************************************************************************//



        listView_ID.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //String Clientes = ClientesFirebase.get(position);
                Id_Externo = IDFirebase.get(position);

                prod_descricao_ID.setText(DescFirebase.get(position));
                preco_01_ID.setText(P01Firebase.get(position));
                preco_02_ID.setText(P02Firebase.get(position));
                preco_03_ID.setText(P03Firebase.get(position));
                preco_04_ID.setText(P04Firebase.get(position));


            }//Fim da classe do onclivk
        });  //Fim da classe do onclick


//*******************************************************************************//

        //-------------------------------------------------------------------------------------------------------------------------//

       grava_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (prod_descricao_ID.getText().toString().length() == 0) {
                Toast.makeText(Cadastro_Produtos.this, "Descrição em branco, preencha", Toast.LENGTH_SHORT).show();

                }else {
                String recebeDesc = prod_descricao_ID.getText().toString();

                //----

                if (preco_01_ID.getText().toString().length() == 0) {
                Toast.makeText(Cadastro_Produtos.this, "O produto precisa ter pelo menos um preço! P1 está em branco!", Toast.LENGTH_SHORT).show();
                }else {
                String recebe_P01 = preco_01_ID.getText().toString();

                //----

                if (preco_02_ID.getText().toString().length() == 0) {
                Toast.makeText(Cadastro_Produtos.this, "Preço 2 está em branco!", Toast.LENGTH_SHORT).show();

                }else {
                String recebeP02 = preco_02_ID.getText().toString();

                            //----
                if (preco_03_ID.getText().toString().length() == 0) {
                Toast.makeText(Cadastro_Produtos.this, "Preço 3 está em branco!", Toast.LENGTH_SHORT).show();
                }else {
                String recebeP03 = preco_03_ID.getText().toString();

                if (preco_04_ID.getText().toString().length() == 0) {
                Toast.makeText(Cadastro_Produtos.this, "Preço 4 está em branco!", Toast.LENGTH_SHORT).show();
                }else {
                String recebeP04 = preco_04_ID.getText().toString();

                    if(Id_Externo.length()>0){

                        //Isso é umn update
                        String IdLogado =firebaseAuth.getCurrentUser().getUid();
                         GravaProdFirebase(Id_Externo,IdLogado);
                        //GravaCodigoClienteFirebase="";
                        Id_Externo="";
                        Limpa_Campos();

                        Toast.makeText(Cadastro_Produtos.this, "Cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        //insert
                        UUID uuid = UUID.randomUUID();
                        String GravaCodigoProdFirebase = uuid.toString();
                        GravaProdFirebase(GravaCodigoProdFirebase,IdLogado);

                        Limpa_Campos();

                        Toast.makeText(Cadastro_Produtos.this, "Cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                    }




                }}}}}//fim dos else
                }//fim da ação
        });//fim do botão Apaga


        //-------------------------------------------------------------------------------------------------------------------------//

        apaga_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if (Id_Externo.length()>0) {
                   Exclui(Id_Externo);
                   Id_Externo = "";
                   Limpa_Campos();
               }else{
                   Toast.makeText(Cadastro_Produtos.this, "Selecione um produto!", Toast.LENGTH_SHORT).show();
               }

            }//fim da ação
        });//fim do botão Apaga

        //-------------------------------------------------------------------------------------------------------------------------//

        voltar_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Id_Externo="";
                Limpa_Campos();
                startActivity( new Intent(Cadastro_Produtos.this,Menu_Principal.class));


            }//fim da ação
        });//fim do botão Apaga

        //-------------------------------------------------------------------------------------------------------------------------//



    }//fim do oncreate


    public void GravaProdFirebase(String Recebe_GravaCodigoProdFirebase, String RecebeID_Logado){

        //Tentativa com firebase aqui na leitura da tabela com cursor
        insere_Produtos.setCodigoExterno(Recebe_GravaCodigoProdFirebase);
        insere_Produtos.setDescricao(prod_descricao_ID.getText().toString());
        insere_Produtos.setP01(preco_01_ID.getText().toString());
        insere_Produtos.setP02(preco_02_ID.getText().toString());
        insere_Produtos.setP03(preco_03_ID.getText().toString());
        insere_Produtos.setP04(preco_04_ID.getText().toString());

        BancoReferencia.child("00642057036").child(RecebeID_Logado).child("Produtos").child(Recebe_GravaCodigoProdFirebase).setValue(insere_Produtos);
    }//fim do gravador

    public void Limpa_Campos(){
        prod_descricao_ID.setText("");
        preco_04_ID.setText("");
        preco_03_ID.setText("");
        preco_02_ID.setText("");
        preco_01_ID.setText("");

    }


    public void Exclui(String IdExterno)
    {

        BancoReferencia.child("00642057036").child(firebaseAuth.getCurrentUser().getUid()).child("Produtos").child(IdExterno).removeValue();


    }
}//fim da classe java
