package com.example.eu7340.egliseteste.DB;

import com.example.eu7340.egliseteste.Models.Funcao;
import com.example.eu7340.egliseteste.Models.Sermao;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public class FuncaoDAO extends BaseDaoImpl<Funcao, Integer> {

    public FuncaoDAO(ConnectionSource connectionSource) throws SQLException {
        super(Funcao.class);
        setConnectionSource(connectionSource);
        initialize();
    }

}
