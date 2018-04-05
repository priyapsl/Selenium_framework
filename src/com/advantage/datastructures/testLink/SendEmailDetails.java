package com.advantage.datastructures.testLink;

public class SendEmailDetails {
	public String sSMPT_Server;
	public String sFromEmailAddress;
	public String[] sToEmailAddresses;
	public String sSubject;
	public String sMessageText;
	public String[] sFileAttachments;
	public String sPathSeparator;
	public String sNewLine;

	/**
	 * Constructor for windows OS. (For other OS, use constructor with
	 * sPathSeparator)
	 * 
	 * @param sSMPT_Server - Server to use to send message
	 * @param sFromEmailAddress - Address that the message will displayed from
	 * @param sToEmailAddresses - E-mail addresses of the recipients
	 * @param sSubject - Subject line of the message
	 * @param sMessageText - Message text of the e-mail
	 * @param sFileAttachments - Location of attachments to send
	 */
	public SendEmailDetails(String sSMPT_Server, String sFromEmailAddress, String[] sToEmailAddresses,
			String sSubject, String sMessageText, String[] sFileAttachments)
	{

		init(sSMPT_Server, sFromEmailAddress, sToEmailAddresses, sSubject, sMessageText, sFileAttachments);

		// On Windows the path separator is \
		this.sPathSeparator = "[\\\\]";
		this.sNewLine = "\r\n";
	}

	/**
	 * Constructor for non-windows OS
	 * 
	 * @param sSMPT_Server - Server to use to send message
	 * @param sFromEmailAddress - Address that the message will displayed from
	 * @param sToEmailAddresses - E-mail addresses of the recipients
	 * @param sSubject - Subject line of the message
	 * @param sMessageText - Message text of the e-mail
	 * @param sFileAttachments - Location of attachments to send
	 * @param sPathSeparator - The path separator used to split the file location and determine the filename
	 * @param sNewLine - New Line feed for use with log4j
	 */
	public SendEmailDetails(String sSMPT_Server, String sFromEmailAddress, String[] sToEmailAddresses,
			String sSubject, String sMessageText, String[] sFileAttachments, String sPathSeparator,
			String sNewLine)
	{

		init(sSMPT_Server, sFromEmailAddress, sToEmailAddresses, sSubject, sMessageText, sFileAttachments);

		// Let user specific path separator. (On Linux the path separator is /)
		this.sPathSeparator = sPathSeparator;
		this.sNewLine = sNewLine;
	}

	private void init(String sSMPT_Server, String sFromEmailAddress, String[] sToEmailAddresses,
			String sSubject, String sMessageText, String[] sFileAttachments)
	{

		this.sSMPT_Server = sSMPT_Server;
		this.sFromEmailAddress = sFromEmailAddress;
		this.sToEmailAddresses = new String[sToEmailAddresses.length];
		System.arraycopy(sToEmailAddresses, 0, this.sToEmailAddresses, 0, sToEmailAddresses.length);
		this.sSubject = sSubject;
		this.sMessageText = sMessageText;
		this.sFileAttachments = new String[sFileAttachments.length];
		System.arraycopy(sFileAttachments, 0, this.sFileAttachments, 0, sFileAttachments.length);
	}
}
