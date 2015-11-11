package gitbucket.backchanneling.model
import java.util.Date

sealed trait Payload

// https://developer.github.com/v3/activity/events/types/#pushevent
case class PushPayload(
  pusher: User,
  ref: String,
  before: String,
  after: String,
  commits: List[Commit],
  repository: Repository) extends Payload

// https://developer.github.com/v3/activity/events/types/#issuesevent
case class IssuesPayload(
  action: String,
  number: Int,
  repository: Repository,
  issue: Issue,
  sender: User) extends Payload

// https://developer.github.com/v3/activity/events/types/#pullrequestevent
case class PullRequestPayload(
  action: String,
  number: Int,
  repository: Repository,
  pull_request: PullRequest,
  sender: User) extends Payload

// https://developer.github.com/v3/activity/events/types/#issuecommentevent
case class IssueCommentPayload(
  action: String,
  repository: Repository,
  issue: Issue,
  comment: Comment,
  sender: User) extends Payload

case class User(
  login: String,
  email: String,
  `type`: String,
  site_admin: Boolean,
  created_at: Date)

case class Repository(
  name: String,
  full_name: String,
  description: String,
  watchers: Int,
  forks: Int,
  `private`: Boolean,
  default_branch: String,
  owner: User)

case class PullRequest(
  number: Int,
  updated_at: Date,
  created_at: Date,
  head: Commit,
  base: Commit,
  mergeable: Option[Boolean],
  title: String,
  body: String,
  user: User)

case class Issue(
  number: Int,
  title: String,
  user: User,
  state: String,
  created_at: Date,
  updated_at: Date,
  body: String)

case class Commit(
  id: String,
  message: String,
  timestamp: Date,
  added: List[String],
  removed: List[String],
  modified: List[String],
  author: PersonIdent,
  committer: PersonIdent)

case class PersonIdent(
  name: String,
  email: String,
  date: Date)

case class Comment(
  id: Int,
  user: User,
  body: String,
  created_at: Date,
  updated_at: Date)
