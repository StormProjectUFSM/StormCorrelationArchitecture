package storm.starter.ExecutionBase;

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
import storm.starter.FlowBase.*;
import storm.starter.TriggerBase.*;

public class CompressionTest{
	public static void main(String[] args) throws Exception{
    	Config config = new Config();
		config.setDebug(true);

    	BrokerHosts hosts = new ZkHosts("localhost:2181");
		SpoutConfig spoutConfig = new SpoutConfig(hosts, "Network", "/Network", "CompressionTestNetwork");
		spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
        KafkaSpout kafkaSpout = new KafkaSpout(spoutConfig);

		TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("call-log-reader-spout", kafkaSpout);
		builder.setBolt("call-log-PcapBolt-bolt", new PcapBolt("/home/storm/StormInfrastructure/Storm/apache-storm-1.0.3/examples/storm-starter/src/jvm/storm/starter/ExecutionBase/CompressionTest.xml")).shuffleGrouping("call-log-reader-spout");
		builder.setBolt("call-log-trigger-bolt", new ChronoBolt("/home/storm/StormInfrastructure/Storm/apache-storm-1.0.3/examples/storm-starter/src/jvm/storm/starter/ExecutionBase/CompressionTest.xml"))
        .fieldsGrouping("call-log-PcapBolt-bolt", new Fields("dstPort", "protocol", "size", "fullpacket"));
		builder.setBolt("call-log-correlation-bolt", new CompressionBolt("/home/storm/StormInfrastructure/Storm/apache-storm-1.0.3/examples/storm-starter/src/jvm/storm/starter/ExecutionBase/","CompressionTest.xml")).fieldsGrouping("call-log-trigger-bolt", new Fields("dstPort", "protocol", "size", "fullpacket", "trigger"));
		builder.setBolt("call-log-action-bolt", new LogBolt("/home/storm/StormInfrastructure/Storm/apache-storm-1.0.3/examples/storm-starter/src/jvm/storm/starter/ExecutionBase/","CompressionTest.xml")).fieldsGrouping("call-log-correlation-bolt", new Fields("request"));

        LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("CompressionTest", config, builder.createTopology());

        Thread.sleep(10000);
        KillOptions killOpts = new KillOptions();
        killOpts.set_wait_secs(1);		cluster.killTopologyWithOpts("CompressionTest", killOpts);
		cluster.shutdown();
    }
}