# Bank-Simulation_Threads1
This small project actually has two parts the first part is
### 1- Bank Simulation
Our bank will track the balances in twenty different accounts. When the program begins, each of the accounts contains $1000. The program will process a list of transactions which transfer money between the accounts. Once all transactions have been processed the program will go through and for each account it will print both the account’s final balance and the number of transactions (deposits and withdrawals) which occurred on that account.

Our bank program will use the main thread to read the list of banking transactions from a file. A separate set of worker threads will process the transactions and update the accounts. Java’s BlockingQueue will handle most of the communication between the main thread and the worker threads.

Instead of using a GUI the program will run from the command line. When executing the program users will provide two pieces of information as parameters—the name of an external file listing all the transactions and the number of threads which will be used to process transactions. Here is an example showing a request to process the transactions in the file “small.txt” using four worker threads.

  >/> java assign4/Bank small.txt 4
  
 >>> acct:0 bal:999 trans:1 <br /> acct:1 bal:1001 trans:1 <br /> acct:2 bal:999 trans:1 <br /> acct:3 bal:1001 trans:1 <br /> acct:4 bal:999 trans:1 <br /> acct:5 bal:1001 trans:1 <br /> acct:6 bal:999 trans:1 <br /> acct:7 bal:1001 trans:1 <br /> acct:8 bal:999 trans:1 <br /> acct:9 bal:1001 trans:1 <br /> acct:10 bal:999 trans:1 <br /> acct:11 bal:1001 trans:1 <br /> acct:12 bal:999 trans:1 <br /> acct:13 bal:1001 trans:1 <br /> acct:14 bal:999 trans:1 <br /> acct:15 bal:1001 trans:1 <br /> acct:16 bal:999 trans:1 <br /> acct:17 bal:1001 trans:1 <br /> acct:18 bal:999 trans:1 <br /> acct:19 bal:1001 trans:1 <br />
 #### Details
 - ###### Account Class
       This class will store an id number, the current balance for the account, and the number of transactions 
       that have occurred on the account. Multiple worker threads may be accessing an account simultaneously and 
       we must ensure that they cannot corrupt its data. We will also override the toString method to handle 
       printing of account information.
