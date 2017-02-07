import java.util.List;

class MiniMax{
   Node<String> dataTree;
   

   public static void main(String[] args){      
      Node<Integer> dataTree   = new Node<Integer>(1000000); 
      
      Node<Integer> childNode1 = new Node<Integer>(-1000000, dataTree);
      Node<Integer> childNode2 = new Node<Integer>(-1000000, dataTree);     
      Node<Integer> childNode3 = new Node<Integer>(-1000000, dataTree);
      
      childNode1.setParent(dataTree);
      childNode2.setParent(dataTree); 
      childNode3.setParent(dataTree);
   
      Node<Integer> childNode1Child1 = new Node<Integer>(3, childNode1);
      Node<Integer> childNode1Child2 = new Node<Integer>(12, childNode1);     
      Node<Integer> childNode1Child3 = new Node<Integer>(8, childNode1);
      
      childNode1Child1.setParent(childNode1);
      childNode1Child2.setParent(childNode1); 
      childNode1Child3.setParent(childNode1);
      
      Node<Integer> childNode2Child1 = new Node<Integer>(2, childNode2);
      Node<Integer> childNode2Child2 = new Node<Integer>(4, childNode2);     
      Node<Integer> childNode2Child3 = new Node<Integer>(6, childNode2);
      
      childNode2Child1.setParent(childNode2);
      childNode2Child2.setParent(childNode2); 
      childNode2Child3.setParent(childNode2);
      
      Node<Integer> childNode3Child1 = new Node<Integer>(14, childNode3);
      Node<Integer> childNode3Child2 = new Node<Integer>(5, childNode3);     
      Node<Integer> childNode3Child3 = new Node<Integer>(2, childNode3);
      
      childNode3Child1.setParent(childNode3);
      childNode3Child2.setParent(childNode3); 
      childNode3Child3.setParent(childNode3);

      System.out.println(miniMax(dataTree));
   }
   
   public static int miniMax(Node node){
       return minValue(node);
   }   
   
   public static int maxValue(Node node){
       int v, w;
       if (node.isLeaf() == true){
         return (int)node.getData();
       }
       
       w = -Integer.MAX_VALUE;; // Negative infinity
       
       for (int i=0; i<node.children.size(); i++){
           v = (int)maxValue((Node)node.children.get(i));
           if (v > w){
              w = v;
           }
       }
       return w;       
   }
   
   public static int minValue(Node node){
       int v, w;
       if (node.isLeaf() == true){
         return (int)node.getData();
       }

       w = Integer.MAX_VALUE; // Positive infinity
       
       for (int i=0; i<node.children.size(); i++){
           v = (int)maxValue((Node)node.children.get(i));
           if (v < w){
              w = v;
           }
       }
       return w;       
   }
}