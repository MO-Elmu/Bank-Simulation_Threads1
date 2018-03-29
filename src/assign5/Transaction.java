package assign5;
import java.util.StringTokenizer;

// An immutable class
public final class Transaction {
	
	public final long fromAccount;
	public final long toAccount;
	public final double moneyAmount;
	
	public Transaction (long fromAccount, long toAccount, double moneyAmount) {
		this.fromAccount = fromAccount;
		this.toAccount = toAccount;
		this.moneyAmount = moneyAmount;
	}
	
	public Transaction (String s) {
		StringTokenizer tok = new StringTokenizer(s);
		this.fromAccount = Long.parseLong(tok.nextToken());
		this.toAccount = Long.parseLong(tok.nextToken());
		this.moneyAmount = Double.parseDouble(tok.nextToken());		
	}
	
	

}
