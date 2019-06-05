package asso.pipes.push

import scala.collection.mutable.Queue

trait MessageQueue

object MessageQueue{

  case class MessageQueue(num_records_returned: Long, runtime_pc: Long, runtime_bbds: Long, runtime_gts: Long, runtime_ss: Long)

  val messages = Queue[MessageQueue](
    MessageQueue(0, 0, 0, 0, 0),
    MessageQueue(0, 0, 0, 0, 0),
    MessageQueue(0, 0, 0, 0, 0),
    MessageQueue(0, 0, 0, 0, 0),
    MessageQueue(0, 0, 0, 0, 0)
  )

}