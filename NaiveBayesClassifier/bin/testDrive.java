import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Scanner;

public class testDrive {

  /**
   * @param args
   * @throws IOException 
   */
  public static void main(String[] args) throws IOException {
    
    String SPLIT_PATH = "/Users/zhangweijia/Documents/Java_workplace/NaiveBayesClassifier/resources/split/";
    String DATA_PATH = "/Users/zhangweijia/Documents/Java_workplace/NaiveBayesClassifier/resources/data/";
    
    BayesClassifier classifier = new BayesClassifier ();
    
    String trainingData = args[0];
    String testingData = args[1]; 
    
    ArrayList<String> trainFile 
      = getLinesFromFile(new File(SPLIT_PATH + trainingData));
    ArrayList<String> testFile 
      = getLinesFromFile(new File(SPLIT_PATH + testingData));
    
    classifier.train(trainFile, DATA_PATH);
    
    /*HashMap<Double, String> map = new HashMap<Double, String>();
    
    for (Map.Entry<String, Double> entry : classifier.word2condiProbInL.entrySet()) {
      String word = entry.getKey();
      Double condiProbGivenC = entry.getValue();
      Double condiProbGivenL;
      if (classifier.word2condiProbInC.containsKey(word)) condiProbGivenL = classifier.word2condiProbInC.get(word);
      else condiProbGivenL = Double.MIN_NORMAL;
      double logNum;
      //if (condiProbGivenC == 0 || condiProbGivenL == 0) logNum = 0;
      //else 
      logNum = Math.log((double)condiProbGivenC/condiProbGivenL);
      
      if (map.containsKey(logNum)) word = map.get(logNum) + "  " + word;
      map.put(logNum, word);
    }
    
    ArrayList<Double> ranking = new ArrayList<Double>();
    for (Map.Entry<Double, String> entry : map.entrySet()) {
      double value = entry.getKey();
      ranking.add(value);
    }
    Collections.sort(ranking, Collections.reverseOrder());
    
    for (int i=0; i<20; i++) {
      double value = ranking.get(i);
      String[] words = map.get(value).split("  ");
      for (int j=0; j<words.length; j++) {
        System.out.print("(" + words[j] + "  " + value+ "), ");
      }
    }*/
    classifier.classifyArticles(testFile, DATA_PATH);
  }
  
  public static ArrayList<String> getLinesFromFile (File file) throws FileNotFoundException {
    ArrayList<String> lines = new ArrayList<String> ();
    
    Scanner sc = new Scanner (file);
    while (sc.hasNext()) {
      lines.add(sc.next());
    }
    return lines;
  }
  

}
