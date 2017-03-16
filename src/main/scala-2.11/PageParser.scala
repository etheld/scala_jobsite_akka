import akka.actor.{Actor, ActorRef}
import org.jsoup.Jsoup

import scala.collection.JavaConversions._

case class addLink(url: String)

class PageParser(indexer: ActorRef) extends Actor {

  override def receive: Receive = {
    case url: String =>
      val response = Jsoup.connect(url)
        .ignoreContentType(true)
        .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1")
        .execute()

      val doc = response.parse()
      val links = doc.select("div.vacRow:nth-child(2n+1) div h3 a").map(_.attr("href"))
      val next = doc.select("a.next").filter(_.text == "Next")
      val numberOfJobs = doc.select("#searches > div > strong:nth-child(2)").text().toInt
      println(numberOfJobs)

      if (next.nonEmpty) {
        val nextUrl = s"http://jobsite.co.uk" + next.get(0).attr("href")
        self ! nextUrl
      }

      links.foreach(indexer ! addLink(_))

  }
}
