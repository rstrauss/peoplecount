package org.peoplecount.getresults;

/**
 * 
 * @author Benjy Strauss
 *
 */

public class QuestionAnswer {
	protected String text;
	protected long totalAnswers;
	
	public QuestionAnswer(String text, long totalAnswers) {
		this.text = text;
		this.totalAnswers = totalAnswers;
	}
	
	public String getText() {
		return text;
	}
	
	public long getTotalAnswers() {
		return totalAnswers;
	}
	
	public String toString() {
		String retVal = "Chosen by ";
		if(totalAnswers < 10) {
			retVal += "0";
		}
		
		retVal += totalAnswers + "%: " + text;
		return retVal;
	}
}
