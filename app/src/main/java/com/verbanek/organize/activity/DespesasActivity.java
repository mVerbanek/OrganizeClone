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

public class DespesasActivity extends AppCompatActivity {

    private TextInputEditText campoData, campoCategoria, campoDescricao;
    private EditText campoValor;
    private Movimentacao movimentacao;
    private FloatingActionButton fabSalvar;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private Double despesaTotal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesas);

        campoData = findViewById(R.id.editData);
        campoCategoria = findViewById(R.id.editCategoria);
        campoDescricao = findViewById(R.id.editDescricao);
        campoValor = findViewById(R.id.editValor);
        fabSalvar = findViewById(R.id.fabSalvar);

        campoData.setText(DateCustom.dataAtual());

        recuparDespesaTotal();

        fabSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvarDespesa();
            }
        });
    }

    public void salvarDespesa(){

        String data = campoData.getText().toString();
        Double ValorRec = Double.parseDouble(campoValor.getText().toString());

        if(validarCamposDespesa()){
            movimentacao = new Movimentacao();
            movimentacao.setValor(ValorRec);
            movimentacao.setCategoria(campoCategoria.getText().toString());
            movimentacao.setDescricao(campoDescricao.getText().toString());
            movimentacao.setData(data);
            movimentacao.setTipo("D");
            
            atualizarDespesa(despesaTotal + ValorRec);

            movimentacao.salvar(data);

            finish();
        }
    }

    public Boolean validarCamposDespesa(){

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
                        Toast.makeText(DespesasActivity.this, "Descrição não foi preenchida", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }else{
                    Toast.makeText(DespesasActivity.this, "Categoria não foi preenchida", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }else{
                Toast.makeText(DespesasActivity.this, "Data não foi preenchida", Toast.LENGTH_SHORT).show();
                return false;
            }
        }else{
            Toast.makeText(DespesasActivity.this, "Valor não foi preenchido", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void recuparDespesaTotal(){
        String idUsuario = Base64Custom.codificarBase64(autenticacao.getCurrentUser().getEmail());
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                despesaTotal = usuario.getDespesaTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void atualizarDespesa(Double despesa){
        String idUsuario = Base64Custom.codificarBase64(autenticacao.getCurrentUser().getEmail());
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        usuarioRef.child("despesaTotal").setValue(despesa);
    }
}