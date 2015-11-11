package gitbucket.backchanneling.controller

import gitbucket.backchanneling.service._
import gitbucket.core.controller.ControllerBase
import gitbucket.core.util.Implicits._
import jp.sf.amateras.scalatra.forms._

class BackChannelingWebHookController extends BackChannelingWebHookControllerBase
  with BackChannelingSettingsService with BackChannelingAPIService

trait BackChannelingWebHookControllerBase extends ControllerBase {
  self: BackChannelingSettingsService with BackChannelingAPIService =>

  val payload = mapping(
    "payload" -> trim(label("Payload", text()))
  )(x => x)

  post("/:owner/:repository/webhook/backchanneling", payload){payload =>
    for {
      owner <- Some(params("owner"))
      repository <- Some(params("repository"))
      urlBase <- Some(request.getRequestURL.toString.replaceAll(s"/${owner}/${repository}/webhook/backchanneling", ""))
      backChanneling <- getBackChanneling(owner, repository)
      token <- token(backChanneling)
      payload <- toPayload(request.getHeader("X-Github-Event"), payload)
    } comment(backChanneling, token, toContent(urlBase, owner, repository, payload))
  }
}
