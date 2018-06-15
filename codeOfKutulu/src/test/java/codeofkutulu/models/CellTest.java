package codeofkutulu.models;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;

public class CellTest {

  @Test
  public void invalidCellIsInvalid() throws Exception {
    assertThat(Cell.INVALID_CELL.isInvalidCell(), is(true));
  }
  
  @Test
  public void standardDistanceIs1() throws Exception {
    assertThat(new Cell().getStandardPathLength(), is(1));
  }

  @Test
  public void minionStandardDistanceIs1() throws Exception {
    assertThat(new Cell().getMinionPathLength(), is(1));
  }
  
  @Test
  public void minionRepulseDistanceIs5() throws Exception {
    Cell cell = new Cell();
    cell.addMinionRepulseEffect();
    assertThat(cell.getMinionPathLength(), is(5));
  }
}
