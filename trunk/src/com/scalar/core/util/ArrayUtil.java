package com.scalar.core.util;

import com.scalar.freequent.l10n.MessageResource;

import java.lang.reflect.Array;

/**
 * User: ssuppala
 * Date: Aug 30, 2013
 * Time: 2:56:24 PM
 */
/**
 * This class provides some convenient array management functions.
 */
public final class ArrayUtil
{
	/**
	 * No instance can be created since all methods are static.
	 */
	private ArrayUtil() {}

	/**
	 * Adds an object to the end of an array.
	 *
	 * @param array The array to which the object is to be added.
	 * @param obj The object to be added.
	 *
	 * @return The resulting array.
	 */
	public static Object addObject(Object array, Object obj) {

		if ( array == null ) {

			// Create a new array with one element
			array = Array.newInstance(obj.getClass(), 1);
			// Add the new element
			Array.set(array, 0, obj);

		} else {

			// Create an array with room for one additional element.
			int length = Array.getLength(array);
			Object newArray = Array.newInstance(obj.getClass(), length+1);

			// Copy the elements from the old array to the new array.
			if ( length > 0 ) {
				System.arraycopy(array, 0, newArray, 0, length);
			}
			array = newArray;

			// Add the new element
			Array.set(array, length, obj);
		}

		return array;

	} // End addObject

	/**
	 * Inserts an object in an array.
	 *
	 * @param array The array into which the object is to be inserted.
	 * @param obj The object to be added.
	 * @param index The position in the array in which the object is to be
	 * inserted.
	 *
	 * @return The resulting array.
	 */
	public static Object insertObject(Object array, Object obj, int index) {

		int length = (array == null ? 0 : Array.getLength(array));
		if ( index < 0 || index > length ) {
			throw new ArrayIndexOutOfBoundsException( MsgObjectUtil.getMsgObject(MessageResource.BASE_NAME, MessageResource.INDEX_OUT_OF_BOUNDS,
																		String.valueOf( index ), String.valueOf( length )).localize() );
		}

		if ( array == null ) {

			// Create a new array with one element
			array = Array.newInstance(obj.getClass(), 1);
			// Add the new element
			Array.set(array, 0, obj);

		} else {

			// Create an array with room for one additional element.
			Object newArray = Array.newInstance(obj.getClass(), length+1);

			// Copy the portion before the element to be inserted
			if ( index > 0 ) {
				System.arraycopy(array, 0, newArray, 0, index);
			}

			// Add the element to be inserted
			Array.set(newArray, index, obj);

			// Copy the portion after the element to be inserted
			int next = index + 1;
			if ( next < (length+1) ) {
				System.arraycopy(array, index, newArray, next, length-index);
			}

			array = newArray;
		}

		return array;

	} // End insertObject

	/**
	 * Removes an object from an array.  The object is found using the equals
	 * method.
	 *
	 * @param array The array from which the object is to be removed.
	 * @param obj The object to be removed.
	 *
	 * @return The resulting array.
	 */
	public static Object removeObject(Object array, Object obj) {

		if ( array != null ) {

			// Loop through the array elements
			int length = Array.getLength(array);
			for (int i=0; i<length; i++) {

				// See if this element matches
				Object elem = Array.get(array, i);
				if ( elem.equals(obj) ) {

					// Remove this element by index
					return removeIndex(array, i);
				}
			}
		}

		return array;

	} // End removeObject

	/**
	 * Removes an object at a specified index from an array.
	 *
	 * @param array The array from which the object is to be removed.
	 * @param index The index of the object to be removed.
	 *
	 * @return The resulting array.
	 */
	public static Object removeIndex(Object array, int index) {

		int length = (array == null ? 0 : Array.getLength(array));
		if ( index < 0 || index >= length ) {
			throw new ArrayIndexOutOfBoundsException( MsgObjectUtil.getMsgObject(MessageResource.BASE_NAME, MessageResource.INDEX_OUT_OF_BOUNDS,
																		String.valueOf( index ), String.valueOf( length )).localize() );
		}

		if ( array == null ) return null;

		Object newArray = Array.newInstance(array.getClass().getComponentType(),
											length-1);

		// Copy the portion before the element to be removed
		if ( index > 0 ) {
			System.arraycopy(array, 0, newArray, 0, index);
		}

		// Copy the portion after the element to be removed
		int next = index + 1;
		if ( next < length ) {
			System.arraycopy(array, next, newArray, index, length-next);
		}

		return newArray;

	} // End removeIndex

