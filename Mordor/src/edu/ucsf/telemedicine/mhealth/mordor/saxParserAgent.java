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



public class saxParserAgent extends DefaultHandler {
		
    List<AgentDataObj> agentL;
    String agentXmlFileName;
    String xmlStr;
    String tmpValue;
    AgentDataObj agentTmp;
    
    public saxParserAgent(String xml) {
    	this.xmlStr = xml;
        agentL = new ArrayList<AgentDataObj>();
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
       // System.out.println(agentL.size());
        for (AgentDataObj tmpB : agentL) {
            System.out.println(tmpB.toString());
        }
    }
    
	public List<AgentDataObj> getAgentList() {
		return agentL;
	}
    @Override
    public void startElement(String s, String s1, String elementName, Attributes attributes) throws SAXException {

        if (elementName.equalsIgnoreCase("Agent")) {
            agentTmp = new AgentDataObj();
            agentTmp.setUuid_agent(attributes.getValue("uuid_agent"));
            agentTmp.setFirst_name(attributes.getValue("first_name"));
            agentTmp.setLast_name(attributes.getValue("last_name"));
            agentTmp.setEmail(attributes.getValue("email"));
            agentTmp.setMobile_1(attributes.getValue("mobile_1"));
            agentTmp.setMobile_2(attributes.getValue("mobile_2"));
            agentTmp.setPhoto(attributes.getValue("photo"));
            agentTmp.setDate_created(attributes.getValue("date_created"));
            agentTmp.setDate_last_modified(attributes.getValue("date_last_modified"));
        }
    }
    @Override
    public void endElement(String s, String s1, String element) throws SAXException {
        if (element.equals("Agent")) {
            agentL.add(agentTmp);
        }
    }
    @Override
    public void characters(char[] ac, int i, int j) throws SAXException {
        tmpValue = new String(ac, i, j);
    }
}

