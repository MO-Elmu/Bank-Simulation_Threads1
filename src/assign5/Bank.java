package assign5;
import java.io.*;
import java.util.Map.Entry;
import java.util.concurrent.*;

public class Bank {
	
	private ConcurrentMap<Long, Account> accounts;
	private BlockingQueue<Transaction> transQ;
	private static final int QSIZE = 20; 
	private static final int NUM_OF_ACCOUNTS = 20;
	private final Transaction nullTrans = new Transaction(-1,0,0);
	
	
	public Bank() {
		accounts = new ConcurrentHashMap<>(NUM_OF_ACCOUNTS);
		transQ = new ArrayBlockingQueue<Transaction>(QSIZE);
		initializeAccounts(NUM_OF_ACCOUNTS);
	}
	
	//Store a new account in the accounts list
	public void addAccount(Account acc) {
		accounts.putIfAbsent(acc.getIdNumber(), acc);
		
	}
	
	private void initializeAccounts(int numOfaccnts) {
		for (int i=0; i<numOfaccnts; i++) {
			Account acc = new Account(i, 1000);
			this.addAccount(acc);
		}
	}
	
	public void fillQ (String fileName, int numberOfWorkers) {
		
		try {
			File transactions = new File(this.getClass().getClassLoader().getResource(fileName).getFile());
			BufferedReader br = new BufferedReader(new FileReader(transactions));
			String line;
			while((line = br.readLine()) != null) {
				Transaction trans = new Transaction(line);
				transQ.put(trans);
			}
			br.close();
			for(int i=0; i<numberOfWorkers; i++) {
				transQ.put(nullTrans);
			}
		} catch (FileNotFoundException e1) {
			System.err.println("File not found try a different file");
			e1.printStackTrace();
			System.out.println("Quitting ......\nExited" );
			System.exit(-1);
		} catch (IOException e2) {
			System.err.println("Can't Read data from the file");
			e2.printStackTrace();
			System.out.println("Quitting ......\nExited" );
			System.exit(-2);
		} catch (InterruptedException e3) {
			System.err.println("Main thread interrupted while reading data from File to the Queue");
			e3.printStackTrace();
			System.out.println("Quitting ......\nExited" );
			System.exit(-3);
		} catch (NullPointerException e4) {
			System.err.println("File can not be located OR wrong path");
			e4.printStackTrace();
			System.out.println("Quitting ......\nExited" );
			System.exit(-4);
		}
		
		
	}
	
	public void printAccounts() {
		
		for (Entry<Long, Account> e : accounts.entrySet()) {
			Account acc = e.getValue();
			System.out.println(acc);
		}
	}
	

	public static void main(String[] args) {
		if(args.length != 2) {
			System.err.println("ERROR : Wrong number of input parameters \n");
			System.out.println("2 arguments required");
			System.out.println("Quitting ......\nExited" );
			return;
		}
		String fileName = args[0];
		int numOfWorkers = Integer.valueOf(args[1]);
		Bank bank = new Bank();
		CountDownLatch latch = new CountDownLatch(numOfWorkers);
		for(int i=0; i<numOfWorkers; i++) {
			Worker w = bank.new Worker(latch);
			w.start();
		}
		
		bank.fillQ(fileName, numOfWorkers);

		try {
			latch.await();
		} catch (InterruptedException e) {
			System.err.println("Main thread interrupted before other threads can finish");
			e.printStackTrace();
			System.exit(0);
		}  
		
		//System.out.println("All Done");
		bank.printAccounts();
		
		
	}
	
	class Worker extends Thread{
		private final CountDownLatch latch;
		public Worker(CountDownLatch latch) {
			this.latch = latch;
		}
		public void run() {
			try {
				while(true) {
					Transaction trans = transQ.take();
					if(trans.fromAccount == -1 && trans.toAccount == 0 && trans.moneyAmount == 0) {
						latch.countDown();
						return;
					}
					accounts.get(trans.fromAccount).withdraw(trans.moneyAmount);
					accounts.get(trans.fromAccount).incrementTransaction();
					accounts.get(trans.toAccount).deposit(trans.moneyAmount);
					accounts.get(trans.toAccount).incrementTransaction();
				}
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
		}
	}

}
