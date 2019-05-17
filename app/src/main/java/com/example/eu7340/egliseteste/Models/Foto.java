package com.example.eu7340.egliseteste.Models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.Date;

@DatabaseTable(tableName = "tbl_fotos")
public class Foto {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private int id_galeria;
    @DatabaseField
    private String foto;
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

    public int getId_galeria() {
        return id_galeria;
    }

    public void setId_galeria(int id_galeria) {
        this.id_galeria = id_galeria;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
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
