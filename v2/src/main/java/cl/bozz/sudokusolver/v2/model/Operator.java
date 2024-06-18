package cl.bozz.sudokusolver.v2.model;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@RequiredArgsConstructor
public enum Operator {
    REMOVE_ROW(start -> {
        Cell cell = start;
        do {
            cell.getUp().setDown(cell.getDown());
            cell.getDown().setUp(cell.getUp());
            cell.getHeader().setSize(cell.getHeader().getSize() - 1);

            cell = cell.getRight();
        } while (cell != start);

        return start;
    }),
    REMOVE_COL(start -> {
        Cell cell = start;
        do {
            cell.getLeft().setRight(cell.getRight());
            cell.getRight().setLeft(cell.getLeft());

            cell = cell.getDown();
        } while (cell != start);

        return start;
    }),
    ADD_ROW(start -> {
        Cell cell = start;
        do {
            cell.getUp().setDown(cell);
            cell.getDown().setUp(cell);
            cell.getHeader().setSize(cell.getHeader().getSize() + 1);

            cell = cell.getRight();
        } while (cell != start);

        return start;
    }),
    ADD_COL(start -> {
        Cell cell = start;
        do {
            cell.getLeft().setRight(cell);
            cell.getRight().setLeft(cell);

            cell = cell.getDown();
        } while (cell != start);

        return start;
    });

    private static final Map<Operator, Operator> REVERSE_OP = new HashMap<Operator, Operator>() {{
       put(ADD_ROW, REMOVE_ROW);
       put(ADD_COL, REMOVE_COL);
       put(REMOVE_ROW, ADD_ROW);
       put(REMOVE_COL, ADD_COL);
    }};

    private final Function<Cell, Cell> op;

    public Cell apply(final Cell cell) {
        return op.apply(cell);
    }

    public Operator getInverse() {
        return REVERSE_OP.get(this);
    }
}
