package storm.starter.CorrelationBase;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

import storm.starter.AlgorithmBase.PoliticsXML;

import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;

public class ChronoFilterBolt implements IRichBolt {
   private PoliticsXML configuration;
   private long startTime, emissionFrequency, metadataOutID;
   private String metadataOutPathBase, metadataOutPath;
   private Map<String, Integer> counterMap;
   private Map<String, List<String>> packetsMap;
   private OutputCollector collector;

   private void makeMetadataXML(){
     try{
        this.metadataOutID++;
        this.metadataOutPath = this.metadataOutPathBase + this.metadataOutID + ".xml";
        FileWriter finserter = new FileWriter(new File(this.metadataOutPath));
        finserter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<elementList>");
        for(Map.Entry<String, Integer> entry:counterMap.entrySet()){
            finserter.write("\n<element>\n      <port>" + entry.getKey() + "</port>\n       <counter>" + entry.getValue() + "</counter>\n       <packets>\n");
            for (String packet : packetsMap.get(entry.getKey())){
	      finserter.write("            <packet>" + packet + "</packet>\n");
	    }
	    finserter.write("       </packets>\n</element>");
        }
        finserter.write("\n</elementList>");
        finserter.close();
     }
     catch(IOException ex){
         for(Map.Entry<String, Integer> entry:counterMap.entrySet()){
             System.out.println(entry.getKey() + ":" + entry.getValue());
         }
     }
   }

   @Override
   public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
      this.configuration = new PoliticsXML("/home/storm/StormInfrastructure/Storm/apache-storm-1.0.3/examples/storm-starter/src/jvm/storm/starter/MetadataBase/PoliticsConfigure.xml");
      this.startTime = System.currentTimeMillis();
      this.emissionFrequency = this.configuration.getCITimeMSAmount();
      this.metadataOutPath = "/home/storm/StormInfrastructure/Storm/apache-storm-1.0.3/examples/storm-starter/src/jvm/storm/starter/MetadataBase/" + this.configuration.getConfID();
      this.metadataOutID = 0;
      this.counterMap = new HashMap<String, Integer>();
      this.packetsMap = new HashMap<String, List<String>>();
      this.collector = collector;

      for(String Port : this.configuration.getCIPortList()){
         this.counterMap.put(Port, 0);
	 this.packetsMap.put(Port, new ArrayList<String>());
      }
   }

   @Override
   public void execute(Tuple tuple) {
      String call = tuple.getString(0);

      if(counterMap.containsKey(call)){
         Integer c = counterMap.get(call) + 1;
         counterMap.put(call, c);
         if(!packetsMap.get(call).contains(tuple.getString(3))){
	   packetsMap.get(call).add(tuple.getString(3));
         }
      }

      if(System.currentTimeMillis() - this.startTime >= this.emissionFrequency){
         this.makeMetadataXML();
         this.collector.emit(new Values(this.metadataOutPath));
         this.startTime = System.currentTimeMillis();
      }

      collector.ack(tuple);
   }

   @Override
   public void cleanup() {}

   @Override
   public void declareOutputFields(OutputFieldsDeclarer declarer) {
      declarer.declare(new Fields("request"));
   }

   @Override
   public Map<String, Object> getComponentConfiguration() {
      return null;
   }
}
