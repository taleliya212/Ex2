/**
 * CellEntry class
 * The CellEntry class is designed to encapsulate references to spreadsheet cells, such as A1 or B2.
 * It validates cell references and converts them from string format (e.g., A1) to numeric indices ([0, 1]).
 * This ensures that all references in formulas are accurate and within the bounds of the spreadsheet.
 * It also assists in identifying whether a cell reference is valid and handles errors for invalid references.
 */

public class CellEntry  implements Index2D {
    private String cell;



    public CellEntry(String cell) {
        this.cell= cell;
    }

    @Override
    public boolean isValid() {
        if (cell == null || cell.length() < 2) return false;

        char xChar = cell.charAt(0);
        String yPart = cell.substring(1);

        if (!Character.isLetter(xChar)) return false;

        try {
            int yValue = Integer.parseInt(yPart);
            return yValue >= 0 && yValue <= 99;
        } catch (NumberFormatException e) {

            return false;
        }
    }

    @Override
    public int getX() {
        if (!isValid()) return Ex2Utils.ERR;
           return Character.toUpperCase(cell.charAt(0)) - 'A';
    }

    @Override
    public int getY() {
        if (!isValid()) return Ex2Utils.ERR;
     return Integer.parseInt(cell.substring(1) ) ;
    }

    public String toString(){
        return getX() + "," + getY();
    }
}