/*
 * Copyright 2016-2020 47 Degrees Open Source <https://www.47deg.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package github4s.interpreters

import cats.data.NonEmptyList
import github4s.Decoders._
import github4s.Encoders._
import github4s.GHResponse
import github4s.algebras.GitData
import github4s.domain._
import github4s.http.HttpClient

class GitDataInterpreter[F[_]](implicit client: HttpClient[F]) extends GitData[F] {
  override def getReference(
      owner: String,
      repo: String,
      ref: String,
      pagination: Option[Pagination],
      headers: Map[String, String]
  ): F[GHResponse[NonEmptyList[Ref]]] =
    client.get[NonEmptyList[Ref]](
      s"repos/$owner/$repo/git/refs/$ref",
      headers,
      pagination = pagination
    )

  override def createReference(
      owner: String,
      repo: String,
      ref: String,
      sha: String,
      headers: Map[String, String]
  ): F[GHResponse[Ref]] =
    client.post[CreateReferenceRequest, Ref](
      s"repos/$owner/$repo/git/refs",
      headers,
      CreateReferenceRequest(ref, sha)
    )

  override def updateReference(
      owner: String,
      repo: String,
      ref: String,
      sha: String,
      force: Boolean,
      headers: Map[String, String]
  ): F[GHResponse[Ref]] =
    client.patch[UpdateReferenceRequest, Ref](
      s"repos/$owner/$repo/git/refs/$ref",
      headers,
      UpdateReferenceRequest(sha, force)
    )

  override def getCommit(
      owner: String,
      repo: String,
      sha: String,
      headers: Map[String, String]
  ): F[GHResponse[RefCommit]] =
    client.get[RefCommit](s"repos/$owner/$repo/git/commits/$sha", headers)

  override def createCommit(
      owner: String,
      repo: String,
      message: String,
      tree: String,
      parents: List[String],
      author: Option[RefAuthor],
      headers: Map[String, String]
  ): F[GHResponse[RefCommit]] =
    client.post[NewCommitRequest, RefCommit](
      s"repos/$owner/$repo/git/commits",
      headers,
      NewCommitRequest(message, tree, parents, author)
    )

  override def getBlob(
      owner: String,
      repo: String,
      fileSha: String,
      headers: Map[String, String]
  ): F[GHResponse[BlobContent]] =
    client.get[BlobContent](s"repos/$owner/$repo/git/blobs/$fileSha", headers)

  override def createBlob(
      owner: String,
      repo: String,
      content: String,
      encoding: Option[String],
      headers: Map[String, String]
  ): F[GHResponse[RefInfo]] =
    client.post[NewBlobRequest, RefInfo](
      s"repos/$owner/$repo/git/blobs",
      headers,
      NewBlobRequest(content, encoding)
    )

  override def getTree(
      owner: String,
      repo: String,
      sha: String,
      recursive: Boolean,
      headers: Map[String, String]
  ): F[GHResponse[TreeResult]] =
    client.get[TreeResult](
      s"repos/$owner/$repo/git/trees/$sha",
      headers,
      (if (recursive) Map("recursive" -> "1") else Map.empty)
    )

  override def createTree(
      owner: String,
      repo: String,
      baseTree: Option[String],
      treeDataList: List[TreeData],
      headers: Map[String, String]
  ): F[GHResponse[TreeResult]] =
    client.post[NewTreeRequest, TreeResult](
      s"repos/$owner/$repo/git/trees",
      headers,
      NewTreeRequest(treeDataList, baseTree)
    )

  override def createTag(
      owner: String,
      repo: String,
      tag: String,
      message: String,
      objectSha: String,
      objectType: String,
      author: Option[RefAuthor],
      headers: Map[String, String]
  ): F[GHResponse[Tag]] =
    client.post[NewTagRequest, Tag](
      s"repos/$owner/$repo/git/tags",
      headers,
      NewTagRequest(tag, message, objectSha, objectType, author)
    )
}
