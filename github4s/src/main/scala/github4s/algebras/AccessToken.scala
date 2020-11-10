package github4s.algebras

import github4s.GHResponse

/**
 * Source of static or expiring github tokens
 *
 * For github app authentication you'd want to create a token source
 * which calls github's installation authentication api with a jwt token, generated from a private key
 * These tokens have a 1h lifetime, so it's a good idea to handle expired tokens here as well
 *
 * @see https://docs.github.com/en/free-pro-team@latest/developers/apps/authenticating-with-github-apps
 */
trait AccessToken[F[_]] {

  def withAccessToken[T](f: Option[String] => F[GHResponse[T]]): F[GHResponse[T]]
}
