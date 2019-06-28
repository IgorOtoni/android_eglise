package com.example.eu7340.egliseteste.DB;

import com.example.eu7340.egliseteste.Models.CongregacaoModulo;
import com.example.eu7340.egliseteste.Models.PerfilCongregacaoModulo;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public class PerfilCongregacaoModuloDAO extends BaseDaoImpl<PerfilCongregacaoModulo, Integer> {

    public PerfilCongregacaoModuloDAO(ConnectionSource connectionSource) throws SQLException {
        super(PerfilCongregacaoModulo.class);
        setConnectionSource(connectionSource);
        initialize();
    }

}
