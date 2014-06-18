package nl.dslmeinte.examples.xtend.builders

import com.google.inject.Inject
import nl.dslmeinte.examples.xtend.builders.json.JsonBuilder

/**
 * @author Meinte Boersma (c) 2012
 */
class JsonResponseExample {

	@Inject extension JsonBuilder

	def example() {
	     object(
	     	"dev"		=> true,
	     	"myArray"	=> array("foo", "bar"),
	     	"nested"	=> object("answer" => 42)
	     )
	}

}
