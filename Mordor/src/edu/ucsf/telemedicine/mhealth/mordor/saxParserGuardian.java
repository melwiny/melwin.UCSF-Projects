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



public class saxParserGuardian extends DefaultHandler {
		
    List<GuardianDataObj> guardianL;
    String guardianXmlFileName;
    String xmlStr;
    String tmpValue;
    GuardianDataObj guardianTmp;
    
    public saxParserGuardian(String xml) {
    	this.xmlStr = xml;
        guardianL = new ArrayList<GuardianDataObj>();
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
       // System.out.println(guardianL.size());
        for (GuardianDataObj tmpB : guardianL) {
            System.out.println(tmpB.toString());
        }
    }
    
	public List<GuardianDataObj> getGuardianList() {
		return guardianL;
	}
    @Override
    public void startElement(String s, String s1, String elementName, Attributes attributes) throws SAXException {

        if (elementName.equalsIgnoreCase("Guardian")) {
            guardianTmp = new GuardianDataObj();
            guardianTmp.setUuid_guardian(attributes.getValue("uuid_guardian"));
            guardianTmp.setFirst_name(attributes.getValue("first_name"));
            guardianTmp.setLast_name(attributes.getValue("last_name"));
            guardianTmp.setAge(attributes.getValue("age"));
            guardianTmp.setGender(attributes.getValue("gender"));
            guardianTmp.setPregnant(attributes.getValue("pregnant"));
            guardianTmp.setNote(attributes.getValue("note"));
            guardianTmp.setUuid_hoh(attributes.getValue("uuid_hoh"));
            guardianTmp.setDate_created(attributes.getValue("date_created"));
            guardianTmp.setDate_last_modified(attributes.getValue("date_last_modified"));
            guardianTmp.setUuid_agent_created(attributes.getValue("uuid_agent_created"));
            guardianTmp.setUuid_agent_modified(attributes.getValue("uuid_agent_modified"));
        }
    }
    @Override
    public void endElement(String s, String s1, String element) throws SAXException {
        if (element.equals("Guardian")) {
            guardianL.add(guardianTmp);
        }
    }
    @Override
    public void characters(char[] ac, int i, int j) throws SAXException {
        tmpValue = new String(ac, i, j);
    }
}
