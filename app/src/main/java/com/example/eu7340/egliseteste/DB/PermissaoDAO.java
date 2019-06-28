package com.example.eu7340.egliseteste.DB;

import com.example.eu7340.egliseteste.Models.Modulo;
import com.example.eu7340.egliseteste.Models.Permissao;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public class PermissaoDAO extends BaseDaoImpl<Permissao, Integer> {

    public PermissaoDAO(ConnectionSource connectionSource) throws SQLException {
        super(Permissao.class);
        setConnectionSource(connectionSource);
        initialize();
    }

}
