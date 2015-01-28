package mapping;

public class Client {
	private int numClient;
	private int codeSecret;
	private String numCB;
	
	public Client(int numClient, int codeSecret, String numCB){
		this.codeSecret = codeSecret;
		this.numCB = numCB;
		this.numClient = numClient;
	}
	public int getNumClient() {
		return numClient;
	}
	public void setNumClient(int numClient) {
		this.numClient = numClient;
	}
	public int getCodeSecret() {
		return codeSecret;
	}
	public void setCodeSecret(int codeSecret) {
		this.codeSecret = codeSecret;
	}
	public String getNumCB() {
		return numCB;
	}
	public void setNumCB(String numCB) {
		this.numCB = numCB;
	}
}
