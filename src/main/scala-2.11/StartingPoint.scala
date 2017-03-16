
import akka.actor.{ActorSystem, PoisonPill, Props}
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.duration._
import scala.language.postfixOps

object StartingPoint extends App {
  val system = ActorSystem()


  val store = system.actorOf(Props(new Store))

//  val sender1 = system.actorOf(Props(new Sender(store, 1)))
//  val sender2 = system.actorOf(Props(new Sender(store, 2)))
//  val sender3 = system.actorOf(Props(new Sender(store, 3)))

  val indexer = system.actorOf(Props(new SuperVisor(system)))
  val pageParser = system.actorOf(Props(new PageParser(indexer)))

  val keyword = "senior java developer"
  val radius = 1
  val location = "London"
  val jobType = "Permanent"

  val url = s"http://www.jobsite.co.uk/vacancies?search_type=advanced&engine=stepmatch&search_referer=internal&query=$keyword&logic=any&Location=$location&radius=$radius&title_query=&title_logic=any&vacancy_type=$jobType&search_currency_code=GBP&salary_type_unit=A&salary_min=&salary_max=&sector=IT&daysback=A&latlong=51.5019,-0.126343"

  pageParser ! url
//  sender1 ! "start"
//  sender2 ! "start"
//  sender3 ! "start"

//  system.scheduler.scheduleOnce(2 seconds)({
//    store ! PoisonPill; system.terminate
//  })

  //  val worker1 = system.actorOf(Props(new Supervisor(system)))
  //  val supervisor = system.actorOf(Props(new Supervisor(system)))
  //
  //  supervisor ! Start("https://foat.me")
  //
  //  Await.result(system.whenTerminated, 10 minutes)
  //
  //  supervisor ! PoisonPill
  //  system.terminate
}
