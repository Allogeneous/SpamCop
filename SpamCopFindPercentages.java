package spamcop;

public class SpamCopFindPercentages {
    
    
   public static double findStringPercentSimilarity(String one, String two, int ld){
      
      if(one.length() >= two.length()){
          return (double) (one.length() - ld) / one.length();
      }else{
          return (double) (two.length() - ld) / two.length();
      }
  }

  public static int findLevDistance(String one, String two){
      
      int l1 = one.length();
      int l2 = two.length();
      
      int[][] dist = new int[l1 +1][l2+2];
      
      for(int i = 0; i <= l2; i++){
          dist[0][i] = i;
      }
      
      for(int j = 0; j <= l1; j++){
          dist[j][0] = j;
      }
      
      for(int i = 1; i <= l1; i++){
          for(int j = 1; j <= l2; j++){
              if(one.toLowerCase().charAt(i - 1) == two.toLowerCase().charAt(j - 1)){
                  dist[i][j] = dist[i-1][j-1];
              }else{
                  dist[i][j] = min3Ints(1 + dist[i][j-1], 1 + dist[i-1][j], 1 + dist[i-1][j-1]);
              }
          }
      }
      
      return dist[l1][l2];
  }
  
  public static int min3Ints(int a, int b, int c){
        if(a < b && a < c){
            return a;
        }else if(b < c && b < a){
            return b;
        }else{
            return c;
        }
  }
  
  //My own word percent sorter
  
  public static double findWordPercent(String one, String two, String splitter){
      
      String[] sa1 = one.split(splitter);
      String[] sa2 = two.split(splitter);
      
      if(one.length() > two.length()){
        sa1 = two.split(splitter);
        sa2 = one.split(splitter);
      }
      
      int maxSize = sa2.length;

      int matches = 0;
      
      for(String s1 : sa1){
          for(String s2 : sa2){
              if(s1.equalsIgnoreCase(s2)){
                  matches++;
                  break;
              }
          }
      }
      double percent = (double) matches / maxSize;
    return percent;
  }
  
}
