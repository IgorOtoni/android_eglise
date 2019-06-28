package com.example.eu7340.egliseteste.DB;

import com.example.eu7340.egliseteste.Models.Membro;
import com.example.eu7340.egliseteste.Models.Sermao;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public class MembroDAO extends BaseDaoImpl<Membro, Integer> {

    public MembroDAO(ConnectionSource connectionSource) throws SQLException {
        super(Membro.class);
        setConnectionSource(connectionSource);
        initialize();
    }

}
