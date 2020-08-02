package com.cardboardcritic.db.entity

import com.cardboardcritic.db.DbUtil

abstract class Entity {
    int id

    List<String> fieldNames() {
        this.class.declaredFields
                .findAll { !it.synthetic }
                .collect { DbUtil.cleanFieldName it.name }
    }

    Map<String, Object> fieldsAsColumns() {
        def fields = fieldNames()
        this.properties
                .findAll { k, v -> (k as String) in fields }
                .collectEntries [:],
                { k, v ->
                    def propertyName = DbUtil.snake_case(DbUtil.cleanFieldName(k as String))
                    [propertyName, v]
                }
    }
}
