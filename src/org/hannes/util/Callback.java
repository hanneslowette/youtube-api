package org.hannes.util;

/**
 * Callback n shit
 * 
 * @author Hannes
 *
 * @param <T>
 */
public interface Callback<T> {

	/**
	 * Callback
	 * 
	 * @param object
	 * @throws Exception
	 */
	public abstract void call(T object) throws Exception;
	
	/**
	 * When an exception occurs somewhere during execution, this method can be called to
	 * notify the callback thread
	 * 
	 * @param t
	 */
	public abstract void exceptionCaught(Throwable t);

}