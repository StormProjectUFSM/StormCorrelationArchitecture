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

public class EventBolt implements IRichBolt {
   private String politicsPath;
   private PoliticsXML configuration;
   private long eventsAmount, emissionFrequency;
   private OutputCollector collector;

   public EventBolt(String politicsPath){
      this.politicsPath = politicsPath;
   }

   @Override
   public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
      this.configuration = new PoliticsXML(this.politicsPath);
      this.emissionFrequency = this.configuration.getCIEventsAmount();
      this.collector = collector;
   }

   @Override
   public void execute(Tuple tuple) {

      this.eventsAmount++;
      if(this.eventsAmount == this.emissionFrequency){
         this.collector.emit(new Values(tuple.getString(0), tuple.getString(1), tuple.getString(2), tuple.getString(3), ""));
         this.eventsAmount = 0;
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
