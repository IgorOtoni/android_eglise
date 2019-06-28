package com.example.eu7340.egliseteste.DB;

import com.example.eu7340.egliseteste.Models.Perfil;
import com.example.eu7340.egliseteste.Models.Usuario;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public class PerfilDAO extends BaseDaoImpl<Perfil, Integer> {

    public PerfilDAO(ConnectionSource connectionSource) throws SQLException {
        super(Perfil.class);
        setConnectionSource(connectionSource);
        initialize();
    }

}
