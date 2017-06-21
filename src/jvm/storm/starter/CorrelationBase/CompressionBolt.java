package storm.starter.CorrelationBase;

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

public class CompressionBolt implements IRichBolt {
   private long metadataOutID;
   private String metadataOutName, metadataOutPath;
   private String basePath, politicsPath, metadataPath, politicsName;
   private PoliticsXML configuration;
   private List<String> compressMap, generalMap;
   private List<List<String>> compressPackets;
   private OutputCollector collector;

   public CompressionBolt(String basePath, String politicsName){
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
      this.compressMap = new ArrayList<String>();
      this.generalMap = new ArrayList<String>();
      this.compressPackets = new ArrayList<List<String>>();
      this.collector = collector;
   }

   @Override
   public void execute(Tuple tuple) {
      String call = tuple.getString(0);

      if (!call.equals("")){
          if(!compressMap.contains(call)){
             compressMap.add(call);
             compressPackets.add(new ArrayList<String>());
          }
          int index = compressMap.indexOf(call);
          if(!compressPackets.get(index).contains(tuple.getString(3))){
              compressPackets.get(index).add(tuple.getString(3));
          }
      }
      else{
          if(!generalMap.contains(tuple.getString(3))){
              generalMap.add(tuple.getString(3));
          }
      }

      if(tuple.getString(4) != null){
          this.metadataOutID++;
          this.metadataOutPath = this.metadataPath + "/" + this.metadataOutName + this.metadataOutID + ".xml";
          new CorrelationCreation(this.metadataOutPath, this.compressPackets, this.compressMap, this.generalMap);
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
