package com.verbanek.organize.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.verbanek.organize.config.ConfiguracaoFirebase;

public class Usuario {
    private String idUsuario;
    private String nome;
    private String email;
    private String senha;
    private Double receitaTotal = 0.00;
    private Double DespesaTotal = 0.00;

    public Usuario() {
    }

    public void salvar(){
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebase();
        firebase.child("usuarios")
                .child(this.idUsuario)
                .setValue(this);
    }

    public Double getReceitaTotal() {
        return receitaTotal;
    }

    public void setReceitaTotal(Double receitaTotal) {
        this.receitaTotal = receitaTotal;
    }

    public Double getDespesaTotal() {
        return DespesaTotal;
    }

    public void setDespesaTotal(Double despesaTotal) {
        DespesaTotal = despesaTotal;
    }

    @Exclude
    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
