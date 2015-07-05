package org.peoplecount.getresults;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class XmlElementUtils {
	static boolean debug = true;
	
	static final protected String DIV = "div";
	static final protected String LI = "li";
	static final protected String OL = "ol";

	protected void pr(String msg) {
		System.out.println(msg);
	}

	protected void dpr(String msg) {
		if (debug)
			System.out.println(msg);
	}

	protected void printElt(String method, Node node) {
		if (!debug)
			return;
		String name = node.getNodeName();
		String clas = getClassAttribute((Element)node);
		System.out.println(method + " got node: " + name + " class='" + clas + "'");
	}
	
	protected Element getNextElement(Node node, String name) {
		do {
			node = node.getNextSibling();
			if (node == null)
				return null;
			if (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
				String nodeName = node.getNodeName();
				if (nodeName.equals(name))
					return (Element)node;
			}
		} while (node != null);
		return null;
	}

	protected Element getNextElementOfClass(Node node, String name, String clas) {
		do {
			node = node.getNextSibling();
			if (node.getNodeType() != org.w3c.dom.Node.ELEMENT_NODE)
				continue;
			if (!node.getNodeName().equals(name))
				continue;
			
			String itsclas = getClassAttribute((Element)node);
			if (clas.equals(itsclas))
				return (Element)node;

		} while (node != null);
		return null;
	}

	protected String getClassAttribute(Element elt) {
		NamedNodeMap map = elt.getAttributes();
		Node atr = null;
		if (map != null)
			atr = map.getNamedItem("class");
		if (atr == null)
			return "";

		String clas = atr.getNodeValue();
		return clas;
	}

	protected Element getFirstElement(Node parent) {
		Node node = parent.getFirstChild();
		while (node.getNodeType() != org.w3c.dom.Node.ELEMENT_NODE)
			node = node.getNextSibling();

		return (Element)node;
	}

	protected Element getLastElement(Node parent) {
		Node node = parent.getLastChild();
		while (node.getNodeType() != org.w3c.dom.Node.ELEMENT_NODE)
			node = node.getPreviousSibling();

		return (Element)node;
	}

	protected ArrayList<Question> processQuestionList(Element li) {
		printElt("processQuestionList, li=", li);
		ArrayList<Question> questions = new ArrayList<Question>();
		while (li != null) {
			Question q = new Question();
			q.fill(li);
			questions.add(q);
			li = getNextElement(li, "li");
		}
		return questions;
	}

	// This is mainly for reference...
	protected String getNodeType(int type) {
		switch (type) {
		case Node.ELEMENT_NODE: // 1
			return("Elt");
		case Node.ATTRIBUTE_NODE: // 2
			return("Attr");
		case Node.TEXT_NODE: // 3
			return("Text");
		case Node.CDATA_SECTION_NODE:  // 4
			return("CDATA");
		case Node.ENTITY_REFERENCE_NODE: // 5
			return("EntRef");
		case Node.ENTITY_NODE: // 6
			return("Entity");
		case Node.PROCESSING_INSTRUCTION_NODE: //7 
			return("ProcInstr");
		case Node.COMMENT_NODE: // 8
			return("Comment");
		case Node.DOCUMENT_NODE:  // 9
			return("Doc.");
		case Node.DOCUMENT_TYPE_NODE: // 10
			return("DocType");
		case Node.DOCUMENT_FRAGMENT_NODE:  // 11
			return("DocFragment");
		case Node.NOTATION_NODE: // 12
			return("Notation");
		}
		return "unknown:" + type;
	}
}
