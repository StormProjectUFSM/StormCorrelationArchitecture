package storm.starter.ActionBase;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import storm.starter.AlgorithmBase.PoliticsXML;
import storm.starter.AlgorithmBase.CorrelationXML;

import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;

public class LogBolt implements IRichBolt {
   private PoliticsXML configuration;
   private String logOutPath;
   private boolean logOn;
   private OutputCollector collector;

   @Override
   public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
      this.configuration = new PoliticsXML("/home/storm/StormInfrastructure/Storm/apache-storm-1.0.3/examples/storm-starter/src/jvm/storm/starter/MetadataBase/PoliticsConfigure.xml");
      this.logOutPath = this.configuration.getAILogLocal();
      this.logOn = true;
      this.collector = collector;
   }

   @Override
   public void execute(Tuple tuple) {

     try{
        CorrelationXML request = new CorrelationXML(tuple.getString(0));
        FileWriter finserter = new FileWriter(new File(this.logOutPath));
        while (request.chargeNextElement()){
           finserter.write("Port: " + request.getElementPort() + "\n");
           if(!request.getElementCounter().equals("")){
              finserter.write("Counter: " + request.getElementCounter() + "\n");
           }
           String[] packets = request.getElementPackets().split("\n");
           for(String packet : packets){
              finserter.write("Packet: " + packet + "\n");
           }
           finserter.write("\n");
        }
        finserter.close();
        request.close();
     }
     catch(IOException ex){}

     collector.ack(tuple);
   }

   @Override
   public void cleanup() {
       String metaInit = this.configuration.getConfID();
       File dir = new File("/home/storm/StormInfrastructure/Storm/apache-storm-1.0.3/examples/storm-starter/src/jvm/storm/starter/MetadataBase");
       File[] filesList = dir.listFiles();

       for (File file : filesList) {
          if ((file.isFile()) && (file.getName().startsWith(metaInit)) && (file.getName().endsWith(".xml"))){
             file.delete();
    	   }
       }
   }

   @Override
   public void declareOutputFields(OutputFieldsDeclarer declarer) {}

   @Override
   public Map<String, Object> getComponentConfiguration() {
      return null;
   }
}
