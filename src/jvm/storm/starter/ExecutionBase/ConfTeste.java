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

public class ConfTeste{
	public static void main(String[] args) throws Exception{
    	Config config = new Config();
		config.setDebug(true);

    	BrokerHosts hosts = new ZkHosts("localhost:2181");
		SpoutConfig spoutConfig = new SpoutConfig(hosts, "Network", "/Network", "ConfTesteNetwork");
		spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
        KafkaSpout kafkaSpout = new KafkaSpout(spoutConfig);

		TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("call-log-reader-spout", kafkaSpout);
		builder.setBolt("call-log-correlationtype-bolt", new xxx("/home/storm/StormInfrastructure/Storm/apache-storm-1.0.3/examples/storm-starter/src/jvm/storm/starter/ExecutionBase/Politics.xml")).shuffleGrouping("call-log-reader-spout");
		builder.setBolt("call-log-trigger-bolt", new EventBolt("/home/storm/StormInfrastructure/Storm/apache-storm-1.0.3/examples/storm-starter/src/jvm/storm/starter/ExecutionBase/Politics.xml"))
        .fieldsGrouping("call-log-correlationtype-bolt", new Fields("dstPort", "protocol", "size", "fullpacket"));
		builder.setBolt("call-log-correlation-bolt", new Filter("/home/storm/StormInfrastructure/Storm/apache-storm-1.0.3/examples/storm-starter/src/jvm/storm/starter/ExecutionBase/","Politics.xml")).fieldsGrouping("call-log-trigger-bolt", new Fields("dstPort", "protocol", "size", "fullpacket", "trigger"));
		builder.setBolt("call-log-action-bolt", new Log("/home/storm/StormInfrastructure/Storm/apache-storm-1.0.3/examples/storm-starter/src/jvm/storm/starter/ExecutionBase/","Politics.xml")).fieldsGrouping("call-log-correlation-bolt", new Fields("request"));

        LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("ConfTeste", config, builder.createTopology());

        Thread.sleep(10000);
        KillOptions killOpts = new KillOptions();
        killOpts.set_wait_secs(1);		cluster.killTopologyWithOpts("ConfTeste", killOpts);
		cluster.shutdown();
    }
}