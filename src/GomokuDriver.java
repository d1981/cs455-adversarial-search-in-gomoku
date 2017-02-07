public class GomokuDriver {
   private static final int RACKETPORT = 17033;         // uses port 1237 on localhost  
   protected RacketClient rc;
   
   public GomokuDriver(String h, int p){
      rc = new RacketClient(h, p);
   }
  
   public static void main(String[] args){
      GomokuDriver client = new GomokuDriver("localhost", RACKETPORT);
   	client.run();
   }
   
   public void think(){
      try {
         String status = rc.gridIn.readLine().toLowerCase();
         System.out.println(status);
         }
      catch(Exception e) { e.printStackTrace(); }

   }
   
   public void run() {
    	       
          think();     
      
    }
}
