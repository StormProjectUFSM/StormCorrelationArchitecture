package storm.starter.TestsBase;

import java.util.*;

import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.topology.IRichSpout;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;

public class FakeTrafficSpout implements IRichSpout {
   private SpoutOutputCollector collector;
   private boolean completed = false;
   private TopologyContext context;

   private Random randomGenerator = new Random();
   private String IPBase = "192.168.0.";

   @Override
   public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
      this.context = context;
      this.collector = collector;
   }

   @Override
   public void nextTuple() {
      String IPEmit = IPBase + randomGenerator.nextInt(255);
      this.collector.emit(new Values(IPEmit));
   }

   @Override
   public void declareOutputFields(OutputFieldsDeclarer declarer) {
      declarer.declare(new Fields("call"));
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
