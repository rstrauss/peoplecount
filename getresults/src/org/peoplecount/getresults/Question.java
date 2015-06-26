package org.peoplecount.getresults;

//import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
 * @author Benjy Strauss
 *
 */

public class Question {
	protected String text;
	protected int num;
	protected long totalAnswers;
	protected ArrayList<Question> SubQuestions;
	protected ArrayList<QuestionAnswer> answers;
	
	public Question() {
		SubQuestions = new ArrayList<Question>();
		answers = new ArrayList<QuestionAnswer>();
		totalAnswers = 0;
	}
	
	public Question(int num, String txt) {
		this.num = num;
		SubQuestions = new ArrayList<Question>();
		answers = new ArrayList<QuestionAnswer>();
		text = txt;
		totalAnswers = 0;
	}
	
	//adds an answer to the question
	public void addAnswer(QuestionAnswer ans) {
		answers.add(ans);
		totalAnswers += ans.getTotalAnswers();
	}
	
	public void addSubQuestion(Question subq) {
		SubQuestions.add(subq);
	}
	
	//updates the question's text
	public void modify(String update) {
		text = update;
	}
	
	//updates the question's number
	public int changeNum (int newNum) {
		int oldNum = num;
		num = newNum;
		return oldNum;
	}
	
	public String toString() {
		String retVal = "Question_" + num + ": " + text;
		return retVal;
	}
	
	public void printAnswers(int depth) {
		int index;
		for(index = 0; index < answers.size(); index++) {
			for(int i = 0; i < depth; i++) {
				System.out.print("--");
			}
			System.out.print("    " + answers.get(index) + "\n");
		}
		
		for(index = 0; index < SubQuestions.size(); index++) {
			for(int i = 0; i < depth; i++) {
				System.out.print("--");
			}
			System.out.println(SubQuestions.get(index));
			SubQuestions.get(index).printAnswers(depth+1);
		}
	}
}
