/**
 * SCell
 * The SCell class represents an individual cell within the spreadsheet.
 * It stores the cell's content, which can be text, a numeric value, or a formula.
 * The class determines the type of the content and maintains a list of dependent cells if the content is a formula.
 * When a value is set, the SCell ensures that it is correctly categorized, and when dependencies exist, it dynamically updates them to maintain consistency.
 */

import java.util.ArrayList;
import java.util.List;



public class SCell implements Cell {
    private String line;
    private int type;
    private int order;
    private List<Cell> dependentCells;

    public SCell(String s) {
        this.dependentCells = new ArrayList<>();
        this.line = s;
        setData(s);
    }

    @Override
    public int getOrder() {
        if (type == Ex2Utils.NUMBER || type == Ex2Utils.TEXT) {
            return 0;
        }
        if (type == Ex2Utils.FORM) {
            int maxOrder = 0;
            for (int i = 0; i < dependentCells.size(); i++) {
                Cell c = dependentCells.get(i);
                maxOrder = Math.max(maxOrder, c.getOrder());
            }
            return maxOrder + 1;
        }
        return order;
    }

    @Override
    public String toString() {
        return getData();
    }

    @Override
public void setData(String s) {
        if (s == null || s.isEmpty()) {
            this.line = Ex2Utils.EMPTY_CELL;
            this.type = Ex2Utils.TEXT;
        } else {
            this.line = s;
            determineType();
        }
    }
        @Override
        public String getData () {
            return line;
        }



    private void determineType() {
        if (ex2.isNumber(line)) {
            this.type = Ex2Utils.NUMBER;
        } else if (ex2.isForm(line)) {
            this.type = Ex2Utils.FORM;
        } else if (ex2.isText(line)) {
            this.type = Ex2Utils.TEXT;
        } else {
            System.out.println("Error: Invalid formula detected: " + line);
            this.type =Ex2Utils.ERR_FORM_FORMAT;
        }

    }
    @Override
    public int getType() {
        return type;
    }

    @Override
    public void setType(int t) {
        this.type = t;
    }

    @Override
    public void setOrder(int t) {
        this.order = t;
    }
    public List<Cell> getDependentCells() {
        return dependentCells;
    }


}
