package com.example.eu7340.egliseteste.DB;

import com.example.eu7340.egliseteste.Models.PerfilPermissao;
import com.example.eu7340.egliseteste.Models.Permissao;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public class PerfilPermissaoDAO extends BaseDaoImpl<PerfilPermissao, Integer> {

    public PerfilPermissaoDAO(ConnectionSource connectionSource) throws SQLException {
        super(PerfilPermissao.class);
        setConnectionSource(connectionSource);
        initialize();
    }

}
