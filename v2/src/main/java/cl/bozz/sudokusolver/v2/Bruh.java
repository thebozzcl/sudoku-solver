package cl.bozz.sudokusolver.v2;

import cl.bozz.sudokusolver.v2.model.Cell;
import cl.bozz.sudokusolver.v2.model.ColumnHeader;
import cl.bozz.sudokusolver.v2.operator.Operator;
import cl.bozz.sudokusolver.v2.utils.SparseMatrixUtils;

public class Bruh {
    public static void main(final String[] args) {

        final ColumnHeader sparseMatrix = SparseMatrixUtils.buildSparseMatrix(
                new boolean[][] {
                        new boolean[] { false, false, false, true },
                        new boolean[] { false, true, true, false },
                        new boolean[] { true, false, true, false },
                        new boolean[] { true, false, false, false }
                },
                new String[] { "A", "B", "C", "D" }
        );
        System.out.println(SparseMatrixUtils.toDetailedPrint(sparseMatrix, 4, 4));
        System.out.println();

        final Cell removedCol = Operator.REMOVE_COL.apply(sparseMatrix.getRight().getRight());
        System.out.println(SparseMatrixUtils.toDetailedPrint(sparseMatrix, 4, 4));
        System.out.println();

        Operator.REMOVE_COL.getInverse().apply(removedCol);
        System.out.println(SparseMatrixUtils.toDetailedPrint(sparseMatrix, 4, 4));
        System.out.println();

        final Cell removedRow = Operator.REMOVE_ROW.apply(sparseMatrix.getRight().getDown().getDown());
        System.out.println(SparseMatrixUtils.toDetailedPrint(sparseMatrix, 4, 4));
        System.out.println();

        Operator.REMOVE_ROW.getInverse().apply(removedRow);
        System.out.println(SparseMatrixUtils.toDetailedPrint(sparseMatrix, 4, 4));
        System.out.println();

        int a = 0;
    }
}
