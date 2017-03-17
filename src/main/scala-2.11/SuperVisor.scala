import java.io.{File, PrintWriter}

import akka.actor.{Actor, ActorSystem}
import org.json4s.jackson.Serialization
import org.jsoup.Jsoup

import scala.concurrent.{ExecutionContext, Future}

case class JobSpec(salary: String, location: String, description: String, url: String)

class SuperVisor(system: ActorSystem) extends Actor {
  var listUrls = List.empty[String]
  var listSpec = List.empty[JobSpec]

  val executor = java.util.concurrent.Executors.newFixedThreadPool(15)
  implicit val ec = ExecutionContext.fromExecutor(executor)


  def parseJob(url: String) = {
    val response = Jsoup.connect(url)
      .ignoreContentType(true)
      .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1")
      .execute()

    val doc = response.parse()
    val salary = doc.select("div.summary span.Salary").text()
    val location = doc.select("div.summary span.locationConcat").text()
    val description = doc.select("div.vacancySection div.Description").text()


    self ! JobSpec(salary, location, description, url)
  }

  @throws[Exception](classOf[Exception])
  override def postStop(): Unit = {
    super.postStop()
    implicit val formats = org.json4s.DefaultFormats

    val pw = new PrintWriter(new File("/Users/peter.varsanyi/Downloads/jobs.json" ), "UTF-8")

    pw.write(Serialization.write(listSpec))
    pw.close()
  }

  override def receive: Receive = {
    case spec: JobSpec => {
      listSpec = spec :: listSpec
      println(spec)
    }
    case link: addLink => {
      listUrls = link.url :: listUrls

      Future(parseJob("http://jobsite.co.uk" + link.url))
      println(link)
    }

  }
}
