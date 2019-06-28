package com.example.eu7340.egliseteste.DB;

import com.example.eu7340.egliseteste.Models.Comunidade;
import com.example.eu7340.egliseteste.Models.Sermao;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public class ComunidadeDAO extends BaseDaoImpl<Comunidade, Integer> {

    public ComunidadeDAO(ConnectionSource connectionSource) throws SQLException {
        super(Comunidade.class);
        setConnectionSource(connectionSource);
        initialize();
    }

}
