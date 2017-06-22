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
import org.apache.commons.io.FileUtils;

public class LogBolt implements IRichBolt {
   private String logOutPath;
   private String basePath, politicsPath, politicsName;
   private PoliticsXML configuration;
   private OutputCollector collector;

   public LogBolt(String basePath, String politicsName){
      this.politicsPath = basePath + politicsName;
      this.basePath = basePath;
      this.politicsName = politicsName;
   }

   @Override
   public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
      this.configuration = new PoliticsXML(this.politicsPath);
      this.logOutPath = this.configuration.getAILogLocal();
      this.collector = collector;
   }

   @Override
   public void execute(Tuple tuple) {
     try{
        CorrelationXML request = new CorrelationXML(tuple.getString(0));
        FileWriter finserter = new FileWriter(new File(this.logOutPath));
        finserter.write("============= POLITICS LOG =============\n\n");
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
	finserter.write("\n============= GENERAL LOG =============\n\n");
        while (request.chargeNextUnrecognized()){
            finserter.write("Data: " + request.getUnrecognizedData() + "\n");
	    if (!request.getUnrecognizedCounter().equals("")){
	        finserter.write("Counter: " + request.getUnrecognizedCounter() + "\n");
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
	 try{
	     File dir = new File(this.basePath + this.politicsName + "TMP");
             FileUtils.deleteDirectory(dir);
	 }
	 catch(IOException ex){}

//       File[] filesList = dir.listFiles();
//       for (File file : filesList) {
//          if ((file.isFile()) && (file.getName().startsWith(metaInit)) && (file.getName().endsWith(".xml"))){
//             file.delete();
//    	   }
//       }
   }

   @Override
   public void declareOutputFields(OutputFieldsDeclarer declarer) {}

   @Override
   public Map<String, Object> getComponentConfiguration() {
      return null;
   }
}
