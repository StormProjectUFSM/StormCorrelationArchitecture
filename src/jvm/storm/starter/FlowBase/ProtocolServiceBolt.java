package storm.starter.FlowBase;

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

public class ProtocolServiceBolt implements IRichBolt {
   private String politicsPath;
   private PoliticsXML configuration;
   private String packetProtocol;
   private String packetPort;
   private OutputCollector collector;

   public ProtocolServiceBolt(String politicsPath){
      this.politicsPath = politicsPath;
   }

   @Override
   public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
      this.configuration = new PoliticsXML(this.politicsPath);
      this.packetProtocol = this.configuration.getCIProtocol();
      this.packetPort = this.configuration.getCIPort();
      this.collector = collector;
   }

   @Override
   public void execute(Tuple tuple) {
      String[] fullPacket = tuple.getString(0).split(",");

      String rProtocol = fullPacket[3];
      String rDstPort = fullPacket[2];
      long rPacketSize = Long.parseLong(fullPacket[4]);
      if ((rProtocol.equals(this.packetProtocol)) && (rDstPort.equals(this.packetPort))){
         this.collector.emit(new Values(fullPacket[2], fullPacket[3], fullPacket[4], tuple.getString(0)));
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
