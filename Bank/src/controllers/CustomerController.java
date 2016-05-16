package controllers;

import interfaces.CustomerControllerInterface;
import managers.AccountManagement;
import managers.TransactionManagement;

public class CustomerController implements CustomerControllerInterface{
	private TransactionManagement transactionManager = new TransactionManagement();
	private AccountManagement accountManager = new AccountManagement();

	@Override
	public Integer connect(int userID, int userPass, String type) throws Exception {
		// TODO create connection
		return null;
	}
	
	/*
	 * send Mor the accounts of a customer
	 * no handling of connection
	 * new customerConnection that mor does
	 * running method from the object
	 * account - only one
	 * empty constructor
	 */

	@Override
	public void transactionOut(int sourceAccountNumber, int targetAccountNumber, int amount) throws Exception {
		//check if target account internal in front of accountManager
		//if yes, make internal transaction
		//if not, withdraw from source account 
		//& connect to external system and then make transaction to external
		// TODO Roll down to account manager & outer system

	}

	@Override
	public void requestLoan(int accountID, int amount) throws Exception {
		// TODO Roll down to account manager
	}

	@Override
	public void createDeposit(int accountID, int amount) throws Exception {
		// TODO Roll down to account manager
	}

}
