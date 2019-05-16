package com.example.eu7340.egliseteste.Models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.Date;

@DatabaseTable(tableName = "tbl_eventos_fixos")
public class EventoFixo {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private int id_igreja;
    @DatabaseField
    private String nome;
    @DatabaseField
    private String dados_horario_local;
    @DatabaseField
    private String foto;
    @DatabaseField
    private String descricao;
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDados_horario_local() {
        return dados_horario_local;
    }

    public void setDados_horario_local(String dados_horario_local) {
        this.dados_horario_local = dados_horario_local;
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
}
