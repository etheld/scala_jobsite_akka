import org.json4s._
import org.json4s.jackson.JsonMethods._

case class JobSpec(salaryMin: Int, salaryMax: Int, location: String, keywords: List[String])

object ParseCSV extends App {
  implicit val formats = DefaultFormats
  val rangeRegex = """.*?\d+(k|K)?\s*-\s*\d+(k|K).*?""".r
  val fixRegex = """.*?(\d+[kK]?\s*[pPaA]*).*?""".r


  private val json = scala.io.Source.fromFile("c:\\Users\\gwelican-laptop\\jobs2.json").mkString

  private val specs = parse(json)
    .extract[List[JobSpec]]
    .map(x =>
      x.copy(salary = x.salary
        .replace(",000.00", "k")
        .replace("000.00", "k")
        .replace(",000", "k")
        .replace(".000", "k")
        .replace(" to ", "-")
        .replace("00000", "00k")
        .replace("0000", "0k")
        .replace("000", "k")
        .replace("£", "")
      )
    )
    .filter(x => !rangeRegex.pattern.matcher(x.salary).matches())
    .filter(x => !fixRegex.pattern.matcher(x.salary).matches())

  specs
    .foreach(println)

  println(specs.size)

  //  val x = ""
  //  x.replace(",000", "k")


  //  specs

  //  val regex = """^([a-zA-Z]\d{6})""".r // enables you to drop escaping \'s

  //  specs.filter(_.salary)

  //  val json = try {  parse(stream) } finally { stream.close() }

  //  Serialization.read[JobSpec]("")
}
