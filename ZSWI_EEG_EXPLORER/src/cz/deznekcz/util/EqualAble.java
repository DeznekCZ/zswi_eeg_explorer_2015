package cz.deznekcz.util;

/**
 * Every class adding to {@link EqualArrayList} must implements
 * this Interface
 * @author Zdeněk Novotný (DeznekCZ)
 * @version 1.0.1
 */
public interface EqualAble {
	
	/**
	 * Must be reimplemented<br>
	 * <br>Example:<br>
	 * <code>
	 * boolean equalsTo(Object object)<br>
	 * {<br>
	 * &nbsp;return<br>
	 * &nbsp;&nbsp;&nbsp;(object instanceof ThisClass<br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&& equalsTo((ThisClass) object))<br>
	 * &nbsp;||<br>
	 * &nbsp;&nbsp;&nbsp;(object instanceof DiffClass<br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&& equalsTo((DiffClass) object))<br>
	 * &nbsp;||<br>
	 * &nbsp;&nbsp;...
	 * }<br>
	 * </code>
	 * <br><br>
	 * Below must be implemented method {@code equalsTo(ThisClass otherInstance)}
	 * and  {@code equalsTo(DiffClass diffClassInstance)}
	 * @param obj
	 * @return returns result of equal method
	 */
	public boolean equalsTo(Object obj);
}
