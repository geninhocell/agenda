package com.alura.agenda;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alura.agenda.converter.AlunoConverter;
import com.alura.agenda.dao.AlunoDAO;
import com.alura.agenda.modelo.Aluno;

import java.util.List;

public class EnviaAlunosTask extends AsyncTask<Void, Void, String> {
    private MainActivity context;
    private ProgressBar progressBar;

    public EnviaAlunosTask(MainActivity context) {
        this.context = context;
        this.progressBar = context.findViewById(R.id.note_list_progress);
    }

    private void exibirProgress(boolean exibir) {
        progressBar.setVisibility(exibir ? View.VISIBLE : View.GONE);
    }

    // executa antes do doInBackground
    // executa na thread principal
    @Override
    protected void onPreExecute() {
        exibirProgress(true);
    }

    @Override
    protected String doInBackground(Void[] objects) {
        AlunoDAO dao = new AlunoDAO(context);
        List<Aluno> alunos = dao.buscaAlunos();
        dao.close();

        AlunoConverter conversor = new AlunoConverter();
        String json = conversor.converteParaJSON(alunos);

        WebClient client = new WebClient();
        String resposta = client.post(json);

        return resposta;
    }

    // executa depois do doInBackground
    // executa na thread principal
    @Override
    protected void onPostExecute(String resposta) {
        exibirProgress(false);
        Toast.makeText(context, resposta, Toast.LENGTH_LONG).show();
    }
}
