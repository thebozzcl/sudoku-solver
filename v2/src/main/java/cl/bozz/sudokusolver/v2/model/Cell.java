package cl.bozz.sudokusolver.v2.model;

import lombok.Data;

@Data
public class Cell {
    private final int row;
    private final int col;
    private Cell up = this, down = this, left = this, right = this;
    private final ColumnHeader header;

    @Override
    public String toString() {
        return coordString();
    }

    public String coordString() {
        return String.format(
                "(%d, %d)",
                row,
                col
        );
    }
}
