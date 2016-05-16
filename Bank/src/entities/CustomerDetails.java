package entities;

public class CustomerDetails {
	private long customerID; // He's real id number - not db id
	private String name;
	private String phoneNumber;
	private String address;
	
	private Account account;

	public CustomerDetails(long customerID, String customerName, String phoneNumber, String address){
		this.customerID = customerID;
		this.name = customerName;
		this.phoneNumber = phoneNumber;
		this.address = address;
	}
	
	public void setCustomerID(long customerID) {
		this.customerID = customerID;
	}
	
	public long getCustomerID() {
		return customerID;
	}

	public String getCustomerName() {
		return name;
	}

	public void setCustomerName(String customerName) {
		this.name = customerName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	
	@Override
	public String toString() {
		return "CustomerDetails [customerID=" + customerID + ", name=" + name + ", phoneNumber=" + phoneNumber
				+ ", address=" + address + ", account=" + account + "]";
	}

	
	
}
