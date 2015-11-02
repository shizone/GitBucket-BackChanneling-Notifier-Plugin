import gitbucket.backchanneling.trigger.ActivityTrigger
import gitbucket.core.service.SystemSettingsService.SystemSettings
import gitbucket.core.plugin._
import gitbucket.core.util.Version
import gitbucket.backchanneling.controller._
import javax.servlet.ServletContext

class Plugin extends gitbucket.core.plugin.Plugin {

  new ActivityTrigger

  override val pluginId: String = "backchanneling_notifier"

  override val pluginName: String = "BackChanneling Notifier Plugin"

  override val description: String = "Notify GitBucket activity to BackChanneling."

  override val versions: List[Version] = List(
    Version(0, 1)
  )

  override def initialize(registry: PluginRegistry, context: ServletContext, settings: SystemSettings): Unit = {
    super.initialize(registry, context, settings)
    println("-- Back-Channeling plug-in initialized --")
  }

  //  override val repositoryRoutings = Seq(
  //    GitRepositoryRouting("gist/(.+?)/(.+?)\\.git", "gist/$1/$2", new GistRepositoryFilter())
  //  )

  override val controllers = Seq(
    "/*" -> new BackChannelingController()
  )
  override def javaScripts(registry: PluginRegistry, context: ServletContext, settings: SystemSettings): Seq[(String, String)] = {
    // Add Snippet link to the header
    val path = settings.baseUrl.getOrElse(context.getContextPath)
    Seq(
      ".*/settings/(?!backchanneling)[^/]*" -> s"""
        |var owner = $$("input[type=hidden][name=owner]").val();
        |var repository = $$("input[type=hidden][name=repository]").val();
        |$$('ul.side-menu li:last').after(
        |  $$('<li></li>').append(
        |   $$('<a href="">Back Channeling</a>').attr('href', '${path}/' + owner + '/' + repository + '/settings/backchanneling')
        |  )
        |);
      """.stripMargin,
      ".*/settings/backchanneling" -> s"""
        |var owner = $$("input[type=hidden][name=owner]").val();
        |var repository = $$("input[type=hidden][name=repository]").val();
        |$$('ul.side-menu li:last').after(
        |  $$('<li class="active"></li>').append(
        |   $$('<a href="">Back Channeling</a>').attr('href', '${path}/' + owner + '/' + repository + '/settings/backchanneling')
        |  )
        |);
        |
      """.stripMargin)
  }
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
