package storm.starter.AlgorithmBase;

public class AuditElement {
   private String IP;
   private String MAC;
   private String Vendor;

   public AuditElement(String IP){
      this.IP = IP;
      this.MAC = "00:25:96:FF:FE:12:34:56";
      this.Vendor = "GIGAVISION srl";
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
}

