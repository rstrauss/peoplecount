package org.peoplecount.getresults;

/**
 * 
 * Contains tools for making HTML Documents
 * 
 * @author Benjy Strauss
 *
 */

public abstract class TagLib {
	protected static final String doctype = "<!DOCTYPE html>\n";
	protected static final String body = "<body>\n";
	protected static final String _body = "</body>\n";
	protected static final String head = "<head lang=\"en-US\">\n";
	protected static final String _head = "</head>\n";
	protected static final String html = "<html>\n";
	protected static final String _html = "</html>\n";
	protected static final String horizontalLine = "<hr></hr>\n";
	protected static final String newLine = "<br></br>";
	
	/**
	 * Turns input into a head tag
	 * @param input
	 * @return
	 */
	protected String makeHead(String input) {
		String retVal = head;
		retVal += input;
		retVal += _head;
		return retVal;
	}
	
	/**
	 * Turns input into a body tag
	 * @param input
	 * @return
	 */
	protected String makeBody(String input) {
		String retVal = body;
		retVal += input;
		retVal += _body;
		return retVal;
	}
	
	/**
	 * Turns a head and a body tag into an HTML Document
	 * @param head
	 * @param body
	 * @return
	 */
	protected String makeIntoHTMLDoc(String head, String body) {
		String retVal = doctype + html;
		retVal += makeHead(head);
		retVal += makeBody(body);
		retVal += _html;
		return retVal;
	}
}
