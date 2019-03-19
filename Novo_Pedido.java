package com.sge_mobileandroid.sge_mobileandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

public class Novo_Pedido extends AppCompatActivity {



    //declarando objetos
    private EditText Cliente_Texto;
    private EditText Produto_Texto;
    private EditText QTDE;
    private EditText ListaPreco;

    private TextView Valor_Total;
    private TextView Exibe_Endereco_ID;

    private Button   seleciona_Cliente_ID;
    private Button   seleciona_Produto_ID;
    private Button   seleciona_ListaPreco_ID;
    private Button   grava_ID;
    private Button   cancelar_ID;
    private Button   Add_QTDE_ID;
    private Button   Excluir_ID;
    private Button   Add_Prod_ID;

    //****************************************/

    private ArrayAdapter<String> lancamentosAdaptador;
    private ArrayList<String>    lancamentosFirebase;
    //Para recuperar as informações no Clik individualmente

    //ID  -> um para cada textfield, um para cada item que pode precisar ser identificado individualmente
    private ArrayList<String>    IDlancamentoFirebase;
    private ArrayAdapter<String> IDlancamentoAdaptador;

    private ArrayList<String>    DesclancamentoFirebase;
    private ArrayAdapter<String> DesclancamentoAdaptador;

    private ArrayList<String>    UnitarioFirebase;
    private ArrayAdapter<String> UnitarioAdaptador;

    private ArrayList<String>    QTDEFirebase;
    private ArrayAdapter<String> QTDEAdaptador;

    private ArrayList<String>    TotalFirebase;
    private ArrayAdapter<String> TotalAdaptador;

    //****************************************/
    private String lanca_Sequencial ="";




    public static final String ARQUIVO_REFERENCIA = "ArquivoReferencia";


    private ListView listView_ID;

     //Firebase autenticação
     private FirebaseAuth firebaseAuth;
     //abre conexao com o banco de dados voltando sempre pro nó raiz
     private DatabaseReference databaseReferencia = FirebaseDatabase.getInstance().getReference();
     private DatabaseReference BancoReferencia = databaseReferencia.child("FirebaseBancoSGE");

     float ValorTotalPedido=0;

