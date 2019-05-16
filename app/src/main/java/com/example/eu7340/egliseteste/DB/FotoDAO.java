package com.example.eu7340.egliseteste.DB;

import com.example.eu7340.egliseteste.Models.Evento;
import com.example.eu7340.egliseteste.Models.Foto;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public class FotoDAO extends BaseDaoImpl<Foto, Integer> {

    public FotoDAO(ConnectionSource connectionSource) throws SQLException {
        super(Foto.class);
        setConnectionSource(connectionSource);
        initialize();
    }

}
