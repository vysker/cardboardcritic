package com.cardboardcritic.db

import org.hibernate.boot.model.naming.Identifier
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment

class SnakeCasePhysicalNamingStrategy extends PhysicalNamingStrategyStandardImpl {

    @Override
    Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
        return super.toPhysicalColumnName(snake_case(name), context)
    }

    /**
     * Takes a camelCased field and turns it into snake_case, e.g. 'shortDescription' becomes 'short_description'.
     *
     * @param field camelCased field
     * @return snake_cased Identifier
     */
    private static Identifier snake_case(Identifier field) {
        def snake_cased_name = field.text.replaceAll('([A-Z])', '_$1').toLowerCase()
        new Identifier(snake_cased_name, field.isQuoted())
    }
}
