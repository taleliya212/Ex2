public class ex2 {
    private static Sheet spreadsheet;

    public static void setSpreadsheet(Sheet sheet) {
        spreadsheet = sheet;
    }


    public static boolean isNumber(String s) {
        if (s == null || s.isEmpty())
            return false;
        s = s.trim();
        if (!Character.isDigit(s.charAt(0)) && s.charAt(0) != '-'){
            return false;
    }
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    public static boolean isText(String s) {
        return !isNumber(s) && !s.startsWith("=");
    }

    public static boolean isCell (String s) {
        if (s == null || s.length() < 2)
            return false;
        char l = s.charAt(0);
        if (!Character.isLetter(l))
            return false;
        try {
            Integer.parseInt(s.substring(1));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
        }

    public static boolean isForm(String s) {
        if (s == null || s.isEmpty()) {

            return false;
        }
        if (!s.startsWith("=")) {
            return false;
        }
        String formula = s.substring(1).trim();

        if (formula.isEmpty()) {
            return false;
        }

        formula = removeParentheses(formula);


        boolean result = isVaildForm(formula);
        return result;

    }

       private static boolean isVaildForm(String s){
        if (s.isEmpty()) {
            return false;
        }

        if (isNumber(s) || isCell(s)) {
            return true;
        }

           if (s.startsWith("-") && isNumber(s.substring(1))) {
               return true;
           }


           int vaildOper = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == '(') {
                vaildOper++;
            } else if (c == ')') {
                vaildOper--;
            }
            if (vaildOper < 0) {
                return false;
            }

            if (vaildOper == 0 && "/-+*".indexOf(c) != -1) {
                if (c == '-' && (i == 0 || "/+*-(".indexOf(s.charAt(i - 1)) != -1)) {
                    continue;
                }


                String left = s.substring(0, i).trim();
                String right = s.substring(i + 1).trim();

                if (left.isEmpty() || right.isEmpty()) {
                    return false;
                    }

                return isVaildForm(left) && isVaildForm(right);
            }
        }

        return vaildOper == 0;
    }



    public static Double computeForm(String form) {

        if (form == null || form.isEmpty()) {
            return null;
        }

        String s = form.startsWith("=") ? form.substring(1).trim() : form.trim();
        return computeHelper(s);

    }
        public static Double computeHelper(String s){
            s = removeParentheses(s);

            if (isNumber(s)) {
                return Double.parseDouble(s);
            }

        if (isCell(s)) {
            if (spreadsheet == null) {
                return null;
            }

            CellEntry entry = new CellEntry(s);
            if (!entry.isValid()) {
                return null;
            }

            String cellValue = spreadsheet.value(entry.getX(), entry.getY());
            if (cellValue == null || cellValue.equals(Ex2Utils.EMPTY_CELL)) {
                return null;
            }
            try {
                return Double.parseDouble(cellValue);
            } catch (NumberFormatException e) {
                return null;
            }
        }

            int parensWeight = 0;
            for (int i = s.length() - 1; i >= 0; i--) {
                char c = s.charAt(i);

                if (c == ')') {
                    parensWeight++;
                } else if (c == '(') {
                    parensWeight--;
                }

                if (parensWeight == 0 && (c == '+' || c == '-')) {
                    String left = s.substring(0, i).trim();
                    String right = s.substring(i + 1).trim();

                    Double leftValue = computeHelper(left);
                    Double rightValue = computeHelper(right);

                    if (leftValue == null || rightValue == null) return null;
                    return c == '+' ? leftValue + rightValue : leftValue - rightValue;
                }
            }

            for (int i = s.length() - 1; i >= 0; i--) {
                char c = s.charAt(i);

                if (c == ')') {
                    parensWeight++;
                } else if (c == '(') {
                    parensWeight--;
                }

                if (parensWeight == 0 && (c == '*' || c == '/')) {
                    String left = s.substring(0, i).trim();
                    String right = s.substring(i + 1).trim();

                    Double leftValue = computeHelper(left);
                    Double rightValue = computeHelper(right);

                    if (leftValue == null || rightValue == null) return null;
                    return c == '*' ? leftValue * rightValue :  leftValue / rightValue;
                }
            }

            return null;
        }
    public static String removeParentheses(String s) {
        s = s.trim();

        while (s.startsWith("(") && s.endsWith(")")) {
            int count = 0;

            boolean isMatching = true;

            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == '(') count++;
                else if (s.charAt(i) == ')') count--;

                if (count == 0 && i != s.length() - 1) {
                    isMatching = false;
                    break;
                }
            }
            if (!isMatching || count != 0)
                break;
            s = s.substring(1, s.length() - 1).trim();
        }
        return s;
    }



}
