package gitbucket.backchanneling.util

import java.io._
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets

object HttpClient {
  def get(urlString: String) : HttpResponse = {
    con(new URL(urlString).openConnection.asInstanceOf[HttpURLConnection]) { connection =>
      connection.setRequestMethod("GET")
      httpResponse(connection)
    }.asInstanceOf[HttpResponse]
  }

  def post(urlString: String, parameter: String): HttpResponse = {
    con(new URL(urlString).openConnection.asInstanceOf[HttpURLConnection]) { connection =>
      connection.setRequestMethod("POST")
      connection.setDoOutput(true)
      handler(new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8))) {writer =>
        writer.write(parameter)
        writer.flush
      }
      httpResponse(connection)
    }.asInstanceOf[HttpResponse]
  }

  private def httpResponse(connection: HttpURLConnection): HttpResponse ={
    val response = new StringBuilder()
    val responseCode = connection.getResponseCode()
    if (responseCode == HttpURLConnection.HTTP_OK) {
      handler(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8)) {isr =>
        handler(new BufferedReader(isr)) {reader =>
          var line: String = reader.readLine
          while (line != null) {
            response.append(line)
            line = reader.readLine
          }
        }
      }
    }
    HttpResponse(responseCode, response.toString())
  }

  private def con[A <% { def disconnect():Unit }](s: A)(f: A=>Any) {
    try f(s) finally s.disconnect()
  }
  private def handler[A <% { def close():Unit }](s: A)(f: A=>Any) {
    try f(s) finally s.close()
  }
}

case class HttpResponse(responseCode: Int, response: String)
