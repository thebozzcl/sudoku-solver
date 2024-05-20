package cl.bozz.sudokusolver.model;

import java.util.Set;
import java.util.stream.Collectors;

public record ExactCoverState(Set<Integer> values, Set<Integer> options, Set<Constraint> constraints, ExactCoverState parent) {

    @Override
    public String toString() {
        return "[" + values.stream()
                .sorted()
                .map(n -> SudokuCellValues.values()[n].name())
                .collect(Collectors.joining(", ")) + "]";
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
