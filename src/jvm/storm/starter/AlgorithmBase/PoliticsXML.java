package storm.starter.AlgorithmBase;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PoliticsXML{

	private File XMLFile;
	private Document XMLDocument;

    	public PoliticsXML(String XMLPath){
		try{
			XMLFile = new File(XMLPath);
			XMLDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(XMLFile);
			XMLDocument.getDocumentElement().normalize();
		}
		catch(SAXException e){}
		catch(IOException e){}
		catch (ParserConfigurationException e){}
        }

	public String getConfID(){
		return XMLDocument.getElementsByTagName("confID").item(0).getTextContent();
	}

	public String getBIFlow(){
		Node bInfo = XMLDocument.getElementsByTagName("basicInfo").item(0);
		return ((Element) bInfo).getElementsByTagName("flow").item(0).getTextContent();
	}

	public String getBICorrelation(){
		Node bInfo = XMLDocument.getElementsByTagName("basicInfo").item(0);
		return ((Element) bInfo).getElementsByTagName("correlation").item(0).getTextContent();
	}

	public String getBIAction(){
		Node bInfo = XMLDocument.getElementsByTagName("basicInfo").item(0);
		return ((Element) bInfo).getElementsByTagName("action").item(0).getTextContent();
	}

	public ArrayList<String> getCIIPList(){
		ArrayList<String> listOutput = new ArrayList<String>();
		Node cInfo = XMLDocument.getElementsByTagName("correlationInfo").item(0);
		NodeList ipList = ((Element)cInfo).getElementsByTagName("ip");

		for (int ip = 0; ip < ipList.getLength(); ip++){
			listOutput.add(ipList.item(ip).getTextContent());
		}
		return listOutput;
	}

	public Long getCITimeMSAmount(){
		Node cInfo = XMLDocument.getElementsByTagName("correlationInfo").item(0);
		return Long.parseLong(((Element) cInfo).getElementsByTagName("timeMSAmount").item(0).getTextContent());
	}

	public Long getCIEventsAmount(){
		Node cInfo = XMLDocument.getElementsByTagName("correlationInfo").item(0);
		return Long.parseLong(((Element) cInfo).getElementsByTagName("eventsAmount").item(0).getTextContent());
	}

	public String getAILogLocal(){
		Node aInfo = XMLDocument.getElementsByTagName("actionInfo").item(0);
		return ((Element) aInfo).getElementsByTagName("logLocal").item(0).getTextContent();
	}

	public String getAIEmail(){
		Node aInfo = XMLDocument.getElementsByTagName("actionInfo").item(0);
		return ((Element) aInfo).getElementsByTagName("email").item(0).getTextContent();
	}
}

