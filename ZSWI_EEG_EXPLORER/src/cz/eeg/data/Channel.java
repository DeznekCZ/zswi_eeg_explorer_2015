package cz.eeg.data;


/**
 * Třída reprezentující objekt jednoho kanálu (elektrody)*/
public class Channel {
	private String channelNumber;
	private String name;
	private double resolutionInUnit;
	private String unit;
	
	public Channel(String vstup){
		String [] pole=rozdel("=",vstup);
		channelNumber=pole[0];
		pole=rozdel(",", pole[1]);
		name=pole[0];
		resolutionInUnit=Double.parseDouble(pole[2]);
		unit=pole[3];
	}
	
	@Override
	public String toString() {
		return (channelNumber + "=" + name+ ",," + resolutionInUnit + "," + unit);
		
	}

	public String[]  rozdel(String delic,String vstup){
		return vstup.split(delic);}
	
}
