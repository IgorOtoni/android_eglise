package com.example.eu7340.egliseteste.Models;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.Date;

@DatabaseTable(tableName = "tbl_frequencias")
public class Frequencia {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(foreign = true, columnName = "id_membro_comunidade")
    private MembroComunidade membroComunidade;
    @DatabaseField(foreign = true, columnName = "id_reuniao")
    private Reuniao reuniao;
    @DatabaseField
    private boolean ausente;
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

    public MembroComunidade getMembroComunidade() {
        return membroComunidade;
    }

    public void setMembroComunidade(MembroComunidade membroComunidade) {
        this.membroComunidade = membroComunidade;
    }

    public boolean isAusente() {
        return ausente;
    }

    public void setAusente(boolean ausente) {
        this.ausente = ausente;
    }

    public Reuniao getReuniao() {
        return reuniao;
    }

    public void setReuniao(Reuniao reuniao) {
        this.reuniao = reuniao;
    }
}
