package storm.starter.CorrelationBase;

import java.util.HashMap;
import java.util.Map;
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

public class EventFilterBolt implements IRichBolt {
   private PoliticsXML configuration;
   private long emissionFrequency, metadataOutID;
   private String metadataOutPathBase, metadataOutPath;
   private Map<String, Integer> counterMap;
   private OutputCollector collector;

   private void makeMetadataXML(){
     try{
        this.metadataOutID++;
        this.metadataOutPath = this.metadataOutPathBase + this.metadataOutID + ".xml";
        FileWriter finserter = new FileWriter(new File(this.metadataOutPath));
        finserter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<elementList>");
        for(Map.Entry<String, Integer> entry:counterMap.entrySet()){
            finserter.write("\n<element>\n      <ip>" + entry.getKey() + "</ip>\n       <counter>" + entry.getValue() + "</counter>\n</element>");
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
      this.emissionFrequency = this.configuration.getCIEventsAmount();
      this.metadataOutPathBase = "/home/storm/StormInfrastructure/Storm/apache-storm-1.0.3/examples/storm-starter/src/jvm/storm/starter/MetadataBase/CorrelationFilter" + this.configuration.getConfID();
      this.metadataOutID = 0;
      this.counterMap = new HashMap<String, Integer>();
      this.collector = collector;

      for(String IP : this.configuration.getCIIPList()){
         this.counterMap.put(IP, 0);
      }
   }

   @Override
   public void execute(Tuple tuple) {
      Integer c;
      String call = tuple.getString(0);

      if(counterMap.containsKey(call)){
         c = counterMap.get(call) + 1;
         counterMap.put(call, c);

          if(c % this.emissionFrequency == 0){
              this.makeMetadataXML();
              this.collector.emit(new Values(this.metadataOutPath));
          }
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
