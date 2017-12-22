package storm.starter.AlgorithmBase;

public class AuditElement {
   private String IP;
   private String MAC;
   private String Vendor;

   public AuditElement(String IP, String MAC, String Vendor){
      this.IP = IP;
      this.MAC = MAC;
      this.Vendor = Vendor;
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

