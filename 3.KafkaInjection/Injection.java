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
        Random sleepGap = new Random();

	try {
	    FileInputStream pcapStream = new FileInputStream("Data/Packets.csv");
            BufferedReader pcapReader = new BufferedReader(new InputStreamReader(pcapStream));
	    while(true) {
		fullPacket = pcapReader.readLine();
		if (fullPacket == null){
            	    pcapStream.getChannel().position(0);
	    	    fullPacket = pcapReader.readLine();
         	}

        	Thread.sleep(sleepGap.nextInt(21));
		KeyedMessage<String, String> data = new KeyedMessage<String, String>("Network", "192.168.10.1", fullPacket.replaceAll("\"", ""));
                producer.send(data);
            }
        }
	catch (IOException e) { }
	catch (InterruptedException e) { }

        producer.close();
    }
}
