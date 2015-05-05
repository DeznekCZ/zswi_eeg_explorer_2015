package cz.deznekcz.reflect;

/**
 *     Instance of class {@link Out} represent a returnable parameter
 *     known as "<code><b>out</b> variableName</code>" from <b>C#</b>
 *     or as "<code>&variable</code>" from <b>C</b> or <b>C++</b>.
 * <br>
 * <br><b>Using in code:</b><code>
 * <br>Out&lt;String&gt; out = new Out&lt;String&gt;();
 * <br>method(out);
 * <br>String fromMethod = out.value();
 *     </code>
 * <br><b>Declared method:</b><code>
 * <br>void method(Out&lt;String&gt; out) {
 * <br>&nbsp;out.lock("myValue");
 * <br>}
 *     </code>
 *
 * @author Zdeněk Novotný (DeznekCZ)
 * @param Instance Class of sending instances
 */
public class Out<I> {

	/** ToString formating {@link String} */
	private static final String FORMAT = "Reference: <%s>";
	/** Referenced instance of {@link I} */
	private I instance;

	/**
	 * Constructor references an external instance
	 * @param defaultInstance instance of {@link I}
	 */
	public Out(I defaultInstance) {
		instance = defaultInstance;
	}
	
	/**
	 * Constructor without referenced instance
	 */
	public Out() {
	}

	/**
	 * Method sets an instance of {@link I}
	 * @param instance reference to instance
	 */
	public void lock(I instance) {
		this.instance = instance;
	}

	/**
	 * Method returns an instance of {@link I}
	 * @return reference to instance
	 */
	public I value() {
		return instance;
	}

	@Override
	public String toString() {
		return  instance == null
				? String.format(FORMAT, "null")
				: String.format(FORMAT, instance.toString()
		);
	}
	
	/**
	 * Method returns true if instance of {@link I}
	 * stored in this instance of {@link Out} equals to
	 * instance stored in parameter instance of {@link Out}.
	 * 
	 * @return <code>true</code> if instances equals else <code>false</code>
	 */
	public boolean equals(Object obj) {
		return  obj != null
			&&	obj instanceof Out<?>
			&&  instance != null
			&&  ((Out<?>) obj).instance != null
			&&  ((Out<?>) obj).instance.equals(instance);
	}
}
