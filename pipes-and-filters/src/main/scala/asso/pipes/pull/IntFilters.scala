package asso.pipes.pull

class MultiplesFilter(source: PullPipe[Long], val multiple: Long) extends SimpleFilter[Long](source = source) {

  override def operate(value: Long): Either[Value[Long], NoValue[Long]] = {
    if (value % multiple == 0) {
      Left(Value(value))
    } else {
      Right(NoValue())
    }
  }
}
