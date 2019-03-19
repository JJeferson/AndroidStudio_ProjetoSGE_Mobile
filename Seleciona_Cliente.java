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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

public class Seleciona_Cliente extends AppCompatActivity {

    private Button Cancelar_ID;
    private ListView listView_ID;



    //Para montar na tela a tabela com os dados
    private ArrayAdapter<String> itensAdaptador;
    private ArrayList<String> itens;
    private ArrayList<Integer> IDS;


    public static final String ARQUIVO_REFERENCIA = "ArquivoReferencia";


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


    grava_Pedido_BotaoGrava grava_pedido_botaoGrava = new grava_Pedido_BotaoGrava();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seleciona_cliente);

        Cancelar_ID = (Button) findViewById(R.id.Cancelar_ID);
        listView_ID = (ListView) findViewById(R.id.listView_ID);

        //Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        //pega o id do usuário logado
        final String IdLogado =firebaseAuth.getCurrentUser().getUid();

        //Gerar um shared preference para pegar um ID direitinho
        final SharedPreferences sharedPreferences = getSharedPreferences(ARQUIVO_REFERENCIA, MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();



        Cancelar_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //limpa os campos do Shared preferences para evitar ficar carregando dados não necessarios na memoria
                editor.putString("ID_ClienteFB","");
                editor.putString("Nome_ClienteFB","");
                editor.putString("Fone_ClienteFB","");
                editor.putString("Email_ClienteFB","");
                editor.putString("Endereco_ClienteFB","");
                editor.commit();

                startActivity( new Intent(Seleciona_Cliente.this,Novo_Pedido.class));

            }//fim da ação
        });//fim do botão Voltar







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

                String ID_ClienteFB       = sharedPreferences.getString("ID_ClienteFB","");
                String Nome_ClienteFB     = sharedPreferences.getString("Nome_ClienteFB","");
                String Fone_ClienteFB     = sharedPreferences.getString("Fone_ClienteFB","");
                String Email_ClienteFB    = sharedPreferences.getString("Email_ClienteFB","");
                String Endereco_ClienteFB = sharedPreferences.getString("Endereco_ClienteFB","");

                 ID_ClienteFB       = IDFirebase.get(position);
                 Nome_ClienteFB     = NomeFirebase.get(position);
                 Fone_ClienteFB     = FoneFirebase.get(position);
                 Email_ClienteFB    = EmailFirebase.get(position);
                 Endereco_ClienteFB = EnderecoFirebase.get(position);


                String ValorTotalPedido="";
                UUID uuid = UUID.randomUUID();
                String Numero_do_Pedido_Firebase = uuid.toString();
                grava_pedido_botaoGrava.setCliente(Nome_ClienteFB);
                grava_pedido_botaoGrava.setNumeroPedido(Numero_do_Pedido_Firebase);
                grava_pedido_botaoGrava.setValor_Total(String.valueOf(ValorTotalPedido));
                grava_pedido_botaoGrava.setEmail(Email_ClienteFB);
                grava_pedido_botaoGrava.setEndereco(Endereco_ClienteFB);
                grava_pedido_botaoGrava.setFone(Fone_ClienteFB);

                //insere_Clientes.setCodigoExterno(Recebe_GravaCodigoClienteFirebase);
                BancoReferencia.child("00642057036").child(IdLogado).child("Pedidos").child(Numero_do_Pedido_Firebase).setValue(grava_pedido_botaoGrava);

                editor.putString("ID_ClienteFB",ID_ClienteFB);
                editor.putString("Nome_ClienteFB",Nome_ClienteFB);
                editor.putString("Fone_ClienteFB",Fone_ClienteFB);
                editor.putString("Email_ClienteFB",Email_ClienteFB);
                editor.putString("Endereco_ClienteFB",Endereco_ClienteFB);
                editor.putString("Numero_Pedido",Numero_do_Pedido_Firebase);
                editor.putString("Cliente_Pedido",Nome_ClienteFB);
                editor.putString("Fone_Pedido",Fone_ClienteFB);
                editor.putString("Email_Pedido",Email_ClienteFB);
                editor.putString("Endereco_Pedido",Endereco_ClienteFB);
                editor.commit();


                Toast.makeText(Seleciona_Cliente.this, "Cliente Selecionado/Pedido Gravado com Sucesso!", Toast.LENGTH_SHORT).show();
                startActivity( new Intent(Seleciona_Cliente.this,Novo_Pedido.class));




            }//Fim da classe do onclivk
        });  //Fim da classe do onclick



        //-------------------------------------------------------------------------------------------------------------------------//



    }//fim do oncreate
}//fim da classe java
