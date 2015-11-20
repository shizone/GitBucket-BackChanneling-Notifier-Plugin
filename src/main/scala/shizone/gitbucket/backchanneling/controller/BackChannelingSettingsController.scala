package shizone.gitbucket.backchanneling.controller

import jp.sf.amateras.scalatra.forms._

import shizone.gitbucket.backchanneling.model.BackChanneling
import shizone.gitbucket.backchanneling.service.BackChannelingSettingsService
import gitbucket.core.controller.ControllerBase
import gitbucket.core.service.{WebHookService, RepositoryService, AccountService}
import gitbucket.core.util._
import gitbucket.core.util.Implicits._

import shizone.gitbucket.backchanneling.html

class BackChannelingSettingsController extends BackChannelingSettingsControllerBase
  with RepositoryService with AccountService with OwnerAuthenticator with WebHookService with BackChannelingSettingsService

trait BackChannelingSettingsControllerBase extends ControllerBase {
  self: RepositoryService with AccountService with OwnerAuthenticator with WebHookService with BackChannelingSettingsService =>

  case class BackChannelingForm(applicationUrl: String, authorizationCode: String, threadId: Long)
  val backChannelingForm = mapping(
    "applicationUrl"    -> trim(label("Application URL"    , text(required, maxlength(200)))),
    "authorizationCode" -> trim(label("Authorization Code" , text(required, maxlength(16)))),
    "threadId"          -> trim(label("Thread ID"          , long(required)))
  )(BackChannelingForm.apply)

  val backChannelngWebHook = "webhook/backchanneling"

  get("/:owner/:repository/settings/backchanneling")(ownerOnly {repository =>
    getBackChanneling(repository.owner, repository.name).map { backChanneling =>
        html.edit(repository, backChanneling, flash.get("info"))
    } getOrElse(html.edit(repository, BackChanneling(repository.owner, repository.name, "", "", 0L), flash.get("info")))
  })

  post("/:owner/:repository/settings/backchanneling/update", backChannelingForm)(ownerOnly {(form, repository) =>
    deleteBackChanneling(repository.owner, repository.name)
    registerBackChanneling(repository.owner, repository.name, form.applicationUrl, form.authorizationCode, form.threadId)
    val url = request.getRequestURL.toString.replaceAll("settings/backchanneling/update", "webhook/backchanneling")
    deleteWebHookURL(repository.owner, repository.name, url)
    addWebHookURL(repository.owner, repository.name, url)
    redirect(s"/${repository.owner}/${repository.name}/settings/backchanneling")
  })

  get("/:owner/:repository/settings/backchanneling/delete")(ownerOnly {repository =>
    deleteBackChanneling(repository.owner, repository.name)
    val url = request.getRequestURL.toString.replaceAll("settings/backchanneling/deletet", "webhook/backchanneling")
    deleteWebHookURL(repository.owner, repository.name, url)
    redirect(s"/${repository.owner}/${repository.name}/settings/backchanneling")
  })

}
