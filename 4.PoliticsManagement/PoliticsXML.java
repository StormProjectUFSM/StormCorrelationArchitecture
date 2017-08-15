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

	public String getBITrigger(){
		Node bInfo = XMLDocument.getElementsByTagName("basicInfo").item(0);
		return ((Element) bInfo).getElementsByTagName("trigger").item(0).getTextContent();
	}

	public String getBICorrelation(){
		Node bInfo = XMLDocument.getElementsByTagName("basicInfo").item(0);
		return ((Element) bInfo).getElementsByTagName("correlation").item(0).getTextContent();
	}

    public String getBICorrelationType(){
        Node bInfo = XMLDocument.getElementsByTagName("basicInfo").item(0);
        return ((Element) bInfo).getElementsByTagName("correlationType").item(0).getTextContent();
    }

	public String getBIAction(){
		Node bInfo = XMLDocument.getElementsByTagName("basicInfo").item(0);
		return ((Element) bInfo).getElementsByTagName("action").item(0).getTextContent();
	}

	public String getCIIPAdressSource(){
		Node cInfo = XMLDocument.getElementsByTagName("correlationInfo").item(0);
		NodeList ipSource = ((Element)((Element)cInfo).getElementsByTagName("checkList").item(0)).getElementsByTagName("ipAdress");
		return ((Element)ipSource.item(0)).getElementsByTagName("source").item(0).getTextContent();
	}

	public String getCIIPAdressDestination(){
		Node cInfo = XMLDocument.getElementsByTagName("correlationInfo").item(0);
		NodeList ipDestination = ((Element)((Element)cInfo).getElementsByTagName("checkList").item(0)).getElementsByTagName("ipAdress");
		return ((Element)ipDestination.item(0)).getElementsByTagName("destination").item(0).getTextContent();
	}

	public String getCIProtocol(){
		Node cInfo = XMLDocument.getElementsByTagName("correlationInfo").item(0);
		NodeList protocol = ((Element)cInfo).getElementsByTagName("checkList");
		return ((Element)protocol.item(0)).getElementsByTagName("protocol").item(0).getTextContent();
	}

	public String getCIService(){
		Node cInfo = XMLDocument.getElementsByTagName("correlationInfo").item(0);
		NodeList service = ((Element)cInfo).getElementsByTagName("checkList");
		return ((Element)service.item(0)).getElementsByTagName("service").item(0).getTextContent();
	}

	public String getCIPort(){
		Node cInfo = XMLDocument.getElementsByTagName("correlationInfo").item(0);
		NodeList port = ((Element)cInfo).getElementsByTagName("checkList");
		return ((Element)port.item(0)).getElementsByTagName("port").item(0).getTextContent();
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

	public String getGenericFromRoot(String path){
		String[] pathElements = path.split("\\.");
		Integer index;
		Node trunk;

		trunk = XMLDocument.getElementsByTagName(pathElements[0]).item(0);
		for(index = 1; index < pathElements.length-1; index++){
			trunk = ((Element) trunk).getElementsByTagName(pathElements[index]).item(0);
		}
		return ((Element) trunk).getElementsByTagName(pathElements[index]).item(0).getTextContent();
	}
}
