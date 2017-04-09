package storm.starter.CorrelationBase;

import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;

public class FilterBolt implements IRichBolt {
   private String metadataOutPath;
   private Map<String, Integer> counterMap;
   private OutputCollector collector;

   @Override
   public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
      this.metadataOutPath = this.metadataOutPath = "/home/storm/StormInfrastructure/Storm/apache-storm-1.0.3/examples/storm-starter/src/jvm/storm/starter/MetadataBase/Correlation.xml";
      this.counterMap = new HashMap<String, Integer>();
      this.collector = collector;

      try{
      	FileReader freader = new FileReader("/home/storm/StormInfrastructure/Storm/apache-storm-1.0.3/examples/storm-starter/src/jvm/storm/starter/CorrelationBase/MetaData/FilterIPs");
      	BufferedReader fbuffer = new BufferedReader(freader);

	for (String IP = fbuffer.readLine(); IP != null; IP = fbuffer.readLine()){
  	  counterMap.put(IP, 0);
	}
        freader.close();
      }
      catch(IOException ex){}
   }

   @Override
   public void execute(Tuple tuple) {
      String call = tuple.getString(0);

      if(counterMap.containsKey(call)){
         Integer c = counterMap.get(call) + 1;
         counterMap.put(call, c);
      }

      collector.ack(tuple);
   }

   @Override
   public void cleanup() {
     try{
        FileWriter finserter = new FileWriter(new File(this.metadataOutPath));
        finserter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<elementList>");
	for(Map.Entry<String, Integer> entry:counterMap.entrySet()){
            finserter.write("\n<element>\n	<ip>" + entry.getKey() + "</ip>\n	<counter>" + entry.getValue() + "</counter>\n</element>");
        }
	finserter.write("\n</elementList>");
        finserter.close();
     }
     catch(IOException ex){
         for(Map.Entry<String, Integer> entry:counterMap.entrySet()){
             System.out.println(entry.getKey()+" : " + entry.getValue());
         }
     }
   }

   @Override
   public void declareOutputFields(OutputFieldsDeclarer declarer) {
      declarer.declare(new Fields("call"));
   }

   @Override
   public Map<String, Object> getComponentConfiguration() {
      return null;
   }
}
