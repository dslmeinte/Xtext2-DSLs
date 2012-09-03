package nl.dslmeinte.examples.xtend.builders.json

import org.eclipse.xtext.xbase.lib.Pair
import org.json.JSONArray
import org.json.JSONObject

/**
 * Xtend class with extension functions for building JSON trees.
 * 
 * @author Meinte Boersma (c) 2012
 */
class JsonBuilder {

	/**
	 * Creates a {@link JSONObject} from the given key-value
	 * {@link Pair pairs}.
	 */
	def object(Pair<String, Object>...keyValuePairs) {
		new JSONObject() => [
			keyValuePairs.forEach[ p | it.put(p.key, p.value) ]
		]
	}

	/**
	 * Creates a {@link JSONArray} from the given objects (as varargs).
	 */
	def array(Object...objects) {
		new JSONArray() => [
			objects.forEach[ o | it.put(o) ]
		]
	}

	/**
	 * Overloads the {@code =>} binary operator to provide
	 * nice syntax for key-value pairs (with the key a {@link String}).
	 */
	def operator_doubleArrow(String key, Object value) {
		new Pair(key, value)
	}

}
