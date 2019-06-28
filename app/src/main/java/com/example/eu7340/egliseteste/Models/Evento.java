package com.example.eu7340.egliseteste.Models;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.Date;

@DatabaseTable(tableName = "tbl_eventos")
public class Evento {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(foreign = true, columnName = "id_igreja")
    private Congregacao congregacao;
    @DatabaseField
    private String nome;
    @DatabaseField(dataType = DataType.DATE)
    private java.util.Date dados_horario_inicio;
    @DatabaseField(dataType = DataType.DATE)
    private java.util.Date dados_horario_fim;
    @DatabaseField
    private String dados_local;
    @DatabaseField
    private String foto;
    @DatabaseField
    private String descricao;
    @DatabaseField(dataType = DataType.SQL_DATE)
    private Date created_at;
    @DatabaseField(dataType = DataType.SQL_DATE)
    private Date updated_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public java.util.Date getDados_horario_inicio() {
        return dados_horario_inicio;
    }

    public void setDados_horario_inicio(java.util.Date dados_horario_inicio) {
        this.dados_horario_inicio = dados_horario_inicio;
    }

    public java.util.Date getDados_horario_fim() {
        return dados_horario_fim;
    }

    public void setDados_horario_fim(java.util.Date dados_horario_fim) {
        this.dados_horario_fim = dados_horario_fim;
    }

    public String getDados_local() {
        return dados_local;
    }

    public void setDados_local(String dados_local) {
        this.dados_local = dados_local;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public Congregacao getCongregacao() {
        return congregacao;
    }

    public void setCongregacao(Congregacao congregacao) {
        this.congregacao = congregacao;
    }
}
