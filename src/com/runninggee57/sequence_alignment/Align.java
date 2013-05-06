package com.runninggee57.sequence_alignment;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Align {
  public static final int PEN_D = 5;
  public static final int PEN_E = 1;
  public static final String SEQFILENAME = "pair.txt";
  public static final String SUBFILENAME = "nucleotide_sub.txt";
  public static final int NEGINFINITY = Integer.MIN_VALUE + Math.max(Math.max(15, PEN_D), PEN_E);

  public static String seq1 = "", seq2 = "";

  public static ArrayList<Character> symbols = new ArrayList<Character>();

  public static GridCell M[][], Ix[][], Iy[][];
  
  public static SubMatrix sub;

  /**
   * @param args
   */
  public static void main(String[] args) {

    String seqs[] = readSeqFile(SEQFILENAME);
    seq1 = seqs[0];
    seq2 = seqs[1];
    System.out.println("Sequence 1: " + seq1);
    System.out.println("Sequence 2: " + seq2);
    sub = new SubMatrix(SUBFILENAME);
    System.out.println("Sub matrix read");

    M = new GridCell[seq1.length()][seq2.length()];
    Ix = new GridCell[seq1.length()][seq2.length()];
    Iy = new GridCell[seq1.length()][seq2.length()];

    // Initialize matrices
    for (int i = 0; i < seq1.length(); i++){
      if (i == 0) {
        M[i][0] = new GridCell(i, 0, 0);
        Ix[i][0] = new GridCell(i, 0, NEGINFINITY);
        Iy[i][0] = new GridCell(i, 0, NEGINFINITY);
      }
      else {
        M[i][0] = new GridCell(i, 0, 0, M[i-1][0]);
        Ix[i][0] = new GridCell(i, 0, NEGINFINITY, Ix[i-1][0]);
        Iy[i][0] = new GridCell(i, 0, NEGINFINITY, Iy[i-1][0]);
      }

    }
    for (int i = 1; i < seq2.length(); i++) {
      M[0][i] = new GridCell(0, i, 0, M[0][i-1]);
      Ix[0][i] = new GridCell(0, i, NEGINFINITY, Ix[0][i-1]);
      Iy[0][i] = new GridCell(0, i, NEGINFINITY, Iy[0][i-1]);
    }
    
    System.out.println("Matrices initialized");

    // Populate matrices
    GridCell max_gridcell = new GridCell(-1, -1);
    for (int i = 1; i < seq1.length(); i++) { //rows
      for (int j = 1; j < seq2.length(); j++) { //cols
        // M matrix
        int sub_value = sub.get(seq1.charAt(i), seq2.charAt(j));
        int m_score = (M[i - 1][j - 1]).val + sub_value;
        int ix_score = (Ix[i - 1][j - 1]).val + sub_value;
        int iy_score = (Iy[i - 1][j - 1]).val + sub_value;
        int max_val = Math.max(Math.max(m_score, ix_score), Math.max(iy_score, 0));
        if (max_val == ix_score) {
          M[i][j] = new GridCell(i, j, max_val, Ix[i-1][j-1]);
          if (max_val > max_gridcell.val) {
            max_gridcell = M[i][j];
          }
        }
        else if (max_val == m_score) {
          M[i][j] = new GridCell(i, j, max_val, M[i-1][j-1]);
          if (max_val > max_gridcell.val) {
            max_gridcell = M[i][j];
          }
        }
        else if (max_val == iy_score) {
          M[i][j] = new GridCell(i, j, max_val, Iy[i-1][j-1]);
          if (max_val > max_gridcell.val) {
            max_gridcell = M[i][j];
          }
        }
        else {
          M[i][j] = new GridCell(i, j);
        }

        // Ix matrix
        m_score = (M[i - 1][j]).val - PEN_D;
        ix_score = (Ix[i-1][j]).val - PEN_E;
        max_val = Math.max(m_score, ix_score);
        if (max_val == m_score) {
          Ix[i][j] = new GridCell(i, j, max_val, M[i-1][j]);
          if (max_val > max_gridcell.val) {
            max_gridcell = Ix[i][j];
          }
        }
        else {
          Ix[i][j] = new GridCell(i, j, max_val, Ix[i-1][j]);
          if (max_val > max_gridcell.val) {
            max_gridcell = Ix[i][j];
          }
        }

        // Iy matrix
        m_score = (M[i][j - 1]).val - PEN_D;
        iy_score = (Iy[i][j - 1]).val - PEN_E;
        max_val = Math.max(m_score, iy_score);
        if (max_val == m_score) {
          Iy[i][j] = new GridCell(i, j, max_val, Iy[i][j]);
          if (max_val > max_gridcell.val) {
            max_gridcell = Iy[i][j];
          }
        }
        else {
          Iy[i][j] = new GridCell(i, j, max_val, Ix[i][j]);
          if (max_val > max_gridcell.val) {
            max_gridcell = Ix[i][j];
          }
        }
      }
    }
    
    System.out.println("Optimal alignment computed, tracing back.");
    
    // Traceback, storing alignment
    boolean done = false;
    String new_seq1 = "";
    String new_seq2 = "";
    GridCell cur_cell = max_gridcell;
    int last_row = -1;
    int last_col = -1;
    while (!done) {
        if (last_row == cur_cell.row) {
            new_seq1 += '-';
        }
        else {
            new_seq1 += seq1.charAt(cur_cell.row);
        }

        if (last_col == cur_cell.col) {
            new_seq2 += '-';
        }
        else {
            new_seq2 += seq2.charAt(cur_cell.col);
        }

        last_row = cur_cell.row;
        last_col = cur_cell.col;
        cur_cell = cur_cell.bestAdj;
        if (cur_cell.row == 0 && cur_cell.col == 0) {
            done = true;
        }
    }

    String final_seq1 = "", final_seq2 = "";
    // Reverse sequences
    for (int i = new_seq1.length() - 1; i >= 0; i--) {
        final_seq1 += new_seq1.charAt(i);
    }
    for (int i = new_seq2.length() - 1; i >= 0; i--) {
      final_seq2 += new_seq2.charAt(i);
    }
    
    System.out.println("Sequence 1: " + final_seq1);
    System.out.println("Sequence 2: " + final_seq2);


  }

  public static String[] readSeqFile(String filename) {
    String[] seqs = new String[2];
    seqs[0] = "";
    seqs[1] = "";
    
    FileInputStream fs;
    try {
      fs = new FileInputStream(filename);
      int in_char = fs.read();
      while ((char)in_char != '>' && in_char != -1) // read to beginning of first sequence
        in_char = fs.read();

      in_char = fs.read();
      while ((char)in_char != '>') {
        seqs[0] += (char)in_char;
        in_char = fs.read();
      }
      String white_regex = "[\\s|\\r|\\n]";
      seqs[0] = seqs[0].replaceAll(white_regex, "");

      in_char = fs.read();
      while (in_char != -1) {
        seqs[1] += (char)in_char;
        in_char = fs.read();
      }
      seqs[1] = seqs[1].replaceAll(white_regex, "");
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
    
    return seqs;
  }
}
