package cl.bozz.sudokusolver.v2;

import cl.bozz.sudokusolver.v2.model.SparseMatrix;
import cl.bozz.sudokusolver.v2.utils.SparseMatrixUtils;

import java.util.Set;

public class Bruh {
    public static void main(final String[] args) {
        final SparseMatrix matrix = new SparseMatrix(5, 5);
        System.out.println("Base");
        System.out.println(SparseMatrixUtils.toPrettyString(matrix));

        final Set<SparseMatrix.Cell> row1 = matrix.removeRow(1);
        System.out.println("Row 1");
        System.out.println(SparseMatrixUtils.toPrettyString(matrix));

        final Set<SparseMatrix.Cell> col3 = matrix.removeCol(3);
        System.out.println("Col 3");
        System.out.println(SparseMatrixUtils.toPrettyString(matrix));

        final SparseMatrix.Cell cell5_5 = matrix.removeCell(5, 5);
        System.out.println("Cell (5, 5)");
        System.out.println(SparseMatrixUtils.toPrettyString(matrix));

        matrix.insert(cell5_5);
        System.out.println("Col 3");
        System.out.println(SparseMatrixUtils.toPrettyString(matrix));

        matrix.insertCol(3, col3);
        System.out.println("Row 1");
        System.out.println(SparseMatrixUtils.toPrettyString(matrix));

        matrix.insertRow(1, row1);
        System.out.println("Base");
        System.out.println(SparseMatrixUtils.toPrettyString(matrix));

        int a = 0;
    }
}
