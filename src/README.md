

# Ex2 - Foundation of Object-Oriented and Recursion - Spreadsheet System

## Overview:

This project is a comprehensive Java-based implementation of a spreadsheet system.
It allows users to manage a 2D grid of cells where each cell can store text, numeric values, or formulas.
The system provides advanced features like dynamic formula evaluation, inter-cell dependency resolution, and cycle detection.
Additionally, it includes functionalities for saving and loading spreadsheets to and from TXT files.

---

## How it runs:

**The main program workflow includes the following steps:**

1. **Initialize the Spreadsheet:**

    - Create an instance of `Ex2Sheet` with a defined grid size (e.g., 3x3 or larger).

2. **Set Cell Values:**

    - Enter values in different formats:
        - Plain text (e.g., `"Hello"`).
        - Numbers (e.g., `123`, `45.67`).
        - Formulas (e.g., `=A1+B2`, `=C3*2`).

3. **Evaluate Cells:**

    - Access cell values dynamically by calling `sheet.value(x, y)`. Formulas are evaluated based on dependencies.

4. **Save and Load Spreadsheets:**

    - **Save:** Use **the** `save` method to export the spreadsheet to a TXT file. Only non-empty cells are saved.
    - **Load:** Use the `load` method to import a previously saved TXT file. All cell values and formulas are restored.

5. **Error Handling:**

    - The system automatically detects and reports issues such as invalid formulas, cyclic dependencies, or out-of-bound references.

---

## My code **features**:

### **Core Functionalities:**

1. **Cell Content Management:**

    - Supports storing the following types of data:
        - **Text**: Plain strings like `"Example"`.
        - **Numbers**: Both integers and decimals.
        - **Formulas**: Dynamic expressions starting with `=` (e.g., `=A1+B2`).

2. **Dynamic Formula Evaluation:**

    - Formulas are parsed and evaluated dynamically.
    - Supports arithmetic operations like addition, subtraction, multiplication, and division.
    - Resolves dependencies recursively to ensure accuracy.

3. **Cycle Detection:**

    - Identifies and prevents infinite loops caused by cyclic dependencies.
    - Marks all cells involved in the cycle with the error state `ERR_CYCLE`.

4. **Depth Calculation:**

    - Calculates the evaluation order of cells based on their dependency depth.
    - Ensures cells are computed only after their dependencies are resolved.

5. **File Management:**

    - **Save:** Exports cell data and formulas to a TXT file.
    - **Load:** Restores a spreadsheet from a CSV file, including all cell contents and dependencies.

6. **Error Handling:**

    - Detects and reports:
        - Invalid formulas (e.g., syntax errors) with `ERR_FORM`.
        - Cyclic dependencies with `ERR_CYCLE`.
        - Out-of-bound references when accessing non-existent cells.

---

## Tests for my code:

### **Comprehensive Testing Suite:**

The project includes a robust set of tests to ensure reliability and correctness:

1. **Cell Value Operations:**

    - Verify that text, numeric, and formula inputs are stored and retrieved correctly.

2. **Formula Evaluation:**

    - Ensure that formulas are evaluated accurately, considering inter-cell dependencies.
    - Test complex expressions involving multiple operations (e.g., `=A1*B2+C3/D4`).

3. **Cycle Detection:**

    - Confirm that cycles are detected and all cells involved are marked with `ERR_CYCLE`.

4. **File Management:**

    - Test saving spreadsheets to CSV and loading them back.
    - Ensure that formulas and values are preserved correctly.

5. **Edge Case Handling:**

    - Empty cells: Ensure they are ignored or handled gracefully.
    - Invalid formulas: Verify they are flagged with `ERR_FORM`.
    - Out-of-bound references: Confirm they do not crash the program and are flagged appropriately.

6. **Performance Tests:**

    - Evaluate the system's performance on larger grids with complex dependencies.

---

## Example Usage:

![images.png](..%2Fimages%2Fimages.png)  

## Summary:

This project provides a lightweight yet powerful spreadsheet implementation with advanced features like dynamic formula evaluation, error detection, and file management.

---





