package nl.dslmeinte.examples.xtend.objectalgebras.closure

import java.util.Map
import org.junit.Test

/*
 * This is the source code for the blog post
 * http://dslmeinte.wordpress.com/2014/06/18/object-algebras-in-xtend/
 * inspired by Tijs van der Storm's presentation during Joy of Coding 2014:
 * http://www.infoq.com/presentations/object-algebras
 */

/*
 * Basic expressions: literals and addition
 */

interface ExpAlg<E> {
  def E lit(int n)
  def E +(E lhs, E rhs)		// note that Xtend >= 2.6 allows this syntax for overloading
}


/*
 * A print operation
 */

interface IPrint {
  def String print()
}

class PrintExp implements ExpAlg<IPrint> {
  override IPrint +(IPrint lhs, IPrint rhs)	{ [| '''«lhs.print» + «rhs.print»''' ] }	// again: nice syntax for overloading
  override IPrint lit(int n)				{ [| n.toString ] }
}
/*
 * Use closures to implement the interface (IPrint in this case),
 * capturing the parameters (lhs, rhs, n) as finals within the inner scope.
 * Note that we have to make explicit that the closures have no arguments
 * (nothing before |) in order to comply with the interface definition.
 */


/*
 * Evaluation of expressions
 */

interface IEval {
  def int eval()
}

class EvalExp implements ExpAlg<IEval> {
  override IEval +(IEval lhs, IEval rhs)	{ [| lhs.eval + rhs.eval ] }
  override IEval lit(int n)					{ [| n ] }
}

/*
 * Using the code
 */

class Example1 {

  def <E> makeExp(extension ExpAlg<E> a) { lit(1) + lit(2) }
  	/*
  	 * Two observations:
  	 *   1. We need to make the makeExp function generic to get things to compile.
  	 *   2. By using the extension keyword, we don't need to use the "a." prefix everywhere.
  	 * 		Together with the overloading of + this makes for very nice syntax.
  	 */

  @Test		// (use a JUnit annotation to easily run this)
  def void baseMain() {
    val x1 = makeExp(new PrintExp)
    println(x1.print)
  
    val x2 = makeExp(new EvalExp)
    println(x2.eval)
  }

}


/*
 * Language extension with multiplication
 */

interface MulAlg<E> extends ExpAlg<E> {
  def E *(E lhs, E rhs)
}

/*
 * Printing multiplication
 */

class PrintExpMul extends PrintExp implements MulAlg<IPrint> {
  override IPrint *(IPrint lhs, IPrint rhs)	{ [| '''«lhs.print» * «rhs.print»''' ] }
}

/*
 * Evaluating multiplication
 */

class EvalExpMul extends EvalExp implements MulAlg<IEval> {
  override IEval *(IEval lhs, IEval rhs)	{ [| lhs.eval * rhs.eval ] }
}

/* Using the extended code */

class Example2 {

  def <E> makeMulExp(extension MulAlg<E> a) { lit(1) + lit(2) * lit(3) }

  @Test
  def void mulMain() {
    val x1 = makeMulExp(new PrintExpMul)
    println(x1.print)

    val x2 = makeMulExp(new EvalExpMul)
    println(x2.eval)
  }

}


/*
 * Expose structure, even when using closures
 */

interface IExpose {
	def Description describe()
}

@Data
class Description {
	String name
	Map<String, Object> properties
}

class DescribeAlg implements MulAlg<IExpose> {

	override lit(int n)						{ [| new Description('lit(-eral)', newHashMap('n' -> n)) ] }
	override *(IExpose lhs, IExpose rhs)	{ [| new Description('multiplication/*', newHashMap('lhs' -> lhs.describe, 'rhs' -> rhs.describe)) ] }
	override +(IExpose lhs, IExpose rhs)	{ [| new Description('addition/*', newHashMap('lhs' -> lhs.describe, 'rhs' -> rhs.describe)) ] }

}

class Example3 extends Example2 {

  @Test
  def void describeMain() {
    println(makeMulExp(new DescribeAlg).describe)
  }

}

