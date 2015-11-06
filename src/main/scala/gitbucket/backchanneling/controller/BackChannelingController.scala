package gitbucket.backchanneling.controller

import jp.sf.amateras.scalatra.forms._

import gitbucket.backchanneling.model.BackChanneling
import gitbucket.backchanneling.service.BackChannelingService
import gitbucket.core.controller.ControllerBase
import gitbucket.core.service.RepositoryService
import gitbucket.core.service.AccountService
import gitbucket.core.util._
import gitbucket.core.util.Implicits._

import gitbucket.backchanneling.html

class BackChannelingController extends BackChannelingControllerBase
  with RepositoryService with AccountService with OwnerAuthenticator with BackChannelingService

trait BackChannelingControllerBase extends ControllerBase {
  self: RepositoryService with AccountService with OwnerAuthenticator with BackChannelingService =>

  case class BackChannelingForm(applicationUrl: String, authorizationCode: String, threadId: Long)
  val backChannelingForm = mapping(
    "applicationUrl"    -> trim(label("Application URL"    , text(required, maxlength(200)))),
    "authorizationCode" -> trim(label("Authorization Code" , text(required, maxlength(16)))),
    "threadId"          -> trim(label("Thread ID"          , long(required)))
  )(BackChannelingForm.apply)

  get("/:owner/:repository/settings/backchanneling")(ownerOnly {repository =>
    getBackChanneling(repository.owner, repository.name).map { backChanneling =>
        html.edit(repository, backChanneling, flash.get("info"))
    } getOrElse(html.edit(repository, BackChanneling(repository.owner, repository.name, "", "", 0L), flash.get("info")))
  })

  post("/:owner/:repository/settings/backchanneling/update", backChannelingForm)(ownerOnly {(form, repository) =>
    deleteBackChanneling(repository.owner, repository.name)
    registerBackChanneling(repository.owner, repository.name, form.applicationUrl, form.authorizationCode, form.threadId)
    redirect(s"/${repository.owner}/${repository.name}/settings/backchanneling")
  })

  get("/:owner/:repository/settings/backchanneling/delete")(ownerOnly {repository =>
    deleteBackChanneling(repository.owner, repository.name)
    redirect(s"/${repository.owner}/${repository.name}/settings/backchanneling")
  })

}
