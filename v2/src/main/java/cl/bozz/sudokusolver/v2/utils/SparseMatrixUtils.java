package cl.bozz.sudokusolver.v2.utils;

import cl.bozz.sudokusolver.v2.model.SparseMatrix;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.Set;

@UtilityClass
public class SparseMatrixUtils {
    public String toPrettyString(final SparseMatrix matrix) {
        final int maxRow = matrix.getRows().values().stream()
                .mapToInt(cells -> cells.stream().mapToInt(SparseMatrix.Cell::getRow).max().orElseThrow())
                .max().orElseThrow();
        final int maxCol = matrix.getCols().values().stream()
                .mapToInt(cells -> cells.stream().mapToInt(SparseMatrix.Cell::getCol).max().orElseThrow())
                .max().orElseThrow();

        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= maxCol; i++) {
            final char[] rowChars = new char[maxRow + 1];
            Arrays.fill(rowChars, '.');
            if (matrix.getRows().containsKey(i)) {
                if (i == 0) {
                    rowChars[0] = ' ';
                } else {
                    rowChars[0] = (char) ('0' + i);
                }

                final Set<SparseMatrix.Cell> cells = matrix.getRows().get(i);

                cells.forEach(cell -> {
                    rowChars[cell.getCol()] = cell.isHeader() ? (char)('0' + cell.getCol()) : 'X';
                });
            }
            sb.append(String.valueOf(rowChars)).append("\n");
        }

        return sb.toString();
    }
}
