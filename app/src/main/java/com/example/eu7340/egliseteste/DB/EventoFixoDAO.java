package com.example.eu7340.egliseteste.DB;

import com.example.eu7340.egliseteste.Models.EventoFixo;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public class EventoFixoDAO extends BaseDaoImpl<EventoFixo, Integer> {

    public EventoFixoDAO(ConnectionSource connectionSource) throws SQLException {
        super(EventoFixo.class);
        setConnectionSource(connectionSource);
        initialize();
    }

}
