package com.example.eu7340.egliseteste;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public class CongregacaoDAO extends BaseDaoImpl<Congregacao, Integer> {

    public CongregacaoDAO(ConnectionSource connectionSource) throws SQLException {
        super(Congregacao.class);
        setConnectionSource(connectionSource);
        initialize();
    }

}
