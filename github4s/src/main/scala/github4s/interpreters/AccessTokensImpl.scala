package github4s.interpreters

import cats.Applicative
import cats.syntax.applicative._
import github4s.algebras.AccessTokens

/**
 * A simple static version
 */
class AccessTokensImpl[F[_]: Applicative](accessToken: Option[String]) extends AccessTokens[F] {

  override val getToken: F[Option[String]] = accessToken.pure
}

object AccessTokensImpl {
  def noToken[F[_]: Applicative] = new AccessTokensImpl[F](None)
}
