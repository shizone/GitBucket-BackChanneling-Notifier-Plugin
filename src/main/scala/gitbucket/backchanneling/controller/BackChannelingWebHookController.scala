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
    val owner = params("owner")
    val repository = params("repository")
    try{
      getBackChanneling(owner, repository) map { backChanneling =>
        token(backChanneling) map { token =>
          comment(backChanneling, token, payload)
        }
      }
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }
}
