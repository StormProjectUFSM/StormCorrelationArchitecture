package storm.starter.CorrelationBase;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import storm.starter.AlgorithmBase.PoliticsXML;
import storm.starter.AlgorithmBase.CorrelationCreation;

import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;

public class EventCounterBolt implements IRichBolt {
   private PoliticsXML configuration;
   private long emissionFrequency, metadataOutID;
   private String metadataOutPathBase, metadataOutPath;
   private Map<String, Integer> counterMap, generalMap;
   private Map<String, List<String>> packetsMap;
   private OutputCollector collector;

   @Override
   public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
      this.configuration = new PoliticsXML("/home/storm/StormInfrastructure/Storm/apache-storm-1.0.3/examples/storm-starter/src/jvm/storm/starter/MetadataBase/PoliticsConfigure.xml");
      this.emissionFrequency = this.configuration.getCIEventsAmount();
      this.metadataOutPathBase = "/home/storm/StormInfrastructure/Storm/apache-storm-1.0.3/examples/storm-starter/src/jvm/storm/starter/MetadataBase/" + this.configuration.getConfID();
      this.metadataOutID = 0;
      this.counterMap = new HashMap<String, Integer>();
      this.generalMap = new HashMap<String, Integer>();
      this.packetsMap = new HashMap<String, List<String>>();
      this.collector = collector;
   }

   @Override
   public void execute(Tuple tuple) {
     Integer c;
     String call = tuple.getString(0);

      if (!call.equals("")){
          if(!counterMap.containsKey(call)){
             counterMap.put(call, 1);
             packetsMap.put(call, new ArrayList<String>());
             packetsMap.get(call).add(tuple.getString(3));
             c = 1;
          }else{
             c = counterMap.get(call) + 1;
             counterMap.put(call, c);
	     if(!packetsMap.get(call).contains(tuple.getString(3))){
	       packetsMap.get(call).add(tuple.getString(3));
             }
          }
       }
       else{
           if(!generalMap.containsKey(tuple.getString(3))){
	     generalMap.put(tuple.getString(3), 1);
             c = 1;
	   }else{
	     c = generalMap.get(tuple.getString(3)) + 1;
             generalMap.put(tuple.getString(3), c);
	  }
       }

      if(c % this.emissionFrequency == 0){
         this.metadataOutID++;
         this.metadataOutPath = this.metadataOutPathBase + this.metadataOutID + ".xml";
         new CorrelationCreation(this.metadataOutPath, this.packetsMap, this.counterMap, this.generalMap);
         this.collector.emit(new Values(this.metadataOutPath));
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
