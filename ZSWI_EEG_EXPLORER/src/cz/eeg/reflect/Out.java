package cz.eeg.reflect;

/**
 *     Instance of class {@link Out} represent a returnable parameter
 *     known as "<code><b>out</b> variableName</code>" from <b>C#</b>
 *     or as "<code>&variable</code>" from <b>C</b> or <b>C++</b>.
 * <br>
 * <br><b>Using in code:</b><code>
 * <br>Out<String> out = new Out<>();
 * <br>method(out);
 * <br>String fromMethod = out.value();
 *     </code>
 * <br><b>Declared method:</b><code>
 * <br>void method(Out<String> out) {
 * <br>&nbsp;out.lock("myValue");
 * <br>}
 *     </code>
 *
 * @author IT Crowd
 * @param <I> Class of sending instances
 */
public class Out<I> {

	private I value;

	/**
	 * Method sets an instance of {@link I}
	 * @param instance reference to instance
	 */
	public void lock(I instance) {
		this.value = instance;
	}

	/**
	 * Method returns an instance of {@link I}
	 * @return reference to instance
	 */
	public I value() {
		return value;
	}

}
