package com.example.eu7340.egliseteste.DB;

import com.example.eu7340.egliseteste.Models.Noticia;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public class NoticiaDAO extends BaseDaoImpl<Noticia, Integer> {

    public NoticiaDAO(ConnectionSource connectionSource) throws SQLException {
        super(Noticia.class);
        setConnectionSource(connectionSource);
        initialize();
    }

}
