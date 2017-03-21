import java.util

import org.apache.spark.ml.feature.{HashingTF, IDF, Tokenizer}
import org.apache.spark.sql.SparkSession

/**
  * Created by peter.varsanyi on 21/03/2017.
  */
object MLParser extends App {

  //  val session = SparkSession.builder()
  //    .appName("bela")
  //    .master("local[2]")
  //    .getOrCreate()
  //
  //
  //  private val map = session.sparkContext.wholeTextFiles("jobs_filtered.json").map(x => x._2)
  //  val df = session.read.json(map)
  //  df.show()
  //
  //  df.filter(df("salaryMin") > 50).orderBy("salaryMin").show()
  //  df.printSchema()

  val spark = SparkSession
    .builder
    .appName("TfIdfExample")
    .master("local[2]")
    .getOrCreate()

  // $example on$
  val sentenceData = spark.createDataFrame(Seq(
    (0.0, "Hi I heard about Spark"),
    (0.0, "I wish Java could use case classes"),
    (1.0, "Logistic regression models are neat"),
    (2.0, "I know")
  )).toDF("label", "sentence")

  val tokenizer = new Tokenizer().setInputCol("sentence").setOutputCol("words")
  val wordsData = tokenizer.transform(sentenceData)

  val hashingTF = new HashingTF()
    .setInputCol("words")
    .setOutputCol("rawFeatures")
    .setNumFeatures(20)

  val tf = hashingTF.transform(wordsData)
//
//  val documents = spark
//
//  documents.show()
//  println(documents.select("description"))

//  val words = documents.collect.flatten.distinct
//  words.foreach{
//    case word =>
//      dict.put(hashingTF.indexOf(word),word)
//  }

  val dict: util.HashMap[Long,String] = new util.HashMap()
  val words = wordsData.collect.flatten.distinct

  val idf = new IDF().setInputCol("rawFeatures").setOutputCol("features")
  val idfModel = idf.fit(tf)


  val tfidf = idfModel.transform(tf)
  tfidf.select("label", "sentence", "features").show(100, 1000)
  http://stackoverflow.com/questions/36372663/spark-scala-tf-idf-value-sorted-vectors

  tfidf.map(x => x.toSparse).map{x => x.indices.zip(x.values)
    .sortBy(-_._2)
    .take(10)
    .map(_._1)
  }
  // $example off$

  //  val documentDF = spark.createDataFrame(Seq(
  //    "Hi I heard about Spark".split(" "),
  //    "I wish Java could use case classes".split(" "),
  //    "Logistic regression models are neat".split(" ")
  //  ).map(Tuple1.apply)).toDF("text")
  //
  //  // Learn a mapping from words to Vectors.
  //  val word2Vec = new Word2Vec()
  //    .setInputCol("text")
  //    .setOutputCol("result")
  //    .setVectorSize(3)
  //    .setMinCount(0)
  //  val model = word2Vec.fit(documentDF)
  //
  //
  //  val result = model.transform(documentDF)
  //  result.collect().foreach { case Row(text: Seq[_], features: Vector) =>
  //    println(s"Text: [${text.mkString(", ")}] => \nVector: $features\n") }
  // $example off$


  spark.stop()
  //  https://gist.githubusercontent.com/anonymous/f5c3910253cb863e9f95bdd741188e5b/raw/bd99b74d3b77667208b6cd4d3bbfa0f966dac997/-


  //  val sc = SparkContext
  //  val sqlContext = new SQLContext(sc)


  //  val sc: SparkContext // An existing SparkContext.

  //  val sqlContext = new org.apache.spark.sql.SQLContext(sc)
  //
  //  val df = sqlContext.read.json("examples/src/main/resources/people.json")
  //
  //  org.apache.spark.sql.SQLContext.read
  //  val df = org.apache.spark.sql.sqlContext.read.json("https://gist.githubusercontent.com/anonymous/f5c3910253cb863e9f95bdd741188e5b/raw/bd99b74d3b77667208b6cd4d3bbfa0f966dac997/-")

  //  val df = "x"


}
