package com.example.eu7340.egliseteste.DB;

import com.example.eu7340.egliseteste.Models.Menu;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public class MenuDAO extends BaseDaoImpl<Menu, Integer> {

    public MenuDAO(ConnectionSource connectionSource) throws SQLException {
        super(Menu.class);
        setConnectionSource(connectionSource);
        initialize();
    }

}
