import groovy.sql.Sql
import groovy.transform.ToString

import java.sql.ResultSet
import java.time.LocalDate

import static DbUtil.snake_case

abstract class Dao<T extends Entity> {
    Sql sql
    Class<T> type
    String table

    Dao(DbConfig config) {
        def url = config.with { "jdbc:postgresql://$host:$port/$db" }
        sql = Sql.newInstance(url, config.user, config.pass, 'org.postgresql.Driver')
    }

    def index() {
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

    def find(int id) {
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

    def create(T object) {
        def fieldNames = object.fieldNames().collect { snake_case it }.join ','
        def placeholders = object.fieldNames().collect { snake_case ":$it" }.join ','
        def query = 'insert into games (' + fieldNames + ') values (' + placeholders + ')'
        sql.executeInsert query, object.declaredProperties()
    }

    def delete(int id) {
        sql.executeUpdate 'delete from ' + table + ' where id = ?', [id]
    }

    abstract T entityInstance()

    Map<String, Class> fieldsWithType() {
        type.declaredFields
                .findAll { !it.synthetic }
                .collectEntries [:], { field -> [(field.name), field.type] }
    }

    Map<Class, FieldMapper> fieldMappers() {
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
            def s = snake_case fieldName
            entity."$fieldName" = fieldMappers[clazz](rs, s)
        }
        entity
    }
}

interface FieldMapper<T> {
    T call(ResultSet rs, String column)
}

class GameDao extends Dao<Game> {
    {
        table = 'games'
        type = Game
    }

    GameDao(DbConfig config) {
        super(config)
    }

    @Override
    Game entityInstance() {
        new Game()
    }
}

abstract class Entity {
    List<String> fieldNames() {
        this.class.declaredFields
                .findAll { !it.synthetic }
                .collect { it.name }
    }

    Map<String, Object> declaredProperties() {
        def fields = fieldNames()
        this.properties
                .findAll { k, v -> (k as String) in fields }
                .collectEntries [:], { k, v -> [snake_case(k as String), v] }
    }
}

@ToString(includeNames = true)
class Game extends Entity {
    int id, score, recommended
    String name, shortDescription, description, designer
    LocalDate releaseDate
}

class DbUtil {
    static String snake_case(String fieldName) {
        fieldName.replaceAll('([A-Z])', '_$1').toLowerCase()
    }
}
