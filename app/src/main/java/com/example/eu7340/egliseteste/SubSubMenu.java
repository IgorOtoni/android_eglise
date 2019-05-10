package com.example.eu7340.egliseteste;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "tbl_sub_sub_menus")
public class SubSubMenu {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private int id_submenu;
    @DatabaseField
    private int ordem;
    @DatabaseField
    private String nome;
    @DatabaseField
    private String link;
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

    public int getId_configuracao() {
        return id_submenu;
    }

    public void setId_configuracao(int id_submenu) {
        this.id_submenu = id_submenu;
    }

    public int getOrdem() {
        return ordem;
    }

    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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

