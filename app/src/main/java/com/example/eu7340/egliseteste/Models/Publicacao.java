package com.example.eu7340.egliseteste.Models;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.Date;

@DatabaseTable(tableName = "tbl_publicacoes")
public class Publicacao {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(foreign = true, columnName = "id_igreja")
    private Congregacao congregacao;
    @DatabaseField(dataType = DataType.BOOLEAN)
    private boolean galeria;
    @DatabaseField
    private String nome;
    @DatabaseField
    private String html;
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

    public boolean isGaleria() {
        return galeria;
    }

    public void setGaleria(boolean galeria) {
        this.galeria = galeria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
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