	/**
	 * adds a boolean to the end of an array.
	 *
	 * @param array the array to which the boolean is to be added.
	 * @param obj the boolean to be added.
	 *
	 * @return the resulting array.
	 */
	public static boolean[] addObject(boolean[] array, boolean obj) {

		if ( array == null ) {

			// Create a new array with one element
			array = new boolean[1];
			// Add the new element
			array[0] = obj;

		} else {

			// Create an array with room for one additional element.
			int length = array.length;
			boolean[] newArray = new boolean[length+1];

			// Copy the elements from the old array to the new array.
			if ( length > 0 ) {
				System.arraycopy(array, 0, newArray, 0, length);
			}
			array = newArray;

			// Add the new element
			array[length] = obj;
		}

		return array;

	} // End addObject

	/**
	 * Inserts a boolean in an array.
	 *
	 * @param array The array into which the boolean is to be inserted.
	 * @param obj The boolean to be added.
	 * @param index The position in the array in which the boolean is to be
	 * inserted.
	 *
	 * @return The resulting array.
	 */
	public static boolean[] insertObject(boolean[] array, boolean obj,
										 int index)
	{
		int length = (array == null ? 0 : array.length);
		if ( index < 0 || index > length ) {
			throw new ArrayIndexOutOfBoundsException( MsgObjectUtil.getMsgObject(MessageResource.BASE_NAME, MessageResource.INDEX_OUT_OF_BOUNDS,
																		String.valueOf( index ), String.valueOf( length )).localize() );
		}

		if ( array == null ) {

			// Create a new array with one element
			array = new boolean[1];
			// Add the new element
			array[0] = obj;

		} else {

			// Create an array with room for one additional element.
			boolean[] newArray = new boolean[length+1];

			// Copy the portion before the element to be inserted
			if ( index > 0 ) {
				System.arraycopy(array, 0, newArray, 0, index);
			}

			// Add the element to be inserted
			newArray[index] = obj;

			// Copy the portion after the element to be inserted
			int next = index + 1;
			if ( next < (length+1) ) {
				System.arraycopy(array, index, newArray, next, length-index);
			}

			array = newArray;
		}

		return array;

	} // End insertObject

	/**
	 * adds an integer to the end of an array.
	 *
	 * @param array the array to which the integer is to be added.
	 * @param obj the integer to be added.
	 *
	 * @return the resulting array.
	 */
	public static int[] addObject(int[] array, int obj) {

		if ( array == null ) {

			// Create a new array with one element
			array = new int[1];
			// Add the new element
			array[0] = obj;

		} else {

			// Create an array with room for one additional element.
			int length = array.length;
			int[] newArray = new int[length+1];

			// Copy the elements from the old array to the new array.
			if ( length > 0 ) {
				System.arraycopy(array, 0, newArray, 0, length);
			}
			array = newArray;

			// Add the new element
			array[length] = obj;
		}

		return array;

	} // End addObject

	/**
	 * Inserts an integer in an array.
	 *
	 * @param array The array into which the integer is to be inserted.
	 * @param obj The integer to be added.
	 * @param index The position in the array in which the integer is to be
	 * inserted.
	 *
	 * @return The resulting array.
	 */
	public static int[] insertObject(int[] array, int obj, int index)
	{
		int length = (array == null ? 0 : array.length);
		if ( index < 0 || index > length ) {
			throw new ArrayIndexOutOfBoundsException( MsgObjectUtil.getMsgObject(MessageResource.BASE_NAME, MessageResource.INDEX_OUT_OF_BOUNDS,
																		String.valueOf( index ), String.valueOf( length )).localize() );
		}

		if ( array == null ) {

			// Create a new array with one element
			array = new int[1];
			// Add the new element
			array[0] = obj;

		} else {

			// Create an array with room for one additional element.
			int[] newArray = new int[length+1];

			// Copy the portion before the element to be inserted
			if ( index > 0 ) {
				System.arraycopy(array, 0, newArray, 0, index);
			}

			// Add the element to be inserted
			newArray[index] = obj;

			// Copy the portion after the element to be inserted
			int next = index + 1;
			if ( next < (length+1) ) {
				System.arraycopy(array, index, newArray, next, length-index);
			}

			array = newArray;
		}

		return array;

	} // End insertObject