     grava_Pedido_BotaoGrava grava_pedido_botaoGrava = new grava_Pedido_BotaoGrava();
     insere_Produtos_Pedidos insere_produtos_pedidos = new insere_Produtos_Pedidos();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.novo_pedido);

      //  FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        //Gerar um shared preference para pegar um ID direitinho
        final SharedPreferences sharedPreferences = getSharedPreferences(ARQUIVO_REFERENCIA, MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();


        Cliente_Texto = (EditText) findViewById(R.id.Cliente_Texto);
        Produto_Texto = (EditText) findViewById(R.id.Produto_Texto);
        QTDE          = (EditText) findViewById(R.id.QTDE);
        ListaPreco    = (EditText) findViewById(R.id.ListaPreco);


        Valor_Total        = (TextView) findViewById(R.id.Valor_Total);
        Exibe_Endereco_ID  = (TextView) findViewById(R.id.Exibe_Endereco_ID);

        seleciona_Cliente_ID    = (Button) findViewById(R.id.seleciona_Cliente_ID);
        seleciona_Produto_ID    = (Button) findViewById(R.id.seleciona_Produto_ID);
        seleciona_ListaPreco_ID = (Button) findViewById(R.id.seleciona_ListaPreco_ID);
        grava_ID       = (Button) findViewById(R.id.grava_ID);
        cancelar_ID    = (Button) findViewById(R.id.cancelar_ID);
        Add_Prod_ID    = (Button) findViewById(R.id.Add_Prod_ID);
        Add_QTDE_ID    = (Button) findViewById(R.id.Add_QTDE_ID);
        Excluir_ID     = (Button) findViewById(R.id.Excluir_ID);

        listView_ID = (ListView) findViewById(R.id.listView_ID);

        //Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        //pega o id do usuário logado
        final String IdLogado =firebaseAuth.getCurrentUser().getUid();

        seleciona_Cliente_ID.requestFocus();





        /**********************************************/

        //Declarando os campos individuais
        lancamentosFirebase        = new ArrayList<>();
        IDlancamentoFirebase       = new ArrayList<>();
        DesclancamentoFirebase     = new ArrayList<>();
        UnitarioFirebase           = new ArrayList<>();
        QTDEFirebase               = new ArrayList<>();
        TotalFirebase              = new ArrayList<>();

        /**********************************************/
//--------------------------------------------------//
        //adaptadores

        lancamentosAdaptador = new ArrayAdapter(
                getApplicationContext(),
                android.R.layout.simple_list_item_2,
                android.R.id.text2,
                lancamentosFirebase);

        IDlancamentoAdaptador = new ArrayAdapter(
                getApplicationContext(),
                android.R.layout.simple_list_item_2,
                android.R.id.text2,
                IDlancamentoFirebase);

        DesclancamentoAdaptador = new ArrayAdapter(
                getApplicationContext(),
                android.R.layout.simple_list_item_2,
                android.R.id.text2,
                DesclancamentoFirebase);
        UnitarioAdaptador = new ArrayAdapter(
                getApplicationContext(),
                android.R.layout.simple_list_item_2,
                android.R.id.text2,
                UnitarioFirebase);
        QTDEAdaptador= new ArrayAdapter(
                getApplicationContext(),
                android.R.layout.simple_list_item_2,
                android.R.id.text2,
                QTDEFirebase);
        TotalAdaptador= new ArrayAdapter(
                getApplicationContext(),
                android.R.layout.simple_list_item_2,
                android.R.id.text2,
                TotalFirebase);



        //Link do listview
        //Faz com que apareça os dados do produtos no ListView
        listView_ID.setAdapter(lancamentosAdaptador);

        //Inicia com zero
        Valor_Total.setText("");
        Exibe_Endereco_ID.setText("");
        Produto_Texto.setText("");
        QTDE.setText("");
        ListaPreco.setText("");
//**************************************************************************************************//
        //Trabalhando com o cadastro de clientes
        String ID_Corrente_Cliente       = "";
        String Nome_Corrente_Cliente     = "";
        String Fone_Corrente_Cliente     = "";
        String Email_Corrente_Cliente    = "";
        String Endereco_Corrente_Cliente = "";

        ID_Corrente_Cliente       = sharedPreferences.getString("ID_ClienteFB","");
        Nome_Corrente_Cliente     = sharedPreferences.getString("Nome_ClienteFB","");
        Fone_Corrente_Cliente     = sharedPreferences.getString("Fone_ClienteFB","");
        Email_Corrente_Cliente    = sharedPreferences.getString("Email_ClienteFB","");
        Endereco_Corrente_Cliente = sharedPreferences.getString("Endereco_ClienteFB","");
       /*
        if(ID_Corrente_Cliente.length()>0) {
        Cliente_Texto.setText(Nome_Corrente_Cliente);
        Exibe_Endereco_ID.setText(Endereco_Corrente_Cliente);
        }
        */

//**************************************************************************************************//

      //   Variaveis de mem☻ria para eiç>o de pedidos
        String Numero_Corrente_Pedido   = "";
        String Cliente_Corrente_Pedido  = "";
        String Fone_Corrente_Pedido     = "";
        String Email_Corrente_Pedido    = "";
        String Endereco_Corrente_Pedido = "";
        String Preco_Corrente="";

       // editor.putString("Preco_prod_Corrente", finalP03);

        Numero_Corrente_Pedido   = sharedPreferences.getString("Numero_Pedido","");
        Cliente_Corrente_Pedido  = sharedPreferences.getString("Cliente_Pedido","");
        Fone_Corrente_Pedido     = sharedPreferences.getString("Fone_Pedido","");
        Email_Corrente_Pedido    = sharedPreferences.getString("Email_Pedido","");
        Endereco_Corrente_Pedido = sharedPreferences.getString("Endereco_Pedido","");


        Preco_Corrente   = sharedPreferences.getString("Preco_prod_Corrente","");


        if (Preco_Corrente.length()>0){
            ListaPreco.setText(Preco_Corrente);
        }

        if(Numero_Corrente_Pedido.length()>0){
        Cliente_Texto.setText(Cliente_Corrente_Pedido);
        Exibe_Endereco_ID.setText(Endereco_Corrente_Pedido);
            //Carrega Grade
        CarregaGradeItens(IdLogado,Numero_Corrente_Pedido);
        }

//**************************************************************************************************//
       // editor.putString("Produto_ID","");
       // editor.putString("Descricao_Prod","");
        String Codigo_Corrente_Prod = "";
        String Descricao_Corrente_Prod = "";
        Codigo_Corrente_Prod = sharedPreferences.getString("Produto_ID","");
        Descricao_Corrente_Prod = sharedPreferences.getString("Descricao_Prod","");
        Produto_Texto.setText(Descricao_Corrente_Prod);



//**************************************************************************************************//




        //*******************************************************************************/
        // declarando o ouvinte, aquele que vai ler o banco na nuvem
        //O famoso Cherador

        //***********************************************************************************************//


        cancelar_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LimpaMemoria();
                LimpaCampos();
                startActivity( new Intent(Novo_Pedido.this,Tela_Pedidos.class));
            }//fim da ação
        });//fim do botão Voltar


