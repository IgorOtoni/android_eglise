package com.example.eu7340.egliseteste.Models;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.Date;

@DatabaseTable(tableName = "tbl_perfis_permissoes")
public class PerfilPermissao {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(foreign = true, columnName = "id_permissao")
    private Permissao permissao;
    @DatabaseField(foreign = true, columnName = "id_perfil_igreja_modulo")
    private PerfilCongregacaoModulo perfilCongregacaoModulo;
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

    public PerfilCongregacaoModulo getPerfilCongregacaoModulo() {
        return perfilCongregacaoModulo;
    }

    public void setPerfilCongregacaoModulo(PerfilCongregacaoModulo perfilCongregacaoModulo) {
        this.perfilCongregacaoModulo = perfilCongregacaoModulo;
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

    public Permissao getPermissao() {
        return permissao;
    }

    public void setPermissao(Permissao permissao) {
        this.permissao = permissao;
    }
}
