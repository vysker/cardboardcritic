package com.cardboardcritic.db.repository


import com.cardboardcritic.db.DbUtil
import com.cardboardcritic.db.FieldMapper
import com.cardboardcritic.db.entity.Entity
import groovy.sql.Sql

import java.sql.ResultSet
import java.time.LocalDate

abstract class Repository<T extends Entity> {
    Sql sql
    Class<T> type
    String table

    Repository(Sql sql) {
        this.sql = sql
    }

    List<T> index() {
        def mappers = fieldMappers()
        def fieldsWithType = fieldsWithType()
        def result = []

        sql.query 'select * from ' + table, { rs ->
            while (rs.next()) {
                result << mapRow(rs, mappers, fieldsWithType)
            }
        }
        result
    }

    T find(int id) {
        def mappers = fieldMappers()
        def fieldsWithType = fieldsWithType()
        def result = null

        sql.query 'select * from ' + table + ' where id = ?', [id], { rs ->
            if (!rs.next())
                return result
            result = mapRow rs, mappers, fieldsWithType
        }
        result
    }

    T find(T object) {
        find object.id as int
    }

    T create(T object) {
        def fieldNames = object.fieldNames().collect { DbUtil.snake_case it }.join ','
        def placeholders = object.fieldNames().collect { DbUtil.snake_case ":$it" }.join ','
        def query = 'insert into games (' + fieldNames + ') values (' + placeholders + ')'
        sql.executeInsert query, object.declaredProperties()
        find object
    }

    boolean delete(int id) {
        def rows = sql.executeUpdate 'delete from ' + table + ' where id = ?', [id]
        rows > 0
    }

    protected abstract T entityInstance()

    protected Map<String, Class> fieldsWithType() {
        type.declaredFields
                .findAll { !it.synthetic }
                .collectEntries [:], { field -> [(field.name), field.type] }
    }

    protected Map<Class, FieldMapper> fieldMappers() {
        fieldsWithType().collectEntries [:], { fieldName, clazz ->
            FieldMapper mapper

            switch (clazz) {
                case int:
                    mapper = { rs, column -> rs.getInt column } as FieldMapper
                    break
                case String:
                    mapper = { rs, column -> rs.getString column } as FieldMapper
                    break
                case LocalDate:
                    mapper = { rs, column -> rs.getDate(column)?.toLocalDate() } as FieldMapper
                    break
                default:
                    mapper = { rs, column -> rs.getObject(column)?.toString() } as FieldMapper
            }

            return [clazz, mapper]
        }
    }

    protected T mapRow(ResultSet rs, Map<Class, FieldMapper> fieldMappers, Map<String, Class> fieldsWithType) {
        def entity = entityInstance()
        fieldsWithType.each { fieldName, clazz ->
            def s = DbUtil.snake_case fieldName
            entity."$fieldName" = fieldMappers[clazz](rs, s)
        }
        entity
    }
}
