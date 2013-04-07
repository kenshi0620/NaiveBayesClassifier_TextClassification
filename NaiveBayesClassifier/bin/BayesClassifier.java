import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * A binary classifier implements a naive Bayes approach to classify blogs
 * 
 * @author Weijia Zhang
 * 
 */
public class BayesClassifier {

  private String C_LABLE = "con";

  private String L_LABLE = "lib";

  /**
   * Prior probablity of lable C
   */
  private double priorC;

  /**
   * Prior probablity of lable L
   */
  private double priorL;
  
  int m = 1;

  HashSet<String> words;

  /**
   * Mapping words to its conditional probablity given lable C
   * 
   * Key: word Value: conditional probablity given lable C
   */
  HashMap<String, Double> word2condiProbInC;

  /**
   * Mapping words to its conditional probablity given lable L
   * 
   * Key: word Value: conditional probablity given lable L
   */
  HashMap<String, Double> word2condiProbInL;

  /**
   * Constructure of the BayesClassifier.
   */
  BayesClassifier() {
    /* innitialize the prior probility according to uniform distribution */
    priorC = 0.5;
    priorL = 0.5;

    words = new HashSet<String>();

    word2condiProbInC = new HashMap<String, Double>();
    word2condiProbInL = new HashMap<String, Double>();
  }

  /**
   * Train the Naive Bayes Binary Classifier.
   * 
   * @param trainingDataFileNames
   *          A List of file names that is uesed as training data
   * @param trainingDataPath
   *          A String that shows the path of the training data folder
   */
  public void train(ArrayList<String> trainingDataFileNames, String trainingDataPath)
          throws FileNotFoundException {

    HashMap<String, Integer> word2freqInC = new HashMap<String, Integer>();
    HashMap<String, Integer> word2freqInL = new HashMap<String, Integer>();

    int wordsC = 0;
    int wordsL = 0;

    /* if no training data, return */
    if (trainingDataFileNames.size() == 0)
      return;

    /* # of C-labled articals */
    int cBlogs = 0;

    /* # of L-labled articals */
    int lBlogs = 0;

    /* for each file, update the word frequency and lable frequency */
    for (String fileName : trainingDataFileNames) {

      File file = new File((trainingDataPath + fileName).trim());

      if (fileName.contains(C_LABLE)) {
        cBlogs++;
        wordsC += countFrequency(file, word2freqInC);

      } else if (fileName.contains(L_LABLE)) {
        lBlogs++;
        wordsL += countFrequency(file, word2freqInL);

      } else {
        System.out.println("unexpected training file");
      }
    }

    /* calculate prior probability of lables */
    priorC = (double) cBlogs / (cBlogs + lBlogs);
    priorL = (double) lBlogs / (cBlogs + lBlogs);

    /* calculate conditional probablities given lables */
    updateWordSet(word2freqInC.keySet());
    updateWordSet(word2freqInL.keySet());

    calculateCondiProbGivenLable(word2condiProbInC, word2freqInC, wordsC);
    calculateCondiProbGivenLable(word2condiProbInL, word2freqInL, wordsL);
  }

  /**
   * Classifiy a list of articles use naive bayes method.
   * 
   * @param articleNames
   *          A List of file names that is to be classified
   * @param folderPath
   *          A String that shows the path of the data folder
   */
  public void classifyArticles(ArrayList<String> articleNames, String folderPath)
          throws FileNotFoundException {
    /* if no article, return */
    if (articleNames.size() == 0)
      return;

    /* classify each article and out put its lable */
    for (String articleName : articleNames) {

      File file = new File((folderPath + articleName).trim());
      Scanner sc = new Scanner(file);

      ArrayList<String> words = new ArrayList<String>();

      while (sc.hasNext()) {
        words.add(sc.next());
      }

      System.out.println(articleName + " " + classifyArticle(words));
    }
  }

  /**
   * Classify an artical use naive bayes method.
   * 
   * To prevent underflow applying a monotonic function on the values.
   * 
   * @see https://en.wikipedia.org/wiki/Naive_Bayes_classifier
   * 
   * @param words
   *          A words list of an artical
   * @return Returns the Lable for given artical.
   */
  public String classifyArticle(ArrayList<String> words) {

    double probL = Math.log(priorL);
    double probC = Math.log(priorC);

    for (String word : words) {
      word = word.toLowerCase().trim();
      if (word2condiProbInC.containsKey(word)) {
        probC += Math.log(word2condiProbInC.get(word));
      }
      if (word2condiProbInL.containsKey(word)) {
        probL += Math.log(word2condiProbInL.get(word));
      }
    }

    if (probL - probC > 0)
      return "L";
    else
      return "C";
  }

  /**
   * Calculate Conditional Probablity of words and update word2condiProb hashmap.
   * 
   * @param lable
   *          calculate the conditional probablity of words given lable
   */
  private void calculateCondiProbGivenLable(HashMap<String, Double> probMap,
          HashMap<String, Integer> counterMap, int tokens) {
    /* calculate conditional probablity of every words and update probability map */
    for (String word : words) {
      int freq = 0;
      if (counterMap.containsKey(word))
        freq = counterMap.get(word);
      double condiProb = (double) (freq+m) / (tokens + m*words.size());
      probMap.put(word, condiProb);
    }
  }

  /**
   * Update the words set
   * 
   * @param set
   *          A new set of words that need to be add to words set
   */
  private void updateWordSet(Set<String> set) {
    words.addAll(set);
  }

  /**
   * Count word frequency of a given file and update word2freq map
   * 
   * @param fileName
   *          the file given to be read.
   * @param word2freq
   *          a hashmpa store the mapping from word to # of accurance
   * @return total # of words appeared in this artical
   */
  @SuppressWarnings("finally")
  private int countFrequency(File fileName, HashMap<String, Integer> word2freq) {

    /* total number of words appeared in given file */
    int tokens = 0;

    try {
      Scanner sc = new Scanner(fileName);

      while (sc.hasNextLine()) {
        String current = sc.nextLine().toLowerCase().trim();
        if (word2freq.containsKey(current)) {
          int freq = word2freq.get(current) + 1;
          word2freq.put(current, freq);
        } else {
          word2freq.put(current, 1);
        }
        tokens++;
      }

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } finally {
      return tokens;
    }
  }
}
