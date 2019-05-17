package com.example.eu7340.egliseteste.DB;

import com.example.eu7340.egliseteste.Models.Configuracao;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public class ConfiguracaoDAO extends BaseDaoImpl<Configuracao, Integer> {

    public ConfiguracaoDAO(ConnectionSource connectionSource) throws SQLException {
        super(Configuracao.class);
        setConnectionSource(connectionSource);
        initialize();
    }

}
