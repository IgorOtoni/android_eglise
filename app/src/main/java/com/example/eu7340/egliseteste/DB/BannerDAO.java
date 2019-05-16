package com.example.eu7340.egliseteste.DB;

import com.example.eu7340.egliseteste.Models.Banner;
import com.example.eu7340.egliseteste.Models.Noticia;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public class BannerDAO extends BaseDaoImpl<Banner, Integer> {

    public BannerDAO(ConnectionSource connectionSource) throws SQLException {
        super(Banner.class);
        setConnectionSource(connectionSource);
        initialize();
    }

}
