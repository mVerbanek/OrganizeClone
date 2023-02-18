package com.verbanek.organize.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.verbanek.organize.config.ConfiguracaoFirebase;
import com.verbanek.organize.helper.Base64Custom;
import com.verbanek.organize.helper.DateCustom;

import java.util.Date;

public class Movimentacao {

    private String data;
    private String categoria;
    private String descricao;
    private String tipo;
    private Double valor;
    private String key;

    public Movimentacao() {
    }

    public void salvar(String dataEscolhida){

        FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        String idUsuario = Base64Custom.codificarBase64(autenticacao.getCurrentUser().getEmail());

        DatabaseReference firebase = ConfiguracaoFirebase.getFirebase();
        firebase.child("movimentacao")
                .child(idUsuario)
                .child(DateCustom.mesAnoDataEscolhida( dataEscolhida ))
                .push()
                .setValue(this);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
}
