package nl.dslmeinte.reflect;

import java.lang.reflect.Method;

import org.eclipse.xtext.util.SimpleCache;

import com.google.common.base.Function;

/**
 * Implements a simple cache to obtain methods with the given signature from
 * {@link Object}s, disregarding visibility and inheritance.
 * 
 * @author Meinte Boersma
 * 
 * @param <T>
 */
public class MethodCache<T> extends SimpleCache<T, Method> {

	public MethodCache(final String methodName, final Class<?>...parameterTypes) {
		super(new Function<T, Method>() {
				@Override
				public Method apply(T t) {
					return ReflectionUtil.getMethod(t, methodName, parameterTypes);
				}
			});
	}

}
