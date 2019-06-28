package com.example.eu7340.egliseteste.DB;

import com.example.eu7340.egliseteste.Models.Modulo;
import com.example.eu7340.egliseteste.Models.ModuloPermissao;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public class ModuloPermissaoDAO extends BaseDaoImpl<ModuloPermissao, Integer> {

    public ModuloPermissaoDAO(ConnectionSource connectionSource) throws SQLException {
        super(ModuloPermissao.class);
        setConnectionSource(connectionSource);
        initialize();
    }

}
