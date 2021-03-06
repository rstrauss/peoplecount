package org.peoplecount.getresults;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * PCXMLParser = PeopleCount XML Parser
 * @author Benjy Strauss
 *
 *		// TODO: Make a class peoplecount.org.xml.XMLParser
		// TODO:    and do: xmlDoc new XMLParser().parse(filename)
		// TODO: If filename doesn't exist, throw a RuntimeExc FileNotFoundException (not the java one which isnt RuntimeExc)
		// TODO: If the file is a/b/filename and a doesn't exist, say that, if b doesn't, etc.
		// TODO:    so we know exactly which part of the path was wrong.
		// TODO:    We should have our own File class extending the Java class with methods logErrorIfMissing() and throwIfMissing():  
		// TODO:    We'll probably use this a lot.
		// TODO: Have it catch exceptions, log them and return null
 */

public class PCXMLParser {
	protected Document xmlDoc;

	public PCXMLParser(String fileName) {
		DocumentBuilderFactory factory;
		factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(false);
		factory.setValidating(false);
		
		setFeature(factory, "http://xml.org/sax/features/namespaces", false);
		setFeature(factory, "http://xml.org/sax/features/validation", false);
		setFeature(factory, "http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		setFeature(factory, "http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		
		DocumentBuilder xparser = null;
		try {
			xparser = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			System.out.println("Parser Configuration Exception: Could not obtain instance from factory.");
			e.printStackTrace();
			return;
		}
		
		try {
			xmlDoc = xparser.parse(fileName);
		} catch (SAXException e) {
			System.out.println("Error: SAX Exception in parsing.");
			e.printStackTrace();

		} catch (IOException e) {
			System.out.println("Error: IO Exception in parsing.");
			e.printStackTrace();
		}
	}
	
	public NodeList getElements() {
		return xmlDoc.getElementsByTagName("*");
	}
	
	public Element getMainElement() {
		return xmlDoc.getDocumentElement();
	}

	private void setFeature(DocumentBuilderFactory factory, String feature, boolean value) {
		try {
			factory.setFeature(feature, false);
		} catch (ParserConfigurationException e) {
			System.out.println("Parser Configuration Exception: "+feature+" could not be set to: "+value);
		}
	}
}
