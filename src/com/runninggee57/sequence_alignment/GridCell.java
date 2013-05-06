package com.runninggee57.sequence_alignment;

public class GridCell {
  public int row;
  public int col;
  public int val;
  public GridCell bestAdj;
  
  public GridCell(int row, int col) {
    this.row = row;
    this.col = col;
    this.val = 0;
    bestAdj = null;
  }
  public GridCell(int row, int col, int val) {
    this.row = row;
    this.col = col;
    this.val = val;
    bestAdj = null;
  }
  public GridCell(int row, int col, int val, GridCell adj) {
    this.row = row;
    this.col = col;
    this.val = val;
    bestAdj = adj;
  }
}
