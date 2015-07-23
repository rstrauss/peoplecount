package org.peoplecount.getresults;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 * @author Benjy Strauss
 *
 */

public class Question extends QuestionElementUtils {
	protected String text;
	protected int num;    // The question number, like 5 for:  5. Which way is up?
	protected int subNum; // The second number, like 1 in 5.1. Which way is up?
	protected long numVotes;
	protected ArrayList<QuestionAnswer> answers;

	protected ArrayList<Question> subQuestions;

	public Question() {
		subQuestions = new ArrayList<Question>();
		answers = new ArrayList<QuestionAnswer>();
		numVotes = 0;
		subNum = 0;
	}
	
	public Question(int num, String txt) {
		this.num = num;
		subQuestions = new ArrayList<Question>();
		answers = new ArrayList<QuestionAnswer>();
		text = txt;
		numVotes = 0;
		subNum = 0;
	}

	/**
	 * Fills the question from the <li> list element. 
	 * Sets the question, all the answers and any subquestions.
	 * We'll set the number and/or sub-number later
	 */
	public void fill(Element li) {
		Element div = getFirstElement(li);
		text = div.getTextContent().trim();
		text = text.replace('\n', ' ');
		text = text.replace('\r', ' ');
		//dpr("Got question: "+text);

		div = getNextElement(div, DIV);  // class="survey_item_indent"
		//printElt("qFill, div(survey_item_indent)", div);

		Element liChild = getFirstElement(div); // <div class="survey_info"></div>
		//printElt("qFill, div(survey_info)", liChild);

		liChild = getNextElement(liChild, DIV);  // <div style="width:100%;">
		//printElt("qFill, div()", liChild);
		
		div = getFirstElement(liChild);
		//printElt("qFill, div(clear)", div);
		div = getNextElementOfClass(div, DIV, "opinion_options");
		//printElt("qFill, div(opinion_options)", div);
		
		NodeList options = div.getElementsByTagName("div");
		for (int i = 0;  i < options.getLength(); i++) {
			Node item = options.item(i);
			String clas = getClassAttribute((Element)item);
			if (!clas.equals("opinion_option")) {
				continue;
			}
			
			getOneAnswer(item);
		}
		
		liChild = getNextElement(liChild, OL);
		if (liChild == null) {
			return; // no nested questions
		}

		Element nestedLi = getFirstElement(liChild);
		subQuestions = processQuestionList(nestedLi);
	}

	private void getOneAnswer(Node item) {
		Element lastDiv = getLastElement(item);
		String optionText = lastDiv.getTextContent().trim();
		
		int numVotesOnOption = 0;

		Node comment = lastDiv.getNextSibling();
		while (comment != null) {
			short type = comment.getNodeType();
			if (type == Node.COMMENT_NODE) {
				String oc = comment.getTextContent();
				int ix = oc.indexOf('=');
				while (++ix < oc.length()) {
					char c = oc.charAt(ix);
					if (c < '0' || '9' < c)
						break;
					numVotesOnOption = (numVotesOnOption * 10) + (c - '0');
				}
				break;
			}
			comment = comment.getNextSibling();
		}
		QuestionAnswer ans = new QuestionAnswer(optionText, numVotesOnOption);
		//dpr("Got answer: "+optionText);
		addAnswer(ans);
		
	}

	//adds an answer to the question
	public void addAnswer(QuestionAnswer ans) {
		answers.add(ans);
		numVotes += ans.chosenBy();
	}
	
	public void addSubQuestion(Question subq) {
		subQuestions.add(subq);
	}
	
	public String toString() {
		String retVal = "Question " + num;
		if (subNum != 0)
			retVal += "." + subNum;

		retVal += ": " + text;
		return retVal;
	}

	public void setNum(int val) {
		num = val;
		if (subQuestions == null)
			return;
		for (int i = 0; i < subQuestions.size(); i++)
			subQuestions.get(i).setSubqNum(val, i+1);
			
	}
	
	public void setSubqNum(int i) {
		subNum = i;
	}

	public void setSubqNum(int n, int subn) {
		num = n;
		subNum = subn;
	}

	protected void print(String prefix) {
		System.out.print(prefix);
		System.out.println(this);

		int index;
		// Print out all the answers
		for (index = 0; index < answers.size(); index++)
			System.out.println(prefix + "    " + answers.get(index));
		
		prefix += "--";
		// print out all the subquestions
		for (index = 0; index < subQuestions.size(); index++)
			subQuestions.get(index).print(prefix);
	}

	public int num() { return num; }
	public int subNum() { return subNum; }
	public String getName() { return text; }
	public long numVotes() { return numVotes; }
	
	public Question getSubQuestion(int i) {
		if(i < subQuestions.size() && i > -1) {
			return subQuestions.get(i);
		} else {
			return null;
		}
	}

	public QuestionAnswer getQAnswer(int i) {
		if(i < answers.size() && i > -1) {
			return answers.get(i);
		} else {
			return null;
		}
	}
}