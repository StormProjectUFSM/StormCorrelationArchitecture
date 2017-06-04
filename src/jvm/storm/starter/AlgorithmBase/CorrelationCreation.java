package storm.starter.AlgorithmBase;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CorrelationCreation {

	public CorrelationCreation (String metadataOutPath, List<List<String>> packetsMap, List<String> compressMap, 
								List<String> generalMap){
		try{
        	FileWriter finserter = new FileWriter(new File(metadataOutPath));
        	finserter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<correlationResult>\n    <elementList>");
        
        	for(String Port : compressMap){
            	finserter.write("\n    <element>\n          <port>" + Port + "</port>\n           <counter></counter>\n           <packets>\n");
	    		for(String packet : packetsMap.get(compressMap.indexOf(Port))){
	       			finserter.write("                <packet>" + packet + "</packet>\n");
	    		}
	    		finserter.write("           </packets>\n</element>");
        	}

        	finserter.write("\n    </elementList>\n    <unrecognizedList>");
			for (String data : generalMap){
	    		finserter.write("\n    <element>\n          <data>" + data + "</data>\n           <counter></counter>\n     </element>\n");
			}

        	finserter.write("\n    </unrecognizedList>\n</correlationResult>");
        	finserter.close();
     	}
     	catch(IOException ex){}
	}

	public CorrelationCreation (String metadataOutPath, Map<String, List<String>> packetsMap, Map<String, Integer> counterMap, 
								Map<String, Integer> generalMap){
		try{
        	FileWriter finserter = new FileWriter(new File(metadataOutPath));
        	finserter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<correlationResult>\n    <elementList>");

        	for(Map.Entry<String, Integer> entry:counterMap.entrySet()){
            	finserter.write("\n    <element>\n          <port>" + entry.getKey() + "</port>\n           <counter>" + entry.getValue() + "</counter>\n           <packets>\n");
            	for (String packet : packetsMap.get(entry.getKey())){
	      			finserter.write("            <packet>" + packet + "</packet>\n");
	    		}
	    		finserter.write("           </packets>\n    </element>");
        	}

        	finserter.write("\n    </elementList>\n    <unrecognizedList>");
        	if (generalMap != null){
			for (Map.Entry<String, Integer> entry : generalMap.entrySet()){
            			finserter.write("\n    <element>\n          <data>" + entry.getKey() + "</data>\n           <counter>" + entry.getValue() + "</counter>\n     </element>\n");
        		}
		}

        	finserter.write("\n    </unrecognizedList>\n</correlationResult>");
        	finserter.close();
     	}
     	catch(IOException ex){}
    }

    public static void main(String [ ] args){
    	System.out.println("oi");
    }
}
