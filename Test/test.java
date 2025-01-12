import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class test {


    @Test
    public void testIsNumber() {
        assertTrue(ex2.isNumber("123"));
        assertTrue(ex2.isNumber("-123"));
        assertTrue(ex2.isNumber("123.45"));
        assertFalse(ex2.isNumber("abc"));
        assertFalse(ex2.isNumber(null));
        assertFalse(ex2.isNumber(""));
    }

    @Test
    public void testIsText() {
        assertTrue(ex2.isText("Hello"));
        assertFalse(ex2.isText("123"));
        assertFalse(ex2.isText("=A1"));
    }

    @Test
    public void testIsCell() {
        assertTrue(ex2.isCell("A1"));
        assertTrue(ex2.isCell("B12"));
        assertFalse(ex2.isCell("1A"));
        assertFalse(ex2.isCell("123"));
        assertFalse(ex2.isCell(null));
    }

    @Test
    public void testIsForm() {
        assertTrue(ex2.isForm("=A1"));
        assertTrue(ex2.isForm("=(1+2)"));
        assertFalse(ex2.isForm("A1"));
        assertFalse(ex2.isForm("123"));
        assertFalse(ex2.isForm(null));
    }

    @Test
    public void testRemoveParentheses() {
        assertEquals("1+2", ex2.removeParentheses("(1+2)"));
        assertEquals("(1+2)+3", ex2.removeParentheses("((1+2)+3)"));
        assertEquals("1+2", ex2.removeParentheses("((1+2))"));
        assertEquals("1+2", ex2.removeParentheses("1+2"));
    }

    @Test
    public void testComputeHelper() {
        assertEquals(6, ex2.computeHelper("(1+2)*2"));
        assertEquals(7, ex2.computeHelper("3+2*2"));
        assertEquals(1.5, ex2.computeHelper("3/2"));
    }

    @Test
    public void testIsValid() {
        // Valid cells
        assertTrue(new CellEntry("A1").isValid());
        assertTrue(new CellEntry("B99").isValid());
        assertTrue(new CellEntry("Z0").isValid());

        // Invalid cells
        assertFalse(new CellEntry(null).isValid());
        assertFalse(new CellEntry("").isValid());
        assertFalse(new CellEntry("A").isValid()); // Missing row number
        assertFalse(new CellEntry("1A").isValid()); // Row starts with a number
        assertFalse(new CellEntry("AA1").isValid()); // Invalid column format
        assertFalse(new CellEntry("A100").isValid()); // Row number out of bounds
    }

    @Test
    public void testGetX() {
        CellEntry cellA1 = new CellEntry("A1");
        assertEquals(0, cellA1.getX());

        CellEntry cellZ99 = new CellEntry("Z99");
        assertEquals(25, cellZ99.getX());

        CellEntry invalidCell = new CellEntry("1A");
        assertEquals(Ex2Utils.ERR, invalidCell.getX());
    }

    @Test
    public void testGetY() {
        CellEntry cellA1 = new CellEntry("A1");
        assertEquals(1, cellA1.getY());

        CellEntry cellB99 = new CellEntry("B99");
        assertEquals(99, cellB99.getY());

        CellEntry invalidCell = new CellEntry("B100");
        assertEquals(Ex2Utils.ERR, invalidCell.getY());
    }

    @Test
    public void testToString() {
        CellEntry cellA1 = new CellEntry("A1");
        assertEquals("0,1", cellA1.toString());

        CellEntry cellZ99 = new CellEntry("Z99");
        assertEquals("25,99", cellZ99.toString());

        CellEntry invalidCell = new CellEntry("1A");
        assertEquals(Ex2Utils.ERR + "," + Ex2Utils.ERR, invalidCell.toString());
    }

    @Test
    public void testSetDataAndGetData() {
        SCell cell = new SCell("123");
        assertEquals("123", cell.getData());

        cell.setData("Hello");
        assertEquals("Hello", cell.getData());

        cell.setData(null);
        assertEquals(Ex2Utils.EMPTY_CELL, cell.getData());
    }

    @Test
    public void testDetermineType() {
        SCell numberCell = new SCell("123");
        assertEquals(Ex2Utils.NUMBER, numberCell.getType());

        SCell formCell = new SCell("=A1+2");
        assertEquals(Ex2Utils.FORM, formCell.getType());

        SCell textCell = new SCell("Hello");
        assertEquals(Ex2Utils.TEXT, textCell.getType());

        SCell invalidCell = new SCell("=A1+");
        assertEquals(Ex2Utils.ERR_FORM_FORMAT, invalidCell.getType());
    }

    @Test
    public void testInitialization() {
        Ex2Sheet sheet = new Ex2Sheet(3, 3);
        assertEquals(3, sheet.width());
        assertEquals(3, sheet.height());

        for (int i = 0; i < sheet.width(); i++) {
            for (int j = 0; j < sheet.height(); j++) {
                assertEquals(Ex2Utils.EMPTY_CELL, sheet.value(i, j));
            }
        }
    }

    @Test
    public void testSetAndGetValue() {
        Ex2Sheet sheet = new Ex2Sheet(3, 3);
        sheet.set(1, 1, "123");
        assertEquals("123.0", sheet.value(1, 1));

        sheet.set(0, 0, "=A1+2");
        assertEquals("ERR_FORM!", sheet.value(0, 0)); // No A1 defined yet

    }

    @Test
    public void testIsIn() {
        Ex2Sheet sheet = new Ex2Sheet(3, 3);

        assertTrue(sheet.isIn(0, 0));
        assertTrue(sheet.isIn(2, 2));
        assertFalse(sheet.isIn(-1, 0));
        assertFalse(sheet.isIn(3, 3));
    }


}