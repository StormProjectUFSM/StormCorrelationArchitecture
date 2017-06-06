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
import storm.starter.AlgorithmBase.CorrelationCreation;

import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;

public class FilterBolt implements IRichBolt {
   private PoliticsXML configuration;
   private long metadataOutID;
   private String metadataOutPathBase, metadataOutPath;
   private Map<String, Integer> counterMap;
   private Map<String, List<String>> packetsMap;
   private OutputCollector collector;

   @Override
   public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
      this.configuration = new PoliticsXML("/home/storm/StormInfrastructure/Storm/apache-storm-1.0.3/examples/storm-starter/src/jvm/storm/starter/MetadataBase/PoliticsConfigure.xml");
      this.metadataOutPathBase = "/home/storm/StormInfrastructure/Storm/apache-storm-1.0.3/examples/storm-starter/src/jvm/storm/starter/MetadataBase/" + this.configuration.getConfID();
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

      if (!call.equals("")){
          if(counterMap.containsKey(call)){
             Integer c = counterMap.get(call) + 1;
             counterMap.put(call, c);
             if(!packetsMap.get(call).contains(tuple.getString(3))){
	       packetsMap.get(call).add(tuple.getString(3));
             }
          }
      }

      if(tuple.getString(4) != null){
         this.metadataOutID++;
         this.metadataOutPath = this.metadataOutPathBase + this.metadataOutID + ".xml";
         new CorrelationCreation(this.metadataOutPath, this.packetsMap, this.counterMap, null);
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
