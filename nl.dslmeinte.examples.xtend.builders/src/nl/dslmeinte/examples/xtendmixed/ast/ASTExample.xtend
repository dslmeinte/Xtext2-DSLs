package nl.dslmeinte.examples.xtendmixed.ast

/**
 * Simple AST building and evaluation; see: https://gist.github.com/2934374
 * 
 * @author Meinte Boersma (c) 2012
 */
class Eval {

	def static void main(String...args) { new Eval().run() }	// run inside an instance instead of statically so we can operator overloading (and use extensions)

	// for convenient AST construction:
	def Expr $(String varName)							{ [ get(varName) ] }
	def Expr $(int value)								{ [ value ] }
	def Expr operator_plus(Expr left, Expr right)		{ [ left.eval(it) + right.eval(it) ] }
	def Expr operator_multiply(Expr left, Expr right)	{ [ left.eval(it) * right.eval(it) ] }

	def run() {
		println( ( $('a') + $(2) * $('b') ).eval( newHashMap("a" -> 3, "b" -> 4, "c" -> 5) ) )
	}

}

