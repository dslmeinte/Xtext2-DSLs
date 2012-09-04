package nl.dslmeinte.examples.xtend.ast

import java.util.Map

/**
 * Simple AST building and evaluation; see: https://gist.github.com/2934374
 * 
 * @author Meinte Boersma (c) 2012
 */
class Eval {

	def static int evaluate(Map<String, Integer> env, Expression exp) { 
		switch exp {
			Variable:	(env.get(exp.name) as Integer).intValue
			Number:		exp.^val
			Multiply:	evaluate(env, exp.x) * evaluate(env, exp.y)
			Add:		evaluate(env, exp.x) + evaluate(env, exp.y)
			default:	0
		}
	}

	def static void main(String...args) {
		val env = <String, Integer>newHashMap
		env.put("a", 3)
		env.put("b", 4)
		env.put("c", 5)

		val expressionTree = new Add(new Variable("a"), new Multiply(new Number(2), new Variable("b")))
		println(evaluate(env, expressionTree))
	}

}

abstract class Expression {}

@Data class Number extends Expression {
	@Property int ^val
}

@Data class Add extends Expression {
	@Property Expression x
	@Property Expression y
}

@Data class Multiply extends Add {}

@Data class Variable extends Expression {
	@Property String name
}

