package cz.deznekcz.util;

/**
 * An example class for {@link EqualArrayList}
 * Example now uses method Overloading. A creator
 * of comparing can automatically test what he needs.
 * 
 * @author Zdeněk Novotný (DeznekCZ)
 * @version 1.0.0
 */
public class EAL_Integer implements EqualAble {
	
	private int value;

	/**
	 * Integer that equals to 0 or "0";
	 */
	public EAL_Integer() {
		this.value = 0;
	}
	
	/**
	 * Integer from value
	 * @param i value
	 */
	public EAL_Integer(int i) {
		this.value = i;
	}
	
	/**
	 * Integer from parse able string
	 * @param i parse able string
	 */
	public EAL_Integer(String i) {
		this.value = Integer.parseInt(i);
	}
	
	// EQUAL PART
	
	/**
	 * Returns result of method override
	 * @param obj instance of {@link Object}
	 * @see #equalsTo(EAL_Integer)
	 * @see #equalsTo(int)
	 * @see #equalsTo(Integer)
	 * @see #equalsTo(String)
	 */
	public boolean equalsTo(Object obj) {
		// INSTANCE OF SAME CLASS
		return  (obj instanceof EAL_Integer 
			    	 && equalsTo((EAL_Integer) obj))
		// INSTANCE OF INTEGER
			 || (obj instanceof Integer 
					 && equalsTo((Integer) obj))
		// INSTANCE OF PARSE-ABLE STRING
			 || (obj instanceof String 
					 && equalsTo((String) obj));			
	}

	/**
	 * Compares value of this instance to parameter
	 * @param otherInstance {@link EAL_Integer} instance
	 * @return boolean result
	 * @see #equalsTo(int)
	 * @see #equalsTo(Integer)
	 * @see #equalsTo(Object)
	 * @see #equalsTo(String)
	 */
	public boolean equalsTo(EAL_Integer otherInstance) {
		return otherInstance.value == value ;
	}

	/**
	 * Compares value of this instance to parameter
	 * @param stringInteger {@link String} value
	 * @return boolean result
	 * @see #equalsTo(EAL_Integer)
	 * @see #equalsTo(int)
	 * @see #equalsTo(Integer)
	 * @see #equalsTo(Object)
	 * @throws NumberFormatException
	 */
	public boolean equalsTo(String stringInteger) {
		return Integer.parseInt(stringInteger) == value;
	}

	/**
	 * Compares value of this instance to parameter
	 * @param integer {@link Integer} value
	 * @return boolean result
	 * @see #equalsTo(EAL_Integer)
	 * @see #equalsTo(int)
	 * @see #equalsTo(Object)
	 * @see #equalsTo(String)
	 */
	public boolean equalsTo(Integer integer) {
		return integer.intValue() == value;
	}

	/**
	 * Compares value of this instance to parameter
	 * @param integer {@code int} value
	 * @return boolean result
	 * @see #equalsTo(EAL_Integer)
	 * @see #equalsTo(Integer)
	 * @see #equalsTo(Object)
	 * @see #equalsTo(String)
	 */
	public boolean equalsTo(int integer) {
		return integer == value;
	}
	
	
}
