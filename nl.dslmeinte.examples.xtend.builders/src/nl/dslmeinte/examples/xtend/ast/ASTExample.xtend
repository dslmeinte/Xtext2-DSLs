package nl.dslmeinte.examples.xtend.ast

import java.util.Map
import org.junit.Test

/**
 * Simple AST building and evaluation; see: https://gist.github.com/2934374
 * 
 * @author Meinte Boersma (c) 2012-2014
 */
class EvalWithObjects {

	def int evaluate(Map<String, Integer> it, Expression exp) { 
		switch exp {
			Number:		exp.value
			Variable:	get(exp.name)
			Add:		evaluate(exp.x) + evaluate(exp.y)
			Multiply:	evaluate(exp.x) * evaluate(exp.y)
		}
	}

	// for convenient AST construction:
	def $(String varName)						{ new Variable(varName) }
	def $(int value)							{ new Number(value) }
	def +(Expression left, Expression right)	{ new Add(left, right) }
	def *(Expression left, Expression right)	{ new Multiply(left, right) }

	@Test
	def void run() {
		val env = newHashMap("a" -> 3, "b" -> 4, "c" -> 5)
		val expressionTree = $('a') + $(2) * $('b')
		println(evaluate(env, expressionTree))
	}

}


abstract class Expression {}

@Data class Number extends Expression {
	int value
}

@Data abstract class Binary extends Expression {
	Expression x
	Expression y
}

@Data class Add extends Binary {}
@Data class Multiply extends Binary {}

@Data class Variable extends Expression {
	String name
}

