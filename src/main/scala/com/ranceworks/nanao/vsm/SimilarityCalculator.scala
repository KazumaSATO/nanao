package com.ranceworks.nanao.vsm

import com.atilika.kuromoji.ipadic.{Token, Tokenizer}
import collection.JavaConversions._

import scala.collection.mutable


object SimilarityCalculator {

  private val tokenizer = new Tokenizer()

  def calcSimilarity(text: String, compared: Map[String, String]): List[(String, Double)] = {
    def getSurfaces(tokens: List[Token]) = tokens.map(t => t.getSurface)

    val tf = calcTf(getSurfaces(tokenizer.tokenize(text).toList))

    val tokensMap: Map[String, Tf] = compared.map(keyText
    => (keyText._1, calcTf(getSurfaces(tokenizer.tokenize(keyText._2).toList))))

    val idf = calcIdf(tf :: tokensMap.values.toList)

    val tfIdf = calcTfIdf(tf, idf)

    val tfIdfMap: Map[String, TfIdf] = tokensMap.map(e => (e._1, calcTfIdf(e._2, idf)))

    calcSimilarity(tfIdf, tfIdfMap)
  }

  private def calcTf(tokens: List[String]): Tf = {
    def countOccur(acc: mutable.Map[String, Int], token: String) = {
      acc get token match {
        case Some(time) => acc(token) = time + 1
        case None => acc(token) = 1
      }
      acc
    }
    Tf((scala.collection.mutable.Map[String, Int]() /: tokens)(countOccur).toMap)
  }

  private def calcIdf(tfList: List[Tf]): Idf = {
    val d: Int =  tfList.length

    val redundantTokens: List[String] = for {
      tf <- tfList
      term <- tf.terms
    } yield term._1

    def countOccur(acc: mutable.Map[String, Int], token: String) = {
      acc(token) = acc.getOrDefault(token, 0) + 1
      acc
    }
    val countMap = (mutable.Map[String, Int]() /: redundantTokens)(countOccur)

    Idf(countMap.map((tokenOccur: (String, Int))
    => (tokenOccur._1, math.log((d + 1).toDouble/(tokenOccur._2 + 1).toDouble))).toMap)
  }

  private def calcTfIdf(tf: Tf, idf: Idf): TfIdf = {
    TfIdf(tf.terms.map(a => (a._1, a._2 * idf.terms(a._1))))
  }

  private def calcSimilarity(target: TfIdf, others: Map[String, TfIdf]): List[(String, Double)] = {
    val similarity: Map[String, Double] = for ( other <- others ) yield (other._1, target * other._2)
    similarity.toList.sortBy(a => a._2).reverse
  }
}

private case class Tf(terms: Map[String, Int])

private case class Idf(terms: Map[String, Double])

private case class TfIdf(terms: Map[String, Double]) {

  def * (right: TfIdf) = {
    def norm(tfIdf: TfIdf) = math.sqrt((0.0 /: tfIdf.terms.values)((acc: Double, e: Double) => acc + math.pow(e, 2)))

    val numerator: Double = (0.0 /: terms)((acc: Double, tokenScore: (String, Double))
    => acc + (tokenScore._2 * right.terms.getOrElse(tokenScore._1, 0.0)))

    numerator / (norm(this) * norm(right))
  }
}
