package storm.starter.ActionBase;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.lang.Math;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import storm.starter.AlgorithmBase.PoliticsXML;
import storm.starter.AlgorithmBase.CorrelationXML;
import storm.starter.AlgorithmBase.AuditElement;

import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;
import org.apache.commons.io.FileUtils;

public class AuditLogBolt implements IRichBolt {
   private String logOutPath;
   private String basePath, politicsPath, politicsName;
   private PoliticsXML configuration;
   private OutputCollector collector;
   private Integer logNumber;

   public AuditLogBolt(String basePath, String politicsName){
      this.politicsPath = basePath + politicsName;
      this.basePath = basePath;
      this.politicsName = politicsName;
      this.logNumber = 1;
   }

   @Override
   public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
      this.configuration = new PoliticsXML(this.politicsPath);
      this.logOutPath = this.configuration.getAILogLocal();
      this.collector = collector;
   }

   private String ARP(){
        Integer Second = (int) (Math.random() * 26);
        Integer Third = (int) (Math.random() * 26);
        String MACTuple = "";

        if (Second < 10) MACTuple += "0" + Second.toString() + ":";
        else MACTuple += Second.toString() + ":";
        if (Third < 10) MACTuple += "0" + Third.toString();
        else MACTuple += Third.toString();

        return "00:" + MACTuple + ":F5:FE:12:34:59";
   }

   private String MACVendor(String MAC){
      try{
      URL url = new URL("http://api.macvendors.com/" + MAC);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      String gotVendor = rd.readLine();
      rd.close();
      return gotVendor;
      }
      catch (Exception e) {
        return "Request Problem";
      }
   }


   @Override
   public void execute(Tuple tuple) {
     try{
	ArrayList<String> consideredIPs = new ArrayList<String> ();
	ArrayList<AuditElement> auditedIPs = new ArrayList<AuditElement> ();
        CorrelationXML request = new CorrelationXML(tuple.getString(0));
        FileWriter finserter = new FileWriter(new File(this.logOutPath + this.logNumber.toString() + ".txt"));
        this.logNumber++;
        finserter.write("============= AUDIT LOG =============\n\n");
	while (request.chargeNextElement()){
           String[] packets = request.getElementPackets().split("\n");
           for(String packet : packets){
	      String[] packetData = packet.split(",");
	      if (!consideredIPs.contains(packetData[0])){
		consideredIPs.add(packetData[0]);
		String gMAC = this.ARP();
		String gVendor = this.MACVendor(gMAC);
		auditedIPs.add(new AuditElement(packetData[0], gMAC, gVendor));
	      }
	      if (!consideredIPs.contains(packetData[1])){
                consideredIPs.add(packetData[1]);
		String gMAC = this.ARP();
		String gVendor = this.MACVendor(gMAC);
                auditedIPs.add(new AuditElement(packetData[1], gMAC, gVendor));
              }
           }
	   for(AuditElement audited : auditedIPs){
		finserter.write(audited.getIP() + " -> " + audited.getVendor() + "\n");
	   }
           finserter.write("\n");
        }
        finserter.close();
        request.close();
     }
     catch(IOException ex){}

     collector.ack(tuple);
   }

   @Override
   public void cleanup() {
	 try{
	     File dir = new File(this.basePath + this.politicsName + "TMP");
             FileUtils.deleteDirectory(dir);
	 }
	 catch(IOException ex){}

//       File[] filesList = dir.listFiles();
//       for (File file : filesList) {
//          if ((file.isFile()) && (file.getName().startsWith(metaInit)) && (file.getName().endsWith(".xml"))){
//             file.delete();
//    	   }
//       }
   }

   @Override
   public void declareOutputFields(OutputFieldsDeclarer declarer) {}

   @Override
   public Map<String, Object> getComponentConfiguration() {
      return null;
   }
}
