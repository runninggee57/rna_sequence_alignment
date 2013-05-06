package com.runninggee57.sequence_alignment.tests;

import static org.junit.Assert.*;
import org.junit.Test;
import com.runninggee57.sequence_alignment.SubMatrix;

public class SubMatrixTest {

  @Test
  public void test() {
    SubMatrix sub = new SubMatrix("nucleotide_sub.txt");
    
    System.out.println(sub);
  }

}
