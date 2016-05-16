package entities;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Account {

	/* Update Object model to remove closeAccount entity and new fields */

	enum AccountStatus{Open, Restricted, Closed}; 

	private int accountID;
	private int numberOfActiveLoans;
	private int balance;
	private boolean isRestricted;
	private LocalDateTime timeOfOpening;
	private LocalDateTime timeOfClosing;
	private AccountStatus status;

	//To add relation table
	private CustomerDetails customer; //ok
	private ArrayList<Loan> loans;
	private Restriction restriction;
	private ArrayList<Deposit> deposits; //ok
	
	public Account(CustomerDetails customer){
		this.customer = customer;
		this.balance = 0;
		this.numberOfActiveLoans = 0;
		this.timeOfOpening = LocalDateTime.now();
		this.status = AccountStatus.Open;
		
		
		this.loans = new ArrayList<Loan>();
		this.deposits = new ArrayList<Deposit>();

		/* Account ID set after creation according to DB */
	}

	public void addLoan(Loan loan) throws Exception {
		synchronized (this) {
			loans.add(loan);
			numberOfActiveLoans++;
			withdrawOrDeposit(loan.getInitialAmount());
		}
	}

	public void returnLoan() throws Exception {
		synchronized (this) {
			if (numberOfActiveLoans == 0) 
				throw new Exception("No loans to return!");

			Loan loanToReturn = null;

			for (int i = 0; i < loans.size(); i++) { //We want to return the oldest loan possible
				Loan currentLoan = loans.get(i);
				if (currentLoan.isActive() && currentLoan.getLoanBalance() <= getAccountBalance()) {
					loanToReturn = currentLoan;
					break;
				}
			}

			if (loanToReturn == null) 
				throw new Exception("Insufficient funds to return a loan!");

			withdrawOrDeposit(loanToReturn.getLoanBalance()*-1);
			loanToReturn.setLoanBalance(0);
			loanToReturn.closeLoan();
			numberOfActiveLoans--;
		}
	}

	public void withdrawFromDeposit(int amountToWithdraw) throws Exception {
		synchronized (this) {
			int totalDepositedAmount = 0;
			int numberOfDesposits = deposits.size();
			for (int i = 0; i < numberOfDesposits; i++)
				totalDepositedAmount += deposits.get(i).getDepositBalance();

			if (totalDepositedAmount < amountToWithdraw) 
				throw new Exception("Insufficient funds in deposits to withdraw this amount!");

			int amountLeftToWithdraw = amountToWithdraw;

			while (amountLeftToWithdraw > 0) { 
				Deposit currentDeposit = deposits.get(numberOfDesposits-1); //We want to withdraw from newest deposit first
				if (amountLeftToWithdraw >= currentDeposit.getDepositBalance()) {
					amountLeftToWithdraw -= currentDeposit.getDepositBalance();
					currentDeposit.setDepositBalance(0);
					deposits.remove(--numberOfDesposits);
				} else {
					currentDeposit.setDepositBalance(currentDeposit.getDepositBalance() - amountLeftToWithdraw);
					amountLeftToWithdraw = 0;
				}
			}

			withdrawOrDeposit(amountToWithdraw);
		}
	}

	public void closeAccount() throws Exception {
		synchronized (this) {
			if (getAccountBalance() != 0) 
				throw new Exception("Balance is different from 0!");

			if (getNumberOfActiveLoans() > 0)
				throw new Exception("There are active loans in this account!");

			if (deposits.size() > 0)
				throw new Exception("There are deposits in this account!");

			if (isRestricted)
				throw new Exception("Account is restricted!");

			setTimeOfClosing(LocalDateTime.now());
			setStatus(AccountStatus.Closed);
		}

	}

	public void addRestriction(Restriction restriction) throws Exception {
		synchronized (this) {
			if (isRestricted && (restriction.getAmount() < getRestriction().getAmount())) //only one restriction, the highest possible
				return;

			if (restriction.getAmount() > getAccountBalance())
				throw new Exception("Amount to restrict greater than balance!");

			setRestriction(restriction);
			withdrawOrDeposit(restriction.getAmount()*-1);
			setIsRestricted(true);
		}
	}

	public void cancelRestriction() throws Exception {
		synchronized (this) {
			if (!isRestricted)
				throw new Exception("Account isn't restricted!");

			withdrawOrDeposit(getRestriction().getAmount());
			setRestriction(null);
			setIsRestricted(false);
		}
	}

	public void withdrawOrDeposit(int amount) throws Exception {
		synchronized (this) {
			if ((amount < 0) && amount*-1 > getAccountBalance())
				throw new Exception("Insufficient funds!");

			this.balance += amount;
		}
	}


	public void addDeposit(Deposit deposit) throws Exception {
		synchronized (this) {
			if (deposit.getDepositBalance() > getAccountBalance())
				throw new Exception("Insufficient funds to deposit this amount!");

			withdrawOrDeposit(deposit.getDepositBalance()*-1);
			deposits.add(deposit);
		}
	}

	public CustomerDetails getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerDetails customer) {
		this.customer = customer;
	}

	public int getAccountBalance() {
		return balance;
	}

	public ArrayList<Loan> getLoans() {
		return loans;
	}

	public void setLoans(ArrayList<Loan> loans) {
		this.loans = loans;
	}

	public boolean isRestricted() {
		return isRestricted;
	}

	public void setIsRestricted(boolean isRestricted) {
		this.isRestricted = isRestricted;
		if (isRestricted)
			this.status = AccountStatus.Restricted; 
		else
			this.status = AccountStatus.Open;
	}

	public Restriction getRestriction() {
		return restriction;
	}

	public void setRestriction(Restriction restriction) {
		this.restriction = restriction;
	}

	public ArrayList<Deposit> getDeposits() {
		return deposits;
	}

	public void setDeposits(ArrayList<Deposit> deposits) {
		this.deposits = deposits;
	}

	public LocalDateTime getTimeOfOpening() {
		return timeOfOpening;
	}

	public void setTimeOfOpening(LocalDateTime timeOfTransaction) {
		this.timeOfOpening = timeOfTransaction;
	}

	public LocalDateTime getTimeOfClosing() {
		return timeOfClosing;
	}

	public void setTimeOfClosing(LocalDateTime timeOfClosing) {
		this.timeOfClosing = timeOfClosing;
	}

	public AccountStatus getStatus() {
		return status;
	}

	public void setStatus(AccountStatus status) {
		this.status = status;
	}

	public int getAccountID() {
		return accountID;
	}

	public void setAccountID(int accountID) {
		this.accountID = accountID;
	}

	public int getNumberOfActiveLoans() {
		return numberOfActiveLoans;
	}

	public void setNumberOfActiveLoans(int numberOfActiveLoans) {
		this.numberOfActiveLoans = numberOfActiveLoans;
	}

	public int getNumberOfActiveDeposits() {
		return deposits.size();
	}

	@Override
	public String toString() {
		return "Account [customer=" + customer + ", balance=" + balance + ", numberOfActiveLoans="
				+ numberOfActiveLoans + ", isRestricted=" + isRestricted
				+ ", timeOfOpening=" + timeOfOpening + ", timeOfClosing=" + timeOfClosing
				+ ", status=" + status + ", accountID=" + accountID + "]";
	}

}
