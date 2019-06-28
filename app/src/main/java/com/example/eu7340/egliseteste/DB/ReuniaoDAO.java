package com.example.eu7340.egliseteste.DB;

import com.example.eu7340.egliseteste.Models.Reuniao;
import com.example.eu7340.egliseteste.Models.Sermao;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public class ReuniaoDAO extends BaseDaoImpl<Reuniao, Integer> {

    public ReuniaoDAO(ConnectionSource connectionSource) throws SQLException {
        super(Reuniao.class);
        setConnectionSource(connectionSource);
        initialize();
    }

}
