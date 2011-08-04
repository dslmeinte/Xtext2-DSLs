package nl.dslmeinte.xpand;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


import org.apache.log4j.Logger;
import org.eclipse.xpand2.XpandExecutionContextImpl;
import org.eclipse.xpand2.XpandFacade;
import org.eclipse.xtend.type.impl.java.JavaBeansMetaModel;
import org.eclipse.xtend.typesystem.MetaModel;
import org.eclipse.xtend.typesystem.emf.EmfMetaModel;
import org.eclipse.xtend.typesystem.emf.EmfRegistryMetaModel;

/**
 * Fluent interface-style API to use {@link XpandFacade}.
 * <p>
 * The fluent style is convenient for comfortably configuring a varying number
 * of details. The current implementation is <em>stateful</em> and does only
 * little checking of its own: most checking is still done by the
 * {@link XpandFacade} itself.
 * 
 * @deprecated Use Xtend2 instead of Xpand/Xtend.
 * 
 * @author Meinte Boersma
 */
@Deprecated
public class XpandCallConfiguration {

	// TODO  make this class immutable, returning copies with each new setting

	private Logger logger = Logger.getLogger(getClass());

	public XpandCallConfiguration() {}

	private boolean withEmfRegistry = false;

	/**
	 * Signals that the {@link EmfRegistryMetaModel} is to be used as
	 * <em>only</em> {@link MetaModel} of the {@link EmfMetaModel} kind.
	 * <p>
	 * (Actually, {@link EmfMetaModel} {@code extends}
	 * {@link EmfRegistryMetaModel} but that's a different story...)
	 */
	public XpandCallConfiguration withEmfRegistry() {
		if( emfMetaModelsPresent() ) {
			logger.warn( "configuring 'withEmfRegistry' but a Emf(Registry)MetaModel is already registered" ); //$NON-NLS-1$
		}
		if( withEmfRegistry ) {
			logger.info("'withEmfRegistry' already configured"); //$NON-NLS-1$
		}
		withEmfRegistry = true;
		return this;
	}

	private boolean emfMetaModelsPresent() {
		for( MetaModel mm : metaModels ) {
			if( mm instanceof EmfRegistryMetaModel ) {
				return true;
			}
		}
		return false;
	}

	private List<MetaModel> metaModels = new ArrayList<MetaModel>();

	/**
	 * Registers the given {@link MetaModel}.
	 */
	public XpandCallConfiguration register(MetaModel metaModel) {
		if( metaModel instanceof EmfRegistryMetaModel && withEmfRegistry ) {
			logger.warn( "registering an Emf(Registry)MetaModel but 'withEmfRegistry' is already configured" ); //$NON-NLS-1$
		}
		metaModels.add(metaModel);
		return this;
	}

	private OutputStream output = null;

	/**
	 * Configures an {@link OutputStream} for the template evaluation result to
	 * end up in.
	 */
	public XpandCallConfiguration to(OutputStream newOutput) {
		if( this.output != null ) {
			logger.info( "destination ('to') already set" ); //$NON-NLS-1$
		}
		this.output = newOutput;
		return this;
	}

	private String definitionName = null;

	/**
	 * Configures the definition to call through its fully qualified name.
	 */
	public XpandCallConfiguration call(String newDefinitionName) {
		if( this.definitionName != null ) {
			logger.info( "definition name ('call') already set" ); //$NON-NLS-1$
		}
		this.definitionName = newDefinitionName;
		return this;
	}

	private Object targetObject = null;

	/**
	 * Configures the target {@link Object}.
	 */
	public XpandCallConfiguration on(Object object) {
		if( targetObject != null ) {
			logger.info( "target object ('on') already set" ); //$NON-NLS-1$
		}
		this.targetObject = object;
		return this;
	}

	private Object[] parameters = new Object[0];

	/**
	 * Configures the (optional) parameter {@link Object}s.
	 */
	public XpandCallConfiguration with(Object...newParameters) {
		if( this.parameters.length > 0 ) {
			logger.info( "parameters ('with') already set" ); //$NON-NLS-1$
		}
		this.parameters = newParameters;
		return this;
	}

	private List<String> advices = new ArrayList<String>();

	/**
	 * Registers an AOP-style advice.
	 */
	public XpandCallConfiguration registerAdvice(String fullyQualifiedName) {
		advices.add(fullyQualifiedName);
		return this;
	}

	/**
	 * Evaluates this configuration.
	 */
	public void evaluate() {
		if( output == null ) {
			throw new IllegalStateException("output ('to') must be set"); //$NON-NLS-1$
		}
		final OutputStreamWrapper out = new OutputStreamWrapper(output);

		XpandExecutionContextImpl context = new XpandExecutionContextImpl(out, null);

		if( withEmfRegistry ) {
			context.registerMetaModel(new EmfRegistryMetaModel());
		} else {
			if( metaModels.size() == 0 ) {
				logger.warn( "no meta models registered: registering JavaBeansMetaModel as default" ); //$NON-NLS-1$
				context.registerMetaModel(new JavaBeansMetaModel());
			} else {
			    for( MetaModel mm : metaModels ) {
				    context.registerMetaModel(mm);
			    }
			}
		}

		for( String advice : advices ) {
			context.registerAdvices(advice);
		}

		XpandFacade facade = XpandFacade.create(context);

	    if( definitionName == null ) {
	    	throw new IllegalStateException("definition name ('call') must be set"); //$NON-NLS-1$
	    }
	    if( targetObject == null ) {
	    	logger.warn( "target object ('on') isn't set (calling Xpand template with Void target)" ); //$NON-NLS-1$
	    }

	    try {
			facade.evaluate(definitionName, targetObject, parameters);
		} catch( Throwable throwable) {
			throw new RuntimeException( "exception thrown during evaluation of Xpand template " + definitionName, throwable ); //$NON-NLS-1$
		}
		/*
		 * Unfortunately, the catch is never hit in the case of
		 * NoClassDefFoundError arising from a missing dependency...
		 */
	}

}
