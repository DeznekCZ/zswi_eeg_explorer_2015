package cz.deznekcz.util;

import java.util.ArrayList;
import java.util.List;


/**
 * Class extends an {@link ArrayList} class.
 * This class changes only method {@link #indexOf(Object)}.
 * In default {@link ArrayList} is index searched by 
 * <code>object.equals(ELEMENT[i])</code>,
 * but this type of {@link List} uses 
 * <code>ELEMENT[i].equalsTo(object)</code>. That method
 * must be implemented in each class stored in {@link EqualArrayList}
 * 
 * @author Zdeněk Novotný (DeznekCZ)
 * @version 1.0.1
 *
 * @param <T> Element class implements {@link EqualAble}
 * 
 * @see ArrayList
 * @see List
 * @see #indexOf(Object)
 * @see #lastIndexOf(Object)
 */
public class EqualArrayList<T extends EqualAble> extends ArrayList<T> {
	
	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * Method returns first index of element {@link T} in {@link EqualArrayList}.
	 * <br>{@link EqualAble} inteface implemeted to {@link T} allow
	 * send more type of objects.
	 * 
	 * @param object instance of any {@link Object} to compare
	 * @see #lastIndexOf(Object)
	 * @see #contains(Object)
	 */
	public int indexOf(Object object) {
		if (object == null) {
	        for (int i = 0; i < size(); i++)
	            if (get(i)==null)
	                return i;
	    } else {
			for (int i = 0; i < size(); i++)
				if (get(i).equalsTo(object))
					return i;
	    }
		return -1;
	}
	
	/**
	 * Method returns last index of element {@link T} in {@link EqualArrayList}.
	 * <br>{@link EqualAble} inteface implemeted to {@link T} allow
	 * send more type of objects.
	 * 
	 * @param object instance of any {@link Object} to compare
	 * @see #indexOf(Object)
	 * @see #contains(Object)
	 */
	public int lastIndexOf(Object object) {
		if (object == null) {
			for (int i = size()-1; i >= 0; i--)
                if (get(i)==null)
                    return i;
        } else {
        	for (int i = size() - 1; i > -1; i++)
				if (get(i).equalsTo(object))
					return i;
        }
		return -1;
	}
	
	/**
	 * Method returns true, if {@link #indexOf(Object)} returns {@link Integer}
	 * greater than <code>-1</code>.
	 * 
	 * @param object instance of any {@link Object} to compare
	 * @see #indexOf(Object)
	 * @see #lastIndexOf(Object)
	 */
	public boolean contains(Object object) {
		return indexOf(object) > -1;
	}
}
