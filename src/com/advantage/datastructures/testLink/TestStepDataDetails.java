package com.advantage.datastructures.testLink;

/**
 * Data Structure to hold variables Parameters of API URL
 */
public class TestStepDataDetails {
	public String sEmail, sFirstName, sLastName;
	
	

	/**
	 * Constructor for Parameters of API URL
	 * 	
	 */
	public TestStepDataDetails(String sEmail,String sFirstName, String sLastName)
	{
		
		set(sEmail, sFirstName, sLastName);
		
	}

	

	/**
	 * Sets all the variables.	
	 */
	private void set(String sEmail,String sFirstName, String sLastName)
	{
		this.sEmail = sEmail;
		this.sFirstName = sFirstName;
		this.sLastName = sLastName;
		
	}



	@Override
	public String toString() {
		return "TestStepDataDetails [sEmail=" + sEmail + ", sFirstName="
				+ sFirstName + ", sLastName=" + sLastName + "]";
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sEmail == null) ? 0 : sEmail.hashCode());
		result = prime * result
				+ ((sFirstName == null) ? 0 : sFirstName.hashCode());
		result = prime * result
				+ ((sLastName == null) ? 0 : sLastName.hashCode());
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TestStepDataDetails other = (TestStepDataDetails) obj;
		if (sEmail == null) {
			if (other.sEmail != null)
				return false;
		} else if (!sEmail.equals(other.sEmail))
			return false;
		if (sFirstName == null) {
			if (other.sFirstName != null)
				return false;
		} else if (!sFirstName.equals(other.sFirstName))
			return false;
		if (sLastName == null) {
			if (other.sLastName != null)
				return false;
		} else if (!sLastName.equals(other.sLastName))
			return false;
		return true;
	}


	

}