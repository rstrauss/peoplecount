package org.peoplecount.getresults;

/**
 * 
 * Contains tools for making HTML Documents
 * 
 * @author Benjy Strauss
 *
 */

public class TagLib {
	protected static final String doctype = "<!DOCTYPE html>\n";
	protected static final String horizontalLine = "<hr></hr>\n";
	protected static final String newLine = "<br></br>";
	
	private String startTag(String tag) {
		return startTag(tag, null);
	}

	private String startTag(String tag, String attrs) {
		if (attrs == null)
			attrs = "";
		StringBuffer sb = new StringBuffer(tag.length() + 4 + attrs.length());
		sb.append('<').append(tag);
		if (attrs != null && attrs.length() > 0)
			sb.append(' ').append(attrs);
		sb.append(">\n");
		return attrs;
	}
	
	private String endTag(String tag) {
		return "</" + tag + ">\n";
	}
	
	/**
	 * Turns input into a head tag
	 * @param input
	 * @return
	 */
	protected String makeHead(String input) {
		String head = "head";
		String retVal = startTag(head, "lang=\"en-US\"") + input + endTag(head);
		return retVal;
	}
	
	/**
	 * Turns input into a body tag
	 * @param input
	 * @return
	 */
	protected String makeBody(String input) {
		String body = "body";
		String retVal = startTag(body) + input + endTag(body);
		return retVal;
	}
	
	/**
	 * Turns a head and a body tag into an HTML Document
	 * @param head
	 * @param body
	 * @return
	 */
	protected String makeIntoHTMLDoc(String head, String body) {
		String html = "html";
		String retVal = doctype + startTag(html) + makeHead(head) + makeBody(body) + endTag(html);
		return retVal;
	}
}
