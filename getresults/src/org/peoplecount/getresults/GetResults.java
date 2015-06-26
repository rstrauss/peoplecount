package org.peoplecount.getresults;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Not ready yet, doesn't read question IDs
 * @author Benjy Strauss
 *
 */

public class GetResults {
	private static byte QUESTION = 0;
	private static byte ANSWER = 1;
	private static byte PERCENT = 2;
	private static byte END_OF_INPUT = 2;
	private static int FILTER_OFFSET = 3;
	
	protected static DocumentBuilderFactory factory;
	protected static DocumentBuilder xparser;
	protected static ArrayList<Question> data;
	protected static ArrayList<NodeMaster> rawData;
	protected static Document xmlDoc;
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
		
		data = new ArrayList<Question>();
		rawData = new ArrayList<NodeMaster>();
		
		// TODO: Make a class peoplecount.org.xml.XMLParser
		// TODO:    and do: xmlDoc new XMLParser().parse(filename)
		// TODO: If filename doesn't exist, throw a RuntimeExc FileNotFoundException (not the java one which isnt RuntimeExc)
		// TODO: If the file is a/b/filename and a doesn't exist, say that, if b doesn't, etc.
		// TODO:    so we know exactly which part of the path was wrong.
		// TODO:    We should have our own File class extending the Java class with methods logErrorIfMissing() and throwIfMissing():  
		// TODO:    We'll probably use this a lot.
		// TODO: Have it catch exceptions, log them and return null
		factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(false);
		factory.setValidating(false);

		try {
			factory.setFeature("http://xml.org/sax/features/namespaces", false);
			factory.setFeature("http://xml.org/sax/features/validation", false);
			factory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
			factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			
			xparser = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			System.out.println("Error: Parser Configuration Exception.");
			e.printStackTrace();
			return;
		}
		
		try {
			xmlDoc = xparser.parse(args[0]);
		} catch (SAXException e) {
			System.out.println("Error: SAX Exception.");
			e.printStackTrace();
			return;
		} catch (IOException e) {
			System.out.println("Error: IO Exception.");
			e.printStackTrace();
			return;
		}

		// 
		nodes = xmlDoc.getElementsByTagName("div");
		filterNodes();
		filterList();
		
		//The first nodes are garbage
		for(int index = 0; index < FILTER_OFFSET; index++) {
			rawData.remove(0);
		}
		
		//viewNodeList();

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
	
/*	private static void viewNodeList() {	
		for(int index = 0; index < rawData.size(); index++) {
			System.out.println("Index " + index + ": " + rawData.get(index));
		}
	}/* */

	// TODO: 
	private static void filterNodes() {
		for(int index = 0; index < nodes.getLength(); index++) {
			//System.out.println("fi-" + index);
			NodeMaster meta = new NodeMaster(nodes.item(index));
			if(meta.getNodeClass() == 0) {  // TODO: This is confusing- isData() is better
				rawData.add(meta);
			}
		}
	}
	
	/**
	 * (%) 
	 * %(#)
	 * #XX(#)%
	 * This should work
	 */
	private static void filterList() {
		for(int index = FILTER_OFFSET; index < rawData.size()-1; index++) {
			boolean removeThis = true;
			
			if(rawData.get(index).isPercent()) {
				removeThis = false;
			} else if (rawData.get(index-1).isPercent()) {
				removeThis = false;
			} else if (rawData.get(index-2).isPercent()) {
				removeThis = false;
			}if (!rawData.get(index-3).isPercent() && rawData.get(index+1).isPercent()) {
				removeThis = false;
			}
				
			if(removeThis) {
				rawData.remove(index);
				index--;
			}
		}
	}
	
	private static void setupData() {
		Question q = null;
		QuestionAnswer qa;
		int questionNum = 1;
		int percent = 0;
		
		for(int index = 0; index < rawData.size(); index++) {
			switch (automataState) {
			case 0: { //QUESTION
				if(q != null) {
					data.add(q);
				}
				
				q = new Question(questionNum, rawData.get(index).toString());
				questionNum++;
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
				
				if(rawData.get(index+1).isPercent()) {
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
		
		System.out.println("incomplete");
	}
	
}
