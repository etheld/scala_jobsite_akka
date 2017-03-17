import org.json4s._
import org.json4s.jackson.JsonMethods._

//case class ParsedJobSpec(salaryMin: Int, salaryMax: Int, salaryMedian: Int, location: String, keywords: List[String])
case class ParsedJobSpec(salaryMin: Int, salaryMax: Int, salaryMedian: Int, location: String, description: String, url: String)

object ParseCSV extends App {
  implicit val formats = DefaultFormats
  val fixRegex = ".*?(\\d+[kK]?\\s*(PA|pa|per)*).*?".r
  val rangeRegex = ".*?(\\d+)[kK]?\\s*-\\s*(\\d+)[kK]?.*?".r

  private val json = scala.io.Source.fromFile("/Users/peter.varsanyi/Downloads/jobs2.json").mkString

  def convertToParsedJobs(x: JobSpec): ParsedJobSpec = {
    if (rangeRegex.pattern.matcher(x.salary).matches()) {
      val rangeRegex(min, max) = x.salary
      ParsedJobSpec(min.toInt, max.toInt, (min.toInt + max.toInt) / 2, x.location, x.description, x.url)
    } else {
      val fixRegex(salary) = x.salary
      ParsedJobSpec(salary.toInt, salary.toInt, salary.toInt, x.location, x.description, x.url)
    }
  }

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
        .replace(",","")
      )
    )
    .filter(x => rangeRegex.pattern.matcher(x.salary).matches())
    .filter(x => fixRegex.pattern.matcher(x.salary).matches())
    .map(x => convertToParsedJobs(x))
    .filter(_.salaryMax < 200)
    .filter(_.salaryMedian < 200)
//    .map(x => if (x.salaryMedian > 15000) x.copy(salaryMax = x.salaryMax / 1000, salaryMin = x.salaryMin / 1000, salaryMedian = x.salaryMedian / 1000) else x)


  specs.
    flatMap(_.description.split("\\W")).
//    foldLeft(Map.empty[String, Int]) {
//      (count, word) => count + (word -> (count.getOrElse(word, 0) + 1))
//    }

  specs
    .sortBy(_.salaryMedian)
    .foreach(x => println(x.salaryMedian, x.salaryMin, x.salaryMax, x.description, x.url))
  //  .foreach(println)

  println(specs.map(_.salaryMedian).sum / specs.size)
  println(specs.size)

  //  val x = ""
  //  x.replace(",000", "k")


  //  specs

  //  val regex = """^([a-zA-Z]\d{6})""".r // enables you to drop escaping \'s

  //  specs.filter(_.salary)

  //  val json = try {  parse(stream) } finally { stream.close() }

  //  Serialization.read[JobSpec]("")
}
