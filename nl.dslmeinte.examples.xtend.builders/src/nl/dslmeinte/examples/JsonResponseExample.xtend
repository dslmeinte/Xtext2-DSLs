package nl.dslmeinte.examples

import com.google.inject.Inject
import nl.dslmeinte.examples.xtend.builders.json.JsonBuilder

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
