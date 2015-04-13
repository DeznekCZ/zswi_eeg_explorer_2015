package cz.eeg.data;


/**
 * Třída reprezentující objekt jednoho kanálu (elektrody)*/
public class Channel {
	private String channelNumber;
	private String name;
	private double resolutionInUnit;
	private String unit;
	
	public Channel(String inputLine){
		String [] pole = inputLine.split("=",2);
		channelNumber=pole[0];
		pole = pole[1].split(",",4);
		name=pole[0];
		resolutionInUnit=Double.parseDouble(pole[2]);
		unit=pole[3];
	}
	
	@Override
	public String toString() {
		return (channelNumber + "=" + name+ ",," + resolutionInUnit + "," + unit);
		
	}

	
	
}