	/**
	 * Removes an integer from an array.  The integer is found using the equals
	 * method.
	 *
	 * @param array The array from which the integer is to be removed.
	 * @param obj The integer to be removed.
	 *
	 * @return The resulting array.
	 */
	public static int[] removeObject(int[] array, int obj) {

		if ( array != null ) {

			// Loop through the array elements
			for (int i=0; i<array.length; i++) {

				// See if this element matches
				int elem = array[i];
				if ( elem == obj ) {

					// Remove this element by index
					return (int[])removeIndex(array, i);
				}
			}
		}

		return array;

	} // End removeObject

	/**
	 * Adds a long to the end of an array.
	 *
	 * @param array The array to which the long is to be added.
	 * @param obj The long to be added.
	 *
	 * @return The resulting array.
	 */
	public static long[] addObject(long[] array, long obj) {

		if ( array == null ) {

			// Create a new array with one element
			array = new long[1];
			// Add the new element
			array[0] = obj;

		} else {

			// Create an array with room for one additional element.
			int length = array.length;
			long[] newArray = new long[length+1];

			// Copy the elements from the old array to the new array.
			if ( length > 0 ) {
				System.arraycopy(array, 0, newArray, 0, length);
			}
			array = newArray;

			// Add the new element
			array[length] = obj;
		}

		return array;

	} // End addObject

	/**
	 * Inserts a long in an array.
	 *
	 * @param array The array into which the long is to be inserted.
	 * @param obj The long to be added.
	 * @param index The position in the array in which the long is to be
	 * inserted.
	 *
	 * @return The resulting array.
	 */
	public static long[] insertObject(long[] array, long obj, int index)
	{
		int length = (array == null ? 0 : array.length);
		if ( index < 0 || index > length ) {
			throw new ArrayIndexOutOfBoundsException( MsgObjectUtil.getMsgObject(MessageResource.BASE_NAME, MessageResource.INDEX_OUT_OF_BOUNDS,
																		String.valueOf( index ), String.valueOf( length )).localize() );
		}

		if ( array == null ) {

			// Create a new array with one element
			array = new long[1];
			// Add the new element
			array[0] = obj;

		} else {

			// Create an array with room for one additional element.
			long[] newArray = new long[length+1];

			// Copy the portion before the element to be inserted
			if ( index > 0 ) {
				System.arraycopy(array, 0, newArray, 0, index);
			}

			// Add the element to be inserted
			newArray[index] = obj;

			// Copy the portion after the element to be inserted
			int next = index + 1;
			if ( next < (length+1) ) {
				System.arraycopy(array, index, newArray, next, length-index);
			}

			array = newArray;
		}

		return array;

	} // End insertObject

	/**
	 * Removes a long from an array.  The long is found using the equals
	 * method.
	 *
	 * @param array The array from which the long is to be removed.
	 * @param obj The long to be removed.
	 *
	 * @return The resulting array.
	 */
	public static long[] removeObject(long[] array, long obj) {

		if ( array != null ) {

			// Loop through the array elements
			for (int i=0; i<array.length; i++) {

				// See if this element matches
				long elem = array[i];
				if ( elem == obj ) {

					// Remove this element by index
					return (long[])removeIndex(array, i);
				}
			}
		}

		return array;

	} // End removeObject

	/**
	 * Adds a float to the end of an array.
	 *
	 * @param array The array to which the float is to be added.
	 * @param obj The float to be added.
	 *
	 * @return The resulting array.
	 */
	public static float[] addObject(float[] array, float obj) {

		if ( array == null ) {

			// Create a new array with one element
			array = new float[1];
			// Add the new element
			array[0] = obj;

		} else {

			// Create an array with room for one additional element.
			int length = array.length;
			float[] newArray = new float[length+1];

			// Copy the elements from the old array to the new array.
			if ( length > 0 ) {
				System.arraycopy(array, 0, newArray, 0, length);
			}
			array = newArray;

			// Add the new element
			array[length] = obj;
		}

		return array;

	} // End addObject

