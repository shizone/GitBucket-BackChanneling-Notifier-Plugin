package shizone.gitbucket.backchanneling.model

trait BackChannelingComponent { self: gitbucket.core.model.Profile =>
  import profile.simple._

  lazy val BackChannelings = TableQuery[BackChannelings]

  class BackChannelings(tag: Tag) extends Table[BackChanneling](tag, "BACK_CHANNELING") {
    val userName          = column[String]("USER_NAME")
    val repositoryName    = column[String]("REPOSITORY_NAME")
    val url               = column[String]("URL")
    val authorizationCode = column[String]("AUTHORIZATION_CODE")
    val threadId          = column[Long]("THREAD_ID")
    def * = (userName, repositoryName, url, authorizationCode, threadId) <> (BackChanneling.tupled, BackChanneling.unapply)
  }
}

case class BackChanneling(
  userName: String,
  repositoryName: String,
  url: String,
  authorizationCode: String,
  threadId: Long
)