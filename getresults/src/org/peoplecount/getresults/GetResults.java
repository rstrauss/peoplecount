package org.peoplecount.getresults;

import java.util.ArrayList;

import org.w3c.dom.NodeList;

/**
 * 
 * @author Benjy Strauss
 *
 */

public class GetResults {
	private static byte QUESTION = 0;
	private static byte ANSWER = 1;
	private static byte PERCENT = 2;
	private static byte END_OF_INPUT = 2;
	private static int FILTER_OFFSET = 3;

	protected static PCXMLParser pcParser;
	protected static ArrayList<Question> data;
	protected static ArrayList<NodeMaster> rawData;
	protected static NodeList nodes;
	protected static byte automataState;

	/**
	 * @param args  Pass in the file name to parse
	 */
	public static void main(String[] args) {
		automataState = QUESTION;
		
		//make sure that the program has an argument
		if(args.length == 0) {
			System.out.println("Error: No Target File.");
			return;
		}
		
		System.out.println("Data:");
		pcParser = new PCXMLParser(args[0]);
		data = new ArrayList<Question>();
		rawData = new ArrayList<NodeMaster>();

		nodes = pcParser.getElements();
		filterNodes();
		filterList();
		
		//The first nodes are garbage
		for(int index = 0; index < FILTER_OFFSET+2; index++) {
			rawData.remove(0);
		}

		setupData();
		printData();
		System.out.println("Completed.");
	}
	
	private static void printData() {
		for(int index = 0; index < data.size(); index++) {
			System.out.println(data.get(index));
			data.get(index).printAnswers(0);
		}
	}
	
	//should be finished
	private static void filterNodes() {
		for(int index = 0; index < nodes.getLength(); index++) {
			String protoFilter = nodes.item(index).getNodeName();
			
			if(protoFilter != "div" && protoFilter != "ol") {
				continue;
			}
				
			NodeMaster meta = new NodeMaster(nodes.item(index));
			if(meta.isData()) {
				rawData.add(meta);
			}
		}
	}
	
	/**
	 * (%) 
	 * %(#)
	 * #XX(#)%
	 * >> Ignore "ol"s
	 * This should work
	 */
	private static void filterList() {
		for(int index = FILTER_OFFSET; index < rawData.size()-1; index++) {
			boolean removeThis = true;
			
			if(rawData.get(index).ol()) {
				removeThis = false;
			} else if(rawData.get(index).isPercent()) {
				removeThis = false;
			} else if (rawData.get(index-1).isPercent()) {
				removeThis = false;
			} else if (rawData.get(index-2).isPercent()) {
				removeThis = false;
			} else if (!rawData.get(index-3).isPercent() && rawData.get(index+1).isPercent()) {
				removeThis = false;
			}
				
			if(removeThis) {
				rawData.remove(index);
				index--;
			}
		}
	}
	
	/**
	 * 
	 */
	private static void setupData() {
		Question q = null;
		QuestionAnswer qa;
		int questionNum = 1;
		int subQuestionNum = 1;
		int percent = 0;
		boolean jump = false;
		byte olLength = 0;
		
		for(int index = 0; index < rawData.size(); index++) {
			//System.out.println("Evaluating: " + rawData.get(index));
			
			if(rawData.get(index).ol()) {
				olLength = rawData.get(index).olLength();
				automataState = QUESTION;
				jump = true;
				continue;
			} 
			
			switch (automataState) {
			case 0: { //QUESTION
				
				//qp("olLen: " + olLength);
				
				if(olLength > 0 && jump) {
					data.add(q);
				} else if(olLength > 0) {
					data.get(data.size()-1).addSubQuestion(q);
					olLength--;
				} else {
					if(q != null) {
						data.add(q);
					}
				}
				
				if(olLength > 0) {
					q = new Question(questionNum-1, rawData.get(index).toString());
					q.setSubqNum(subQuestionNum);
					subQuestionNum++;
				} else {
					q = new Question(questionNum, rawData.get(index).toString());
					questionNum++;
					subQuestionNum = 1;
				}

				jump = false;
				automataState = PERCENT;
				break;
			}
			case 1: { //ANSWER
				qa = new QuestionAnswer(rawData.get(index).toString(), percent);
				q.addAnswer(qa);
				
				if(index == rawData.size()-1) {
					automataState = END_OF_INPUT;
					break;
				}
				
				if(rawData.get(index+1).isPercent() && (!rawData.get(index+1).ol())) {
					automataState = PERCENT;
				} else {
					automataState = QUESTION;
				}
				
				break;
			}
			case 2: { //PERCENT
				String s = rawData.get(index).toString();
				s = s.substring(0, s.length()-1);
				percent = Integer.parseInt(s);
				
				automataState = ANSWER;
				break;
			}
			}
		}
		
		if(automataState == END_OF_INPUT) {
			data.add(q);
		}
	}
	
}
