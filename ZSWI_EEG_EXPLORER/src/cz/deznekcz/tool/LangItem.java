package cz.deznekcz.tool;

/**
 * Instances of class {@link LangItem} represents a {@link String}
 * value of an {@link Lang} item defined by specific symbol 
 * 
 * @author Zdeněk Novotný (DeznekCZ)
 * @version 2.0
 */
public class LangItem {
	private final String symbol;
	private String value;
	
	/**
	 * Constructor that creates a new instance of {@link LangItem}.
	 * Sets symbol by param and sets value to "&#60symbol&#62"
	 * @param symbol
	 */
	public LangItem(String symbol) {
		this.symbol = symbol;
		this.setValue("<"+symbol+">");
	}

	/**
	 * Method returns a value of {@link LangItem}.
	 * Value can contains formating symbols.
	 * @return {@link String} value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Method sets a value of {@link LangItem}.
	 * Value can contains formating symbols.
	 * @param value {@link String} value
	 * 
	 * @see String#format(String, Object...)
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Method returns a calling symbol of {@link LangItem}.
	 * @return {@link String} value
	 */
	public String getSymbol() {
		return symbol;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj != null 
				&& obj instanceof LangItem
				&& ((LangItem) obj).getSymbol().equals(this.getSymbol());
	}
}
