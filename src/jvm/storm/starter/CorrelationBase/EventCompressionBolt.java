package storm.starter.CorrelationBase;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import storm.starter.AlgorithmBase.PoliticsXML;

import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;

public class EventCompressionBolt implements IRichBolt {
   private PoliticsXML configuration;
   private long eventsAmount, emissionFrequency, metadataOutID;
   private String metadataOutPathBase, metadataOutPath;
   private List<String> compressMap;
   private OutputCollector collector;

   private void makeMetadataXML(){
     try{
        this.metadataOutID++;
        this.metadataOutPath = this.metadataOutPathBase + this.metadataOutID + ".xml";
        FileWriter finserter = new FileWriter(new File(this.metadataOutPath));
        finserter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<elementList>");
        for(String IP : compressMap){
            finserter.write("\n<element>\n      <ip>" + IP + "</ip>\n       <counter></counter>\n</element>");
        }
        finserter.write("\n</elementList>");
        finserter.close();
     }
     catch(IOException ex){
         for(String IP : compressMap){
             System.out.println(IP);
         }
     }
   }

   @Override
   public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
      this.configuration = new PoliticsXML("/home/storm/StormInfrastructure/Storm/apache-storm-1.0.3/examples/storm-starter/src/jvm/storm/starter/MetadataBase/PoliticsConfigure.xml");
      this.eventsAmount = 0;
      this.emissionFrequency = this.configuration.getCIEventsAmount();
      this.metadataOutPathBase = "/home/storm/StormInfrastructure/Storm/apache-storm-1.0.3/examples/storm-starter/src/jvm/storm/starter/MetadataBase/CorrelationCompression" + this.configuration.getConfID();
      this.metadataOutID = 0;
      this.compressMap = new ArrayList<String>();
      this.collector = collector;
   }

   @Override
   public void execute(Tuple tuple) {
      String call = tuple.getString(0);

      this.eventsAmount++;
      if(!compressMap.contains(call)){
         compressMap.add(call);
      }

      if(this.eventsAmount == this.emissionFrequency){
	 this.makeMetadataXML();
         this.collector.emit(new Values(this.metadataOutPath));
         this.eventsAmount = 0;
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
