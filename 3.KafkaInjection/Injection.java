import java.util.*;
import java.io.*;
import java.lang.InterruptedException;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class Injection {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("metadata.broker.list", "localhost:9092");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("partitioner.class", "example.producer.SimplePartitioner");
        props.put("request.required.acks", "1");
        ProducerConfig config = new ProducerConfig(props);
        Producer<String, String> producer = new Producer<String, String>(config);

	String fullPacket;
        String[] packetData;
        Float sleepTime;
	try {
	    FileInputStream pcapStream = new FileInputStream("Data/DoS.csv");
            BufferedReader pcapReader = new BufferedReader(new InputStreamReader(pcapStream));
	    while(true) {
		fullPacket = pcapReader.readLine();
		if (fullPacket == null){
            	    pcapStream.getChannel().position(0);
	    	    fullPacket = pcapReader.readLine();
         	}
		packetData = fullPacket.split(",");
         	sleepTime = Float.parseFloat(packetData[0].replaceAll("\"", ""))*1000;
        	Thread.sleep(sleepTime.intValue());
                String message = packetData[1].replaceAll("\"", "") + "," + packetData[2].replaceAll("\"", "") + "," + packetData[3].replaceAll("\"", "") + "," + packetData[4].replaceAll("\"", "") + "," + packetData[5].replaceAll("\"", "") + "," + packetData[6].replaceAll("\"", "");
                KeyedMessage<String, String> data = new KeyedMessage<String, String>("Network", "192.168.10.1", message);
                producer.send(data);
            }
        }
	catch (IOException e) { }
	catch (InterruptedException e) { }

        producer.close();
    }
}
