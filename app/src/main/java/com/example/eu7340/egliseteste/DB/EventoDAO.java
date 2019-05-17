package com.example.eu7340.egliseteste.DB;

import com.example.eu7340.egliseteste.Models.Evento;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public class EventoDAO extends BaseDaoImpl<Evento, Integer> {

    public EventoDAO(ConnectionSource connectionSource) throws SQLException {
        super(Evento.class);
        setConnectionSource(connectionSource);
        initialize();
    }

}
