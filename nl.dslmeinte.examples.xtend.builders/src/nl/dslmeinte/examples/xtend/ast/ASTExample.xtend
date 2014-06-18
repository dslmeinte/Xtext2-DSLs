package nl.dslmeinte.examples.xtend.ast

import java.util.Map

/**
 * Simple AST building and evaluation; see: https://gist.github.com/2934374
 * 
 * @author Meinte Boersma (c) 2012
 */
class Eval {

	def static void main(String...args) { new Eval().run() }	// run inside an instance instead of statically so we can operator overloading (and use extensions)

	def int evaluate(Map<String, Integer> it, Expression exp) { 
		switch exp {
			Number:		exp.value
			Variable:	get(exp.name)
			Add:		evaluate(exp.x) + evaluate(exp.y)
			Multiply:	evaluate(exp.x) * evaluate(exp.y)
		}
	}

	// for convenient AST construction:
	def $(String varName)	{ new Variable(varName) }
	def $(int value)		{ new Number(value) }
	def operator_plus(Expression left, Expression right)		{ new Add(left, right) }
	def operator_multiply(Expression left, Expression right)	{ new Multiply(left, right) }

	def run() {
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

