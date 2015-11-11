package gitbucket.backchanneling.service

import gitbucket.backchanneling.model._
import org.apache.http.NameValuePair
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.message.BasicNameValuePair
import org.apache.http.util.EntityUtils
import org.json4s._
import org.json4s.jackson.JsonMethods._

trait BackChannelingAPIService {

  val httpClient = HttpClientBuilder.create.build

  def token(backChanneling: BackChanneling): Option[String] = {
    implicit val formats = DefaultFormats
    val httpPost = new HttpPost(s"${backChanneling.url}/api/token")
    httpPost.addHeader("Accept", "application/json")
    val params: java.util.List[NameValuePair] = new java.util.ArrayList()
    params.add(new BasicNameValuePair("code", backChanneling.authorizationCode))
    httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"))
    val response = httpClient.execute(httpPost)
    val responseCode = response.getStatusLine().getStatusCode()
    val responseBody = EntityUtils.toString(response.getEntity(), "UTF-8")
    httpPost.releaseConnection()
    responseCode match {
      case 201 => Some(parse(responseBody).extract[Token].`access-token`)
      case _ => None
    }
  }

  def toPayload(event: String, payload: String): Option[Payload] = {
    implicit val formats = DefaultFormats
    lazy val json = parse(payload)
    event match {
      case "push" => Some(json.extract[PushPayload])
      case "issues" => Some(json.extract[IssuesPayload])
      case "issue_comment" => Some(json.extract[IssueCommentPayload])
      case "pull_request" => Some(json.extract[PullRequestPayload])
      case _ => None
    }
  }

  def toContent(urlBase: String, owner: String, repository: String, payload: Payload): String = payload match {
    case issues: IssuesPayload =>
      s"""
        |Repository: [${issues.repository.full_name}](${urlBase}/${owner}/${repository})
        |Issue: [#${issues.issue.number} ${issues.issue.title}](${urlBase}/${owner}/${repository}/issues/${issues.issue.number}) ${issues.action} by [${issues.sender.login}](${urlBase}/${issues.sender.login})
      """.stripMargin
    case issueComment: IssueCommentPayload =>
      s"""
        |Repository: [${issueComment.repository.full_name}](${urlBase}/${owner}/${repository})
        |Issue: [#${issueComment.issue.number} ${issueComment.issue.title}](${urlBase}/issues/${issueComment.issue.number}) commented by [${issueComment.comment.user}](${urlBase}/${issueComment.comment.user})
        |Comment:
        |${issueComment.comment.body}
       """.stripMargin
    case pullRequest: PullRequestPayload =>
      s"""
         |Repository: [${pullRequest.repository.full_name}](${urlBase}/${owner}/${repository})
         |Pull request: [#${pullRequest.pull_request.number} ${pullRequest.pull_request.title}](${urlBase}/pulls/${pullRequest.pull_request.number}) ${pullRequest.action} commented by [${pullRequest.sender.login}](${urlBase}/${pullRequest.sender.login})
         |${pullRequest.pull_request.body}
       """.stripMargin
    case push: PushPayload =>
      s"""
         |Repository: [${push.repository.full_name}](${urlBase}/${owner}/${repository})
         |Pushed to [${push.ref.replaceAll("refs/heads/", "")}](${urlBase}/${owner}/${repository}/tree/${push.ref.replaceAll("refs/heads/", "")}) by [${push.pusher.login}](${urlBase}/${push.pusher.login})
         |Commits:
       """.stripMargin +
        (push.commits.map{commit =>
          s"[${commit.id}](${urlBase}/${owner}/${repository}/commit/${commit.id}) ${commit.message}"
        } reduce(_ + _))
  }

  def comment(backChanneling: BackChanneling, token: String, content: String): Unit = {
    implicit val formats = DefaultFormats
    val httpPost = new HttpPost(s"${backChanneling.url}/api/thread/${backChanneling.threadId}/comments")
    httpPost.addHeader("Accept", "application/json")
    httpPost.addHeader("Content-Type", "application/edn")
    httpPost.addHeader("Authorization", s"Token ${token}")
    httpPost.setEntity(new StringEntity(
      s"""
         |{:comment/content "${content}", :comment/format :comment.format/markdown, :thread/id ${backChanneling.threadId}}
       """.stripMargin, "UTF-8"))
    httpClient.execute(httpPost)
    httpPost.releaseConnection()
  }
}