package org.peoplecount.getresults;

import java.util.ArrayList;

/**
 * The skeleton for an HTML writer class
 * 
 * @author Benjy Strauss
 *
 */

public class PCHTMLWriter extends TagLib {
	protected ArrayList<Question> data;
	private static final int BAR_SCALE = 3;
	private static final int BAR_HEIGHT = 20;
	
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
	
	/**
	 * 	protected static final String doctype = "<!DOCTYPE html>\n";
	protected static final String body = "<body>\n";
	protected static final String _body = "</body>\n";
	protected static final String head = "<head lang=\"en-US\">\n";
	protected static final String _head = "</head>\n";
	protected static final String html = "<html>\n";
	protected static final String _html = "</html>\n";
	protected static final String horizontalLine = "<hr></hr>\n";
	protected static final String newLine = "<br></br>";
	 * @return
	 */
	
	public String getHTML() {
		String output = doctype;
		output += html;
		output += head;
		output += _head;
		output += body;
		
		/**
		 * Process the questions in the array list
		 */
		for(int aa = 0; aa < data.size(); aa++) {
			output += processQuestion(data.get(aa));
		}
		
		output += _body;
		output += _html;
		
		quickPrint(output);
		return output;
	}
	
	private void quickPrint(String str) {
		System.out.println(str);
	}
	
	/**
	 * Turns a question and it's answers into HTML (no indenting)
	 * @param q: the question being turned into HTML
	 * @return: the html
	 */
	private String processQuestion(Question q) {
		String retHTML = "";
		
		if(q.subNum() == 0) {
			retHTML += "<h2>" + q.getName() + "</h2>\n";
		} else {
			retHTML += "<h2><small>" + q.getName() + "</small></h2>\n";
		}
		
		retHTML += "<table style=\"width:100%\">\n";
		
		//process answers
		for(int ans = 0; q.getQAnswer(ans) != null; ans++) {
			retHTML += processAnswer(q.getQAnswer(ans), q.numVotes());
		}
		retHTML += "</table>\n";
		
		//process sub-questions
		for(int subQ = 0; q.getSubQuestion(subQ) != null; subQ++) {
			retHTML += processQuestion(q.getSubQuestion(subQ));
		}
		
		return retHTML;
	}
	
	/**
	 * 
	 * @param qAnswer
	 * @param totalAnswers
	 * @return
	 */
	private String processAnswer (QuestionAnswer qa, long totalAnswers) {
		int percent = 0;
		//quickPrint("qa.chosenBy() for question: " + qa.chosenBy());
		//quickPrint("totalAnswers for question: " + totalAnswers);
		
		try {
			double d = qa.chosenBy();
			d /= totalAnswers;
			d *= 100;
			percent = (int) d;
		} catch(Exception e) {
			
		}
		
		String retHTML = "<tr>\n";
		String _percent = "<td>" + percent + "%</td>\n";
		
		String _bar = "<td>\n";
		
		_bar += "<img src=\"barcolor.jpg\" alt=\"error\" style=\"width:";
		_bar += BAR_SCALE * percent;
		_bar += "px;height:" + BAR_HEIGHT + "px;\">\n</td>\n";
		
		//BAR_HEIGHT
		String _question = "<td>" + qa.getText() + "</td>\n";
		retHTML += _percent;
		retHTML += _bar;
		retHTML += _question;
		
		retHTML += "</tr>\n";
		
		return retHTML;
	}
}
