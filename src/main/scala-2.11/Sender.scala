import akka.actor.{Actor, ActorRef, PoisonPill}

/**
  * Created by peter.varsanyi on 15/03/2017.
  */
class Sender(store: ActorRef, id: Int) extends Actor {
  var count = 0

  def receive: Receive = {
    case "start" =>
      println(s"$id sends $count to the store")
      store ! s"$id => $count"
      count += 1
      if (count < 5)
        self ! "start"
      else self ! PoisonPill
  }
}
