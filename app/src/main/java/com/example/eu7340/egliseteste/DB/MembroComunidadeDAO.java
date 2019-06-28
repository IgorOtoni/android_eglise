package com.example.eu7340.egliseteste.DB;

import com.example.eu7340.egliseteste.Models.MembroComunidade;
import com.example.eu7340.egliseteste.Models.Sermao;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public class MembroComunidadeDAO extends BaseDaoImpl<MembroComunidade, Integer> {

    public MembroComunidadeDAO(ConnectionSource connectionSource) throws SQLException {
        super(MembroComunidade.class);
        setConnectionSource(connectionSource);
        initialize();
    }

}
