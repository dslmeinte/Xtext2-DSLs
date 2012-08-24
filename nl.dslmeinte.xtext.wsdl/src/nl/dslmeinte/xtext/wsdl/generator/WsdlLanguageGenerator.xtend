package nl.dslmeinte.xtext.wsdl.generator

import com.google.inject.Inject
import nl.dslmeinte.xtext.wsdl.WsdlExtensions
import nl.dslmeinte.xtext.wsdl.wsdlLanguage.Binding
import nl.dslmeinte.xtext.wsdl.wsdlLanguage.Definitions
import nl.dslmeinte.xtext.wsdl.wsdlLanguage.HttpTransport
import nl.dslmeinte.xtext.wsdl.wsdlLanguage.Message
import nl.dslmeinte.xtext.wsdl.wsdlLanguage.NotificationOperation
import nl.dslmeinte.xtext.wsdl.wsdlLanguage.OneWayOperation
import nl.dslmeinte.xtext.wsdl.wsdlLanguage.Operation
import nl.dslmeinte.xtext.wsdl.wsdlLanguage.PortType
import nl.dslmeinte.xtext.wsdl.wsdlLanguage.RequestResponseOperation
import nl.dslmeinte.xtext.wsdl.wsdlLanguage.Service
import nl.dslmeinte.xtext.wsdl.wsdlLanguage.SoapTransport
import nl.dslmeinte.xtext.wsdl.wsdlLanguage.SolicitResponseOperation
import nl.dslmeinte.xtext.xsd.xsdLanguage.TopLevelElement
import nl.dslmeinte.xtext.xsd.xsdLanguage.TopLevelType
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess
import org.eclipse.xtext.generator.IGenerator

class WsdlLanguageGenerator implements IGenerator {
	
	override void doGenerate(Resource resource, IFileSystemAccess fsa) {
		val model = resource.contents.head as Definitions
		fsa.generateFile(model.name + ".wsdl", model.wsdlFile)
	}

	@Inject extension WsdlExtensions

	def private wsdlFile(Definitions it)
		'''
		<?xml version="1.0" encoding="UTF-8" standalone="no"?>
		<wsdl:definitions
			name="«name»"
			xmlns:tns="«nsUri»"
			targetNamespace="«nsUri»"
			xmlns:«xsdImport.nsPrefix»="«xsdImport.resolve.nsURI»"
			xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/"
		«IF hasSoapBindings()»	xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/"«ENDIF»
		«IF hasHttpBindings()»	xmlns:http="http://schemas.xmlsoap.org/wsdl/http/"«ENDIF»
		>

		  <wsdl:import namespace="«xsdImport.resolve.nsURI»" location="«xsdImport.resolve.name».xsd" />« /* TODO  this shouldn't be a relative URI */ »

		«FOR message : messages»
			«message.message»
		«ENDFOR»
		«FOR portType : portTypes»
			«portType.portType»
		«ENDFOR»
		«FOR binding : bindings»
			«binding.binding(nsUri)»
		«ENDFOR»
		«FOR service : services»
			«service.service»
		«ENDFOR»

		</wsdl:definitions>
		'''

	def private message(Message it)
		'''
		  <wsdl:message name="«name»">
		    «FOR it : parts»
		    <wsdl:part name="«name»" «^def.defRef(xsdImport.nsPrefix)» />
		    «ENDFOR»
		  </wsdl:message>
		'''

	def private dispatch defRef(TopLevelElement it, String prefix)	'''element="«prefix»:«name»"'''
	def private dispatch defRef(TopLevelType it, String prefix)		'''type="«prefix»:«name»"'''

	def private portType(PortType it)
		'''
		  <wsdl:portType name="«name»">
		  «FOR operation : operations»
		    <wsdl:operation name="«operation.name»">
			«operation.operationContents»
		    </wsdl:operation>
		  «ENDFOR»
		  </wsdl:portType>
		'''

	def private binding(Binding it, String nsUri)
		'''
		  <wsdl:binding name="«name»" type="tns:«type.name»">
		  «transport.binding»
		  «FOR op : type.operations»
		  	<wsdl:operation name="«op.name»">
		  	«transport.operationInterface(nsUri, op)»
		  	</wsdl:operation>
		  «ENDFOR»
		  </wsdl:binding>
		'''

	def private dispatch binding(SoapTransport it)
		'''
		    <soap:binding style="«type»" transport="http://schemas.xmlsoap.org/soap/http"/>
		'''

	def private dispatch binding(HttpTransport it)
		'''
		    <http:binding verb="«typeName().toUpperCase()»" />
		'''

	def private dispatch operationInterface(SoapTransport it, String nsUri, Operation operation)
		'''
  		<soap:operation soapAction="«nsUri»/«operation.name»" />
  		<wsdl:input>
  			<soap:body use="literal" />
  		</wsdl:input>
  		<wsdl:output>
  			<soap:body use="literal" />
  		</wsdl:output>
		'''

	def private dispatch operationInterface(HttpTransport it, String nsUri, Operation operation)
		'''
  		<http:operation location="«operation.name»" />
  		<wsdl:input>
  			<mime:content type="application/x-www-form-urlencoded"/>
  		</wsdl:input>
  		<wsdl:output>
  			<mime:content type="application/octet-stream"/>
  		</wsdl:output>
		'''

	def private service(Service it)
		'''
		  <wsdl:service name="«name»">
		  «IF documentation.trim().length > 0»
		    <wsdl:documentation>«documentation»</wsdl:documentation>
		  «ENDIF»
		  «FOR it : ports»
		    <wsdl:port name="«name»" binding="tns:«binding.name»">
		      <«binding.transport.name()»:address location="«location»"/>
		    </wsdl:port>
		  «ENDFOR»
		  </wsdl:service>
		'''

	def private dispatch operationContents(OneWayOperation it)
		'''
		      <wsdl:input message="tns:«input.name»" />
		'''

	def private dispatch operationContents(RequestResponseOperation it)
		'''
		      <wsdl:input message="tns:«input.name»" />
		      <wsdl:output message="tns:«output.name»" />
		'''

	def private dispatch operationContents(SolicitResponseOperation it)
		'''
		      <wsdl:output message="tns:«output.name»" />
		      <wsdl:input message="tns:«input.name»" />
		'''

	def private dispatch operationContents(NotificationOperation it)
		'''
		      <wsdl:output message="tns:«output.name»" />
		'''

}
