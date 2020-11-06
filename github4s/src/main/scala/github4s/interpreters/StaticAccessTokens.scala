package github4s.interpreters

import github4s.GHResponse
import github4s.algebras.AccessTokens

/**
 * A simple static version
 */
class StaticAccessTokens[F[_]](accessToken: Option[String]) extends AccessTokens[F] {

  override def withAccessToken[T](f: Option[String] => F[GHResponse[T]]): F[GHResponse[T]] =
    f(accessToken)
}

object StaticAccessTokens {
  def noToken[F[_]] = new StaticAccessTokens[F](None)
}
