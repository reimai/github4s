package github4s.algebras

import github4s.GHResponse

/**
 * Source of static or expiring github tokens
 */
trait AccessTokens[F[_]] {

  def withAccessToken[T](f: Option[String] => F[GHResponse[T]]): F[GHResponse[T]]
}

object AccessTokens {}
