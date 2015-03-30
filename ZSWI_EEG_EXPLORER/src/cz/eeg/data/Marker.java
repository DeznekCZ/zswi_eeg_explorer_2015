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
		markerNumber=lineF[0].substring(2);
		String [] lineF1=lineF[1].split(",");
		type=lineF1[0];
		description=lineF1[1];
		positionInDataPoints=lineF1[2];
		sizeInDataPoints=lineF1[3];
		channelNumber=lineF1[4];
	}

	public String getMarkerNumber() {
		return markerNumber;
	}

	public String getType() {
		return type;
	}

	public String getDescription() {
		return description;
	}

	public String getPositionInDataPoints() {
		return positionInDataPoints;
	}

	public void setPositionInDataPoints(String positionInDataPoints) {
		this.positionInDataPoints = positionInDataPoints;
	}

	public String getSizeInDataPoints() {
		return sizeInDataPoints;
	}

	public void setSizeInDataPoints(String sizeInDataPoints) {
		this.sizeInDataPoints = sizeInDataPoints;
	}

	public String getChannelNumber() {
		return channelNumber;
	}

	public void setChannelNumber(String channelNumber) {
		this.channelNumber = channelNumber;
	}

	public void setMarkerNumber(String markerNumber) {
		this.markerNumber = markerNumber;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