//**************************************************************************************************//
        final int[] QTDE_Inc = {0};
        QTDE.setText("0");

        Add_QTDE_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               QTDE_Inc[0] = QTDE_Inc[0] +1;
               QTDE.setText(""+ QTDE_Inc[0]);
              //  Toast.makeText(Novo_Pedido.this, "Item adicionado com sucesso!", Toast.LENGTH_SHORT).show();
              }//fim da ação

        });//fim do botão Grava
//**************************************************************************************************//

        final String finalNumero_Corrente_Pedido = Numero_Corrente_Pedido;
        final String finalCodigo_Corrente_Prod1 = Codigo_Corrente_Prod;
        Add_Prod_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              if(Cliente_Texto.length() == 0){
                  Toast.makeText(Novo_Pedido.this, "Selecione um cliente primeiro", Toast.LENGTH_SHORT).show();

              }else {
               if(Produto_Texto.length() ==0){
                   Toast.makeText(Novo_Pedido.this, "Selecione um produto primeiro!", Toast.LENGTH_SHORT).show();

               } else {
                   String Desc_Prod =  Produto_Texto.getText().toString();
                   String QTDE_Prod =  QTDE.getText().toString();
                   String Valor_Prod = ListaPreco.getText().toString();

                   insere_produtos_pedidos.setDescricao(Desc_Prod);
                   insere_produtos_pedidos.setCod_Prod(finalCodigo_Corrente_Prod1);
                   insere_produtos_pedidos.setQTDE(QTDE_Prod);
                   insere_produtos_pedidos.setValor(Valor_Prod);
                   float FQTDE = Float.parseFloat(QTDE_Prod);
                   float FValorUnitario = Float.parseFloat(Valor_Prod);
                   float FTotal = FQTDE*FValorUnitario;
                   insere_produtos_pedidos.setTotal_Unitario(String.valueOf(FTotal));

                   UUID uuid = UUID.randomUUID();
                   lanca_Sequencial = uuid.toString();

                   //insere_Clientes.setCodigoExterno(Recebe_GravaCodigoClienteFirebase);
                   BancoReferencia.child("00642057036").child(IdLogado).child("Pedidos").child(finalNumero_Corrente_Pedido).child("Lancamentos").child(lanca_Sequencial).setValue(insere_produtos_pedidos);

                   // finalNumero_Corrente_Pedido
                   Toast.makeText(Novo_Pedido.this, "Item adicionado com sucesso!", Toast.LENGTH_SHORT).show();

                   QTDE.setText("");
                   Produto_Texto.setText("");
                   ListaPreco.setText("");

                   editor.putString("Produto_ID","");
                   editor.putString("Descricao_Prod","");
                   editor.putString("Preco_prod_Corrente","");
                   editor.putString("Preco_prod_01","");
                   editor.putString("Preco_prod_02","");
                   editor.putString("Preco_prod_03","");
                   editor.putString("Preco_prod_04","");
                   editor.commit();

               } }//else

            }//fim da ação

        });//fim do botão Grava
//**************************************************************************************************//


        grava_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Toast.makeText(Novo_Pedido.this, "Função ainda no disponivel.", Toast.LENGTH_SHORT).show();



            }//fim da ação

        });//fim do botão Grava

