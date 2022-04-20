package zio.quill.experiment

import io.getquill._
import io.getquill.context.ZioJdbc.DataSourceLayer
import io.getquill.context.{ActionMacro, QueryMacro}
import zio._

import java.sql.SQLException
import javax.sql.DataSource
import scala.reflect.macros.blackbox

object macros {
  PostgresZioJdbcContext
}

abstract class MagicContext extends PostgresZioJdbcContext[SnakeCase](SnakeCase) { self =>

  case class InnerContext(dataSource: DataSource) {
    def run[T](quoted: Quoted[Query[T]]): ZIO[Any, SQLException, List[T]] =
      macro ExperimentalMacros.runImpl[T]
  }

  def layer: URLayer[DataSource, InnerContext] =
    macro ExperimentalMacros.layerImpl

}

class ExperimentalMacros(val c: blackbox.Context) {
  import c.universe._

  def runImpl[T: c.WeakTypeTag](quoted: c.Tree): c.Expr[ZIO[Any, SQLException, List[T]]] = {
    val result = q"run($quoted).provideService(${c.prefix}.dataSource)"
    println(show(result))
    c.Expr[ZIO[Any, SQLException, List[T]]](result)
  }

  def layerImpl =
    q"((ds: _root_.javax.sql.DataSource) => ${c.prefix}.InnerContext(ds)).toLayer"

}
