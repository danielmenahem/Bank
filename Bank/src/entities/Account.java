package entities;

import java.time.LocalDateTime;

public class Account {

	/* Update Object model to remove closeAccount entity and new fields */

	enum AccountStatus{Open, Restricted, Closed}; 

	private CustomerDetails customer;
	private int balance;
	private Loan loan;
	private boolean isRestricted;
	private Restriction restriction;
	private Deposit deposit;
	private LocalDateTime timeOfOpening;
	private LocalDateTime timeOfClosing;
	private AccountStatus status;

	private int accountID;

	public Account(CustomerDetails customer){
		this.customer = customer;
		this.balance = 0;
		this.timeOfOpening = LocalDateTime.now();
		this.status = AccountStatus.Open;

		/* Account ID set after creation according to DB */
	}

	public void addLoan(Loan loan) throws Exception {
			this.loan = loan;
			withdrawOrDeposit(loan.getInitialAmount());//if loan is added, the amount of is ADDED to balance
	}

	public void returnLoan() throws Exception {
			if (loan == null) 
				throw new Exception("No loans to return!");

			withdrawOrDeposit(loan.getLoanBalance()*-1);
			loan.setLoanBalance(0);
			loan.closeLoan();
			loan = null;
	}

	public void withdrawFromDeposit(int amountToWithdraw) throws Exception {	
		if (deposit == null || deposit.getDepositBalance() < amountToWithdraw)
			throw new Exception("Insufficient funds in deposits to withdraw this amount!");

		withdrawOrDeposit(amountToWithdraw);
		deposit.setDepositBalance(deposit.getDepositBalance() - amountToWithdraw);
		
		if (deposit.getDepositBalance() == 0)
			deposit = null;
	}

	public void closeAccount() throws Exception {
			if (getAccountBalance() != 0) 
				throw new Exception("Balance is different from 0!");

			if (loan != null)
				throw new Exception("There is a loans in this account!");

			if (deposit != null)
				throw new Exception("There is a deposit in this account!");

			if (isRestricted)
				throw new Exception("Account is restricted!");

			setTimeOfClosing(LocalDateTime.now());
			setStatus(AccountStatus.Closed);

	}

	public void addRestriction(Restriction restriction) throws Exception {
			if (isRestricted && (restriction.getAmount() < getRestriction().getAmount())) //only one restriction, the highest possible
				return;

			if (restriction.getAmount() > getAccountBalance())
				throw new Exception("Amount to restrict greater than balance!");

			setRestriction(restriction);
			withdrawOrDeposit(restriction.getAmount()*-1);
			setIsRestricted(true);
		
	}

	public void cancelRestriction() throws Exception {
			if (!isRestricted)
				throw new Exception("Account isn't restricted!");

			withdrawOrDeposit(getRestriction().getAmount());
			setRestriction(null);
			setIsRestricted(false);
		
	}

	public void withdrawOrDeposit(int amount) throws Exception {
			if ((amount < 0) && amount*-1 > getAccountBalance())
				throw new Exception("Insufficient funds!");

			this.balance += amount;
		
	}


	public void addDeposit(Deposit deposit) throws Exception {
			if (deposit.getDepositBalance() > getAccountBalance())
				throw new Exception("Insufficient funds to deposit this amount!");

			withdrawOrDeposit(deposit.getDepositBalance()*-1);
			this.deposit = deposit;
	
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

	public Loan getLoan() {
		return loan;
	}

	public void setLoans(Loan loan) {
		this.loan = loan;
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



	public Deposit getDeposit() {
		return deposit;
	}

	public void setDeposits(Deposit deposit) {
		this.deposit = deposit;
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

	@Override
	public String toString() {
		return "Account [customer=" + customer + ", balance=" + balance + ", loan=" + loan + ", isRestricted="
				+ isRestricted + ", restriction=" + restriction + ", deposit=" + deposit + ", timeOfOpening="
				+ timeOfOpening + ", timeOfClosing=" + timeOfClosing + ", status=" + status + ", accountID=" + accountID
				+ "]";
	}



}
