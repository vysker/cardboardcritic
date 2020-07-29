package com.cardboardcritic.db

import groovy.sql.Sql

class DbConfig {
    String host, port, user, pass, db, url, protocol = 'postgresql', driver = 'org.postgresql.Driver'

    Sql connect() {
        if (!url) url = "jdbc:$protocol://$host:$port/$db"
        Sql.newInstance(url, user, pass, driver)
    }
}
