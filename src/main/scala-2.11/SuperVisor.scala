import akka.actor.{Actor, ActorSystem, Props}


class SuperVisor(system: ActorSystem) extends Actor {
  var list = List.empty[String]

  val process = "Process next url"
  //  val tick =
  //    context.system.scheduler.schedule(0 millis, 1000 millis, self, process)
  val pageParser1 = system.actorOf(Props(new PageParser(self)))

  val pageParser2 = system.actorOf(Props(new PageParser(self)))
  val pageParser3 = system.actorOf(Props(new PageParser(self)))
  val pageParser4 = system.actorOf(Props(new PageParser(self)))
  val pageParser5 = system.actorOf(Props(new PageParser(self)))

  override def receive: Receive = {
    case url: addLink => {
      list = url.url :: list
      println(url)
    }
    case `process` => {
      println("triggered")
    }

  }
}
