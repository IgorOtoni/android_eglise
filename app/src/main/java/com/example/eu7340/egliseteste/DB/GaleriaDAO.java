package com.example.eu7340.egliseteste.DB;

import com.example.eu7340.egliseteste.Models.EventoFixo;
import com.example.eu7340.egliseteste.Models.Galeria;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public class GaleriaDAO extends BaseDaoImpl<Galeria, Integer> {

    public GaleriaDAO(ConnectionSource connectionSource) throws SQLException {
        super(Galeria.class);
        setConnectionSource(connectionSource);
        initialize();
    }

}
