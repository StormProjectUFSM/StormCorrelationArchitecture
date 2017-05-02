
package storm.starter.PacketBase;

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

public class SelectionBolt implements IRichBolt {
   private PoliticsXML configuration;
   private long minPacketSize, maxPacketSize;
   private String packetProtocol;
   private OutputCollector collector;

   @Override
   public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
      this.configuration = new PoliticsXML("/home/storm/StormInfrastructure/Storm/apache-storm-1.0.3/examples/storm-starter/src/jvm/storm/starter/MetadataBase/PoliticsConfigure.xml");
      this.packetProtocol = this.configuration.getCIProtocol();
      this.minPacketSize = this.configuration.getCIMinPacketSize();
      this.maxPacketSize = this.configuration.getCIMaxPacketSize();
      this.collector = collector;
   }

   @Override
   public void execute(Tuple tuple) {
      String rProtocol = tuple.getString(3);
      long rPacketSize = Long.parseLong(tuple.getString(4));

      if ((rProtocol.equals(this.packetProtocol)) && (rPacketSize >= this.minPacketSize) && (rPacketSize <= this.maxPacketSize)){
         this.collector.emit(new Values(tuple.getString(2), tuple.getString(3), tuple.getString(4)));
      }
      collector.ack(tuple);
   }

   @Override
   public void cleanup() {}

   @Override
   public void declareOutputFields(OutputFieldsDeclarer declarer) {
      declarer.declare(new Fields("dstPort", "protocol", "size"));
   }

   @Override
   public Map<String, Object> getComponentConfiguration() {
      return null;
   }
}
