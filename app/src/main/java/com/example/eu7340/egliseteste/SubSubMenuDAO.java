package com.example.eu7340.egliseteste;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public class SubSubMenuDAO extends BaseDaoImpl<SubMenu, Integer> {

    public SubSubMenuDAO(ConnectionSource connectionSource) throws SQLException {
        super(SubMenu.class);
        setConnectionSource(connectionSource);
        initialize();
    }

}
