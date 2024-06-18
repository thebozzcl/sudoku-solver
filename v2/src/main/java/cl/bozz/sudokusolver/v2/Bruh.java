package cl.bozz.sudokusolver.v2;

import cl.bozz.sudokusolver.v2.model.Cell;
import cl.bozz.sudokusolver.v2.model.Header;
import cl.bozz.sudokusolver.v2.model.Operator;
import cl.bozz.sudokusolver.v2.utils.SparseMatrixUtils;

public class Bruh {
    public static void main(final String[] args) {

        final Header sparseMatrix = SparseMatrixUtils.buildSparseMatrix(
                new boolean[][] {
                        new boolean[] { false, false, false, true },
                        new boolean[] { false, true, true, false },
                        new boolean[] { true, false, true, false },
                        new boolean[] { true, false, false, false }
                },
                new String[] { "A", "B", "C", "D" }
        );
        System.out.println(SparseMatrixUtils.toPrettyPrint(sparseMatrix, 4, 4));
        System.out.println();

        final Cell removedCol = Operator.REMOVE_COL.getOp().apply(sparseMatrix.getRight().getRight());
        System.out.println(SparseMatrixUtils.toPrettyPrint(sparseMatrix, 4, 4));
        System.out.println();

        Operator.REMOVE_COL.getInverse().getOp().apply(removedCol);
        System.out.println(SparseMatrixUtils.toPrettyPrint(sparseMatrix, 4, 4));
        System.out.println();

        final Cell removedRow = Operator.REMOVE_ROW.getOp().apply(sparseMatrix.getRight().getDown().getDown());
        System.out.println(SparseMatrixUtils.toPrettyPrint(sparseMatrix, 4, 4));
        System.out.println();

        Operator.REMOVE_ROW.getInverse().getOp().apply(removedRow);
        System.out.println(SparseMatrixUtils.toPrettyPrint(sparseMatrix, 4, 4));
        System.out.println();

        int a = 0;
    }
}
