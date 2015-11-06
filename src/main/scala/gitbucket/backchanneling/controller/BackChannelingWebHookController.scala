package gitbucket.backchanneling.controller

import jp.sf.amateras.scalatra.forms._

import gitbucket.backchanneling.model.BackChanneling
import gitbucket.backchanneling.service.BackChannelingService
import gitbucket.core.controller.ControllerBase
import gitbucket.core.service.{WebHookService, RepositoryService, AccountService}
import gitbucket.core.util._
import gitbucket.core.util.Implicits._

import gitbucket.backchanneling.html

class BackChannelingWebHookController extends BackChannelingWebHookControllerBase
  with BackChannelingService

trait BackChannelingWebHookControllerBase extends ControllerBase {
  self: BackChannelingService =>

  val payload = mapping(
    "payload" -> trim(label("Payload"    , text()))
  )(x => x)

  post("/:owner/:repository/webhook/backchanneling", payload){payload =>
    val owner = params("owner")
    val repository = params("repository")
    getBackChanneling(owner, repository).map { backChanneling =>
      println(s"settings:\n${backChanneling}")
      println(s"payload:\n${payload}")
    }
  }

}
