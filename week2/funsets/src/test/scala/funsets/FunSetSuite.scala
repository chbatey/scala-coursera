package funsets

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * This class is a test suite for the methods in object FunSets. To run
 * the test suite, you can either:
 *  - run the "test" command in the SBT console
 *  - right-click the file in eclipse and chose "Run As" - "JUnit Test"
 */
@RunWith(classOf[JUnitRunner])
class FunSetSuite extends FunSuite {


  /**
   * Link to the scaladoc - very clear and detailed tutorial of FunSuite
   *
   * http://doc.scalatest.org/1.9.1/index.html#org.scalatest.FunSuite
   *
   * Operators
   *  - test
   *  - ignore
   *  - pending
   */

  /**
   * Tests are written using the "test" operator and the "assert" method.
   */
  test("string take") {
    val message = "hello, world"
    assert(message.take(5) == "hello")
  }

  /**
   * For ScalaTest tests, there exists a special equality operator "===" that
   * can be used inside "assert". If the assertion fails, the two values will
   * be printed in the error message. Otherwise, when using "==", the test
   * error message will only say "assertion failed", without showing the values.
   *
   * Try it out! Change the values so that the assertion fails, and look at the
   * error message.
   */
  test("adding ints") {
    assert(1 + 2 === 3)
  }

  
  import FunSets._

  test("contains is implemented") {
    assert(contains(x => true, 100))
  }
  
  /**
   * When writing tests, one would often like to re-use certain values for multiple
   * tests. For instance, we would like to create an Int-set and have multiple test
   * about it.
   * 
   * Instead of copy-pasting the code for creating the set into every test, we can
   * store it in the test class using a val:
   * 
   *   val s1 = singletonSet(1)
   * 
   * However, what happens if the method "singletonSet" has a bug and crashes? Then
   * the test methods are not even executed, because creating an instance of the
   * test class fails!
   * 
   * Therefore, we put the shared values into a separate trait (traits are like
   * abstract classes), and create an instance inside each test method.
   * 
   */

  trait TestSets {
    val s1 = singletonSet(1)
    val s2 = singletonSet(2)
    val s3 = singletonSet(3)

    val s4 = singletonSet(4)
    val s5 = singletonSet(5)
    val s7 = singletonSet(7)

    val s1000 = singletonSet(1000)

    val s134571000 = union(union(union(union(union(s1, s3), s4), s5), s7), s1000)
    val s1234 = union(union(union(s1, s2), s3), s4)
  }

  /**
   * This test is currently disabled (by using "ignore") because the method
   * "singletonSet" is not yet implemented and the test would fail.
   * 
   * Once you finish your implementation of "singletonSet", exchange the
   * function "ignore" by "test".
   */
  test("singletonSet(1) contains 1") {
    
    /**
     * We create a new instance of the "TestSets" trait, this gives us access
     * to the values "s1" to "s3". 
     */
    new TestSets {
      /**
       * The string argument of "assert" is a message that is printed in case
       * the test fails. This helps identifying which assertion failed.
       */
      assert(contains(s1, 1), "Singleton")
      assert(!contains(s1, 2), "Singleton")
    }
  }

  test("union contains all elements") {
    new TestSets {
      val s = union(s1, s2)
      assert(contains(s, 1), "Union 1")
      assert(contains(s, 2), "Union 2")
      assert(!contains(s, 3), "Union 3")
    }
  }

  test("inersect contains elements in both") {
    new TestSets {
      val s1s2 = union(s1, s2)
      val s2s3 = union(s2, s3)
      val inter = intersect(s1s2, s2s3)
      assert(!contains(inter, 1), "intersect 1")
      assert(contains(inter, 2), "intersect 2")
      assert(!contains(inter, 3), "intersect 3")
    }
  }

  test("diff contains elements not both") {
    new TestSets {
      val s1s2 = union(s1, s2)
      val s2s3 = union(s2, s3)
      val diff1 = diff(s1s2, s2s3)
      assert(contains(diff1, 1), "diff 1")
      assert(!contains(diff1, 2), "diff 2")
      assert(!contains(diff1, 3), "diff 3")

      val diffTest = diff(s134571000, s1234)
      println(FunSets.toString(diffTest))
    }
  }


  test("filter") {
    new TestSets {
      val s1s2s3 = union(union(s1, s2), s3)
      val isOne = (elem : Int) => elem == 1
      val filterTest = filter(s1s2s3, isOne)
      assert(contains(s1s2s3, 1), "1")
      assert(contains(s1s2s3, 2), "2")
      assert(contains(s1s2s3, 3), "3")
      assert(contains(filterTest, 1), "1")
      assert(!contains(filterTest, 2), "2")
      assert(!contains(filterTest, 3), "3")
    }
  }

  test("forall") {
    new TestSets {
      val s1s3 = union(s1, s3)
      val isOdd = (elem: Int) => elem % 2 != 0
      val isEven = (elem: Int) => elem % 2 == 0

      assert(forall(s1s3, isOdd))
      assert(!forall(s1s3, isEven))
    }
  }

  test("exists") {
    new TestSets {
      val s1s3 = union(s1, s3)
      val s1s2s3 = union(s1s3, s2)
      val isOdd = (elem: Int) => elem % 2 != 0
      val isEven = (elem: Int) => elem % 2 == 0

      assert(exists(s1s2s3, isOdd))
      assert(exists(s1s2s3, isEven))
      assert(!exists(s1s3, isEven))
      assert(!exists(s1s2s3, _ == 4))
    }
  }

  test("map") {
    new TestSets {
      val s1s3 = union(s1, s3)
      val s1s2s3 = union(s1s3, s2)
      val addOne = (elem: Int) => elem + 3
      val addOneSet: Set = map(s1s2s3, addOne)

      assert(contains(s1s2s3, 1))
      assert(contains(s1s2s3, 2))
      assert(contains(s1s2s3, 3))

      assert(contains(addOneSet, 4))
      assert(contains(addOneSet, 5))
      assert(contains(addOneSet, 6))
    }
  }

}
