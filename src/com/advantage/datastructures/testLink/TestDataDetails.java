package com.advantage.datastructures.testLink;

/**
 * Data Structure to hold variables for Test Case
 */
public class TestDataDetails {
	public String sEmailTag, sFirstNameTag, sIdTag, sLastNameTag;
	
	

	/**
	 * Constructor for Create Incident 
	 * 	
	 */
	public TestDataDetails(String sEmailTag, String sFirstNameTag, String sIdTag, String sLastNameTag)
	{
		
		set(sEmailTag, sFirstNameTag, sIdTag, sLastNameTag);
		
	}

	

	/**
	 * Sets all the variables.	
	 */
	private void set(String sEmailTag, String sFirstNameTag, String sIdTag, String sLastNameTag)
	{
		this.sEmailTag = sEmailTag;
		this.sFirstNameTag = sFirstNameTag;
		this.sIdTag = sIdTag;
		this.sLastNameTag = sLastNameTag;
		
	}



	@Override
	public String toString() {
		return "TestDataDetails [sEmailTag=" + sEmailTag + ", sFirstNameTag="
				+ sFirstNameTag + ", sIdTag=" + sIdTag + ", sLastNameTag="
				+ sLastNameTag + "]";
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((sEmailTag == null) ? 0 : sEmailTag.hashCode());
		result = prime * result
				+ ((sFirstNameTag == null) ? 0 : sFirstNameTag.hashCode());
		result = prime * result + ((sIdTag == null) ? 0 : sIdTag.hashCode());
		result = prime * result
				+ ((sLastNameTag == null) ? 0 : sLastNameTag.hashCode());
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
		TestDataDetails other = (TestDataDetails) obj;
		if (sEmailTag == null) {
			if (other.sEmailTag != null)
				return false;
		} else if (!sEmailTag.equals(other.sEmailTag))
			return false;
		if (sFirstNameTag == null) {
			if (other.sFirstNameTag != null)
				return false;
		} else if (!sFirstNameTag.equals(other.sFirstNameTag))
			return false;
		if (sIdTag == null) {
			if (other.sIdTag != null)
				return false;
		} else if (!sIdTag.equals(other.sIdTag))
			return false;
		if (sLastNameTag == null) {
			if (other.sLastNameTag != null)
				return false;
		} else if (!sLastNameTag.equals(other.sLastNameTag))
			return false;
		return true;
	}



}