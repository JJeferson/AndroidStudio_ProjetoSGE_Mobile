package com.sge_mobileandroid.sge_mobileandroid;

/**
 * Created by Jeferson on 29/08/2017.
 */
public class insere_Produtos_Pedidos {
    private String Descricao;
    private String QTDE;
    private String Valor;

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

    public String getQTDE() {
        return QTDE;
    }

    public void setQTDE(String QTDE) {
        this.QTDE = QTDE;
    }

    public String getValor() {
        return Valor;
    }

    public void setValor(String valor) {
        Valor = valor;
    }

    public String getCod_Prod() {
        return Cod_Prod;
    }

    public void setCod_Prod(String cod_Prod) {
        Cod_Prod = cod_Prod;
    }

    public String getTotal_Unitario() {
        return Total_Unitario;
    }

    public void setTotal_Unitario(String total_Unitario) {
        Total_Unitario = total_Unitario;
    }

    private String Cod_Prod;
    private String Total_Unitario;
}//fim da classe java
