/**
 * Ex2Sheet class
 * The Ex2Sheet class is the central class of the project, representing the spreadsheet itself.
 * It manages a 2D grid of cells (Cell[][]) and provides functionality for key operations such as setting cell values, evaluating formulas, and saving/loading the spreadsheet.
 * This class also handles cycle detection among cell dependencies, ensuring that invalid references or circular dependencies are flagged appropriately.
 * It acts as the core of the system, orchestrating the interactions between other components.
 */


import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class Ex2Sheet implements Sheet {
    private Cell[][] table;


    public Ex2Sheet(int x, int y) {
        table = new SCell[x][y];
        for(int i=0;i<x;i=i+1) {
            for(int j=0;j<y;j=j+1) {
                table[i][j] = new SCell(Ex2Utils.EMPTY_CELL);
            }
        }
        eval();
    }
    public Ex2Sheet() {
        this(Ex2Utils.WIDTH, Ex2Utils.HEIGHT);
    }

    @Override
    public String value(int x, int y) {
        String ans = Ex2Utils.EMPTY_CELL;

        Cell c = get(x,y);
        if(c!=null) {
            if (c.getType() == Ex2Utils.ERR_FORM_FORMAT) {
                ans = Ex2Utils.ERR_FORM;
            } else if (c.getType() == Ex2Utils.ERR_CYCLE_FORM) {
                ans = Ex2Utils.ERR_CYCLE;
            } else if (c.getType() == Ex2Utils.FORM) {
                ans = eval(x, y);
            } else if (c.getType() == Ex2Utils.NUMBER) {
                ans = Double.parseDouble(c.getData()) + "";
            } else {
                ans = c.getData();
            }
        }

        return ans;
    }
    @Override
    public Cell get(int x, int y) {
        if (!isIn(x, y)) {
            return null;
        }
        return table[x][y];
    }

    @Override
    public Cell get(String cords) {
        Cell ans = null;
        if (cords != null || !cords.isEmpty() ) {
            CellEntry entry = new CellEntry(cords);
            if (entry.isValid()) {
                ans = get(entry.getX(), entry.getY());
            }
        }
            return ans;
        }

    @Override
    public int width() {
        return table.length;
    }
    @Override
    public int height() {
        return table[0].length;
    }
    @Override
    public void set(int x, int y, String s) {
        if (!isIn(x, y))
            return;
        if (s == null) {
            s = Ex2Utils.EMPTY_CELL;
        }
            Cell c = new SCell(s);
            table[x][y] = c;
            eval();
        }

    @Override
    public void eval() {
        int[][] dd = depth();
        int maxDepth = 0;

        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                if (dd[i][j] > maxDepth)
                    maxDepth = dd[i][j];
                }
            }
        for (int d = 0; d <= maxDepth; d++) {
            for (int i = 0; i < width(); i++) {
                for (int j = 0; j < height(); j++){
                        Cell cell = get(i, j);
                    if(cell.getData().length() > 0) {
                        System.out.printf("Processing cell [%d,%d] (%s) depth: %d type: %d%n",
                                i, j, cell.getData(), dd[i][j], cell.getType());
                    }

                    if (dd[i][j] == -1) {
                        if (cell != null) {
                            cell.setType(Ex2Utils.ERR_CYCLE_FORM);
                        }
                    }
                    else if (dd[i][j] == d) {
                        eval(i, j);
                    }
                }
            }
        }
    }
    @Override
    public boolean isIn(int xx, int yy) {
        return  xx>=0 && yy>=0 && xx < width() && yy < height();
    }

    @Override
    public int[][] depth() {
        int[][] ans = new int[width()][height()];
        boolean[][] visiting = new boolean[width()][height()];

        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                ans[x][y] = -2;
            }
        }
        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                if (ans[x][y] == -2 ) {
                    calculDepth (x, y, ans, visiting);
                }
            }
        }
        return ans;
        }

    private int calculDepth(int x, int y, int[][] depths, boolean[][] visiting) {
        if(depths[x][y] == -1) return -1;

        if(visiting[x][y]) {
            depths[x][y] = -1;

        get(x,y).setType(Ex2Utils.ERR_CYCLE_FORM);
        return -1;
    }
      if(depths[x][y] >= 0) return depths[x][y];

        visiting[x][y] = true;

        Cell cell = get(x, y);
        if (cell == null || cell.getType() != Ex2Utils.FORM) {
            depths[x][y] = 0;
            visiting[x][y] = false;
            return 0;
        }




        List<CellEntry> refs = findCellReferences(cell.getData());
        int maxDepth = 0;
        boolean hasCycle = false;

        for(CellEntry ref : refs) {
            if(ref.isValid() && isIn(ref.getX(), ref.getY())) {
                int depthResult = calculDepth(ref.getX(), ref.getY(), depths, visiting);
                if(depthResult == -1) {
                    hasCycle = true;
                    break;
                }
                maxDepth = Math.max(maxDepth, depthResult);
            }
        }

        visiting[x][y] = false;

        if(hasCycle) {
            depths[x][y] = -1;
            cell.setType(Ex2Utils.ERR_CYCLE_FORM);
            return -1;
        } else {
            depths[x][y] = maxDepth + 1;
            return depths[x][y];
        }
    }

    @Override
    public void load(String fileName) throws IOException {

        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                table[i][j] = new SCell(Ex2Utils.EMPTY_CELL);
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {

            String line = reader.readLine();

            while ((line = reader.readLine()) != null) {

                String[] parts = line.split(",", 3);

                if (parts.length >= 3) {
                    try {
                        int x = Integer.parseInt(parts[0].trim());
                        int y = Integer.parseInt(parts[1].trim());
                        String value = parts[2].trim();


                        int commentStart = value.indexOf(',');
                        if (commentStart != -1) {
                            value = value.substring(0, commentStart).trim();
                        }


                        if (isIn(x, y)) {
                            set(x, y, value);

                        }
                    } catch (NumberFormatException e) {


                    }
                }
            }
        }


        eval();
    }

    @Override
    public void save(String fileName) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {

            writer.println("I2CS ArielU: SpreadSheet (Ex2) assignment");


            for (int i = 0; i < width(); i++) {
                for (int j = 0; j < height(); j++) {
                    Cell cell = get(i, j);
                    if (cell != null) {
                        String data = cell.getData();

                        if (!data.equals(Ex2Utils.EMPTY_CELL)) {

                            writer.printf("%d,%d,%s%n", i, j, data);
                        }
                    }
                }
            }
        }
    }

    @Override
    public String eval(int x, int y) {
        if (!isIn(x, y)) {
            return null;
        }
        Cell cell = get(x, y);

        if (cell == null) {
            return null;
        }


        if (hasCycle(x, y, new boolean[width()][height()])) {
            cell.setType(Ex2Utils.ERR_CYCLE_FORM);
            return Ex2Utils.ERR_CYCLE;
        }
        if (cell.getType() == Ex2Utils.TEXT || cell.getType() == Ex2Utils.NUMBER) {
            return cell.getData();
        }
        else if (cell.getType() == Ex2Utils.FORM) {
            ex2.setSpreadsheet(this);
            Double result = ex2.computeForm(cell.getData());

            if (result != null) {
                return Double.toString(result);
            }
            cell.setType(Ex2Utils.ERR_FORM_FORMAT);
            return Ex2Utils.ERR_FORM;
        } else if (cell.getType() == Ex2Utils.ERR_CYCLE_FORM) {
            return Ex2Utils.ERR_CYCLE;
        } else if (cell.getType() == Ex2Utils.ERR_FORM_FORMAT) {
            return Ex2Utils.ERR_FORM;
        } else {
            return Ex2Utils.ERR_CYCLE;
        }

    }
    private boolean hasCycle(int x, int y, boolean[][] visited) {
        Cell cell = get(x, y);
        if (cell == null || cell.getData().isEmpty()) {
            return false;
        }

        if (visited[x][y]) {
            return true;
        }

        if (cell.getType() == Ex2Utils.ERR_CYCLE_FORM) {
            return true;
        }

        visited[x][y] = true;

        if ( cell.getType() == Ex2Utils.FORM) {

            List<CellEntry> refs = findCellReferences(cell.getData());
            for (CellEntry ref : refs) {
                if (ref.isValid() && isIn(ref.getX(), ref.getY())){
                    if (hasCycle(ref.getX(), ref.getY(), visited)){
                        return true;
                    }
            }
        }
        }
        visited[x][y] = false;
        return false;
    }

    public List<CellEntry> findCellReferences(String formula) {
        List<CellEntry> refs = new ArrayList<>();
        if (formula == null || !formula.startsWith("=")) return refs;


        formula = formula.substring(1);


        String[] parts = formula.split("[+\\-*/()\\s]+");

        for (String part : parts) {
            part = part.trim();
            if (!part.isEmpty()) {

                if (!part.isEmpty() && ex2.isCell(part)) {
                    CellEntry entry = new CellEntry(part);
                    if (entry.isValid()) {
                        refs.add(entry);

                    }
                }
            }
        }
        return refs;
    }
}

