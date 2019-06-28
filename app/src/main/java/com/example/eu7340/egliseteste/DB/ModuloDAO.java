package com.example.eu7340.egliseteste.DB;

import com.example.eu7340.egliseteste.Models.Menu;
import com.example.eu7340.egliseteste.Models.Modulo;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public class ModuloDAO extends BaseDaoImpl<Modulo, Integer> {

    public ModuloDAO(ConnectionSource connectionSource) throws SQLException {
        super(Modulo.class);
        setConnectionSource(connectionSource);
        initialize();
    }

}
