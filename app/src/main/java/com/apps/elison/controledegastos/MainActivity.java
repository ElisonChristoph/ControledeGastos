package com.apps.elison.controledegastos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.elison.controledegastos.DAO.Gasto;
import com.apps.elison.controledegastos.DAO.GastoDAO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String NOME_ARQUIVO = "arquivo_gastos.txt";
    private static final int Activity_DADOS_PESSOAIS = 10;

    private RecyclerView recyclerView;
    private GastosAdapter adapter;

    private TextView seu_nome;
    private TextView seu_email;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private ConstraintLayout main_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main_layout = findViewById(R.id.main_layoutID);

        ImageButton btnEntrar = (ImageButton) findViewById(R.id.bentrar);
        btnEntrar.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.include_main).setVisibility(View.VISIBLE);
                findViewById(R.id.inicio).setVisibility(View.INVISIBLE);
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.include_main).setVisibility(View.INVISIBLE);
                findViewById(R.id.menu_add).setVisibility(View.VISIBLE);
            }
        });

        FloatingActionButton fabSair = (FloatingActionButton) findViewById(R.id.fabSair);
        fabSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.menu_add).setVisibility(View.INVISIBLE);
                findViewById(R.id.include_main).setVisibility(View.VISIBLE);
            }
        });



        //Menu

        Button bAddGasto = (Button) findViewById(R.id.bAddGasto2);
        bAddGasto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.menu_add).setVisibility(View.INVISIBLE);
               findViewById(R.id.gasto_add).setVisibility(View.VISIBLE);
            }
        });

        // Obtem a referência do layout de navegação
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Obtem a referência da view de cabeçalho (importante para achar os componentes)
        View headerView = navigationView.getHeaderView(0);
        seu_nome = headerView.findViewById(R.id.seuNomeID);
        seu_email = headerView.findViewById(R.id.seuEmailID);

        // Inicialização para gravar as preferencias
        pref = getSharedPreferences("ListaGastosPrefArq", MODE_PRIVATE);
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


        //        Botões de Incluir/Cancelar/Limpar
        Button btnCancelar = (Button) findViewById(R.id.btn_cancelarID);
        btnCancelar.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.menu_add).setVisibility(View.VISIBLE);
                findViewById(R.id.gasto_add).setVisibility(View.INVISIBLE);
            }
        });


        Button btnLimpar = (Button) findViewById(R.id.btn_limparID);
        btnLimpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        Button btnSalvar = (Button) findViewById(R.id.btn_salvarID);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView txtNome = findViewById(R.id.tvNome);
                TextView txtValor = findViewById(R.id.tvValor);
                TextView txtCategoria = findViewById(R.id.tvCategoria);
                TextView txtData = findViewById(R.id.tvData);

                //pegando os valores
                String nome = txtNome.getText().toString();
                int valor = Integer.parseInt(txtValor.getText().toString());
                String categoria = txtCategoria.getText().toString();
                String data = txtData.getText().toString();

                if (nome.equals("")) {
                    Snackbar.make(view, "Preencha o nome!", Snackbar.LENGTH_SHORT).show();
                } else {
                    //salvando os dados
                    Gasto gasto = new Gasto(0, categoria, nome, data, valor);
                    GastoDAO dao = new GastoDAO(getBaseContext());
                    long salvoID = dao.salvarItem(gasto);
                    if (salvoID != -1) {
                        //limpa os campos
                        txtNome.setText("");
                        txtValor.setText("");
                        txtCategoria.setText("");
                        txtData.setText("");

                        //adiciona no recyclerView
                        gasto.setID(salvoID);
                        adapter.adicionarGasto(gasto);

                        Snackbar.make(view, "Salvou!", Snackbar.LENGTH_LONG).show();
                        findViewById(R.id.menu_add).setVisibility(View.VISIBLE);
                        findViewById(R.id.gasto_add).setVisibility(View.INVISIBLE);
                    } else {
                        Snackbar.make(view, "Erro ao salvarItem, consulte os logs!", Snackbar.LENGTH_LONG).show();
                        findViewById(R.id.menu_add).setVisibility(View.VISIBLE);
                        findViewById(R.id.gasto_add).setVisibility(View.INVISIBLE);
                    }
                }
            }
        });


        configurarRecycler();
    }

    private void configurarRecycler() {
        // Configurando o gerenciador de layout para ser uma lista.
        recyclerView = (RecyclerView) findViewById(R.id.main_recyclerViewID);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Adiciona o adapter que irá anexar os objetos à lista.
        GastoDAO dao = new GastoDAO(this);
        adapter = new GastosAdapter(dao.retornarTodos());
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        // Adicionar o arrastar para direita para excluir item
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(addArrastarItem());
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }


    // Recebendo retorno de activity chamadas
    protected void onActivityResult(int codigo, int resultado, Intent i) {

        // se o resultado de uma Activity for da Activity_DADOS_PESSOIS
        if (codigo == Activity_DADOS_PESSOAIS) {
            // se o "i" (Intent) estiver preenchido, pega os seus dados (getExtras())
            Bundle params = i != null ? i.getExtras() : null;
            if (params != null) {
                seu_nome.setText(params.getString("Nome"));
                seu_email.setText(params.getString("Email"));
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


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
        return deslizarItem;
    }

    private void exportarLista() {

        String texto = "";
        GastoDAO dao = new GastoDAO(this);
        List<Gasto> compras = dao.retornarTodos();
        for (Gasto item : compras) {
            texto = texto + item.getNome() + ":" + item.getData() + ":" + item.getCategoria() + "\n";

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
                dao.recriarTabela();
                //ler o arquivo
                InputStreamReader inputStreamReader = new InputStreamReader(arquivo);
                //Gerar Buffer do arquivo lido
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                //Recuperar textos do arquivo
                String linhaArquivo = "";
                while ((linhaArquivo = bufferedReader.readLine()) != null) {
                    Log.i("ListadeGastos", linhaArquivo);
                    String info[] = linhaArquivo.split(":");
                    Gasto item = new Gasto(0, info[0], info[1], info[2], Integer.parseInt(info[3]));
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
