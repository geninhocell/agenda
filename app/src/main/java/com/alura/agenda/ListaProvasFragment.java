package com.alura.agenda;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alura.agenda.modelo.Prova;

import java.util.Arrays;
import java.util.List;

public class ListaProvasFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewInflate = inflater.inflate(R.layout.fragment_lista_provas, container, false);

        List<String> topicosPortugues = Arrays.asList("Sujeito", "Objeto direto", "Objeto indireto");
        Prova provaPortugues = new Prova("Português", "25/05/2022", topicosPortugues);

        List<String> topicosMatematica = Arrays.asList("Equações do 1° grau", "Trigonometria", "Razão e proporção");
        Prova provaMatematica = new Prova("Matemática", "27/05/2022", topicosMatematica);

        List<Prova> provas = Arrays.asList(provaPortugues, provaMatematica);

        ArrayAdapter<Prova> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, provas);

        ListView lista = viewInflate.findViewById(R.id.provas_lista);
        lista.setAdapter(adapter);

        lista.setOnItemClickListener((parent, view, position, id) -> {
            Prova prova = (Prova) parent.getItemAtPosition(position);
            Toast.makeText(getContext(), "Clicou na prova de " + prova, Toast.LENGTH_SHORT).show();
            Intent vaiPraDetalhes = new Intent(getContext(), DetalhesProvaActivity.class);
            vaiPraDetalhes.putExtra("prova", prova);
            startActivity(vaiPraDetalhes);
        });

        return viewInflate;
    }
}
