package nl.dslmeinte.examples.xtend.builders.dom

class DomBuilder extends BaseDomBuilder {

def head(Html parent, (Head)=>void subBuilder) {
	val child = new Head().subBuildWith(subBuilder)
	child.appendTo(parent)
}
def title(Head parent, (Title)=>void subBuilder) {
	val child = new Title().subBuildWith(subBuilder)
	child.appendTo(parent)
}
def body(Html parent, (Body)=>void subBuilder) {
	val child = new Body().subBuildWith(subBuilder)
	child.appendTo(parent)
}
def b(ContentTag parent, (B)=>void subBuilder) {
	val child = new B().subBuildWith(subBuilder)
	child.appendTo(parent)
}
def p(ContentTag parent, (P)=>void subBuilder) {
	val child = new P().subBuildWith(subBuilder)
	child.appendTo(parent)
}
def h1(ContentTag parent, (H1)=>void subBuilder) {
	val child = new H1().subBuildWith(subBuilder)
	child.appendTo(parent)
}
def a(ContentTag parent, String href, (A)=>void subBuilder) {
	val child = new A().subBuildWith(subBuilder)
	child.attributes.put("href", href)
	child.appendTo(parent)
}

}

