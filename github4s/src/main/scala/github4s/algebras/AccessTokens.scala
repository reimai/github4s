package github4s.algebras

import cats.FlatMap
import github4s.GHResponse

/**
 * Source of static or expiring github tokens
 */
trait AccessTokens[F[_]] {
  val getToken: F[Option[String]]
}

object AccessTokens {
  import cats.syntax.flatMap._

  def withAccessToken[F[_]: FlatMap, T](f: Option[String] => F[GHResponse[T]])(implicit
      a: AccessTokens[F]
  ): F[GHResponse[T]] =
    a.getToken >>= f
}
