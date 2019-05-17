package com.example.eu7340.egliseteste.DB;

import com.example.eu7340.egliseteste.Models.Publicacao;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public class PublicacaoDAO extends BaseDaoImpl<Publicacao, Integer> {

    public PublicacaoDAO(ConnectionSource connectionSource) throws SQLException {
        super(Publicacao.class);
        setConnectionSource(connectionSource);
        initialize();
    }

}
