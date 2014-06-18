package nl.dslmeinte.examples.xtend.ast

import java.util.Map

/**
 * Simple AST building and evaluation; see: https://gist.github.com/2934374
 * 
 * @author Meinte Boersma (c) 2012-2014
 */
class EvalWithoutObjects {

	def static void main(String...args) { new EvalWithoutObjects().run() }
		// run inside an instance instead of statically so we can do operator overloading (and use extensions)

	// for convenient AST construction:
	def Expr $(String varName)							{ [ get(varName) ] }
	def Expr $(int value)								{ [ value ] }
	def Expr operator_plus(Expr left, Expr right)		{ [ left.eval(it) + right.eval(it) ] }
	def Expr operator_multiply(Expr left, Expr right)	{ [ left.eval(it) * right.eval(it) ] }

	/*
	 * This works because Xtend does "ducktyping" (of sorts) w.r.t. interfaces versus closures:
	 * the interface Expr has one method with signature  (Map<String, Integer) => int
	 * (in Xtend closure syntax) and Xtend infers the closure argument to Map<String, Integer>
	 * by matching to the declared return type Expr.
	 */

	def run() {
		println( ( $('a') + $(2) * $('b') ).eval( newHashMap("a" -> 3, "b" -> 4, "c" -> 5) ) )
	}

}

interface Expr {

	def int eval(Map<String, Integer> env)

}

