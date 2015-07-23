package org.peoplecount.getresults;

/**
 * 
 * @author Benjy Strauss
 *
 */

public class QuestionAnswer {
	protected String answer;
	protected int numAnswered;
	private int percent;
	
	public QuestionAnswer(String text, int num) {
		answer = text;
		numAnswered = num;
	}
	
	public QuestionAnswer(String text, int percent, int num) {
		answer = text;
		numAnswered = num;
		this.percent = percent;
	}
	
	public String getText() {
		return answer;
	}
	
	public int chosenBy() {
		return numAnswered;
	}
	
	public int chosenByPercent() {
		return percent;
	}
	
	public String toString() {
		String retVal = "Chosen by ";
		if (numAnswered > 0)  {
			retVal += answer;
		} else {
			return "Data is corrupt.";
		}

		return retVal;
	}
}
