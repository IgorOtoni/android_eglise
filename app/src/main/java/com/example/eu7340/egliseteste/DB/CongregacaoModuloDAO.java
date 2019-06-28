package com.example.eu7340.egliseteste.DB;

import com.example.eu7340.egliseteste.Models.CongregacaoModulo;
import com.example.eu7340.egliseteste.Models.ModuloPermissao;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public class CongregacaoModuloDAO extends BaseDaoImpl<CongregacaoModulo, Integer> {

    public CongregacaoModuloDAO(ConnectionSource connectionSource) throws SQLException {
        super(CongregacaoModulo.class);
        setConnectionSource(connectionSource);
        initialize();
    }

}
