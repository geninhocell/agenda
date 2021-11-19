package com.alura.agenda;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alura.agenda.modelo.Prova;

public class DetalhesProvaFragment extends Fragment {
    private TextView campoMateria;
    private TextView campoData;
    private ListView listaTopicos;

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        View viewInflate = inflater.inflate(R.layout.fragment_detalhes_prova, container, false);
        campoMateria = viewInflate.findViewById(R.id.detalhes_prova_materia);
        campoData = viewInflate.findViewById(R.id.detalhes_prova_data);
        listaTopicos = viewInflate.findViewById(R.id.detalhes_prova_topicos);

        Bundle parametros = getArguments();
        if(parametros != null) {
            Prova prova = (Prova) parametros.getSerializable("prova");
            populaCamposCom(prova);
        }

        return viewInflate;
    }

    public void populaCamposCom(Prova prova){
        campoMateria.setText(prova.getMateria());
        campoData.setText(prova.getData());

        ArrayAdapter<String> adapterTopicos = new ArrayAdapter<String>(
                getContext(),
                android.R.layout.simple_list_item_1,
                prova.getTopicos()
        );
        listaTopicos.setAdapter(adapterTopicos);
    }
}