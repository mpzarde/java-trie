package com.truecool.trie;

import com.truecool.trie.NonUniqueKeyException;
import com.truecool.trie.Trie;

/**
 * Created by IntelliJ IDEA.
 * User: mpzarde
 * Date: Jul 17, 2006
 * Time: 8:57:35 PM
 */
public class TrieRunner {

  public static String input(String prompt) {
    StringBuffer buildInput = new StringBuffer();
    char b = ' ';

    System.out.print(prompt);
    System.out.flush();

    do {
      try {
        b = (char) System.in.read();
      }
      catch (java.io.IOException e) {
        //  generally an IOException here means that
        //  the player in question has disconnected
        System.out.println(e.toString());
        System.exit(1);
      }
      if (b != '\n' && b != 0) {
        buildInput.append(b);
      }
    } while (b != '\n' && b != 0);

    return (new String(buildInput));
  }


  /**
   * This is a simple little test routine
   * included so this class can be run as
   * an application to test its cababilities
   */
  public static void main(String args[]) {
    Trie main = new Trie();
    String entry;

    do {
      System.out.println("\ncom.truecool.trie.Trie contains " + main.length() + " elements");
      entry = input("dump,quit,add,search,delete,avg,num: ");
      if (entry.length() == 0) {
      } else if (entry.equals("avg")) {
        System.out.println("\nAverage clash: " + Float.toString(main.averageClash()) + "\n");
      } else if (entry.equals("num")) {
        System.out.println("\nTotal number of tries: " + main.numberTries() + "\n");
      } else if (entry.equals("dump")) {
        System.out.println("\nDUMPING:\n--------");
        main.dump();
      } else if (entry.startsWith("search ")) {
        entry = entry.substring(7);
        System.out.println("Searching for " + entry + "...");
        Object result = null;
        result = main.search(entry);
        if (result == null) {
          System.out.println("Not found");
        } else if (result instanceof Trie) {
          System.out.println("Multiple matches:");
          Trie trie = (Trie) result;
          trie.quickDump();
        } else {
          System.out.println("Found. Value is " + result.toString());
        }
      } else if (entry.equals("quit")) {
      } else if (entry.startsWith("delete ")) {
        entry = entry.substring(7);
        System.out.println("Deleting " + entry + "...");
        try {
          main.remove(entry);
        }
        catch (java.util.NoSuchElementException e) {
          System.out.println(e.toString());
        }
        catch (NonUniqueKeyException e) {
          System.out.println(e.toString());
        }
      } else if (entry.startsWith("add ")) {
        entry = entry.substring(4);
        System.out.println("Inserting " + entry + "...");
        try {
          main.insert(entry, entry);
        }
        catch (NonUniqueKeyException e) {
          System.out.println(e.toString());
        }
      } else {
        System.out.println("Unknown command '" + entry + "'");
      }
    } while (!entry.equals("quit"));
  }

}
