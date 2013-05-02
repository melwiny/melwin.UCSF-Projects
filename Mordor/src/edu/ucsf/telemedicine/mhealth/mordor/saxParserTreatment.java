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



public class saxParserTreatment extends DefaultHandler {
		
    List<TreatmentDataObj> treatmentL;
    String treatmentXmlFileName;
    String xmlStr;
    String tmpValue;
    TreatmentDataObj treatmentTmp;
    
    public saxParserTreatment(String xml) {
    	this.xmlStr = xml;
        treatmentL = new ArrayList<TreatmentDataObj>();
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
       // System.out.println(treatmentL.size());
        for (TreatmentDataObj tmpB : treatmentL) {
            System.out.println(tmpB.toString());
        }
    }
    
	public List<TreatmentDataObj> getTreatmentList() {
		return treatmentL;
	}
    @Override
    public void startElement(String s, String s1, String elementName, Attributes attributes) throws SAXException {

        if (elementName.equalsIgnoreCase("Treatment")) {
            treatmentTmp = new TreatmentDataObj();
            treatmentTmp.setUuid_treatment(attributes.getValue("uuid_treatment"));
            treatmentTmp.setObtained_consent(attributes.getValue("obtained_consent"));
            treatmentTmp.setStatus(attributes.getValue("status"));
            treatmentTmp.setDose(attributes.getValue("dose"));
            treatmentTmp.setNote(attributes.getValue("note"));
            treatmentTmp.setUuid_child(attributes.getValue("uuid_child"));
            treatmentTmp.setDate_created(attributes.getValue("date_created"));
            treatmentTmp.setDate_last_modified(attributes.getValue("date_last_modified"));
            treatmentTmp.setUuid_agent_created(attributes.getValue("uuid_agent_created"));
            treatmentTmp.setUuid_agent_modified(attributes.getValue("uuid_agent_modified"));
        }
    }
    @Override
    public void endElement(String s, String s1, String element) throws SAXException {
        if (element.equals("Treatment")) {
            treatmentL.add(treatmentTmp);
        }
    }
    @Override
    public void characters(char[] ac, int i, int j) throws SAXException {
        tmpValue = new String(ac, i, j);
    }
}

