package org.peoplecount.getresults;

import java.util.ArrayList;

/**
 * The skeleton for an HTML writer class
 * 
 * @author Benjy Strauss
 *
 */

public class PCHTMLWriter {
	protected ArrayList<Question> data;
	
	//create a new PeopleCountHTMLWriter
	public PCHTMLWriter() {
		data = new ArrayList<Question>();
	}
	
	//create a new PeopleCountHTMLWriter with preset questions
	public PCHTMLWriter(ArrayList<Question> data) {
		this.data = data;
	}
	
	//sets the data
	public void setData(ArrayList<Question> data) {
		this.data = data;
	}
	
	public String getHTML() {
		String output = "";
		
		return output;
	}
	
}
