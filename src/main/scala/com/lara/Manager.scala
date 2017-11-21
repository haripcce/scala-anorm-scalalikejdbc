package com.lara

import java.sql.Connection
import java.util.Date

import anorm._
import anorm._
import anorm.SqlParser._
import scalikejdbc.ConnectionPool
import scalikejdbc.config.DBs

case class Product(
                    id: Int,
                    name: String,
                    description: String)

object Manager {
  val sql: SqlQuery = SQL("select * from products order by name asc")

  def main(args: Array[String]): Unit = {
    //Class.forName("com.mysql.jdbc.Driver")
    // ConnectionPool.singleton("jdbc:mysql://localhost:3306/test", "root", "agreeya@123")
    DBs.setupAll()
    getAllWithParser.foreach(x=>println(x.description))
  }

  import anorm.RowParser

  val productParser: RowParser[Product] = {
    import anorm.~
    import anorm.SqlParser._
      int("id") ~
      str("name") ~
      str("description") map {
      case id ~ name ~ description =>
        Product(id, name, description)
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
