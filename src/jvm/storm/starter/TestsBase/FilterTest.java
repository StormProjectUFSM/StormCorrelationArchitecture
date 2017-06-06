package storm.starter.TestsBase;

import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.kafka.*;
import org.apache.storm.kafka.KafkaConfig;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.generated.KillOptions;
import storm.starter.CorrelationBase.*;
import storm.starter.ActionBase.*;
import storm.starter.PacketBase.*;
import storm.starter.TriggerBase.*;

public class FilterTest {
   public static void main(String[] args) throws Exception{
      Config config = new Config();
      config.setDebug(true);

      BrokerHosts hosts = new ZkHosts("localhost:2181");
      SpoutConfig spoutConfig = new SpoutConfig(hosts, "Network", "/Network", "STORM-ID");
      spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
      KafkaSpout kafkaSpout = new KafkaSpout(spoutConfig);

      TopologyBuilder builder = new TopologyBuilder();
      builder.setSpout("call-log-reader-spout", kafkaSpout);
      builder.setBolt("call-log-selection-bolt", new SelectionBolt())
      .shuffleGrouping("call-log-reader-spout");
      builder.setBolt("call-log-trigger-bolt", new EventBolt())
      .fieldsGrouping("call-log-selection-bolt", new Fields("dstPort", "protocol", "size", "fullpacket"));
      builder.setBolt("call-log-filter-bolt", new FilterBolt())
      .fieldsGrouping("call-log-trigger-bolt", new Fields("dstPort", "protocol", "size", "fullpacket", "trigger"));
      builder.setBolt("call-log-bolt", new LogBolt())
      .fieldsGrouping("call-log-filter-bolt", new Fields("request"));

      LocalCluster cluster = new LocalCluster();
      cluster.submitTopology("LogAnalyserStorm", config, builder.createTopology());

      Thread.sleep(10000);
      KillOptions killOpts = new KillOptions();
      killOpts.set_wait_secs(1);
      cluster.killTopologyWithOpts("LogAnalyserStorm", killOpts);
      cluster.shutdown();
   }
}

