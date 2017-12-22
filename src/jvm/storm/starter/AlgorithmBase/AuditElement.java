package storm.starter.AlgorithmBase;

import java.util.Random;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AuditElement {
   private String IP;
   private String MAC;
   private String Vendor;

   public AuditElement(String IP){
      this.IP = IP;
      this.MAC = this.ARP();
      this.Vendor = this.LocalMACVendor();
   }

   private String ARP(){
	Random Numbers = new Random();
        Integer Second = Numbers.nextInt(2) + 10;
        Integer Third = Numbers.nextInt(10) + 10;
        String MACTuple = "";

	return "00:" + Second.toString() + ":" + Third.toString() + ":F5:FE:12:34:59";
   }

   private String LocalMACVendor(){
      String[] FirstVendors = {"INITIO CORPORATION", "Cisco Systems, Inc", "PROCESSOR SYSTEMS (I) PVT LTD", "Kontron America, Inc.", "Cisco Systems, Inc", "OOmon Inc.", "T.SQWARE", "Bosch Access Systems GmbH", "Broadcom", "SIRONA DENTAL SYSTEMS GmbH & Co. KG"};
      String[] SecondVendors = {"Maxanna Technology Co., Ltd.", "Intel Corporation", "Honeywell CMSS", "Fraunhofer FOKUS", "EverFocus Electronics Corp.", "EPIN Technologies, Inc.", "COTEAU VERT CO., LTD.", "CESNET", "BLX IC Design Corp., Ltd.", "Solteras, Inc."};

      String[] splittedMAC = this.MAC.split(":");
      if (Integer.parseInt(splittedMAC[1]) == 10){
	return FirstVendors[Integer.parseInt(splittedMAC[2]) - 10];
      }
      else{
	return SecondVendors[Integer.parseInt(splittedMAC[2]) - 10];
      }

   }

   private String MACVendor(){
      try{
      URL url = new URL("http://api.macvendors.com/" + this.MAC);
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

   public String getIP(){
      return this.IP;
   }

   public String getMAC(){
      return this.MAC;
   }

   public String getVendor(){
      return this.Vendor;
   }

//  public static void main(String [ ] args){
//     AuditElement lala = new AuditElement("192.168.0.0.1");
//     System.out.println("Vendor: " + lala.getVendor());
//  }
}

