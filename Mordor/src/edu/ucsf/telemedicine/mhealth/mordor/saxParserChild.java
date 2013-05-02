package edu.ucsf.telemedicine.mhealth.mordor;


import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;



public class saxParserChild extends DefaultHandler {
		
    List<ChildDataObj> childL;
    String childXmlFileName;
    String xmlStr;
    String tmpValue;
    ChildDataObj childTmp;
    
    public saxParserChild(String xml) {
    	this.xmlStr = xml;
        childL = new ArrayList<ChildDataObj>();
        parseDocument();
    }
    private void parseDocument() {
        // parse
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser parser = factory.newSAXParser();
            parser.parse(new InputSource(new StringReader(xmlStr)), this);
        } catch (ParserConfigurationException e) {
            System.out.println("ParserConfig error");
        } catch (SAXException e) {
            System.out.println("SAXException : xml not well formed");
        } catch (IOException e) {
            System.out.println("IO error");
        }
    }
    private void printDatas() {
       // System.out.println(childL.size());
        for (ChildDataObj tmpB : childL) {
            System.out.println(tmpB.toString());
        }
    }
    
	public List<ChildDataObj> getChildList() {
		return childL;
	}
    @Override
    public void startElement(String s, String s1, String elementName, Attributes attributes) throws SAXException {

        if (elementName.equalsIgnoreCase("Child")) {
            childTmp = new ChildDataObj();
            childTmp.setUuid_child(attributes.getValue("uuid_child"));
            childTmp.setFirst_name(attributes.getValue("first_name"));
            childTmp.setLast_name(attributes.getValue("last_name"));
            childTmp.setNickname(attributes.getValue("nickname"));
            childTmp.setAge(attributes.getValue("age"));
            childTmp.setAge_unit(attributes.getValue("age_unit"));
            childTmp.setGender(attributes.getValue("gender"));
            childTmp.setStatus(attributes.getValue("status"));
            childTmp.setNote(attributes.getValue("note"));
            childTmp.setUuid_hoh(attributes.getValue("uuid_hoh"));
            childTmp.setUuid_guardian(attributes.getValue("uuid_guardian"));
            childTmp.setDate_created(attributes.getValue("date_created"));
            childTmp.setDate_last_modified(attributes.getValue("date_last_modified"));
            childTmp.setUuid_agent_created(attributes.getValue("uuid_agent_created"));
            childTmp.setUuid_agent_modified(attributes.getValue("uuid_agent_modified"));
        }
    }
    @Override
    public void endElement(String s, String s1, String element) throws SAXException {
        if (element.equals("Child")) {
            childL.add(childTmp);
        }
    }
    @Override
    public void characters(char[] ac, int i, int j) throws SAXException {
        tmpValue = new String(ac, i, j);
    }
}

