package cl.bozz.sudokusolver.v2.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColumnHeader extends Cell {
    private final String label;
    private int size = 0;

    public ColumnHeader(final String label, final int row, final int col) {
        super(row, col, null);
        this.label = label;
    }

    @Override
    public String toString() {
        return String.format(
                "%s %s (%d)",
                label,
                super.toString(),
                size
        );
    }
}
