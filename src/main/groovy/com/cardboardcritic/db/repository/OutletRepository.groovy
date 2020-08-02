package com.cardboardcritic.db.repository

import com.cardboardcritic.db.entity.Outlet
import groovy.sql.Sql

class OutletRepository extends Repository<Outlet> {
    {
        table = 'outlets'
        type = Outlet
    }

    OutletRepository(Sql sql) {
        super(sql)
    }

    @Override
    protected Outlet entityInstance() {
        new Outlet()
    }
}
