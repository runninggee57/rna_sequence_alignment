package com.runninggee57.sequence_alignment;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class SubMatrix {

  private ArrayList<ArrayList<Integer>> mat;
  private ArrayList<Character> symbols;
  
  public SubMatrix() {
    mat = new ArrayList<ArrayList<Integer>>();
    symbols = new ArrayList<Character>();
  }
  
  public SubMatrix(String filename) {
    mat = new ArrayList<ArrayList<Integer>>();
    symbols = new ArrayList<Character>();
    readFromFile(filename);
  }
  
  public void add_symbol(char c) {
    if (!symbols.contains(c)) {
      symbols.add(c);
      ArrayList<Integer> newRow = new ArrayList<Integer>();
      for (int i = 0; i < symbols.size(); i++) {
        newRow.add(0);
        if (i != symbols.size() - 1) // add the new rightmost column
          mat.get(i).add(0);
      }
      mat.add(newRow); // add the new row
    }
  }
  
  public void set_val(char c1, char c2, int val) {
    mat.get(symbols.indexOf(c1)).set(symbols.indexOf(c2), val);
  }
  
  public int get(char c1, char c2) {
    return mat.get(symbols.indexOf(c1)).get(symbols.indexOf(c2));
  }
  
  private void readFromFile(String filename) {
    FileInputStream fs;
    try {
      fs = new FileInputStream(filename);
      int in_char = fs.read();
      int i = 0, j = 0;
      char cur_row_char = ' ';
      while (in_char != -1) {
        String int_str = "";
        if ((char)in_char == ' ') {
          // do nothing, space...
        }
        else if ((char)in_char == '\n') {
          i++;
          j = 0;
        }
        else {
          if (i == 0) { // reading single letters from top row
            add_symbol((char)in_char);
            j++;
          }
          else if (j == 0) { // reading char identifying a row
            cur_row_char = (char)in_char;
            j++;
          }
          else {
            while ((char)in_char != ' ') {
              int_str += (char)in_char;
              in_char = fs.read();
            }
            set_val(cur_row_char, symbols.get(j - 1), Integer.parseInt(int_str));
            set_val(symbols.get(j - 1), cur_row_char, Integer.parseInt(int_str));
            j++;
          }
        }
        in_char = fs.read();
      }
      fs.close();
    }
    catch (FileNotFoundException e) {
      System.out.println("Could not open file '" + filename + "'");
      System.exit(-1);
    }
    catch (IOException e) {
      System.out.println("There was an IO error while reading file '" + filename + "'");
      System.exit(-1);
    }
  }
  
  public String toString() {
    String ret = "   ";
    for (int i = 0; i < symbols.size(); i++) {
      ret += symbols.get(i) + "  ";
    }
    ret += '\n';
    for (int i = 0; i < mat.size(); i++) {
      for (int j = 0; j < mat.get(0).size(); j++) {
        if (j == 0) {
          ret += symbols.get(i) + " ";
        }
        
        int val = mat.get(i).get(j);
        if (val < 10 && val > -1)
          ret += ' ';
        
        ret += val + " ";
      }
      ret += '\n';
    }
    
    return ret;
  }
}
