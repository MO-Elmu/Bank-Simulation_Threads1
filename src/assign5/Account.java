package assign5;


public class Account {
	
	private final long idNumber;
	private double balance = 0.0;
	private long numOfTransactions;
	
	public Account(long id, double bal) {
		this.idNumber = id;
		setBalance(bal);
		setNumOfTransactions(0);
	}

	/**
	 * @return the idNumber
	 */
	public long getIdNumber() {
		return idNumber;
	}

	/**
	 * @return the balance
	 */
	public synchronized double getBalance() {
		return balance;
	}

	/**
	 * @param balance the balance to set
	 */
	private synchronized void setBalance(double balance) {
		this.balance = balance;
	}
	
	public synchronized void deposit (double money) {
		this.balance += money;
	}
	
	public synchronized void  withdraw(double money) {
		this.balance -= money;
		//TODO code if account is overdrawn
	}

	/**
	 * @return the numOfTransactions
	 */
	public synchronized long getNumOfTransactions() {
		return numOfTransactions;
	}

	/**
	 * @param numOfTransactions the numOfTransactions to set
	 */
	public synchronized void setNumOfTransactions(long numOfTransactions) {
		this.numOfTransactions = numOfTransactions;
	}
	public synchronized void incrementTransaction(){
		this.numOfTransactions += 1;
		
	}
	
	@Override
	public String toString() {
		String acc = "acct:" + this.idNumber + "  bal:" + String.format("%.0f",this.balance) + "  trans:" + this.numOfTransactions;
		
		return acc;
	}
	
	
	

}
