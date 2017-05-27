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

public class CorrelationXML{
	private File XMLFile;
    private Document XMLDocument;
	private NodeList ElementsList;
	private NodeList UnrecognizedList;
	private Node ActualElement;
	private Node ActualUnrecognized;
	private Integer NextElement;
	private Integer NextUnrecognized;

	public CorrelationXML(String XMLPath){
		try{
			XMLFile = new File(XMLPath);
			XMLDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(XMLFile);
			XMLDocument.getDocumentElement().normalize();
			ElementsList = ((Element) XMLDocument.getElementsByTagName("elementList").item(0)).getElementsByTagName("element");
			UnrecognizedList = ((Element) XMLDocument.getElementsByTagName("unrecognizedList").item(0)).getElementsByTagName("element");
			ActualElement = null;
			ActualUnrecognized = null;
			NextElement = 0;
			NextUnrecognized = 0;
		}
		catch(SAXException e){}
		catch(IOException e){}
		catch (ParserConfigurationException e){}
	}

	public boolean chargeNextElement(){
		if (NextElement < ElementsList.getLength()){
			ActualElement = ElementsList.item(NextElement);
			NextElement++;
			return true;
		}
		ActualElement = null;
		return false;
	}

	public boolean chargeNextUnrecognized(){
		if (NextUnrecognized < UnrecognizedList.getLength()){
			ActualUnrecognized = UnrecognizedList.item(NextUnrecognized);
			NextUnrecognized++;
			return true;
		}
		ActualUnrecognized = null;
		return false;
	}

	public String getElementPort(){
		if (ActualElement != null){
			return ((Element) ActualElement).getElementsByTagName("port").item(0).getTextContent();
		}
		return null;
	}

	public String getElementCounter(){
		if (ActualElement != null){
			if (((Element) ActualElement).getElementsByTagName("counter").getLength() > 0){
				return ((Element) ActualElement).getElementsByTagName("counter").item(0).getTextContent();
			}
		}
		return null;
	}

	public String getElementPackets(){
		String packetsData = "";
		if (ActualElement != null){
			NodeList packetsList = ((Element) ActualElement).getElementsByTagName("packet");
			for (int packetIndex = 0; packetIndex < packetsList.getLength(); packetIndex++){
				packetsData += ((Element) packetsList.item(packetIndex)).getTextContent() + "\n";
			}
			return packetsData;
		}
		return null;
	}

	public String getUnrecognizedData(){
		if (ActualUnrecognized != null){
			return ((Element) ActualUnrecognized).getElementsByTagName("data").item(0).getTextContent();
		}
		return null;
	}

	public String getUnrecognizedCounter(){
		if (ActualUnrecognized != null){
			if (((Element) ActualUnrecognized).getElementsByTagName("counter").getLength() > 0){
				return ((Element) ActualUnrecognized).getElementsByTagName("counter").item(0).getTextContent();
			}
		}
		return null;
	}

	public void close(){
		this.XMLFile.delete();
	}
}
