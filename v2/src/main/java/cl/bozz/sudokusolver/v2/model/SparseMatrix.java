package cl.bozz.sudokusolver.v2.model;

import lombok.Data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SparseMatrix {
    final Map<Integer, Set<Cell>> allCells = new HashMap<>();

    public SparseMatrix(
            final int columnCount, final int rowCount
    ) {
        final Cell[][] tempMatrix = new Cell[rowCount + 1][columnCount];
        for (int i = 0; i <= rowCount; i++) {
            allCells.put(i, new HashSet<>());
            for (int j = 0; j < columnCount; j++) {
                final Cell cell = new Cell(i, j, i == 0);

                allCells.get(i).add(cell);
                tempMatrix[i][j] = cell;
            }
        }

        for (int i = 0; i <= rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                final Cell cell = tempMatrix[i][j];
                final Cell left = tempMatrix[i][(columnCount + j - 1) % columnCount];
                final Cell right = tempMatrix[i][(columnCount + j + 1) % columnCount];
                final Cell up = tempMatrix[(rowCount + i - 1) % rowCount][j];
                final Cell down = tempMatrix[(rowCount + i + 1) % rowCount][j];

                cell.left = left;
                left.right = cell;

                cell.right = right;
                right.left = cell;

                cell.up = up;
                up.down = cell;

                cell.down = down;
                down.up = cell;
            }
        }
    }

    public Cell remove(final int row, final int col) {
        final Cell targetCell = allCells.get(row).stream()
                .filter(cell -> cell.col == col)
                .findFirst()
                .orElseThrow();

        return remove(targetCell);
    }

    public Cell remove(final Cell cell) {

        cell.left.right = cell.right;
        cell.right.left = cell.left;
        cell.up.down = cell.down;
        cell.down.up = cell.up;

        return cell;
    }

    public void insert(final Cell cell) {
        cell.left.right = cell;
        cell.right.left = cell;
        cell.up.down = cell;
        cell.down.up = cell;
    }

    @Data
    public static class Cell {
        Cell up = this;
        Cell down = this;
        Cell left = this;
        Cell right = this;
        final int row;
        final int col;
        final boolean isHeader;

        @Override
        public String toString() {
            return String.format("%s(%d, %d)", isHeader ? "H" : "", row, col);
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }
    }
}
