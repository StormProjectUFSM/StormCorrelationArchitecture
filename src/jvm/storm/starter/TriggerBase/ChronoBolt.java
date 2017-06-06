package storm.starter.TriggerBase;

import java.util.Map;
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

public class ChronoBolt implements IRichBolt {
   private PoliticsXML configuration;
   private long startTime, emissionFrequency;
   private OutputCollector collector;

   @Override
   public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
      this.configuration = new PoliticsXML("/home/storm/StormInfrastructure/Storm/apache-storm-1.0.3/examples/storm-starter/src/jvm/storm/starter/MetadataBase/PoliticsConfigure.xml");
      this.startTime = System.currentTimeMillis();
      this.emissionFrequency = this.configuration.getCITimeMSAmount();
      this.collector = collector;
   }

   @Override
   public void execute(Tuple tuple) {

      if(System.currentTimeMillis() - this.startTime >= this.emissionFrequency){
          this.collector.emit(new Values(tuple.getString(0), tuple.getString(1), tuple.getString(2), tuple.getString(3), ""));
          this.startTime = System.currentTimeMillis();
      }
      else{
	  this.collector.emit(new Values(tuple.getString(0), tuple.getString(1), tuple.getString(2), tuple.getString(3), null));
      }

      collector.ack(tuple);
   }

   @Override
   public void cleanup() {}

   @Override
   public void declareOutputFields(OutputFieldsDeclarer declarer) {
       declarer.declare(new Fields("dstPort", "protocol", "size", "fullpacket", "trigger"));
   }

   @Override
   public Map<String, Object> getComponentConfiguration() {
      return null;
   }
}
