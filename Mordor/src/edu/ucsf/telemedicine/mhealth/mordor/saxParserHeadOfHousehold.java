package edu.ucsf.telemedicine.mhealth.mordor;

import java.io.IOException;
import java.io.StringReader;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;



public class saxParserHeadOfHousehold extends DefaultHandler {
		
    List<HeadOfHouseholdDataObj> hohL;
    String hohXmlFileName;
    String xmlStr;
    String tmpValue;
    HeadOfHouseholdDataObj hohTmp;
    
    public saxParserHeadOfHousehold(String xml) {
    	this.xmlStr = xml;
        hohL = new ArrayList<HeadOfHouseholdDataObj>();
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
       // System.out.println(hohL.size());
        for (HeadOfHouseholdDataObj tmpB : hohL) {
            System.out.println(tmpB.toString());
        }
    }
    
	public List<HeadOfHouseholdDataObj> getHeadOfHouseholdList() {
		return hohL;
	}
    @Override
    public void startElement(String s, String s1, String elementName, Attributes attributes) throws SAXException {

        if (elementName.equalsIgnoreCase("HeadOfHousehold")) {
            hohTmp = new HeadOfHouseholdDataObj();
            hohTmp.setUuid_hoh(attributes.getValue("uuid_hoh"));
            hohTmp.setFirst_name(attributes.getValue("first_name"));
            hohTmp.setLast_name(attributes.getValue("last_name"));
            hohTmp.setAge(attributes.getValue("age"));
            hohTmp.setGender(attributes.getValue("gender"));
            hohTmp.setNote(attributes.getValue("note"));
            hohTmp.setGps(attributes.getValue("gps"));
            hohTmp.setVillage(attributes.getValue("village"));
            hohTmp.setCountry(attributes.getValue("country"));
            hohTmp.setDate_created(attributes.getValue("date_created"));
            hohTmp.setDate_last_modified(attributes.getValue("date_last_modified"));
            hohTmp.setUuid_agent_created(attributes.getValue("uuid_agent_created"));
            hohTmp.setUuid_agent_modified(attributes.getValue("uuid_agent_modified"));
        }
    }
    @Override
    public void endElement(String s, String s1, String element) throws SAXException {
        if (element.equals("HeadOfHousehold")) {
            hohL.add(hohTmp);
        }
    }
    @Override
    public void characters(char[] ac, int i, int j) throws SAXException {
        tmpValue = new String(ac, i, j);
    }
}