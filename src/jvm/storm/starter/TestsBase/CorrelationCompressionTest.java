package storm.starter.TestsBase;

import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import storm.starter.CorrelationBase.*;
import storm.starter.ActionBase.*;

public class CorrelationCompressionTest{
   public static void main(String[] args) throws Exception{
      Config config = new Config();
      config.setDebug(true);

      TopologyBuilder builder = new TopologyBuilder();
      builder.setSpout("call-log-reader-spout", new FakeTrafficSpout());
      //builder.setBolt("call-log-compress-bolt", new ChronoCompressionBolt())
      builder.setBolt("call-log-compress-bolt", new EventCompressionBolt())
      .fieldsGrouping("call-log-reader-spout", new Fields("srcID", "dstID", "protocol", "size", "payload"));
      builder.setBolt("call-log-bolt", new LogBolt())
      .fieldsGrouping("call-log-compress-bolt", new Fields("request"));

      LocalCluster cluster = new LocalCluster();
      cluster.submitTopology("LogAnalyserStorm", config, builder.createTopology());

      Thread.sleep(10000);
      cluster.shutdown();
   }
}

