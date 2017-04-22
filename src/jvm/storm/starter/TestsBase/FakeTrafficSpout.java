package storm.starter.TestsBase;

import java.util.*;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.lang.InterruptedException;

import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.topology.IRichSpout;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;

public class FakeTrafficSpout implements IRichSpout {
   private SpoutOutputCollector collector;
   private TopologyContext context;
   private FileInputStream packetStream;
   private BufferedReader csvReader;

   @Override
   public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
      try{
         this.context = context;
         this.collector = collector;
         this.packetStream = new FileInputStream("/home/storm/StormInfrastructure/Storm/apache-storm-1.0.3/examples/storm-starter/src/jvm/storm/starter/TestsBase/DoS.csv");
         this.csvReader = new BufferedReader(new InputStreamReader(this.packetStream));
      }
      catch (FileNotFoundException e) { }
   }

   @Override
   public void nextTuple() {
      String fullPacket;
      String[] packetData;
      Float sleepTime;

      try{
         fullPacket = this.csvReader.readLine();
         if (fullPacket.length() == 0){
            this.packetStream.getChannel().position(0);
	    fullPacket = this.csvReader.readLine();
         }
	 packetData = fullPacket.split(",");
         sleepTime = Float.parseFloat(packetData[0].replaceAll("\"", ""))*1000;
         Thread.sleep(sleepTime.intValue());
         collector.emit(new Values(packetData[1], packetData[2], packetData[3], packetData[4], packetData[5]));
      }
      catch (IOException e) { }
      catch (InterruptedException e) { }
   }

   @Override
   public void declareOutputFields(OutputFieldsDeclarer declarer) {
      declarer.declare(new Fields("srcID", "dstID", "protocol", "size", "payload"));
   }

   @Override
   public void close() {}

   public boolean isDistributed() {
      return false;
   }

   @Override
   public void activate() {}

   @Override
   public void deactivate() {}

   @Override
   public void ack(Object msgId) {}

   @Override
   public void fail(Object msgId) {}

   @Override
   public Map<String, Object> getComponentConfiguration() {
      return null;
   }
}
