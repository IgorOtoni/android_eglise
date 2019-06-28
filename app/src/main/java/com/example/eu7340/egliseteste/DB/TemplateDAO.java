package com.example.eu7340.egliseteste.DB;

import com.example.eu7340.egliseteste.Models.Template;
import com.example.eu7340.egliseteste.Models.Usuario;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public class TemplateDAO extends BaseDaoImpl<Template, Integer> {

    public TemplateDAO(ConnectionSource connectionSource) throws SQLException {
        super(Template.class);
        setConnectionSource(connectionSource);
        initialize();
    }

}