	/**
	 * Inserts a float in an array.
	 *
	 * @param array The array into which the float is to be inserted.
	 * @param obj The float to be added.
	 * @param index The position in the array in which the float is to be
	 * inserted.
	 *
	 * @return The resulting array.
	 */
	public static float[] insertObject(float[] array, float obj, int index)
	{
		int length = (array == null ? 0 : array.length);
		if ( index < 0 || index > length ) {
			throw new ArrayIndexOutOfBoundsException( MsgObjectUtil.getMsgObject(MessageResource.BASE_NAME, MessageResource.INDEX_OUT_OF_BOUNDS,
																		String.valueOf( index ), String.valueOf( length )).localize() );
		}

		if ( array == null ) {

			// Create a new array with one element
			array = new float[1];
			// Add the new element
			array[0] = obj;

		} else {

			// Create an array with room for one additional element.
			float[] newArray = new float[length+1];

			// Copy the portion before the element to be inserted
			if ( index > 0 ) {
				System.arraycopy(array, 0, newArray, 0, index);
			}

			// Add the element to be inserted
			newArray[index] = obj;

			// Copy the portion after the element to be inserted
			int next = index + 1;
			if ( next < (length+1) ) {
				System.arraycopy(array, index, newArray, next, length-index);
			}

			array = newArray;
		}

		return array;

	} // End insertObject

	/**
	 * Removes a float from an array.  The float is found using the equals
	 * method.
	 *
	 * @param array The array from which the float is to be removed.
	 * @param obj The float to be removed.
	 *
	 * @return The resulting array.
	 */
	public static float[] removeObject(float[] array, float obj) {

		if ( array != null ) {

			// Loop through the array elements
			for (int i=0; i<array.length; i++) {

				// See if this element matches
				float elem = array[i];
				if ( elem == obj ) {

					// Remove this element by index
					return (float[])removeIndex(array, i);
				}
			}
		}

		return array;

	} // End removeObject

	/**
	 * Returns a string representation of the array object passed in.  We'll
	 * print up to printCount element values.  For elements after that, we just
	 * print "n more...".  For example, if the array contained the names of
	 * the days of the week and the printCount was 2, the returned String
	 * would look like this: "[Monday,Tuesday,(5 more...)]
	 *
	 * @param array The array object whose string representation is to
	 * be returned.
	 * @param printCount The maximum number of element values to be printed.
	 *
	 * @return A String describing the value.
	 * @vgnInternal
	 */
	public static String toString(Object array, int printCount) {

		if ( array == null ) return "null";

		// Print the elements, up to printCount
		int len = Array.getLength(array);
		if ( len == 0 ) return "[]";

		if ( printCount <= 0 ) return "[(" + len + " items...)]";

		// At this point we know we have at least one and we're printing
		// at least one.
		int count = Math.min(len, printCount);
		StringBuilder buf = new StringBuilder(128 + count << 7);
		buf.append('[');
		for (int i=0; i<count; i++) {
			buf.append(Array.get(array, i)).append(',');
		}
		if (count > 0) {
			// remove trailing comma
			buf.deleteCharAt(buf.length() - 1);
		}

		// Print the number of remaining, unprinted elements.
		if (count < len) {
			buf.append(",(" + (len-count) + " more...)");
		}

		buf.append(']');
		return buf.toString();

	} // End toString

	/**
	 * Returns the byte array as a printable string using the provided separator
	 * to separate the elements.
	 *
	 * @param separator the separator string.
	 * @param array the array.
	 * @return a printable string using the provided separator
	 * to separate the elements.
	 */
	public static String printableString(String separator, byte[] array) {
		if (array == null) return null;
		Byte[] list = new Byte[array.length];
		for (int i = 0 ; i < array.length ; i++) {
			list[i] = array[i];
		}
		return printableString(separator, list);
	}

	/**
	 * Returns the integer array as a printable string using the provided separator
	 * to separate the elements.
	 *
	 * @param separator the separator string.
	 * @param array the array.
	 * @return a printable string using the provided separator
	 * to separate the elements.
	 */
	public static String printableString(String separator, int[] array) {
		if (array == null) return null;
		Integer[] list = new Integer[array.length];
		for (int i = 0 ; i < array.length ; i++) {
			list[i] = array[i];
		}
		return printableString(separator, list);
	}

	/**
	 * Returns the array as a printable string using the provided separator
	 * to separate the elements.
	 *
	 * @param separator the separator string.
	 * @param array the array.
	 * @return a printable string using the provided separator
	 * to separate the elements.
	 */
	public static <T> String printableString(String separator, T... array) {
		if (array == null) return null;
		if (array.length == 0) return "";
		StringBuilder strBld = new StringBuilder(array.length);
		for (T ele : array) {
			strBld.append(ele).append(separator);
		}
		strBld.deleteCharAt(strBld.lastIndexOf(separator));
		return strBld.toString();
	}
} // End ArrayUtil
