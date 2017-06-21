package storm.starter.FlowBase;

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

public class SyslogBolt implements IRichBolt {
   private OutputCollector collector;

   public SyslogBolt(String politicsPath){}

   @Override
   public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
      this.collector = collector;
   }

   @Override
   public void execute(Tuple tuple) {
      String[] fullPacket = tuple.getString(0).split("      ");

      if (fullPacket.length == 2){
	  //SYSLOG
          this.collector.emit(new Values("", "", "", tuple.getString(0)));
      }

      collector.ack(tuple);
   }

   @Override
   public void cleanup() {}

   @Override
   public void declareOutputFields(OutputFieldsDeclarer declarer) {
      declarer.declare(new Fields("dstPort", "protocol", "size", "fullpacket"));
   }

   @Override
   public Map<String, Object> getComponentConfiguration() {
      return null;
   }
}
