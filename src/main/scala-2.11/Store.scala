import akka.actor.Actor

/**
  * Created by peter.varsanyi on 15/03/2017.
  */
class Store extends Actor {

  var list: List[String] = List.empty[String]

  @scala.throws[Exception](classOf[Exception])
  override def postStop(): Unit = {
    super.postStop()
    println(list)
  }

  def receive: Receive = {
    case msg: String =>
      list = msg :: list
  }
}
