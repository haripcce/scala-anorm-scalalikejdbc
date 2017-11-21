package com.lara

import java.sql.Connection
import java.util.Date

import anorm._
import anorm._
import anorm.SqlParser._
import scalikejdbc.ConnectionPool
import scalikejdbc.config.DBs

case class Product(
                    id: Long,
                    ean: Long,
                    name: String,
                    description: String)

object Manager {
  val sql: SqlQuery = SQL("select * from products order by name asc")

  def main(args: Array[String]): Unit = {
    DBs.setupAll()
    getAllWithParser
  }

  import anorm.RowParser

  val productParser: RowParser[Product] = {
    import anorm.~
    import anorm.SqlParser._
    long("id") ~
      long("ean") ~
      str("name") ~
      str("description") map {
      case id ~ ean ~ name ~ description =>
        Product(id, ean, name, description)
    }
  }

  import anorm.ResultSetParser

  val productsParser: ResultSetParser[List[Product]] = {
    productParser *
  }

  def getAllWithParser: List[Product] = DB.withConnection {
    implicit connection =>
      sql.as(productsParser)
  }


  object DB {
    def withConnection[A](block: Connection => A): A = {
      val connection: Connection = ConnectionPool.borrow()

      try {
        block(connection)
      } finally {
        connection.close()
      }
    }
  }

}
