package cz.eeg.data;


public class Marker {

	private String markerNumber;
	private String type;
	private String description;
	private String positionInDataPoints;
	private String sizeInDataPoints;
	private String channelNumber;

	public Marker(String line){

		String [] lineF=line.split("=");
		markerNumber=lineF[0];
		if (lineF[1].startsWith("Stimulus")) 
		{
			String [] lineF1=lineF[1].split(" ");
			type=lineF1[0];
			description=lineF1[1];
			lineF=lineF1[1].split(",");
			positionInDataPoints=lineF[0]+","+lineF[1];
			sizeInDataPoints=lineF[2];
			channelNumber=lineF[3];
		}
	}


}
