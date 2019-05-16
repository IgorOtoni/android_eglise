package com.example.eu7340.egliseteste.DB;

import com.example.eu7340.egliseteste.Models.Sermao;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public class SermaoDAO extends BaseDaoImpl<Sermao, Integer> {

    public SermaoDAO(ConnectionSource connectionSource) throws SQLException {
        super(Sermao.class);
        setConnectionSource(connectionSource);
        initialize();
    }

}
