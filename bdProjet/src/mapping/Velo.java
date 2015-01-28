package mapping;

public class Velo {
	private int numRFID;
	private String etat;
	
	public Velo(int numRFID, String etat){
		this.setNumRFID(numRFID);
		this.setEtat(etat);
	}

	public int getNumRFID() {
		return numRFID;
	}

	public void setNumRFID(int numRFID) {
		this.numRFID = numRFID;
	}

	public String getEtat() {
		return etat;
	}

	public void setEtat(String etat) {
		this.etat = etat;
	}
	
}
