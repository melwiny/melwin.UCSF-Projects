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



public class saxParserCensus extends DefaultHandler {
		
    List<CensusDataObj> censusL;
    String censusXmlFileName;
    String xmlStr;
    String tmpValue;
    CensusDataObj censusTmp;
    
    public saxParserCensus(String xml) {
    	this.xmlStr = xml;
        censusL = new ArrayList<CensusDataObj>();
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
       // System.out.println(censusL.size());
        for (CensusDataObj tmpB : censusL) {
            System.out.println(tmpB.toString());
        }
    }
    
	public List<CensusDataObj> getCensusList() {
		return censusL;
	}
    @Override
    public void startElement(String s, String s1, String elementName, Attributes attributes) throws SAXException {

        if (elementName.equalsIgnoreCase("Census")) {
            censusTmp = new CensusDataObj();
            censusTmp.setUuid_census(attributes.getValue("uuid_census"));
            censusTmp.setUuid_hoh(attributes.getValue("uuid_hoh"));
            censusTmp.setPhase_id(attributes.getValue("phase_id"));
            censusTmp.setDate_created(attributes.getValue("date_created"));
            censusTmp.setDate_last_modified(attributes.getValue("date_last_modified"));
            censusTmp.setUuid_agent_created(attributes.getValue("uuid_agent_created"));
            censusTmp.setUuid_agent_modified(attributes.getValue("uuid_agent_modified"));
        }
    }
    @Override
    public void endElement(String s, String s1, String element) throws SAXException {
        if (element.equals("Census")) {
            censusL.add(censusTmp);
        }
    }
    @Override
    public void characters(char[] ac, int i, int j) throws SAXException {
        tmpValue = new String(ac, i, j);
    }
}

