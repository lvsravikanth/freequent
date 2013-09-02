package com.scalar.freequent.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * The <code>CollectionsUtil</code> class provides utility functions for working with collections.
 *
 * User: Sujan Kumar Suppala
 * Date: Aug 31, 2013
 * Time: 12:58:15 PM
 */
public final class CollectionsUtil {
    protected static final Log logger = LogFactory.getLog(CollectionsUtil.class);
	/**
	 * Constructs a new <code>StringUtil</code>. This is the default constructor and is unavailable to others.
	 */
	private CollectionsUtil() {
	}

	/**
	 * Converts an unknown <code>Map</code> to the specified key and value types.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param map the <code>Map</code> to convert
	 * @param keyClass the key <code>Class</code>
	 * @param valueClass the value <code>Class</code>
	 * @return the converted <code>Map</code>
	 */
	public static <K, V> Map<K, V> convert(Map<?, ?> map, Class<K> keyClass, Class<V> valueClass) {
		Map<K, V> convert = new HashMap<K, V>();

		for ( Map.Entry<?, ?> entry : map.entrySet() ) {
			Object entryKey = entry.getKey();
			Object entryValue = entry.getValue();

			K key = keyClass.isInstance(entryKey) ? keyClass.cast(entryKey) : null;
			V value = valueClass.isInstance(entryValue) ? valueClass.cast(entryValue) : null;
			convert.put(key, value);
		}

		return convert;
	}

	/**
	 * Converts an unknown <code>Map</code> to the specified key and value types. This is unchecked but faster than
	 * {@link #convert(Map, Class, Class)}.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param map the <code>Map</code> to convert
	 * @return the converted <code>Map</code>
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> uncheckedConvert(Map<?, ?> map) {
		return Map.class.cast(map);
	}

	/**
	 * Converts an unknown <code>List</code> to the specified type.
	 *
	 * @param <T> the type
	 * @param list the <code>List</code> to convert
	 * @param typeClass the type <code>Class</code>
	 * @return the converted <code>List</code>
	 */
	public static <T> List<T> convert(List<?> list, Class<T> typeClass) {
		List<T> convert = new ArrayList<T>();

		for ( Object object : list ) {
			T type = typeClass.isInstance(object) ? typeClass.cast(object) : null;
			convert.add(type);
		}

		return convert;
	}

	/**
	 * Converts an unknown <code>List</code> to the specified type. This is unchecked but faster than
	 * {@link #convert(List, Class)}.
	 *
	 * @param <T> the type
	 * @param list the <code>List</code> to convert
	 * @return the converted <code>List</code>
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> uncheckedConvert(List<?> list) {
		return List.class.cast(list);
	}

	/**
	 * Converts an unknown <code>Collection</code> to the specified type.
	 *
	 * @param <T> the type
	 * @param collection the <code>Collection</code> to convert
	 * @param typeClass the type <code>Class</code>
	 * @return the converted <code>List</code>
	 */
	public static <T> Collection<T> convert(Collection<?> collection, Class<T> typeClass) {
		List<T> convert = new ArrayList<T>();

		for ( Object object : collection ) {
			T type = typeClass.isInstance(object) ? typeClass.cast(object) : null;
			convert.add(type);
		}

		return convert;
	}

	/**
	 * Converts an unknown <code>Collection</code> to the specified type. This is unchecked but faster than
	 * {@link #convert(Collection, Class)}.
	 *
	 * @param <T> the type
	 * @param collection the <code>Collection</code> to convert
	 * @return the converted <code>Collection</code>
	 */
	@SuppressWarnings("unchecked")
	public static <T> Collection<T> uncheckedConvert(Collection<?> collection) {
		return Collection.class.cast(collection);
	}

	/**
	 * Converts an unknown <code>Enumeration</code> to the specified type. This is unchecked.
	 *
	 * @param <T> the type
	 * @param enumeration the <code>Enumeration</code> to convert
	 * @return the converted <code>Enumeration</code>
	 */
	@SuppressWarnings("unchecked")
	public static <T> Enumeration<T> uncheckedConvert(Enumeration<?> enumeration) {
		return Enumeration.class.cast(enumeration);
	}


	/**
	 * Sorts an unknown class using the specified <code>Comparator</code>. This is unchecked.
	 *
	 * @param t <T> the array of objects for sorting
	 * @param c the <code>Comparator</code> used for sorting
	 */
	@SuppressWarnings("unchecked")
	public static <T> void uncheckedSort(T[] t, Comparator c) {
		try {
			java.util.Arrays.sort(t, c.getClass().newInstance());
		} catch (InstantiationException e) {
			// fail, depart
		} catch (IllegalAccessException e) {
			// fail, depart
		}
	}

	/**
	 * Returns the <code>String</code> value in the <code>Map</code> for the provided key. If the value is
	 * <code>null</code>, an empty <code>String</code> is returned.
	 *
	 * @param map the <code>Map<code>
	 * @param key the key
	 * @return the <code>String</code> value if not <code>null</code>; otherwise empty <code>String</code>
	 */
	public static String getNonNullString(Map<String, ?> map, String key) {
		return StringUtil.nonNull(map.get(key));
	}

	/**
	 * Returns the <code>boolean</code> value in the <code>Map</code> for the provided key. If the value is
	 * <code>null</code>, the default value is returned.
	 *
	 * @param map the <code>Map<code>
	 * @param key the key
	 * @param defaultValue the default value
	 * @return the <code>boolean</code> value if not <code>null</code>; otherwise the default value
	 */
	public static boolean getBoolean(Map<String, ?> map, String key, boolean defaultValue) {
		Object value = map.get(key);
		return (null == value) ? defaultValue : Boolean.parseBoolean(value.toString());
	}

	/**
	 * Returns the <code>int</code> value in the <code>Map</code> for the provided key. If the value is
	 * <code>null</code>, the default value is returned.
	 *
	 * @param map the <code>Map<code>
	 * @param key the key
	 * @param defaultValue the default value
	 * @return the <code>int</code> value if not <code>null</code>; otherwise the default value
	 */
	public static int getInt(Map<String, ?> map, String key, int defaultValue) {
		Object value = map.get(key);
		return (null == value) ? defaultValue : Integer.parseInt(value.toString());
	}

	/**
	 * Returns a <code>String</code> that is a list of the values in the given list separated by the given
	 * separator.
	 *
	 * @param list the list to convert
	 * @param separator the separator
	 * @return a <code>String</code> that is the list of the values
	 */
	public static String toString(List<?> list, String separator) {
		int last = list.size() - 1;
		if ( last < 0 ) {
			return "";
		}

		StringBuilder builder = new StringBuilder();
		for ( int i = 0 ;  ; ++i ) {
			builder.append(list.get(i).toString());
			if ( i == last ) {
				return builder.toString();
			}

			builder.append(separator);
		}
	}
}

