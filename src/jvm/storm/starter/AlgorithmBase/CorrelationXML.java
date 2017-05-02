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
	private Node ActualElement;
	private Integer NextElement;

	public CorrelationXML(String XMLPath){
		try{
			XMLFile = new File(XMLPath);
			XMLDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(XMLFile);
			XMLDocument.getDocumentElement().normalize();
			ElementsList = XMLDocument.getElementsByTagName("element");
			ActualElement = null;
			NextElement = 0;
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

	public String getElementIP(){
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

	public void close(){
		this.XMLFile.delete();
	}
}
