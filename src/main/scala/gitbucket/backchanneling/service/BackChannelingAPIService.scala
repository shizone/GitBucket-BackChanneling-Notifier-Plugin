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
import org.json4s.JsonDSL._

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
    println(responseCode)
    println(responseBody)
    responseCode match {
      case 201 => Some(parse(responseBody).extract[Token].`access-token`)
      case _ => None
    }
  }

  def comment(backChanneling: BackChanneling, token: String, payload: String): Unit = {
    implicit val formats = DefaultFormats
    val httpPost = new HttpPost(s"${backChanneling.url}/api/thread/${backChanneling.threadId}/comments")
    httpPost.addHeader("Accept", "application/json")
    httpPost.addHeader("Content-Type", "application/json")
    httpPost.addHeader("Authorization", s"Token ${token}")
    httpPost.setEntity(new StringEntity(compact(render("comment/content" -> payload))))
    val response = httpClient.execute(httpPost)
    val responseCode = response.getStatusLine().getStatusCode()
    val responseBody = EntityUtils.toString(response.getEntity(), "UTF-8")
    httpPost.releaseConnection()
    println(responseCode)
    println(responseBody)
  }
}