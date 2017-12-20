package storm.starter.CorrelationBase;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import storm.starter.AlgorithmBase.CorrelationCreation;
import storm.starter.AlgorithmBase.PoliticsXML;

import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;

public class AckBolt implements IRichBolt {
   private long metadataOutID;
   private String metadataOutName, metadataOutPath;
   private String basePath, politicsPath, metadataPath, politicsName;
   private PoliticsXML configuration;
   private Map<String, String> ackMap;
   private OutputCollector collector;

   public AckBolt(String basePath, String politicsName){
      this.politicsPath = basePath + politicsName;
      this.basePath = basePath;
      this.politicsName = politicsName;
      this.metadataPath = basePath + politicsName + "TMP";
      new File(this.metadataPath).mkdir();
   }

   @Override
   public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
      this.configuration = new PoliticsXML(this.politicsPath);
      this.metadataOutName = this.configuration.getConfID();
      this.metadataOutID = 0;
      this.ackMap = new HashMap<String, String>();
      this.collector = collector;
   }

   @Override
   public void execute(Tuple tuple) {

      if (tuple.getString(0) != null){
          String call = tuple.getString(3);
          String[] packet = call.split(",");

	  if(packet.length == 8){
            if(!this.ackMap.containsKey(packet[7])){
              this.ackMap.remove(packet[7]);
            }
	    this.ackMap.put(packet[6], call);
	  }
      }

      if(tuple.getString(4) != null){
         this.metadataOutID++;
         this.metadataOutPath = this.metadataPath + "/" + this.metadataOutName + this.metadataOutID + ".xml";
         new CorrelationCreation(this.metadataOutPath, this.ackMap);
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
