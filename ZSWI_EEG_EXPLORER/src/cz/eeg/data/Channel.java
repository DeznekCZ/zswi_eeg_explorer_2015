package cz.eeg.data;


/**
 * Class reprezenting one channel
 * */
public class Channel {
	private String channelNumber;
	

	private String name;
	private double resolutionInUnit;
	private String unit;
	
	/**
	 * Constructor
	 * @param inputLine line of one channel
	 * */
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

	public String getChannelNumber() {
		return channelNumber;
	}

	public String getName() {
		return name;
	}

	public double getResolutionInUnit() {
		return resolutionInUnit;
	}

	public String getUnit() {
		return unit;
	}
	
}
