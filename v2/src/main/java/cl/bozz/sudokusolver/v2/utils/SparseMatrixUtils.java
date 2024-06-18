package cl.bozz.sudokusolver.v2.utils;

import cl.bozz.sudokusolver.v2.model.Cell;
import cl.bozz.sudokusolver.v2.model.Header;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@UtilityClass
public class SparseMatrixUtils {
    public Header buildSparseMatrix(final boolean[][] conditions, final String[] labels) {
        final Header root = new Header("root", 0, 0);

        for (int j = 0; j < labels.length; j++) {
            final Header header = new Header(labels[j], 0, j + 1);
            header.setRight(root);
            header.setLeft(root.getLeft());
            root.getLeft().setRight(header);
            root.setLeft(header);
        }

        for (int i = 0; i < conditions.length; i++) {
            Header colRoot = (Header)root.getRight();
            Cell lastCreated = null;
            for (int j = 0; j < conditions[i].length; j++) {
                if (conditions[i][j]) {
                    final Cell cell = new Cell(i + 1, j + 1, colRoot);
                    colRoot.setSize(colRoot.getSize() + 1);

                    cell.setDown(colRoot);
                    cell.setUp(colRoot.getUp());
                    colRoot.getUp().setDown(cell);
                    colRoot.setUp(cell);

                    if (lastCreated != null) {
                        cell.setLeft(lastCreated);
                        cell.setRight(lastCreated.getRight());
                        lastCreated.setRight(cell);
                        cell.getRight().setLeft(cell);
                    }
                    lastCreated = cell;
                }
                colRoot = (Header)colRoot.getRight();
            }
        }

        return root;
    }

    public String toPrettyPrint(final Header root, int rows, int cols) {
        final int padding = Integer.toString(cols + 1).length();
        final String paddedFormat = "%0" + padding + "d";

        final String filler = IntStream.range(0, padding)
                .mapToObj(n -> " ")
                .collect(Collectors.joining());

        final String marker = filler.substring(0, filler.length() / 2) + 'X' + filler.substring(filler.length() / 2 + 1);

        final String interLinePadding = IntStream.range(0, cols + 1)
                .mapToObj(m -> IntStream.range(0, padding)
                        .mapToObj(n -> "─")
                        .collect(Collectors.joining())
                ).collect(Collectors.joining("┼"));

        final String[][] matrix = new String[rows + 1][cols + 1];
        for (int i = 0; i < rows + 1; i++) {
            final String[] row = new String[cols + 1];
            Arrays.fill(row, filler);
            matrix[i] = row;
        }

        Header header = root;
        do {
            matrix[0][header.getCol()] = String.format(paddedFormat, header.getCol());

            Cell cell = header.getDown();
            while (cell != header) {
                matrix[cell.getRow()][0] = String.format(paddedFormat, cell.getRow());
                matrix[cell.getRow()][cell.getCol()] = marker;
                cell = cell.getDown();
            }

            header = (Header) header.getRight();
        } while (header != root);

        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows + 1; i++) {
            sb.append(String.join("│", matrix[i]));
            if (i < rows) {
                sb.append("\n");
                sb.append(interLinePadding);
                sb.append("\n");
            }
        }
        return sb.toString();
    }
    public String toDetailedPrint(final Header root, int rows, int cols) {

        final String[][] matrix = new String[rows + 1][cols + 1];
        for (int i = 0; i < rows + 1; i++) {
            final String[] row = new String[cols + 1];
            Arrays.fill(row, null);
            matrix[i] = row;
        }

        Header header = root;
        int tempPadding = 0;
        do {
            matrix[0][header.getCol()] = detailedCellPrint(header);
            tempPadding = Math.max(tempPadding, matrix[0][header.getCol()].length());

            Cell cell = header.getDown();
            while (cell != header) {
                matrix[cell.getRow()][0] = cell.getRow() + "";
                matrix[cell.getRow()][cell.getCol()] = detailedCellPrint(cell);
                tempPadding = Math.max(tempPadding, matrix[cell.getRow()][0].length());
                cell = cell.getDown();
            }

            header = (Header) header.getRight();
        } while (header != root);
        final int padding = tempPadding;

        final String filler = " ".repeat(padding);

        final String interLinePadding = IntStream.range(0, cols + 1)
                .mapToObj(m -> "─".repeat(padding))
                .collect(Collectors.joining("┼"));

        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows + 1; i++) {
            for (int j = 0; j < cols + 1; j++) {
                if (matrix[i][j] == null) {
                    matrix[i][j] = filler;
                } else {
                    matrix[i][j] = matrix[i][j] + " ".repeat(padding - matrix[i][j].length());
                }
            }
            sb.append(String.join("│", matrix[i]));
            if (i < rows) {
                sb.append("\n");
                sb.append(interLinePadding);
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    public String detailedCellPrint(final Cell cell) {
        return String.format(
                "%s [%s]",
                cell.toString(),
                Stream.of(cell.getUp(), cell.getDown(), cell.getLeft(), cell.getRight())
                        .map(Cell::toString)
                        .collect(Collectors.joining(", "))
        );
    }
}
