package com.example.eu7340.egliseteste.DB;

import com.example.eu7340.egliseteste.Models.Template;
import com.example.eu7340.egliseteste.Models.TemplateFoto;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public class TemplateFotoDAO extends BaseDaoImpl<TemplateFoto, Integer> {

    public TemplateFotoDAO(ConnectionSource connectionSource) throws SQLException {
        super(TemplateFoto.class);
        setConnectionSource(connectionSource);
        initialize();
    }

}
