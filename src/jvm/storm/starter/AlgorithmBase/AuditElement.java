package storm.starter.AlgorithmBase;

import java.lang.Math;
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
      this.Vendor = this.MACVendor();
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

