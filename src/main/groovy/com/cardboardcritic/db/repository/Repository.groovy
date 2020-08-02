package com.cardboardcritic.db.repository

import com.cardboardcritic.db.DbUtil
import com.cardboardcritic.db.FieldMapper
import com.cardboardcritic.db.entity.meta.Column
import com.cardboardcritic.db.entity.meta.Entity
import groovy.sql.GroovyRowResult
import groovy.sql.Sql

import java.sql.ResultSet
import java.time.LocalDate

abstract class Repository<T extends Entity> {
    private static final String ID_COLUMN = 'ID'

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
        def fieldNames = object.persistentFieldNames().collect { DbUtil.snake_case it }.join ','
        def placeholders = object.persistentFieldNames().collect { DbUtil.snake_case ":$it" }.join ','
        def query = "insert into $table ($fieldNames) values ($placeholders)"
        List<GroovyRowResult> generatedKeys = sql.executeInsert(object.valuesByColumn(), query, [ID_COLUMN])

        if (!generatedKeys[0]) return object

        def id = generatedKeys[0][ID_COLUMN] as Integer
        find id
    }

    boolean delete(int id) {
        def rows = sql.executeUpdate 'delete from ' + table + ' where id = ?', [id]
        rows > 0
    }

    protected abstract T entityInstance()

    protected Map<String, Class> fieldsWithType() {
        entityInstance().persistentFields()
                .findAll { it.isAnnotationPresent Column }
                .unique { it.name }
                .collectEntries [:], { field -> [(DbUtil.cleanFieldName(field.name)), field.type] }
    }

    protected Map<Class, FieldMapper> fieldMappers() {
        fieldsWithType().collectEntries [:], { fieldName, clazz ->
            FieldMapper mapper

            switch (clazz) {
                case boolean:
                    mapper = { rs, column -> rs.getBoolean column } as FieldMapper
                    break
                case int:
                case Integer:
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

    protected T mapRow(ResultSet resultSet, Map<Class, FieldMapper> fieldMappers, Map<String, Class> fieldsWithType) {
        def map = [:]
        fieldsWithType.each { fieldName, clazz ->
            def column = DbUtil.snake_case(DbUtil.cleanFieldName(fieldName))
            map[fieldName] = fieldMappers[clazz](resultSet, column)
        }
        map.asType type
    }
}
