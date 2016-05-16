package entities;

import java.util.ArrayList;

public class CustomerDetails {
	private String customerName;
	private Account account;
	private String address;
	private long phoneNumber;
	
	private int customerID;
	
	public CustomerDetails(String customerName, String address, long phoneNumber) {
		this.customerName = customerName;
		this.address = address;
		this.phoneNumber = phoneNumber;
				
		/* customer ID set after creation according to DB */
	}
	

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Account getAccounts() {
		return account;
	}

	public void setAccounts(Account account) {
		this.account = account;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(Long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public int getCustomerID() {
		return customerID;
	}

	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}

	@Override
	public String toString() {
		return "CustomerDetails [customerName=" + customerName + ", address=" + address
				+ ", phoneNumber=" + phoneNumber + ", customerID=" + customerID + "]";
	}


}
