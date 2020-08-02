package com.cardboardcritic.db.entity.meta

import com.cardboardcritic.db.DbUtil

import java.lang.reflect.Field

abstract class Entity implements HasId {
    List<Field> persistentFields() {
        (this.class.fields + this.class.declaredFields)
                .findAll { it.isAnnotationPresent Column }
                .unique { DbUtil.cleanFieldName it.name }
    }

    List<String> persistentFieldNames() {
        persistentFields().collect { DbUtil.cleanFieldName it.name }
    }

    Map<String, Object> valuesByColumn() {
        def fields = persistentFieldNames()
        this.properties
                .findAll { k, v -> (k as String) in fields }
                .collectEntries [:],
                { k, v ->
                    def propertyName = DbUtil.snake_case(DbUtil.cleanFieldName(k as String))
                    [propertyName, v]
                }
    }
}
