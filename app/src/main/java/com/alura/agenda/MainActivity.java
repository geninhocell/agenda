package com.alura.agenda;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.alura.agenda.dao.AlunoDAO;
import com.alura.agenda.modelo.Aluno;
import androidx.appcompat.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listaAlunos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        listaAlunos = findViewById(R.id.lista_alunos);

        listaAlunos.setOnItemClickListener((lista, item, position, id) -> {
            Aluno aluno = (Aluno) listaAlunos.getItemAtPosition(position);
            Intent intentVaiFormulario = new Intent(MainActivity.this, FormularioActivity.class);
            intentVaiFormulario.putExtra("aluno", aluno);
            startActivity(intentVaiFormulario);
        });

        Button novoAluno = findViewById(R.id.novo_aluno);
        novoAluno.setOnClickListener(v -> {
            Intent intentVaiFormulario = new Intent(MainActivity.this, FormularioActivity.class);
            startActivity(intentVaiFormulario);
        });

        registerForContextMenu(listaAlunos);
    }

    private void carregaLista() {
        AlunoDAO alunoDAO = new AlunoDAO(this);
        List<Aluno> alunos = alunoDAO.buscaAlunos();
        alunoDAO.close();
        ArrayAdapter<Aluno> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, alunos);
        listaAlunos.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregaLista();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final Aluno aluno = (Aluno) listaAlunos.getItemAtPosition(info.position);
        
        MenuItem itemSMS = menu.add("Enviar SMS");
        Intent intentSMS = new Intent(Intent.ACTION_VIEW);
        intentSMS.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intentSMS.setData(Uri.parse("sms:" + aluno.getTelefone()));
        itemSMS.setIntent(intentSMS);

        MenuItem itemMapa = menu.add("Visualizar no mapa");
        Intent intentMapa = new Intent(Intent.ACTION_VIEW);
        intentMapa.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intentMapa.setData(Uri.parse("geo:0,0?q=" + aluno.getEndereco()));
        itemMapa.setIntent(intentMapa);

        MenuItem itemSite = menu.add("Visitar site");
        Intent intentSite = new Intent(Intent.ACTION_VIEW);
        intentSite.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);



        String site = aluno.getSite();
        if(!site.startsWith("https://")){
            site = "https://" + site;
        }

        intentSite.setData(Uri.parse(site));
        itemSite.setIntent(intentSite);

        MenuItem deletar = menu.add("Deletar");
        deletar.setOnMenuItemClickListener(item -> {
            AlunoDAO alunoDAO = new AlunoDAO(this);
            alunoDAO.deleta(aluno);
            alunoDAO.close();
            carregaLista();
            return false;
        });
    }
}