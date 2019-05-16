package com.example.eu7340.egliseteste.Models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "tbl_configuracoes")
public class Configuracao {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private int id_igreja;
    @DatabaseField
    private int id_template;
    @DatabaseField
    private String cor;
    @DatabaseField
    private String url;
    @DatabaseField
    private String texto_apresentativo;
    @DatabaseField
    private String facebook;
    @DatabaseField
    private String twitter;
    @DatabaseField
    private String youtube;
    @DatabaseField
    private String created_at;
    @DatabaseField
    private String updated_at;

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

    public int getId_template() {
        return id_template;
    }

    public void setId_template(int id_template) {
        this.id_template = id_template;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTexto_apresentativo() {
        return texto_apresentativo;
    }

    public void setTexto_apresentativo(String texto_apresentativo) {
        this.texto_apresentativo = texto_apresentativo;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}

