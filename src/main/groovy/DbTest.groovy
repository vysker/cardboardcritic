import java.time.LocalDate

def dbConfig = new DbConfig(
        host: 'localhost',
        port: 5432,
        user: 'dick',
        pass: 'root',
        db: 'cbc')
//client.sql.query 'select 1', { rs -> rs.next(); println rs.getInt(1) }
def game = new Game(
        id: 2, score: 80, recommended: 70,
        name: 'Anachrony',
        shortDescription: 'Quite short',
        description: 'Bit longer',
        designer: 'David Turczi',
        releaseDate: LocalDate.now())
def gameDao = new GameDao(dbConfig)
println gameDao.index()
//gameDao.find 1
gameDao.delete 2
gameDao.create game
//gameDao.find 2
println '==after=='
println gameDao.index()
gameDao.sql.close()
