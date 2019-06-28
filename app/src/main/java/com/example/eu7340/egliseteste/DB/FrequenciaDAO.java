package com.example.eu7340.egliseteste.DB;

import com.example.eu7340.egliseteste.Models.Comunidade;
import com.example.eu7340.egliseteste.Models.Frequencia;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public class FrequenciaDAO extends BaseDaoImpl<Frequencia, Integer> {

    public FrequenciaDAO(ConnectionSource connectionSource) throws SQLException {
        super(Frequencia.class);
        setConnectionSource(connectionSource);
        initialize();
    }

}
