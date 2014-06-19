package nl.dslmeinte.examples.xtend.objectalgebras

import org.junit.Test

/*
 * TODO  ref to blog post and Tijs' video
 */

/*
 * Basic expressions: literals and addition
 */

interface ExpAlg<E> {
  def E lit(int n)
  def E +(E lhs, E rhs)
}


/*
 * A print operation
 */

interface IPrint {
  def String print()
}

class PrintExp implements ExpAlg<IPrint> {
  override IPrint +(IPrint lhs, IPrint rhs) { new PrintAdd(lhs, rhs) }
  override IPrint lit(int n) { new PrintLit(n) }
}

@Data
class PrintAdd implements IPrint {
  @Property IPrint lhs
  @Property IPrint rhs
  
  override String print() { '''«lhs.print» + «rhs.print»''' }
}

@Data
class PrintLit implements IPrint {
  @Property int value

  override String print() { value.toString }
}

/*
 * Evaluation of expressions
 */

interface IEval {
  def int eval()
}

class EvalExp implements ExpAlg<IEval> {
  override IEval +(IEval lhs, IEval rhs) { new EvalAdd(lhs, rhs) }
  override IEval lit(int n) { new EvalLit(n) }
}

@Data
class EvalAdd implements IEval {
  @Property IEval lhs
  @Property IEval rhs
  
  override int eval() { lhs.eval + rhs.eval }
}

@Data
class EvalLit implements IEval {
  @Property int value

  override int eval() { value }
}

/*
 * Using the code
 */

class Example1 {

  def <E> makeExp(extension ExpAlg<E> a) { lit(1) + lit(2) }

  @Test
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
  override IPrint *(IPrint lhs, IPrint rhs) { new PrintMul(lhs, rhs) }
}

@Data
class PrintMul implements IPrint {
  @Property IPrint lhs
  @Property IPrint rhs
  
  override String print() { '''«lhs.print» * «rhs.print»''' }
}

/*
 * Evaluating multiplication
 */

class EvalExpMul extends EvalExp implements MulAlg<IEval> {
  override IEval *(IEval lhs, IEval rhs) { new EvalMul(lhs, rhs) }
}

@Data
class EvalMul implements IEval {
  @Property IEval lhs
  @Property IEval rhs
  
  override int eval() { lhs.eval * rhs.eval }
}

/* Using the extend code */

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

