package gitbucket.backchanelling.trigger

import java.sql.Connection

import gitbucket.backchanelling.service.BackChanellingRequest
import org.h2.api.Trigger

/**
 * Created by razon on 15/09/12.
 */
class ActivityTrigger extends Trigger {

  @Override
  def init(conn: Connection, schemaName: String,
    triggerName: String, tableName: String , before: Boolean, typ: Int) {
    // initialize the trigger object is necessary
  }

  @Override
  def fire(conn: Connection, oldRow: Array[Object], newRow: Array[Object]): Unit = {
    BackChanellingRequest.post(
      s"user:$newRow[1], message:$newRow[5], additional_info:$newRow[6]")
  }

  @Override
  def close() {
    // ignore
  }

  @Override
  def remove() {
    // ignore
  }
}
