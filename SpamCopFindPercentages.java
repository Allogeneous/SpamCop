package spamcop;

public class SpamCopFindPercentages {
    /* 
     Levenshtein Distance strongly adapted from:
     http://rosettacode.org/wiki/Levenshtein_distance#Java
  */
    
  public static double findCharacterPercent(String s1, String s2) {
    String longer = s1, shorter = s2;
    if (s1.length() < s2.length()) { 
      longer = s2; shorter = s1;
    }
    int longerLength = longer.length();
    if (longerLength == 0) { 
        return 0.0; 
    }
   
    
    return (longerLength - editDistanceCharacters(longer, shorter)) / (double) longerLength;
  }

  public static int editDistanceCharacters(String s1, String s2) {
    s1 = s1.toLowerCase();
    s2 = s2.toLowerCase();

    int[] costs = new int[s2.length() + 1];
    for (int i = 0; i <= s1.length(); i++) {
      int lastValue = i;
      for (int j = 0; j <= s2.length(); j++) {
        if (i == 0){
          costs[j] = j;
        }else {
          if (j > 0) {
            int newValue = costs[j - 1];
            if (s1.charAt(i - 1) != s2.charAt(j - 1)){
              newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
            }
            costs[j - 1] = lastValue;
            lastValue = newValue;
          }
        }
      }
      if (i > 0){
        costs[s2.length()] = lastValue;
      }
    }
    return costs[s2.length()];
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
