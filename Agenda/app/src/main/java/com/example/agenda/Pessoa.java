package com.example.agenda;

class Pessoa {
    private String nome, telefone, email, endereco, website;

    Pessoa(String nome, String telefone, String email, String endereco, String website) {
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.endereco = endereco;
        this.website = website;
    }

    void setNome(String s) {
        this.nome = s;
    }

    void setTelefone(String s) {
        this.telefone = s;
    }

    void setEmail(String s) {
        this.email = s;
    }

    void setEndereco(String s) {
        this.endereco = s;
    }


}
