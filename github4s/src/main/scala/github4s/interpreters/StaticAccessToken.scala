package github4s.interpreters

import github4s.GHResponse
import github4s.algebras.AccessToken

/**
 * A simple static version
 */
class StaticAccessToken[F[_]](accessToken: Option[String]) extends AccessToken[F] {

  override def withAccessToken[T](f: Option[String] => F[GHResponse[T]]): F[GHResponse[T]] =
    f(accessToken)
}

object StaticAccessToken {
  def noToken[F[_]] = new StaticAccessToken[F](None)
}
