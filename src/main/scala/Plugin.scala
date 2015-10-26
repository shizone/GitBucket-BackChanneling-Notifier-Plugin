import gitbucket.backchanelling.trigger.ActivityTrigger
import gitbucket.core.service.SystemSettingsService.SystemSettings
import gitbucket.core.plugin._
import gitbucket.core.util.Version
import javax.servlet.ServletContext

class Plugin extends gitbucket.core.plugin.Plugin {

  new ActivityTrigger

  override val pluginId: String = "back-channeling"

  override val pluginName: String = "Back-Chanelling Plugin"

  override val description: String = "Notify GitBucket activity to Back-Chanelling."

  override val versions: List[Version] = List(
    Version(0, 1)
  )

  override def initialize(registry: PluginRegistry, context: ServletContext, settings: SystemSettings): Unit = {
    super.initialize(registry, context, settings)
    println("-- Back-Chanelling plug-in initialized --")
  }

//  override val repositoryRoutings = Seq(
//    GitRepositoryRouting("gist/(.+?)/(.+?)\\.git", "gist/$1/$2", new GistRepositoryFilter())
//  )
//
//  override val controllers = Seq(
//    "/*" -> new GistController()
//  )
//
//  override def javaScripts(registry: PluginRegistry, context: ServletContext, settings: SystemSettings): Seq[(String, String)] = {
//    // Add Snippet link to the header
//    val path = settings.baseUrl.getOrElse(context.getContextPath)
//    Seq(
//      ".*" -> s"""
//        |$$('a.global-header-menu:last').after(
//        |  $$('<a href="${path}/gist" class="global-header-menu">Gist</a>')
//        |);
//      """.stripMargin
//    )
//  }
}

//class GistRepositoryFilter extends GitRepositoryFilter with AccountService {
//
//  override def filter(path: String, userName: Option[String], settings: SystemSettings, isUpdating: Boolean)
//                     (implicit session: Session): Boolean = {
//    if(isUpdating){
//      (for {
//        userName <- userName
//        account  <- getAccountByUserName(userName)
//      } yield
//        path.startsWith("/" + userName + "/") || account.isAdmin
//      ).getOrElse(false)
//    } else true
//  }
//
//}
