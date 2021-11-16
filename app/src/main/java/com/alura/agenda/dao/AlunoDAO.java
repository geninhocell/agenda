package com.alura.agenda.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alura.agenda.modelo.Aluno;

import java.util.ArrayList;
import java.util.List;

public class AlunoDAO extends SQLiteOpenHelper {

    public AlunoDAO(@Nullable Context context) {
        super(context, "Agenda", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE Alunos (id INTEGER PRIMARY KEY, " +
                "nome TEXT NOT NULL, " +
                "endereco TEXT, " +
                "telefone TEXT, " + "" +
                "site TEXT, " +
                "nota REAL, " +
                "caminhoFoto TEXT);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        String sql = "DROP TABLE IF EXISTS Alunos;";
        String sql = "";
        switch (oldVersion){
            case 1:
                sql = "ALTER TABLE Alunos ADD COLUMN caminhoFoto TEXT;";
                db.execSQL(sql);
        }

//        onCreate(db);
    }

    public void insere(Aluno aluno) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues dados = getContentValues(aluno);

        db.insert("Alunos", null, dados);
    }

    @NonNull
    private ContentValues getContentValues(Aluno aluno) {
        ContentValues dados = new ContentValues();
        dados.put("nome", aluno.getNome());
        dados.put("endereco", aluno.getEndereco());
        dados.put("telefone", aluno.getTelefone());
        dados.put("site", aluno.getSite());
        dados.put("nota", aluno.getNota());
        dados.put("caminhoFoto", aluno.getCaminhoFoto());
        return dados;
    }

    @SuppressLint("Range")
    public List<Aluno> buscaAlunos() {
        String sql = "SELECT * FROM Alunos;";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        List<Aluno> alunos = new ArrayList<>();
        while(cursor.moveToNext()) {
            Aluno aluno = new Aluno();
            aluno.setId(cursor.getLong(cursor.getColumnIndex("id")));
            aluno.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            aluno.setEndereco(cursor.getString(cursor.getColumnIndex("endereco")));
            aluno.setTelefone(cursor.getString(cursor.getColumnIndex("telefone")));
            aluno.setSite(cursor.getString(cursor.getColumnIndex("site")));
            aluno.setNota(cursor.getDouble(cursor.getColumnIndex("nota")));
            aluno.setCaminhoFoto(cursor.getString(cursor.getColumnIndex("caminhoFoto")));

            alunos.add(aluno);
        }
        cursor.close();

        return alunos;
    }

    public void deleta(Aluno aluno) {
        SQLiteDatabase db = getWritableDatabase();

        String[] params = {aluno.getId().toString()};
        db.delete("Alunos", "id = ?", params);
    }

    public void altera(Aluno aluno) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues dados = getContentValues(aluno);

        String[] params = {aluno.getId().toString()};
        db.update("Alunos", dados, "id = ?", params);
    }

    public boolean alunoExiste(String telefone){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM Alunos WHERE telefone = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{telefone});
        boolean result = cursor.getCount() > 0;
        cursor.close();

        return result;
    }
}
