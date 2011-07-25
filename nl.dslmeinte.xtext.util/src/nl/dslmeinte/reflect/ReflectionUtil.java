package nl.dslmeinte.reflect;

import java.lang.reflect.Method;

/**
 * Quick and dirty util class to help with hacking into classes with otherwise
 * unaccessible methods.
 * 
 * @author Meinte Boersma
 */
public class ReflectionUtil {

	/**
	 * Tries to obtain the method with given signature from the given, taking
	 * the super type hierarchy into account. Throws a
	 * {@link IllegalArgumentException} in case no method was found.
	 */
	public static Method getMethod(Object object, String methodName, Class<?>...parameterTypes) {
		Class<?> clazz = object.getClass();
		Method method = getSafeMethod(clazz, methodName, parameterTypes);
		while( ( method == null ) && ( clazz.getSuperclass() != null ) ) {
			clazz = clazz.getSuperclass();
			method = getSafeMethod(clazz, methodName, parameterTypes);
		}
		if( method == null ) {
			throw new IllegalArgumentException(ReflectionUtil.message(object.getClass(), methodName, "obtain"));
		}
		method.setAccessible(true);
		return method;
	}

	private static Method getSafeMethod(Class<?> clazz, String methodName, Class<?>...parameterTypes) {
		try {
			return clazz.getDeclaredMethod(methodName, parameterTypes);
		} catch( NoSuchMethodException e ) {
			return null;
		}
	}

	/**
	 * Invokes the given method on the given {@link Object} with the given arguments.
	 */
	public static Object invoke(Method method, Object object, Object...arguments) {
		try {
			return method.invoke(object, arguments);
		} catch (Exception e) {
			throw new RuntimeException(message(object.getClass(), method.getName(), "call"), e);
		}
	}

	private static String message(Class<?> clazz, String methodName, String verb) {
		return String.format( "could not %s %s#%s(..)", verb, clazz.getName(), methodName );
	}

}
