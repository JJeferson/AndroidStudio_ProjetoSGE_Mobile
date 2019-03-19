package com.sge_mobileandroid.sge_mobileandroid;

/**
 * Classe destinada a gravar os dados dos usuarios no firebase
 */
public class insere_Clientes {
    private String Nome;
    private String Fone;
    private String Email;
    private String Endereco;
    private String CodigoExterno;

    public String getCodigoExterno() {
        return CodigoExterno;
    }

    public void setCodigoExterno(String codigoExterno) {
        CodigoExterno = codigoExterno;
    }

    public String getEndereco() {
        return Endereco;
    }

    public void setEndereco(String endereco) {
        Endereco = endereco;
    }



    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getFone() {
        return Fone;
    }

    public void setFone(String fone) {
        Fone = fone;
    }
}
