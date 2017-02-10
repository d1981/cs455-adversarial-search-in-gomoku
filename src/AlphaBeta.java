import java.util.List;

class AlphaBeta{
   Node<String> dataTree;
   

   public static void main(String[] args){      
      Node<Integer> dataTree   = new Node<Integer>(1000000); 
      
      Node<Integer> childNode1 = new Node<Integer>(-1000000, dataTree);
      Node<Integer> childNode2 = new Node<Integer>(-1000000, dataTree);     
      Node<Integer> childNode3 = new Node<Integer>(-1000000, dataTree);
      
      childNode1.setParent(dataTree);
      childNode2.setParent(dataTree); 
      childNode3.setParent(dataTree);
   
      Node<Integer> childNode1Child1 = new Node<Integer>(7, childNode1);
      Node<Integer> childNode1Child2 = new Node<Integer>(2, childNode1);     
      Node<Integer> childNode1Child3 = new Node<Integer>(4, childNode1);
      
      childNode1Child1.setParent(childNode1);
      childNode1Child2.setParent(childNode1); 
      childNode1Child3.setParent(childNode1);
      
      Node<Integer> childNode2Child1 = new Node<Integer>(1, childNode2);
      Node<Integer> childNode2Child2 = new Node<Integer>(8, childNode2);     
      Node<Integer> childNode2Child3 = new Node<Integer>(9, childNode2);
      
      childNode2Child1.setParent(childNode2);
      childNode2Child2.setParent(childNode2); 
      childNode2Child3.setParent(childNode2);
      
      Node<Integer> childNode3Child1 = new Node<Integer>(5, childNode3);
      Node<Integer> childNode3Child2 = new Node<Integer>(6, childNode3);     
      Node<Integer> childNode3Child3 = new Node<Integer>(7, childNode3);
      
      childNode3Child1.setParent(childNode3);
      childNode3Child2.setParent(childNode3); 
      childNode3Child3.setParent(childNode3);

      System.out.println(AlphaBeta(dataTree));
   }
   
   public static int AlphaBeta(Node node){
       return maxValue(node, -Integer.MAX_VALUE, Integer.MAX_VALUE);
   }   
   
   public static int maxValue(Node node, int alpha, int beta){
       int v;
       if (node.isLeaf()){
         return (int)node.getData();
       }
       
       v = -Integer.MAX_VALUE;; // Negative infinity
       
       for (int i=0; i<node.children.size(); i++){
           v = Math.max( (int) minValue( (Node) node.children.get(i), alpha, beta), v);
           if (v >= beta){ return v;}
           alpha = Math.max(alpha, v);
       }
       return v;       
   }
   
   public static int minValue(Node node, int alpha, int beta){
       int v;
       if (node.isLeaf()){
         return (int)node.getData();
       }

       v = Integer.MAX_VALUE; // Positive infinity
       
       for (int i=0; i<node.children.size(); i++){
           v = Math.min( (int) maxValue( (Node) node.children.get(i), alpha, beta), v);
           if (v <= alpha){return v;}
           beta=Math.min(beta, v);
       }
       return v;       
   }
}