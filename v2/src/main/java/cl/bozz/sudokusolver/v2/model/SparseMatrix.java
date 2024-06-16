package cl.bozz.sudokusolver.v2.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

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
                final Cell up = tempMatrix[(rowCount + i) % (rowCount + 1)][j];
                final Cell down = tempMatrix[(rowCount + i + 2) % (rowCount + 1)][j];

                cell.setLeft(left);
                left.setRight(cell);

                cell.setRight(right);
                right.setLeft(cell);

                cell.setUp(up);
                up.setDown(cell);

                cell.setDown(down);
                down.setUp(cell);
            }
        }
    }

    public Cell remove(final int row, final int col) {
        final Cell targetCell = allCells.get(row).stream()
                .filter(cell -> cell.getCol() == col)
                .findFirst()
                .orElseThrow();

        return remove(targetCell);
    }

    public Cell remove(final Cell cell) {

        cell.getLeft().setRight(cell.getRight());
        cell.getRight().setLeft(cell.getLeft());
        cell.getUp().setDown(cell.getDown());
        cell.getDown().setUp(cell.getUp());

        return cell;
    }

    public void insert(final Cell cell) {
        cell.getLeft().setRight(cell);
        cell.getRight().setLeft(cell);
        cell.getUp().setDown(cell);
        cell.getDown().setUp(cell);
    }

    @Getter
    @Setter
    @RequiredArgsConstructor
    public static class Cell {
        private Cell up = this;
        private Cell down = this;
        private Cell left = this;
        private Cell right = this;
        private final int row;
        private final int col;
        private final boolean isHeader;

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
