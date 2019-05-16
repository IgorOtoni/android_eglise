package com.example.eu7340.egliseteste.Models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.Date;

@DatabaseTable(tableName = "tbl_publicacoes")
public class Publicacao {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private int id_igreja;
    @DatabaseField
    private boolean galeria;
    @DatabaseField
    private String nome;
    @DatabaseField
    private String html;
    @DatabaseField
    private Date created_at;
    @DatabaseField
    private Date updated_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_igreja() {
        return id_igreja;
    }

    public void setId_igreja(int id_igreja) {
        this.id_igreja = id_igreja;
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
}
