package gitbucket.backchanelling.controller

import java.io.File
import gitbucket.core.view.helpers
import jp.sf.amateras.scalatra.forms._

import gitbucket.core.controller.ControllerBase
import gitbucket.core.service._
import gitbucket.core.util._
import gitbucket.core.util.Directory._
import gitbucket.core.util.ControlUtil._
import gitbucket.core.util.Implicits._
import gitbucket.core.view.helpers._

import gitbucket.backchanelling.html

import org.apache.commons.io.FileUtils
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib._
import org.scalatra.Ok

class BackChanellingController extends BackChanellingControllerBase
  with RepositoryService with AccountService with OwnerAuthenticator

trait BackChanellingControllerBase extends ControllerBase {
  self: RepositoryService with AccountService with OwnerAuthenticator =>

  get("/:owner/:repository/settings/backchanelling")(ownerOnly {
    html.edit(_, flash.get("info"))
  })

}
