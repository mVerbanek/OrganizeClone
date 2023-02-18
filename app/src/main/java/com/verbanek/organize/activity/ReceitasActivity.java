package com.verbanek.organize.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.verbanek.organize.R;
import com.verbanek.organize.config.ConfiguracaoFirebase;
import com.verbanek.organize.helper.Base64Custom;
import com.verbanek.organize.helper.DateCustom;
import com.verbanek.organize.model.Movimentacao;
import com.verbanek.organize.model.Usuario;

public class ReceitasActivity extends AppCompatActivity {

    private TextInputEditText campoData, campoCategoria, campoDescricao;
    private EditText campoValor;
    private Movimentacao movimentacao;
    private FloatingActionButton fabSalvar;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private Double receitaTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receitas);

        campoData = findViewById(R.id.editData);
        campoCategoria = findViewById(R.id.editCategoria);
        campoDescricao = findViewById(R.id.editDescricao);
        campoValor = findViewById(R.id.editValor);
        fabSalvar = findViewById(R.id.fabSalvar);

        campoData.setText(DateCustom.dataAtual());
        recuparReceitaTotal();

        fabSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvarReceita();
            }
        });
    }

    public Boolean validarCamposReceita(){

        String textoValor = campoValor.getText().toString();
        String textoData = campoData.getText().toString();
        String textoCategoria = campoCategoria.getText().toString();
        String textoDescricao = campoDescricao.getText().toString();

        if ( !textoValor.isEmpty() ){
            if ( !textoData.isEmpty() ){
                if ( !textoCategoria.isEmpty() ){
                    if ( !textoDescricao.isEmpty() ){
                        return true;
                    }else{
                        Toast.makeText(ReceitasActivity.this, "Descrição não foi preenchida", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }else{
                    Toast.makeText(ReceitasActivity.this, "Categoria não foi preenchida", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }else{
                Toast.makeText(ReceitasActivity.this, "Data não foi preenchida", Toast.LENGTH_SHORT).show();
                return false;
            }
        }else{
            Toast.makeText(ReceitasActivity.this, "Valor não foi preenchido", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void salvarReceita(){

        String data = campoData.getText().toString();
        Double ValorRec = Double.parseDouble(campoValor.getText().toString());

        if(validarCamposReceita()){
            movimentacao = new Movimentacao();
            movimentacao.setValor(ValorRec);
            movimentacao.setCategoria(campoCategoria.getText().toString());
            movimentacao.setDescricao(campoDescricao.getText().toString());
            movimentacao.setData(data);
            movimentacao.setTipo("R");

            atualizarReceita(receitaTotal + ValorRec);

            movimentacao.salvar(data);

            finish();
        }
    }

    public void recuparReceitaTotal(){
        String idUsuario = Base64Custom.codificarBase64(autenticacao.getCurrentUser().getEmail());
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                receitaTotal = usuario.getReceitaTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void atualizarReceita(Double receita){
        String idUsuario = Base64Custom.codificarBase64(autenticacao.getCurrentUser().getEmail());
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        usuarioRef.child("receitaTotal").setValue(receita);
    }
}