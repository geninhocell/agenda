package com.alura.agenda;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.alura.agenda.dao.AlunoDAO;
import com.alura.agenda.modelo.Aluno;

import java.io.File;

public class FormularioActivity extends AppCompatActivity {
    private static final int CODIGO_CAMERA = 567;

    private FormularioHelper helper;
    private String caminhoFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        helper = new FormularioHelper(this);

        Intent intent = getIntent();
        Aluno aluno = (Aluno) intent.getSerializableExtra("aluno");
        if(aluno != null){
            helper.preencheFormulario(aluno);
        }

        Button botaoFoto = findViewById(R.id.formulario_botao_foto);
        botaoFoto.setOnClickListener(v -> {
            Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            caminhoFoto = getExternalFilesDir(null) + "/" + System.currentTimeMillis() + ".jpg";
            File arquivoFoto = new File(caminhoFoto);
            Uri uri = FileProvider.getUriForFile(getBaseContext(), "com.alura.agenda.fileprovider", arquivoFoto);
            intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intentCamera, CODIGO_CAMERA);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            if (requestCode == CODIGO_CAMERA) {
                ImageView foto = findViewById(R.id.formulario_foto);
                Bitmap bitmap = BitmapFactory.decodeFile(caminhoFoto);
                Bitmap bitmapReduzido = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
                foto.setImageBitmap(bitmapReduzido);
                foto.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_formulario, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_formulario_ok) {
            Aluno aluno = helper.pegaAluno();
            AlunoDAO alunoDAO = new AlunoDAO(this);

            if(aluno.getId() != null){
                alunoDAO.altera(aluno);
            }else{
                alunoDAO.insere(aluno);
            }

            alunoDAO.close();

            Toast.makeText(FormularioActivity.this, "Aluno " + aluno.getNome() + " salvo!", Toast.LENGTH_SHORT).show();
            finish();
        }


        return super.onOptionsItemSelected(item);
    }
}