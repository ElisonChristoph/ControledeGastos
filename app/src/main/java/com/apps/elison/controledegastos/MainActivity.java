package com.apps.elison.controledegastos;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.widget.DatePicker;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.apps.elison.controledegastos.DAO.Credito;
import com.apps.elison.controledegastos.DAO.CreditoDAO;
import com.apps.elison.controledegastos.DAO.Gasto;
import com.apps.elison.controledegastos.DAO.GastoDAO;
import com.apps.elison.controledegastos.DAO.Mes;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.support.annotation.Nullable;


import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener ,NavigationView.OnNavigationItemSelectedListener {

    private static final String NOME_ARQUIVO = "arquivo_listadegastos.txt";
    private static final int Activity_DADOS_PESSOAIS = 10;

    private RecyclerView recyclerView;
    private RecyclerView recyclerViewMeses;
    private GastosAdapter adapter;
    List<Mes> mesList = new ArrayList<>();

    private static EditText txtData;
    private static int Ano;
    private static int Mes;
    private static int Dia;
    private int Hora;
    private int Minuto;

    private String array_spinner[];

    private TextView seu_nome;
    private TextView seu_email;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private Button bAddGasto, bAddCredito, bCadastro, btnCancelar, btnSalvar, btnLimpar,btn_cancelarID2,bSalvarCredito,btnLimparCredito;
    private ImageButton bMeses, btnEntrar, bScan, bjaneiro, bfevereiro, bmarco, babril, bmaio, bjunho, bjulho, bagosto, bsetembro, boutubro, bnovembro, bdezembro;
    private FloatingActionButton fab, fabSair;
    private ConstraintLayout backApp;
    Spinner categoria;

    final int[] mesesButtonArray = {
            R.drawable.ic_janeiro,
            R.drawable.ic_fevereiro,
            R.drawable.ic_marco,
            R.drawable.ic_abril,
            R.drawable.ic_maio,
            R.drawable.ic_junho,
            R.drawable.ic_julho,
            R.drawable.ic_agosto,
            R.drawable.ic_setembro,
            R.drawable.ic_outubro,
            R.drawable.ic_novembro,
            R.drawable.ic_dezembro

    };

    String qrnome;
    String qrdata;
    String qrvalor;

    EditText tvnome;
    EditText tvdata;
    EditText tvvalor;

    EditText tvNomeCredito;
    EditText nValorCredito;

    TextView tvDespesas,tvReceitas,tvSaldo;
    private String mesSelected;
    Date d = new Date();
    SimpleDateFormat dataCom = new SimpleDateFormat("d/M/y");

    private ConstraintLayout main_layout;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main_layout = findViewById(R.id.main_layoutID);

        array_spinner=new String[4];
        array_spinner[0]="ENTRETERIMENTO";
        array_spinner[1]="ALIMENTAÇÃO";
        array_spinner[2]="TRANSPORTE";
        array_spinner[3]="GASTOS MENSAIS";
        categoria = (Spinner) findViewById(R.id.sCategoria);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item,array_spinner);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        categoria.setAdapter(spinnerArrayAdapter);

        //Leitor do QRCODE
        //---------------------------------------------------------------------------

        bScan =  (ImageButton) findViewById(R.id.bScan);

        tvnome = (EditText) findViewById(R.id.tvNome);
        tvdata = (EditText) findViewById(R.id.txtData);
        tvvalor = (EditText) findViewById(R.id.tvValor);

        backApp = (ConstraintLayout) findViewById(R.id.backApp);

        tvNomeCredito = (EditText) findViewById(R.id.tvNomeCredito);
        nValorCredito =(EditText) findViewById(R.id.nValorCredito);

        tvDespesas = (TextView) findViewById(R.id.tvDespesas);
        tvReceitas = (TextView) findViewById(R.id.tvReceitas);
        tvSaldo = (TextView) findViewById(R.id.tvSaldo);

        btn_cancelarID2 = (Button) findViewById(R.id.btn_cancelarID2);

        final Activity activity = this;

        bScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                integrator.setPrompt("Poscione a camera"); // texto que irá aparecer quando iniciar o scanner
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setOrientationLocked(true);
                integrator.initiateScan();

            }
        });

        // Busca da data para apresentação dos gastos do mes atual
        //------------------------------------------------------------------------
        InicializaListeners();
        txtData.setEnabled(false);

        btn_cancelarID2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.addcredito).setVisibility(View.INVISIBLE);
                findViewById(R.id.menu_add).setVisibility(View.VISIBLE);
            }
        });



        ImageButton ibEditarData = (ImageButton) findViewById(R.id.ibEditarData);
        ibEditarData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MostrarData();
            }
        });

        final Calendar cal = Calendar.getInstance();
        Ano = cal.get(Calendar.YEAR);
        Mes = cal.get(Calendar.MONTH);
        Dia = cal.get(Calendar.DAY_OF_MONTH);
        Hora = cal.get(Calendar.HOUR);
        Minuto = cal.get(Calendar.MINUTE);

        AtualizarData();

        // data/hora atual
        d = new Date();
        SimpleDateFormat mesO = new SimpleDateFormat("M");




        int mes = Integer.parseInt(mesO.format(d));
        System.out.println("mesatual "+mes);
        mesSelected=mesO.format(d);

        mesList.add(new Mes(1,"Janeiro", 0, 0,0,R.drawable.ic_janeiro));
        mesList.add(new Mes(2,"Fevereiro", 0, 0,0,R.drawable.ic_fevereiro));
        mesList.add(new Mes(3,"Março", 0, 0,0,R.drawable.ic_marco));
        mesList.add(new Mes(4,"Abril", 0, 0,0,R.drawable.ic_abril));
        mesList.add(new Mes(5,"Maio", 0, 0,0,R.drawable.ic_maio));
        mesList.add(new Mes(6,"Junho", 0, 0,0,R.drawable.ic_junho));
        mesList.add(new Mes(7,"Julho", 0, 0,0,R.drawable.ic_julho));
        mesList.add(new Mes(8,"Agosto", 0, 0,0,R.drawable.ic_agosto));
        mesList.add(new Mes(9,"Setembro", 0, 0,0,R.drawable.ic_setembro));
        mesList.add(new Mes(10,"Outubro", 0, 0,0,R.drawable.ic_outubro));
        mesList.add(new Mes(11,"Novembro", 0, 0,0,R.drawable.ic_novembro));
        mesList.add(new Mes(12,"Dezembro", 0, 0,0,R.drawable.ic_dezembro));


        //Metodo para buscar e apresentar os gastos
        //-------------------------------------------------------------------------------------------------------



        //----------------------------------------------------------------------------------------------------

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Obtem a referência do layout de navegação
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Obtem a referência da view de cabeçalho (importante para achar os componentes)
        View headerView = navigationView.getHeaderView(0);
        seu_nome = headerView.findViewById(R.id.seuNomeID);
        seu_email = headerView.findViewById(R.id.seuEmailID);

        // Inicialização para gravar as preferencias
        pref = getSharedPreferences("ListaComprasPrefArq", MODE_PRIVATE);
        editor = pref.edit();

        // Verifica se já foi gravado valores
        if (pref.contains("Nome")) {
            seu_nome.setText(pref.getString("Nome", "sem nome"));
            seu_email.setText(pref.getString("Email", "sem email"));
        } else {
            Snackbar.make(main_layout, "Por favor configure seu nome e email", Snackbar.LENGTH_LONG).show();
            seu_nome.setText("sem nome");
            seu_email.setText("sem email");
        }

        configurarRecycler();

        //configuração grafico

        setupPieChart();

        //Configuração dos Buttons
        //-------------------------------------------------------------------------------------------------------

        bMeses = (ImageButton) findViewById(R.id.bmeses);
        bMeses.setImageResource(mesesButtonArray[mes-1]);
        bMeses.setOnClickListener(this);
        btnEntrar = (ImageButton) findViewById(R.id.bentrar);
        btnEntrar.setOnClickListener(this);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        fabSair = (FloatingActionButton) findViewById(R.id.fabSair);
        fabSair.setOnClickListener(this);
        bAddGasto = (Button) findViewById(R.id.bAddGasto2);
        bAddGasto.setOnClickListener(this);
        bAddCredito  = (Button) findViewById(R.id.bAddCredito);
        bAddCredito.setOnClickListener(this);
        btnCancelar = (Button) findViewById(R.id.btn_cancelarID);
        btnCancelar.setOnClickListener(this);
        btnLimpar = (Button) findViewById(R.id.btn_limparID);
        btnLimpar.setOnClickListener(this);

        btnLimparCredito = (Button) findViewById(R.id.btn_limparID2);
        btnLimparCredito.setOnClickListener(this);

        btnSalvar = (Button) findViewById(R.id.btn_salvarID);
        btnSalvar.setOnClickListener(this);
        bSalvarCredito = (Button) findViewById(R.id.bSalvarCredito);
        bSalvarCredito.setOnClickListener(this);

        //botoes dos meses
        bjaneiro = (ImageButton) findViewById(R.id.ibJaneiro);
        bjaneiro.setOnClickListener(this);
        bfevereiro = (ImageButton) findViewById(R.id.ibFevereiro);
        bfevereiro.setOnClickListener(this);
        bmarco = (ImageButton) findViewById(R.id.ibMarco);
        bmarco.setOnClickListener(this);
        babril = (ImageButton) findViewById(R.id.ibAbril);
        babril.setOnClickListener(this);
        bmaio = (ImageButton) findViewById(R.id.ibMaio);
        bmaio.setOnClickListener(this);
        bjunho = (ImageButton) findViewById(R.id.ibJunho);
        bjunho.setOnClickListener(this);
        bjulho = (ImageButton) findViewById(R.id.ibJulho);
        bjulho.setOnClickListener(this);
        bagosto = (ImageButton) findViewById(R.id.ibAgosto);
        bagosto.setOnClickListener(this);
        bsetembro = (ImageButton) findViewById(R.id.ibSetembro);
        bsetembro.setOnClickListener(this);
        boutubro = (ImageButton) findViewById(R.id.ibOutubro);
        boutubro.setOnClickListener(this);
        bnovembro = (ImageButton) findViewById(R.id.ibNovembro);
        bnovembro.setOnClickListener(this);
        bdezembro = (ImageButton) findViewById(R.id.ibDezembro);
        bdezembro.setOnClickListener(this);

    }
    // Click Listeners
    //--------------------------------------------------------------------------------------

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bmeses:
                findViewById(R.id.include_main).setVisibility(View.INVISIBLE);
                findViewById(R.id.meses).setVisibility(View.VISIBLE);
                break;

            case R.id.bentrar:
                findViewById(R.id.include_main).setVisibility(View.VISIBLE);
                findViewById(R.id.inicio).setVisibility(View.INVISIBLE);
                break;

            case R.id.fab:
                findViewById(R.id.include_main).setVisibility(View.INVISIBLE);
                findViewById(R.id.menu_add).setVisibility(View.VISIBLE);
                break;

            case R.id.fabSair:
                findViewById(R.id.menu_add).setVisibility(View.INVISIBLE);
                findViewById(R.id.include_main).setVisibility(View.VISIBLE);
                break;

            case R.id.bAddGasto2:
                findViewById(R.id.menu_add).setVisibility(View.INVISIBLE);
                findViewById(R.id.gasto_add).setVisibility(View.VISIBLE);
                break;

            case R.id.bAddCredito:
                findViewById(R.id.menu_add).setVisibility(View.INVISIBLE);
                findViewById(R.id.addcredito).setVisibility(View.VISIBLE);
                break;

            case R.id.bCadastro:
                findViewById(R.id.menu_add).setVisibility(View.INVISIBLE);
                findViewById(R.id.cadastro).setVisibility(View.VISIBLE);
                break;
            case R.id.btn_cancelarID:
                findViewById(R.id.menu_add).setVisibility(View.VISIBLE);
                findViewById(R.id.gasto_add).setVisibility(View.INVISIBLE);
                break;
            case R.id.btn_limparID:
                limpaGasto();
                break;
            case R.id.btn_limparID2:
                limpaCredito();
                break;

            case R.id.btn_cancelarID2:
                findViewById(R.id.addcredito).setVisibility(View.INVISIBLE);
                findViewById(R.id.menu_add).setVisibility(View.VISIBLE);
                break;

            case R.id.ibJaneiro:
                mesSelected = "01";
                configurarRecycler();
                bMeses.setImageResource(mesesButtonArray[0]);
                findViewById(R.id.meses).setVisibility(View.INVISIBLE);
                findViewById(R.id.include_main).setVisibility(View.VISIBLE);

                break;
            case R.id.ibFevereiro:
                mesSelected = "02";
                configurarRecycler();
                bMeses.setImageResource(mesesButtonArray[1]);
                findViewById(R.id.meses).setVisibility(View.INVISIBLE);
                findViewById(R.id.include_main).setVisibility(View.VISIBLE);

                break;
            case R.id.ibMarco:
                mesSelected = "03";
                configurarRecycler();
                bMeses.setImageResource(mesesButtonArray[2]);
                findViewById(R.id.meses).setVisibility(View.INVISIBLE);
                findViewById(R.id.include_main).setVisibility(View.VISIBLE);

                break;
            case R.id.ibAbril:
                mesSelected = "04";
                configurarRecycler();
                bMeses.setImageResource(mesesButtonArray[3]);
                findViewById(R.id.meses).setVisibility(View.INVISIBLE);
                findViewById(R.id.include_main).setVisibility(View.VISIBLE);

                break;
            case R.id.ibMaio:
                mesSelected = "05";
                configurarRecycler();
                bMeses.setImageResource(mesesButtonArray[4]);
                findViewById(R.id.meses).setVisibility(View.INVISIBLE);
                findViewById(R.id.include_main).setVisibility(View.VISIBLE);

                break;
            case R.id.ibJunho:
                mesSelected = "06";
                configurarRecycler();
                bMeses.setImageResource(mesesButtonArray[5]);
                findViewById(R.id.meses).setVisibility(View.INVISIBLE);
                findViewById(R.id.include_main).setVisibility(View.VISIBLE);

                break;
            case R.id.ibJulho:
                mesSelected = "07";
                configurarRecycler();
                bMeses.setImageResource(mesesButtonArray[6]);
                findViewById(R.id.meses).setVisibility(View.INVISIBLE);
                findViewById(R.id.include_main).setVisibility(View.VISIBLE);

                break;
            case R.id.ibAgosto:
                mesSelected = "08";
                configurarRecycler();
                bMeses.setImageResource(mesesButtonArray[7]);
                findViewById(R.id.meses).setVisibility(View.INVISIBLE);
                findViewById(R.id.include_main).setVisibility(View.VISIBLE);

                break;
            case R.id.ibSetembro:
                mesSelected = "09";
                configurarRecycler();
                bMeses.setImageResource(mesesButtonArray[8]);
                findViewById(R.id.meses).setVisibility(View.INVISIBLE);
                findViewById(R.id.include_main).setVisibility(View.VISIBLE);

                break;
            case R.id.ibOutubro:
                mesSelected = "10";
                configurarRecycler();
                bMeses.setImageResource(mesesButtonArray[9]);
                findViewById(R.id.meses).setVisibility(View.INVISIBLE);
                findViewById(R.id.include_main).setVisibility(View.VISIBLE);

                break;
            case R.id.ibNovembro:
                mesSelected = "11";
                configurarRecycler();
                bMeses.setImageResource(mesesButtonArray[10]);
                findViewById(R.id.meses).setVisibility(View.INVISIBLE);
                findViewById(R.id.include_main).setVisibility(View.VISIBLE);

                break;
            case R.id.ibDezembro:
                mesSelected = "12";
                configurarRecycler();
                bMeses.setImageResource(mesesButtonArray[11]);
                findViewById(R.id.meses).setVisibility(View.INVISIBLE);
                findViewById(R.id.include_main).setVisibility(View.VISIBLE);

                break;
            case R.id.btn_salvarID:

                //pegando os valores
                String nomegasto = tvnome.getText().toString();
                String valorgasto = tvvalor.getText().toString();
                String datagasto = txtData.getText().toString();
                String categoriagasto = (String) categoria.getSelectedItem();

                if (nomegasto.equals("")) {
                    Snackbar.make(v, "Preencha o Nome!", Snackbar.LENGTH_SHORT).show();
                    if (valorgasto.equals("")) {
                        Snackbar.make(v, "Preencha o Valor!", Snackbar.LENGTH_SHORT).show();
                    }
                    if (datagasto.equals("")) {
                        Snackbar.make(v, "Selecione a Data!", Snackbar.LENGTH_SHORT).show();
                    }
                    if (categoriagasto.equals("")) {
                        Snackbar.make(v, "Preencha a Categoria!", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    //salvando os dados
                    Gasto gasto = new Gasto(0, nomegasto, valorgasto, datagasto,categoriagasto);
                    GastoDAO dao = new GastoDAO(getBaseContext());
                    long salvoID = dao.salvarItem(gasto);
                    if (salvoID != -1) {
                        //limpa os campos
                        tvnome.setText("");
                        tvvalor.setText("");


                        //adiciona no recyclerView
                        gasto.setID(salvoID);
                        adapter.adicionarGasto(gasto);
                        //tvDespesas.setText("R$ "+String.valueOf(dao.getValorTotal()));

                        Snackbar.make(v, "Salvo com Sucesso!", Snackbar.LENGTH_LONG).show();
                        findViewById(R.id.include_main).setVisibility(View.VISIBLE);
                        findViewById(R.id.gasto_add).setVisibility(View.INVISIBLE);
                        configurarRecycler();
                    } else {
                        Snackbar.make(v, "Erro ao salvarItem, consulte os logs!", Snackbar.LENGTH_LONG).show();
                        findViewById(R.id.include_main).setVisibility(View.VISIBLE);
                        findViewById(R.id.gasto_add).setVisibility(View.INVISIBLE);
                    }
                }


                break;


            case R.id.bSalvarCredito:
                //pegando os valores
                String nomeCredito = tvNomeCredito.getText().toString();
                String valorCredito = nValorCredito.getText().toString();
                String dataCredito = dataCom.format(d);
                String categoriaGasto = "Credito";
                if (nomeCredito.equals("")) {
                    Snackbar.make(v, "Preencha o Nome!", Snackbar.LENGTH_SHORT).show();
                    if (valorCredito.equals("")) {
                        Snackbar.make(v, "Preencha o Valor!", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    //salvando os dados
                    Gasto gasto = new Gasto(0, nomeCredito, valorCredito, dataCredito, categoriaGasto);
                    GastoDAO dao = new GastoDAO(getBaseContext());
                    long salvoID = dao.salvarItem(gasto);
                    if (salvoID != -1) {
                        //limpa os campos
                        tvNomeCredito.setText("");
                        tvNomeCredito.setText("");

                        gasto.setID(salvoID);

                        Snackbar.make(v, "Salvo com Sucesso!", Snackbar.LENGTH_LONG).show();
                        findViewById(R.id.include_main).setVisibility(View.VISIBLE);
                        findViewById(R.id.addcredito).setVisibility(View.INVISIBLE);
                        configurarRecycler();
                    } else {
                        Snackbar.make(v, "Erro ao salvarItem, consulte os logs!", Snackbar.LENGTH_LONG).show();
                        findViewById(R.id.include_main).setVisibility(View.VISIBLE);
                        findViewById(R.id.addcredito).setVisibility(View.INVISIBLE);
                    }
                    findViewById(R.id.gasto_add).setVisibility(View.INVISIBLE);
                }



                break;

        }
    }


    // configuração do botão fisico voltar do android
    //----------------------------------------------------------------------------------------------
    @Override
    public void onBackPressed() {

        if ((findViewById(R.id.include_main).getVisibility()) == View.VISIBLE){
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }

        if((findViewById(R.id.menu_add).getVisibility()) == View.VISIBLE)
            findViewById(R.id.menu_add).setVisibility(View.INVISIBLE);
        findViewById(R.id.include_main).setVisibility(View.VISIBLE);

        if((findViewById(R.id.gasto_add).getVisibility()) == View.VISIBLE)
            findViewById(R.id.gasto_add).setVisibility(View.INVISIBLE);
        findViewById(R.id.menu_add).setVisibility(View.VISIBLE);

        if((findViewById(R.id.addcredito).getVisibility()) == View.VISIBLE) {
            findViewById(R.id.addcredito).setVisibility(View.INVISIBLE);
            findViewById(R.id.include_main).setVisibility(View.INVISIBLE);
            findViewById(R.id.menu_add).setVisibility(View.VISIBLE);

        }
        if((findViewById(R.id.cadastro).getVisibility()) == View.VISIBLE)
            findViewById(R.id.cadastro).setVisibility(View.INVISIBLE);
        findViewById(R.id.menu_add).setVisibility(View.VISIBLE);

        if((findViewById(R.id.meses).getVisibility()) == View.VISIBLE){
            findViewById(R.id.meses).setVisibility(View.INVISIBLE);
            findViewById(R.id.menu_add).setVisibility(View.INVISIBLE);
            findViewById(R.id.include_main).setVisibility(View.VISIBLE);
        }

    }

    //LER QRCODE
    //----------------------------------------------------------------------------------------------
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult leitura = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        String[] cod;
        if(leitura!=null){
            if(leitura.getContents()!=null){

                cod =leitura.getContents().split("=");// separa a chave pois o texto lido retorna um site mais complexo

                BuscaNota buscaNota = new BuscaNota(cod[1]);

                qrdata = buscaNota.getDataEmissao(buscaNota.getWebPage());

                String date[];
                date = qrdata.split("/");
                Dia = Integer.parseInt(date[0]);
                Mes = Integer.parseInt(date[1]);
                Ano = Integer.parseInt(date[2]);

                AtualizarData();

                qrnome = buscaNota.getEmissor(buscaNota.getWebPage());
                qrvalor = buscaNota.getValor(buscaNota.getWebPage());


                tvnome.setText(qrnome);
                tvvalor.setText(qrvalor);


            }else{
                System.out.println("erro");
            }
        }else {
            onActivityResult(requestCode, resultCode, data);
        }

    }


    //metodo set do grafico
    //----------------------------------------------------------------------------------------------
    private void setupPieChart() {
        List<PieEntry> entries = new ArrayList<>();
        float entre = 0.0f, alim = 0.0f,trans = 0.0f, gast = 0.0f, total = 0.0f;
        String cate;

        GastoDAO gastos = new GastoDAO(this);
        List<Gasto> gastosTotais = gastos.retornaMes(mesSelected);
        for (int i=0;i<gastosTotais.size();i++){
            cate = gastosTotais.get(i).getCategoria();
            if (cate.equals("ENTRETERIMENTO")){
                entre = entre+ Float.parseFloat(gastosTotais.get(i).getValor().replaceAll(",","."));
                total =total + entre;
            }else if(cate.equals("ALIMENTAÇÃO")){
                alim = alim+ Float.parseFloat(gastosTotais.get(i).getValor().replaceAll(",","."));
                total =total + alim;
            }
            else if(cate.equals("TRANSPORTE")){
                trans = trans + Float.parseFloat(gastosTotais.get(i).getValor().replaceAll(",","."));
                total =total + trans;
            }
            else if(cate.equals("GASTOS MENSAIS")){
                gast = gast+ Float.parseFloat(gastosTotais.get(i).getValor().replaceAll(",","."));
                total =total + gast;
            }
        }

        entries.add(new PieEntry(entre, "ENTRETERIMENTO"));
        entries.add(new PieEntry(alim, "ALILMENTAÇÃO"));
        entries.add(new PieEntry(trans, "TRANSPORTE"));
        entries.add(new PieEntry(gast, "GASTOS MENSAIS"));

        PieDataSet set = new PieDataSet(entries, "Gastos");
        set.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(set);
        PieChart chart = (PieChart) findViewById(R.id.chart);
        chart.setData(data);
        chart.setEntryLabelColor(R.color.black);
        chart.invalidate(); // refresh
    }

    public void InicializaListeners()
    {
        txtData = (EditText) findViewById(R.id.txtData);
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
    {
        @Override
        public Dialog onCreateDialog(Bundle      savedInstanceState)
        {
            final Calendar calendario = Calendar.getInstance();
            Ano = calendario.get(Calendar.YEAR);
            Mes = calendario.get(Calendar.MONTH);
            Dia = calendario.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, Ano, Mes, Dia);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day)
        {
            Ano = year;
            Mes = month+1;
            Dia = day;
            AtualizarData();
            //MensagemData();
        }

        @Override
        public int show(FragmentTransaction transaction, String tag)
        {
            return super.show(transaction, tag);
        }
    }

    private static void AtualizarData()
    {
        if (Dia<10 && Mes <10){
            txtData.setText(new StringBuilder().append("0").append(Dia).append("/").append("0").append(Mes).append("/").append(Ano).append(" "));
        }else if (Dia<10 && Mes >=10){
            txtData.setText(new StringBuilder().append("0").append(Dia).append("/").append(Mes).append("/").append(Ano).append(" "));
        }else if (Dia>=10 && Mes <10){
            txtData.setText(new StringBuilder().append(Dia).append("/").append("0").append(Mes).append("/").append(Ano).append(" "));
        } else {
            txtData.setText(new StringBuilder().append(Dia).append("/").append(Mes).append("/").append(Ano).append(" "));
        }
    }

//    private static void MensagemData()
//    {
//        Toast.makeText(this, new StringBuilder().append("Data: ").append(txtData.getText()),  Toast.LENGTH_SHORT).show();
//    }

    public void MostrarData()
    {
        DialogFragment ClasseData = new  DatePickerFragment();
        ClasseData.show(getFragmentManager(),  "datepicker");
    }

    private void configurarRecycler() {
        DecimalFormat df = new DecimalFormat("0.00");
        float valorGastos = 0.00f;
        float valorCredito = 0.00f;
        float saldo = 0.00f;
        // Configurando o gerenciador de layout para ser uma lista.
        recyclerView = (RecyclerView) findViewById(R.id.main_recyclerViewID);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Adiciona o adapter que irá anexar os objetos à lista.
        GastoDAO dao = new GastoDAO(this);
        adapter = new GastosAdapter(dao.retornaMes(mesSelected));
        valorGastos = dao.getValorTotal();
        valorCredito = dao.getValorTotalCreditos();

        tvDespesas.setText("R$ "+ df.format(valorGastos));
        tvReceitas.setText("R$ "+df.format(valorCredito));

        saldo = (valorCredito - valorGastos);
        tvSaldo.setText("R$ " + df.format(saldo));

        if(saldo<0) {

        }else if(saldo>0){

        }

        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        setupPieChart();

        // Adicionar o arrastar para direita para excluir item
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(addArrastarItem());
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }


    // Recebendo retorno de activity chamadas
//    protected void onActivityResult(int codigo, int resultado, Intent i) {
//
//        // se o resultado de uma Activity for da Activity_DADOS_PESSOIS
//        if (codigo == Activity_DADOS_PESSOAIS) {
//            // se o "i" (Intent) estiver preenchido, pega os seus dados (getExtras())
//            Bundle params = i != null ? i.getExtras() : null;
//            if (params != null) {
//                seu_nome.setText(params.getString("Nome"));
//                seu_email.setText(params.getString("Email"));
//            }
//        }
//    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void compartilhar() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        GastoDAO dao = new GastoDAO(this);
        List<Gasto> gastos = dao.retornarTodos();
        String texto = "";
        for (Gasto gasto : gastos) {
            texto = texto + gasto.getNome() + "\n";
        }
        sendIntent.putExtra(Intent.EXTRA_TEXT, texto);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    public ItemTouchHelper.SimpleCallback addArrastarItem() {

        ItemTouchHelper.SimpleCallback deslizarItem = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Toast.makeText(getBaseContext(), "on Move", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                final int deleteViewID = viewHolder.getAdapterPosition();
                AlertDialog.Builder builder = new AlertDialog.Builder(viewHolder.itemView.getContext());
                builder.setTitle("Confirmação")
                        .setMessage("Tem certeza que deseja excluir este item? " + deleteViewID)
                        .setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                GastoDAO dao = new GastoDAO(getBaseContext());
                                int numItens = dao.excluirItem(adapter.getDbID(deleteViewID));
                                if (numItens > 0) {
                                    adapter.removerGasto(deleteViewID);

                                    Snackbar.make(main_layout, "Excluiu!", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                } else {
                                    Snackbar.make(main_layout, "Erro ao excluir o cliente!", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Snackbar.make(main_layout, "Cancelando...", Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();
                                adapter.cancelarRemocao(deleteViewID);
                            }
                        })
                        .create()
                        .show();
            }
        };
       // configurarRecycler();
        return deslizarItem;
    }

    private void limpaGasto(){
        tvnome.setText("");
        tvvalor.setText("");
    }

    private void limpaCredito(){
        tvNomeCredito.setText("");
        nValorCredito.setText("");
    }

    private void exportarLista() {

        String texto = "";
        GastoDAO dao = new GastoDAO(this);
        List<Gasto> gastos = dao.retornarTodos();
        for (Gasto gasto : gastos) {
            texto = texto + gasto.getNome() + ":" + gasto.getValor() + ":" + gasto.getData() + ":" + gasto.getCategoria() + "\n";

        }
        Log.w("Texto",texto);
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(NOME_ARQUIVO, Context.MODE_PRIVATE));
            outputStreamWriter.write(texto);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("MainActivity", e.toString());
        }

    }

    private void importarLista() {

        try {
            //Abrir o arquivo
            InputStream arquivo = openFileInput(NOME_ARQUIVO);
            if (arquivo != null) {
                // Conecta ao banco para remoção do  conexão
//                if (!db_conexao.onDelete()){
//                    Log.e("ListadeCompras:","Banco não removido");
//                }
                // Instancia o dao para criar o banco e a tabela
                GastoDAO dao = new GastoDAO(this);
                CreditoDAO credDAO = new CreditoDAO(this);
                dao.recriarTabela();
                credDAO.recriarTabela();
                //ler o arquivo
                InputStreamReader inputStreamReader = new InputStreamReader(arquivo);
                //Gerar Buffer do arquivo lido
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                //Recuperar textos do arquivo
                String linhaArquivo = "";
                while ((linhaArquivo = bufferedReader.readLine()) != null) {
                    Log.i("ListadeGastos", linhaArquivo);
                    String info[] = linhaArquivo.split(":");
                    Gasto item = new Gasto(0, info[0], info[1], info[2], info[3]);
                    dao.salvarItem(item);

                }
                arquivo.close();

                adapter = new GastosAdapter(dao.retornarTodos());
                recyclerView.setAdapter(adapter);
            }
        } catch (IOException e) {
            Log.e("MainActivity", e.toString());
        }
    }
}
