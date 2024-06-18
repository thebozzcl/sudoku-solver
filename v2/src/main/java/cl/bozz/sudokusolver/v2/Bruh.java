package cl.bozz.sudokusolver.v2;

import cl.bozz.sudokusolver.v2.model.Header;
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

        int a = 0;
    }
}
