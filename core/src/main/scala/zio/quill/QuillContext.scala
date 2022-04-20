package zio.quill

import io.getquill.context.ZioJdbc.DataSourceLayer
import zio.{ZIO, _}
import zio.quill.experiment.MagicContext

import java.sql.SQLException
import javax.sql.DataSource

object QuillContext extends MagicContext {}

final case class Account(email: String)

final case class UserService(innerContext: QuillContext.InnerContext) {
  import QuillContext._
  def allUsers: ZIO[Any, SQLException, List[Account]] =
    innerContext.run(query[Account])
}

object UserService {
  val live: URLayer[QuillContext.InnerContext, UserService] =
    (UserService.apply _).toLayer
}

object Main extends ZIOAppDefault {

  val run =
    ZIO
      .serviceWithZIO[UserService](_.allUsers)
      .debug
      .provide(
        UserService.live,
        QuillContext.layer.map { env => println(env); env },
        DataSourceLayer.fromPrefix("postgresDB").orDie
      )

}