//**************************************************************************************************//

        final String finalID_Corrente_Cliente = ID_Corrente_Cliente;
        seleciona_Cliente_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (finalID_Corrente_Cliente.length() > 0) {
                    Toast.makeText(Novo_Pedido.this, "Este pedido já tem um cliente selecionado, Caso precise trocar crie novo pedido.", Toast.LENGTH_SHORT).show();
                }else {

                    startActivity(new Intent(Novo_Pedido.this, Seleciona_Cliente.class));
                }


            }//fim da ação
        });//fim do botão Grava

        //-------------------------------------------------------------------------------------------------------------------------//


        seleciona_Produto_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Cliente_Texto.length() == 0) {
                    Toast.makeText(Novo_Pedido.this, "É preciso selecionar um cliente primeiro!", Toast.LENGTH_SHORT).show();
                }else {

                    startActivity(new Intent(Novo_Pedido.this, Seleciona_Produto.class));
                }


            }//fim da ação
        });//fim do botão Grava

        //-------------------------------------------------------------------------------------------------------------------------//

        final String finalCodigo_Corrente_Prod = Codigo_Corrente_Prod;
        seleciona_ListaPreco_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Cliente_Texto.length() == 0) {
                    Toast.makeText(Novo_Pedido.this, "É preciso selecionar um cliente primeiro!", Toast.LENGTH_SHORT).show();
                }else {
                    if(finalCodigo_Corrente_Prod.length() == 0 ){
                        Toast.makeText(Novo_Pedido.this, "Primeiro selecione um produto!", Toast.LENGTH_SHORT).show();
                    }else {

                        startActivity(new Intent(Novo_Pedido.this, Seleciona_Lista_Preco.class));
                    } }


            }//fim da ação
        });//fim do botão Grava

        //-------------------------------------------------------------------------------------------------------------------------//

        final String finalNumero_Corrente_Pedido1 = Numero_Corrente_Pedido;
        Excluir_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (finalNumero_Corrente_Pedido1.length()>0) {
                    BancoReferencia.child("00642057036").child(firebaseAuth.getCurrentUser().getUid()).child("Pedidos").child(finalNumero_Corrente_Pedido1).removeValue();
                    Toast.makeText(Novo_Pedido.this, "Excluido com sucesso!", Toast.LENGTH_SHORT).show();
                    LimpaCampos();
                    LimpaMemoria();
                    startActivity(new Intent(Novo_Pedido.this, Tela_Pedidos.class));
                }else{
                    Toast.makeText(Novo_Pedido.this, "Para excluso é preciso selecionar um pedido já  lançado", Toast.LENGTH_SHORT).show();
                }

                LimpaCampos();

            }//fim da ação
        });//fim do botão Apaga

        //-------------------------------------------------------------------------------------------------------------------------//


    }//fim do on create





    public void LimpaCampos(){
        Cliente_Texto.setText("");
        Produto_Texto.setText("");
        QTDE.setText("");
        ListaPreco.setText("");
        Exibe_Endereco_ID.setText("");


    }

    public void LimpaMemoria(){



        //Gerar um shared preference para pegar um ID direitinho
        final SharedPreferences sharedPreferences = getSharedPreferences(ARQUIVO_REFERENCIA, MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        //limpa os campos do Shared preferences para evitar ficar carregando dados não necessarios na memoria
        //variaveiss de seleç>o de clientes
        editor.putString("ID_ClienteFB","");
        editor.putString("Nome_ClienteFB","");
        editor.putString("Fone_ClienteFB","");
        editor.putString("Email_ClienteFB","");
        editor.putString("Endereco_ClienteFB","");

        //Variaveis de ediç>o de pedido
        //usados para editar pedidos
        editor.putString("Numero_Pedido","");
        editor.putString("Cliente_Pedido","");
        editor.putString("Fone_Pedido","");
        editor.putString("Email_Pedido","");
        editor.putString("Endereco_Pedido","");
        //produtos
        editor.putString("Produto_ID","");
        editor.putString("Descricao_Prod","");
        editor.putString("Preco_prod_01","");
        editor.putString("Preco_prod_02","");
        editor.putString("Preco_prod_03","");
        editor.putString("Preco_prod_04","");
        editor.putString("Preco_prod_Corrente","");

        editor.commit();
    }

    public void CarregaGradeItens(String IDAtual, String NumeroPedido){


        BancoReferencia.child("00642057036").child(IDAtual).child("Pedidos").child(NumeroPedido).child("Lancamentos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                lancamentosFirebase.clear();
                IDlancamentoFirebase.clear();
                DesclancamentoFirebase.clear();
                UnitarioFirebase.clear();
                QTDEFirebase.clear();
                TotalFirebase.clear();

                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    insere_Produtos_Pedidos Lancamentos = dados.getValue(insere_Produtos_Pedidos.class);

                    // insere_Produtos Produtos = dados.getValue(insere_Produtos.class);
                    lancamentosFirebase.add(Lancamentos.getDescricao()+" | QTDE : "+Lancamentos.getQTDE()+" | Unitario: "+Lancamentos.getValor()+" | -->"+Lancamentos.getTotal_Unitario());

                    IDlancamentoFirebase.add(Lancamentos.getCod_Prod());
                    DesclancamentoFirebase.add(Lancamentos.getDescricao());
                    UnitarioFirebase.add(Lancamentos.getValor());
                    QTDEFirebase.add(Lancamentos.getQTDE());
                    TotalFirebase.add(Lancamentos.getTotal_Unitario());


                }//fim do dataSpnapshot
                lancamentosAdaptador.notifyDataSetChanged();

            }//fim do data snapshot


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });//fim do ouvinte
    }


}//fim da classe java
