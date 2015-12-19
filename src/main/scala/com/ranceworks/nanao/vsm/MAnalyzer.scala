package com.ranceworks.nanao.vsm

import com.atilika.kuromoji.ipadic.{Token, Tokenizer}
import collection.JavaConversions._

import scala.collection.mutable


object MAnalyzer {

  val tokenizer = new Tokenizer()

  def main(args :Array[String]): Unit = {

    val tokens: java.util.List[String] = for{
      token: Token <- tokenizer.tokenize("お寿司が食べたい.お寿司が食べたいなー")
    } yield token.getSurface

    val hoges: java.util.List[String] = for{
      token: Token <- tokenizer.tokenize("寿司猫寿司")
    } yield token.getSurface

    val tft = createTf(tokens.toList)
    val tfh = createTf(hoges.toList)
    val idf = calcIdf(List(tft, tfh))

    println (calcTfIdf(tft, idf))
    println (calcTfIdf(tfh, idf))
  }


  private def createTf(tokens: List[String]): Tf = {
    val fn = (b: mutable.Map[String, Int], a: String) => {
      b get a match {
        case Some(x) => b(a) = x + 1
        case None => b(a) = 1
      }
      b
    }
    Tf((scala.collection.mutable.Map[String, Int]() /: tokens)(fn).toMap)
  }

  private def calcIdf(tfs: List[Tf]): Idf = {
    val d: Int =  tfs.length

    val freqs: List[String] = for {
      tf <- tfs
      term <- tf.terms
    } yield term._1

    val fn = (b: mutable.Map[String, Int], a: String) => {
      b get a match {
        case Some(x) => b(a) = x + 1
        case None => b(a) = 1
      }
      b
    }
    val countMap = (scala.collection.mutable.Map[String, Int]() /: freqs)(fn)
    Idf(countMap.map((b: (String, Int)) => (b._1, math.log((d + 1).toDouble/(b._2 + 1).toDouble))).toMap)
  }

  private def calcTfIdf(tf: Tf, idf: Idf): TfIdf = {
    
    TfIdf(idf.terms.map(a => (a._1, tf.terms.getOrElse(a._1, 0) * a._2)))
  }
  private def calcSimilarity(left: TfIdf, right: TfIdf) = {
    def norm(tfidf: TfIdf) = math.sqrt((0.0 /: tfidf.terms.values)((a: Double, b: Double) => a + math.pow(b, 2)))

    left.terms.map(f=> right.terms.getOrElse(f._1, 0))

    val decimel = (0.0 /: left.terms)((a: Double, b: (String, Double)) => a + (b._2 * right.terms.getOrElse(b._1, 0.0)))

    decimel / (norm(left) * norm(right))
  }
}

case class Tf(terms: Map[String, Int])

case class Idf(terms: Map[String, Double])

case class TfIdf(terms: Map[String, Double]) {
}
