package com.cardboardcritic.db.entity

import com.cardboardcritic.db.DbUtil

abstract class Entity {
    int id

    List<String> fieldNames() {
        this.class.declaredFields
                .findAll { !it.synthetic }
                .collect { it.name }
    }

    Map<String, Object> declaredProperties() {
        def fields = fieldNames()
        this.properties
                .findAll { k, v -> (k as String) in fields }
                .collectEntries [:], { k, v -> [DbUtil.snake_case(k as String), v] }
    }
}
