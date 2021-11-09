package model;


public enum Cardinality {
	zeroToMany("Zero To Many", "[]", "[0..n]"),
	oneToMany("One To Many", "[+]", "[1..n]");
	
	private final String text;
	private final String symbol;
	private final String MPSsymbol;
	
	Cardinality(String text, String symbol,String MPSsymbol) {
		this.text = text;
		this.symbol = symbol;
		this.MPSsymbol = MPSsymbol;
	}
	public String getText() {
		return text;
	}
	public String getSymbol() {
		return symbol;
	}
	public String getMPSSymbol() {
		return MPSsymbol;
	}
	
}
