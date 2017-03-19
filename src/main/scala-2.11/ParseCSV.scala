import org.json4s._
import org.json4s.jackson.JsonMethods._

//case class ParsedJobSpec(salaryMin: Int, salaryMax: Int, salaryMedian: Int, location: String, keywords: List[String])
case class ParsedJobSpec(salaryMin: Int, salaryMax: Int, salaryMedian: Int, location: String, description: String, url: String)

object ParseCSV extends App {
  implicit val formats = DefaultFormats
  val fixRegex = ".*?(\\d+[kK]?\\s*(PA|pa|per)*).*?".r
  val rangeRegex = ".*?(\\d+)[kK]?\\s*-\\s*(\\d+)[kK]?.*?".r

  private val json = scala.io.Source.fromFile("c:\\Users\\gwelican-laptop\\jobs2.json").mkString

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
        .replace(",", "")
      )
    )
    .filter(x => rangeRegex.pattern.matcher(x.salary).matches())
    .filter(x => fixRegex.pattern.matcher(x.salary).matches())
    .map(x => convertToParsedJobs(x))
    .filter(_.salaryMax < 200)
    .filter(_.salaryMedian < 200)
    .map(x => if (x.salaryMedian > 15000) x.copy(salaryMax = x.salaryMax / 1000, salaryMin = x.salaryMin / 1000, salaryMedian = x.salaryMedian / 1000) else x)


  //  specs
  //    .filter(_.description.contains("scala"))
  //    .filter(_.location.toLowerCase.contains("london"))
  //    .sortBy(_.salaryMedian)

  //    .foreach(println)

  var nonKeywordWords = Array[String]("and", "to", "the", "of", "in", "for", "with", "will", "developer", "a", "an", "on", "be", "are", "you", "team",
    "software", "apply", "have", "working", "skills", "experience", "this", "development", "as", "now", "role", "your", "their", "company",
    "we", "work", "that", "or", "", "is", "opportunity", "looking", "client", "business", "they", "knowledge", "new", "000", "based", "from",
    "technical", "environment", "please", "join", "our", "if", " engineer", " end", "technologies", "within", "technology", "it",
    "s", "applications", "senior", "end", "engineer", "strong", "at", "up", "solutions", "who", "projects", "excellent", "benefits", "systems", "developers", "using",
    "cv", "leading", "would", "all", "services", "required", "well"
  )

  val keywords = specs.
    flatMap(_.description.replace("front end", "frontend").replace("big data", "bigdata").toLowerCase.split("\\W")).
    foldLeft(Map.empty[String, Int]) {
      (count, word) => count + (word -> (count.getOrElse(word, 0) + 1))
    }
    .filterKeys(!nonKeywordWords.contains(_))


  keywords
    .toList
    .sortBy(_._2)
    .foreach(println)

  //  println(keywords)
  //  specs
  //    .sortBy(_.salaryMedian)
  //    .foreach(x => println(x.salaryMedian, x.salaryMin, x.salaryMax, x.description, x.url))
  //  .foreach(println)

  //  println(specs.map(_.salaryMedian).sum / specs.size)
  //  println(specs.size)

  //  val x = ""
  //  x.replace(",000", "k")


  //  specs

  //  val regex = """^([a-zA-Z]\d{6})""".r // enables you to drop escaping \'s

  //  specs.filter(_.salary)

  //  val json = try {  parse(stream) } finally { stream.close() }

  //  Serialization.read[JobSpec]("")
}
