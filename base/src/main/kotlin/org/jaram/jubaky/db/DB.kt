package org.jaram.jubaky.db

import kotlinx.coroutines.Dispatchers
import org.jaram.jubaky.QueryEmptyResultException
import org.jetbrains.exposed.exceptions.EntityNotFoundException
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import javax.sql.DataSource

class DB(
    dataSource: DataSource,
    readOnlyDataSource: DataSource
) {

    private val database = Database.connect(dataSource)
    private val readOnlyDatabase = Database.connect(readOnlyDataSource)

    suspend fun <T> execute(statement: suspend Transaction.() -> T): T {
        return newSuspendedTransaction(Dispatchers.IO, database, statement)
    }

    suspend fun <T> read(statement: suspend Transaction.() -> T): T = try {
        newSuspendedTransaction(Dispatchers.IO, readOnlyDatabase, statement)
    } catch (e: EntityNotFoundException) {
        throw QueryEmptyResultException(e)
    } catch (e: NoSuchElementException) {
        throw QueryEmptyResultException(e)
    }
}