package com.advantage.datastructures.testLink;

/**
 * Data Structure to hold variables for Test Steps
 */
public class TestStepDetails {
	public String sStepNo, sURL, sTestStepData, sExpResponseCode, sExpContentType, sExpTestData, sOperation;
	
	

	/**
	 * Constructor for Create Incident 
	 * 	
	 */
	public TestStepDetails(String sStepNo, String sURL, String sTestStepData, 
			String sExpResponseCode, String sExpContentType, String sExpTestData, 
			String sOperation)
	{
		
		set(sStepNo, sURL, sTestStepData, sExpResponseCode, sExpContentType, sExpTestData, sOperation);
		
	}

	

	/**
	 * Sets all the variables.	
	 */
	private void set(String sStepNo, String sURL, String sTestStepData, 
			String sExpResponseCode, String sExpContentType, String sExpTestData, 
			String sOperation)
	{
		this.sStepNo = sStepNo;
		this.sURL = sURL;
		this.sTestStepData = sTestStepData;
		this.sExpResponseCode = sExpResponseCode;
		this.sExpContentType = sExpContentType;
		this.sExpTestData = sExpTestData;
		this.sOperation = sOperation;		
		
		
	}



	@Override
	public String toString() {
		return "TestStepDetails [sStepNo=" + sStepNo + ", sURL=" + sURL
				+ ", sTestStepData=" + sTestStepData + ", sExpResponseCode="
				+ sExpResponseCode + ", sExpContentType=" + sExpContentType
				+ ", sExpTestData=" + sExpTestData + ", sOperation="
				+ sOperation + "]";
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((sExpContentType == null) ? 0 : sExpContentType.hashCode());
		result = prime
				* result
				+ ((sExpResponseCode == null) ? 0 : sExpResponseCode.hashCode());
		result = prime * result
				+ ((sExpTestData == null) ? 0 : sExpTestData.hashCode());
		result = prime * result
				+ ((sOperation == null) ? 0 : sOperation.hashCode());
		result = prime * result + ((sStepNo == null) ? 0 : sStepNo.hashCode());
		result = prime * result
				+ ((sTestStepData == null) ? 0 : sTestStepData.hashCode());
		result = prime * result + ((sURL == null) ? 0 : sURL.hashCode());
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
		TestStepDetails other = (TestStepDetails) obj;
		if (sExpContentType == null) {
			if (other.sExpContentType != null)
				return false;
		} else if (!sExpContentType.equals(other.sExpContentType))
			return false;
		if (sExpResponseCode == null) {
			if (other.sExpResponseCode != null)
				return false;
		} else if (!sExpResponseCode.equals(other.sExpResponseCode))
			return false;
		if (sExpTestData == null) {
			if (other.sExpTestData != null)
				return false;
		} else if (!sExpTestData.equals(other.sExpTestData))
			return false;
		if (sOperation == null) {
			if (other.sOperation != null)
				return false;
		} else if (!sOperation.equals(other.sOperation))
			return false;
		if (sStepNo == null) {
			if (other.sStepNo != null)
				return false;
		} else if (!sStepNo.equals(other.sStepNo))
			return false;
		if (sTestStepData == null) {
			if (other.sTestStepData != null)
				return false;
		} else if (!sTestStepData.equals(other.sTestStepData))
			return false;
		if (sURL == null) {
			if (other.sURL != null)
				return false;
		} else if (!sURL.equals(other.sURL))
			return false;
		return true;
	}	

}