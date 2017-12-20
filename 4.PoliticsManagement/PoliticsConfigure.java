import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class PoliticsConfigure{

	private FileWriter stormTopology;
	private PoliticsXML politics;

	public PoliticsConfigure(String basePath, String politicsFile){
		try{
			this.politics = new PoliticsXML(basePath+politicsFile);
			File outputFile = new File(basePath + this.politics.getConfID() + ".java");
			if(outputFile.exists()){
				outputFile.delete();
			}
			outputFile.createNewFile();
			this.stormTopology = new FileWriter(outputFile, true);

			this.stormTopology.write("package storm.starter.ExecutionBase;\n\nimport org.apache.storm.tuple.Fields;\nimport org.apache.storm.tuple.Values;" + 
								"\nimport org.apache.storm.Config;\nimport org.apache.storm.LocalCluster;\nimport org.apache.storm.topology.TopologyBuilder;" +
								"\nimport org.apache.storm.kafka.*;\nimport org.apache.storm.kafka.KafkaConfig;\nimport org.apache.storm.spout.SchemeAsMultiScheme;" +
								"\nimport org.apache.storm.generated.KillOptions;\nimport storm.starter.CorrelationBase.*;\nimport storm.starter.ActionBase.*;" +
								"\nimport storm.starter.FlowBase.*;\nimport storm.starter.TriggerBase.*;\n");

			this.stormTopology.write("\npublic class " + this.politics.getConfID() + "{\n");

			this.stormTopology.write("	public static void main(String[] args) throws Exception{\n");
			this.stormTopology.write("		Config config = new Config();\n");
			this.stormTopology.write("		config.setDebug(true);\n\n");

			this.stormTopology.write("    	BrokerHosts hosts = new ZkHosts(\"localhost:2181\");\n");
			this.stormTopology.write("		SpoutConfig spoutConfig = new SpoutConfig(hosts, \"Network\", \"/Network\", \"" + this.politics.getConfID() + "Network\");\n");
			this.stormTopology.write("		spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());\n");
			this.stormTopology.write("		KafkaSpout kafkaSpout = new KafkaSpout(spoutConfig);\n\n");

			this.stormTopology.write("		TopologyBuilder builder = new TopologyBuilder();\n");
			this.stormTopology.write("		builder.setSpout(\"call-log-reader-spout\", kafkaSpout);\n");

			this.stormTopology.write("		builder.setBolt(\"call-log-trigger-bolt\", new " + this.politics.getBITrigger() + "(\"" + basePath + politicsFile + "\"))\n");
			this.stormTopology.write("		.shuffleGrouping(\"call-log-reader-spout\");\n");

			this.stormTopology.write("		builder.setBolt(\"call-log-correlationtype-bolt\", new " + this.politics.getBICorrelationType() + "(\"" + basePath + politicsFile + "\"))\n");
			this.stormTopology.write("		.fieldsGrouping(\"call-log-trigger-bolt\", new Fields(\"fullpacket\", \"trigger\"));\n");

			this.stormTopology.write("		builder.setBolt(\"call-log-correlation-bolt\", new " + this.politics.getBICorrelation() + "(\"" + basePath + "\",\"" + politicsFile + "\"))\n");
			this.stormTopology.write("		.fieldsGrouping(\"call-log-correlationtype-bolt\", new Fields(\"dstPort\", \"protocol\", \"size\", \"fullpacket\", \"trigger\"));\n");

			this.stormTopology.write("		builder.setBolt(\"call-log-action-bolt\", new " + this.politics.getBIAction() + "(\"" + basePath + "\",\"" + politicsFile + "\"))\n");
			this.stormTopology.write("		.fieldsGrouping(\"call-log-correlation-bolt\", new Fields(\"request\"));\n\n");

			this.stormTopology.write("		LocalCluster cluster = new LocalCluster();\n");
			this.stormTopology.write("		cluster.submitTopology(\"" + this.politics.getConfID() + "\", config, builder.createTopology());\n\n");

			this.stormTopology.write("		Thread.sleep(10000);\n");
			this.stormTopology.write("		KillOptions killOpts = new KillOptions();\n");
			this.stormTopology.write("		killOpts.set_wait_secs(1);\n");
			this.stormTopology.write("		cluster.killTopologyWithOpts(\"" + this.politics.getConfID() + "\", killOpts);\n");
			this.stormTopology.write("		cluster.shutdown();\n");
			this.stormTopology.write("	}\n");
			this.stormTopology.write("}");

			this.stormTopology.close();
		}
		catch(IOException e){}
	}

	public static void main(String [ ] args){
		if (args.length == 1){
			PoliticsConfigure teste = new PoliticsConfigure("/home/storm/StormInfrastructure/Storm/apache-storm-1.0.3/examples/storm-starter/src/jvm/storm/starter/ExecutionBase/", args[0]);
		}
		else{
			System.out.println("USAGE: politics_name.xml");
		}
	}
}
