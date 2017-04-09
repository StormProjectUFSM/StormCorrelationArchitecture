package storm.starter.CorrelationBase;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;

public class CompressionBolt implements IRichBolt {
   private long startTime, emissionFrequency;
   private String metadataOutPath;
   private List<String> compressMap;
   private OutputCollector collector;

   private void makeMetadataXML(){
     try{
        FileWriter finserter = new FileWriter(new File(this.metadataOutPath));
        finserter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<elementList>");
        for(String IP : compressMap){
            finserter.write("\n<element>\n      <ip>" + IP + "</ip>\n</element>");
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
      this.startTime = System.currentTimeMillis();
      this.emissionFrequency = 9000;
      this.metadataOutPath = "/home/storm/StormInfrastructure/Storm/apache-storm-1.0.3/examples/storm-starter/src/jvm/storm/starter/MetadataBase/Correlation.xml";
      this.compressMap = new ArrayList<String>();
      this.collector = collector;
   }

   @Override
   public void execute(Tuple tuple) {
      String call = tuple.getString(0);

      if(!compressMap.contains(call)){
         compressMap.add(call);
      }

      //System.out.println("\n\n\n\nTime: " + this.startTime + " " + this.timer.getTime() + " "  + (this.timer.getTime() - this.startTime) + "\n\n");
      if(System.currentTimeMillis() - this.startTime >= this.emissionFrequency){
	 this.makeMetadataXML();
         //Acorda o bolt de ação.
         this.metadataOutPath = "/home/storm/StormInfrastructure/Storm/apache-storm-1.0.3/examples/storm-starter/src/jvm/storm/starter/MetadataBase/Correlation2.xml"; //teste
         this.startTime = System.currentTimeMillis();
      }

      collector.ack(tuple);
   }

   @Override
   public void cleanup() {
    System.out.println("\n\n\n\n" + this.metadataOutPath + "\n\n\n\n\n");
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
