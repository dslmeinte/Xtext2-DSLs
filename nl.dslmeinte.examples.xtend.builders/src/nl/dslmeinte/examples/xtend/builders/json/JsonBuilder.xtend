package nl.dslmeinte.examples.xtend.builders.json

import org.eclipse.xtext.xbase.lib.Pair
import org.json.JSONArray
import org.json.JSONObject

class JsonBuilder {

	def object(Pair<String, Object>...keyValuePairs) {
		new JSONObject() => [
			keyValuePairs.forEach[ p | it.put(p.key, p.value) ]
		]
	}

	def <T> array(T...objects) {
		new JSONArray() => [
			objects.forEach[ o | it.put(o) ]
		]
	}

	def operator_doubleArrow(String key, Object value) {
		new Pair(key, value)
	}

}
