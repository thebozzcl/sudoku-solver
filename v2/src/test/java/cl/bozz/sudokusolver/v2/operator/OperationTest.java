package cl.bozz.sudokusolver.v2.operator;

import cl.bozz.sudokusolver.v2.model.Cell;
import cl.bozz.sudokusolver.v2.model.ColumnHeader;
import cl.bozz.sudokusolver.v2.utils.SparseMatrixUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OperationTest {
    @Test
    public void testRemoveCol_thenRevert() {
        // Arrange
        final ColumnHeader matrix = getSparseMatrix();
        final String originalState = SparseMatrixUtils.toDetailedPrint(matrix, 4, 4);

        // Act
        final ColumnHeader removedHeader = (ColumnHeader) Operator.REMOVE_COL.apply(matrix.getRight().getRight());
        SparseMatrixUtils.toDetailedPrint(matrix, 4, 4);

        Operator.REMOVE_COL.getInverse().apply(removedHeader);
        final String restoredState = SparseMatrixUtils.toDetailedPrint(matrix, 4, 4);

        // Assert
        assertEquals(originalState, restoredState);
        assertEquals(removedHeader.toString(), "B (0, 2) (1)");
    }

    @Test
    public void testRemoveRow_thenRevert() {
        // Arrange
        final ColumnHeader matrix = getSparseMatrix();
        final String originalState = SparseMatrixUtils.toDetailedPrint(matrix, 4, 4);

        // Act
        final Cell removedCell = Operator.REMOVE_ROW.apply(matrix.getRight().getDown().getDown());
        SparseMatrixUtils.toDetailedPrint(matrix, 4, 4);

        Operator.REMOVE_ROW.getInverse().apply(removedCell);
        final String restoredState = SparseMatrixUtils.toDetailedPrint(matrix, 4, 4);

        // Assert
        assertEquals(originalState, restoredState);
        assertEquals(removedCell.toString(), "(4, 1)");
    }

    private ColumnHeader getSparseMatrix() {
        return SparseMatrixUtils.buildSparseMatrix(
                new boolean[][] {
                        new boolean[] { false, false, false, true },
                        new boolean[] { false, true, true, false },
                        new boolean[] { true, false, true, false },
                        new boolean[] { true, false, false, false }
                },
                new String[] { "O", "A", "B", "C", "D" }
        );
    }
}
