package com.lara

import java.sql.Connection
import java.util.Date

import anorm.SqlQuery
import anorm.SQL
import anorm.~
import anorm.SqlParser.{int, str};
import anorm.ResultSetParser
import scalikejdbc.ConnectionPool
import scalikejdbc.config.DBs

case class Product(
                    id: Int,
                    name: String,
                    description: String)

object Manager {
  val sql: SqlQuery = SQL("select * from products order by name asc")

  def main(args: Array[String]): Unit = {
    DBs.setupAll()
    getAllWithParser.foreach(x => println(x))
  }

  import anorm.RowParser

  val productParser: RowParser[Product] = {
    int("id") ~
      str("name") ~
      str("description") map {
      case id ~ name ~ description =>
        Product(id, name, description)
    }
  }



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
