package com.example.eu7340.egliseteste.DB;

import com.example.eu7340.egliseteste.Models.Sermao;
import com.example.eu7340.egliseteste.Models.Usuario;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public class UsuarioDAO extends BaseDaoImpl<Usuario, Integer> {

    public UsuarioDAO(ConnectionSource connectionSource) throws SQLException {
        super(Usuario.class);
        setConnectionSource(connectionSource);
        initialize();
    }

}
