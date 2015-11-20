package shizone.gitbucket.backchanneling.service

import shizone.gitbucket.backchanneling.model.BackChanneling
import shizone.gitbucket.backchanneling.model.Profile._
import profile.simple._

trait BackChannelingSettingsService {
  def getBackChanneling(userName: String, repositoryName: String)(implicit s: Session): Option[BackChanneling] =
    BackChannelings.filter(t => (t.userName === userName.bind) && (t.repositoryName === repositoryName.bind)).firstOption

  def registerBackChanneling(userName: String, repositoryName: String, url: String, authorizationCode: String, threadId: Long)(implicit s: Session): Unit =
    BackChannelings.insert(BackChanneling(userName, repositoryName, url, authorizationCode, threadId))

  def deleteBackChanneling(userName: String, repositoryName: String)(implicit s: Session): Unit = {
    BackChannelings.filter(t => (t.userName === userName.bind) && (t.repositoryName === repositoryName.bind)).delete
  }
}