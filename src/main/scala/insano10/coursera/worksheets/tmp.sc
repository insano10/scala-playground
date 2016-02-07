import insano10.coursera.assignments.week06.Anagrams.{Word, Occurrences}

import scala.io.Source

type Word = String
type Sentence = List[Word]
type Occurrences = List[(Char, Int)]

def loadDictionary: List[Word] =
  Source.fromURL("http://lamp.epfl.ch/files/content/sites/lamp/files/teaching/progfun/linuxwords.txt").
    getLines().
    toList.
    filter(w => w.forall(_.isLetter))


val dictionary: List[Word] = loadDictionary

//for a given word, return a sorted list of pairs indicating the frequency of each character in ascending order
//e.g. hello => List(('e' -> 1), ('h' -> 1), ('l' -> 2), ('o' -> 1)
def wordOccurrences(w: Word): Occurrences =
  w.groupBy(c => c).mapValues(c => c.length).toList.sorted


//for a given word, return all anagrams of that word
//these words will all have the same occurence list as the original word
def wordAnagrams(word: Word): List[Word] =
  dictionary.filter(w => wordOccurrences(w) == wordOccurrences(word))


//for a given occurrence list, return all the possible sublists
//this also includes all variations on a pair. e.g. ('a',2) = List(('a',1),('a',2))
def combinations(occurrences: Occurrences): List[Occurrences] = {

  def subCombos(pair: (Char, Int)): Occurrences = {
    for (n <- 1 to pair._2) yield (pair._1, n)
  }.toList

  occurrences.foldLeft(List(List()): List[Occurrences])((acc, pair) =>
    acc ::: (for (subPair <- subCombos(pair); o <- acc) yield subPair :: o))
}

//remove the frequencies in y from Occurrences x
def subtract(x: Occurrences, y: Occurrences): Occurrences =
  y.foldLeft(x.toMap)((acc, pair) =>
    if (pair._2 == acc(pair._1)) acc - pair._1
    else acc.updated(pair._1, acc(pair._1) - pair._2)).toList


def wordsForOccurrences(occurrence: Occurrences): List[Word] = {
  val chars = occurrence.flatMap { case (char, freq) => for (n <- 1 to freq) yield char }.toString
  wordAnagrams(chars)
}

def sentenceAnagrams(sentence: Sentence): List[Sentence] = {

  def anagrams(occurrences: Occurrences): List[Sentence] = {

    if (occurrences.isEmpty) List(List())
    else for {
        combination <- combinations(occurrences)
        possibleWord <- wordsForOccurrences(combination)
        sentence <- anagrams(subtract(occurrences, combination))
      } yield possibleWord :: sentence

  }
  anagrams(wordOccurrences(sentence.mkString))
}

sentenceAnagrams(List("i", "love", "you"))

