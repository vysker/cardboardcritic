package com.cardboardcritic.db.repository

import com.cardboardcritic.db.entity.Critic
import groovy.sql.Sql

class CriticRepository extends Repository<Critic> {
    {
        table = 'critics'
        type = Critic
    }

    CriticRepository(Sql sql) {
        super(sql)
    }

    @Override
    protected Critic entityInstance() {
        new Critic()
    }
}
