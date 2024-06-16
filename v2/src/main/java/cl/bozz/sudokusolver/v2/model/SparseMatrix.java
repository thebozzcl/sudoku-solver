package cl.bozz.sudokusolver.v2.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
public class SparseMatrix {
    final Map<Integer, Set<Cell>> rows = new HashMap<>();
    final Map<Integer, Set<Cell>> cols = new HashMap<>();

    public SparseMatrix(
            final int columnCount, final int rowCount
    ) {
        final Cell[][] tempMatrix = new Cell[rowCount + 1][columnCount];
        for (int i = 0; i <= rowCount; i++) {
            rows.put(i, new HashSet<>());
            for (int j = 0; j < columnCount; j++) {
                final Cell cell = new Cell(i, j + 1, i == 0);

                rows.get(i).add(cell);
                if (!cols.containsKey(j + 1)) {
                    cols.put(j + 1, new HashSet<>());
                }
                cols.get(j + 1).add(cell);

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

    public Cell removeCell(final int row, final int col) {
        final Cell targetCell = rows.get(row).stream()
                .filter(cell -> cell.getCol() == col)
                .findFirst()
                .orElseThrow();

        return removeCell(targetCell);
    }

    public Cell removeCell(final Cell cell) {

        cell.getLeft().setRight(cell.getRight());
        cell.getRight().setLeft(cell.getLeft());
        cell.getUp().setDown(cell.getDown());
        cell.getDown().setUp(cell.getUp());

        rows.get(cell.getRow()).remove(cell);
        if (rows.get(cell.getRow()).isEmpty()) {
            rows.remove(cell.getRow());
        }

        cols.get(cell.getCol()).remove(cell);
        if (cols.get(cell.getCol()).isEmpty()) {
            cols.remove(cell.getCol());
        }

        return cell;
    }

    public Set<Cell> removeRow(final int row) {
        final Set<Cell> rowCells = rows.get(row);

        rowCells.forEach(cell -> {
            cell.getUp().setDown(cell.getDown());
            cell.getDown().setUp(cell.getUp());

            cols.get(cell.getCol()).remove(cell);
            if (cols.get(cell.getCol()).isEmpty()) {
                cols.remove(cell.getCol());
            }
        });
        rows.remove(row);

        return rowCells;
    }

    public Set<Cell> removeCol(final int col) {
        final Set<Cell> colCells = cols.get(col);

        colCells.forEach(cell -> {
            cell.getLeft().setRight(cell.getRight());
            cell.getRight().setLeft(cell.getLeft());

            rows.get(cell.getRow()).remove(cell);
            if (rows.get(cell.getRow()).isEmpty()) {
                rows.remove(cell.getRow());
            }
        });
        cols.remove(col);

        return colCells;
    }

    public void insert(final Cell cell) {
        cell.getLeft().setRight(cell);
        cell.getRight().setLeft(cell);
        cell.getUp().setDown(cell);
        cell.getDown().setUp(cell);

        if (!rows.containsKey(cell.getRow())) {
            rows.put(cell.getRow(), new HashSet<>());
        }
        rows.get(cell.getRow()).add(cell);

        if (!cols.containsKey(cell.getCol())) {
            cols.put(cell.getCol(), new HashSet<>());
        }
        cols.get(cell.getCol()).add(cell);
    }

    public void insertRow(final int row, final Set<Cell> cells) {
        if (rows.containsKey(row)) {
            throw new IllegalArgumentException();
        }
        rows.put(row, cells);

        cells.forEach(cell -> {
            cell.getUp().setDown(cell);
            cell.getDown().setUp(cell);
            cols.get(cell.getCol()).add(cell);
        });
    }

    public void insertCol(final int col, final Set<Cell> cells) {
        if (cols.containsKey(col)) {
            throw new IllegalArgumentException();
        }
        cols.put(col, cells);

        cells.forEach(cell -> {
            cell.getLeft().setRight(cell);
            cell.getRight().setLeft(cell);
            rows.get(cell.getRow()).add(cell);
        });
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
