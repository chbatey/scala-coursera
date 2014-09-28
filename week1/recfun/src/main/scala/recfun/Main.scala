package recfun
import common._

object Main {
  def main(args: Array[String]) {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }
  }

  /**
   * Exercise 1
   */
  def pascal(c: Int, r: Int): Int = {
    if (r == 0) {
      1
    } else if (c == 0 || c == r) {
      1
    } else {
      pascal(c-1, r-1) + pascal(c, r-1)
    }
  }

  /**
   * Exercise 2
   */
  def balance(chars: List[Char]): Boolean = {
    def count(chars: List[Char], acc: Int) : Int = {
      if (chars.isEmpty) {
        acc
      } else if (acc < 0) {
        -1
      } else if (chars.head == '(') {
        count(chars.tail, acc + 1)
      } else if (chars.head == ')') {
        count(chars.tail, acc - 1)
      } else {
        count(chars.tail, acc)
      }
    }

    count(chars, 0) == 0
  }

  /**
   * Exercise 3
   */
  def countChange(money: Int, coins: List[Int]): Int = {
    def count(mon : Int, counts: List[Int]) : Int = {
      if (mon == 0) {
        1
      } else if (mon < 0) {
        0
      } else if (counts.isEmpty) {
        0
      } else {
        count(mon - counts.head, counts) + count(mon, counts.tail)
      }
    }
    count(money, coins)
  }
}
