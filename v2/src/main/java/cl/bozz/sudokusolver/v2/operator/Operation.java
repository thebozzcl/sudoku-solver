package cl.bozz.sudokusolver.v2.operator;

import cl.bozz.sudokusolver.v2.model.Cell;

public record Operation(Operator operator, Cell cell) {
    public Cell apply() {
        return operator.apply(cell);
    }

    public Cell invert() {
        return operator.getInverse().apply(cell);
    }
}
