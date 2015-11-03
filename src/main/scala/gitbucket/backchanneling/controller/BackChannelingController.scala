package gitbucket.backchanneling.controller

import gitbucket.backchanneling.model.BackChanneling
import gitbucket.backchanneling.service.BackChannelingService
import gitbucket.core.controller.ControllerBase
import gitbucket.core.service._
import gitbucket.core.util._

import gitbucket.backchanneling.html

class BackChannelingController extends BackChannelingControllerBase
  with RepositoryService with AccountService with OwnerAuthenticator with BackChannelingService

trait BackChannelingControllerBase extends ControllerBase {
  self: RepositoryService with AccountService with OwnerAuthenticator with BackChannelingService =>

  get("/:owner/:repository/settings/backchanneling")(ownerOnly {
    val userName = params("userName")
    val repoName = params("repoName")
    getBackChanneling(userName, repoName) match {
      case Some(backChanneling) => {
        html.edit(_, backChanneling, flash.get("info"))
      }
      case None => {
        html.edit(_, BackChanneling(userName, repoName, "", "", 0L), flash.get("info"))
      }
    }
  })

  get("/:owner/:repository/settings/backchanneling/update")(ownerOnly {
    val userName = params("userName")
    val repoName = params("repoName")
    redirect(s"/${userName}/${repoName}/settings/backchanneling")
    getBackChanneling(userName, repoName) match {
      case Some(backChanneling) => {
        html.edit(_, backChanneling, flash.get("info"))
      }
      case None => {
        html.edit(_, BackChanneling(userName, repoName, "", "", 0L), flash.get("info"))
      }
    }
  })

  get("/:owner/:repository/settings/backchanneling/delete")(ownerOnly {
    val userName = params("userName")
    val repoName = params("repoName")
    redirect(s"/${userName}/${repoName}/settings/backchanneling")
    getBackChanneling(userName, repoName) match {
      case Some(backChanneling) => {
        html.edit(_, backChanneling, flash.get("info"))
      }
      case None => {
        html.edit(_, BackChanneling(userName, repoName, "", "", 0L), flash.get("info"))
      }
    }
  })

}
