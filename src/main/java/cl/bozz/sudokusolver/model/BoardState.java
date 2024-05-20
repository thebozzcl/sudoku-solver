package cl.bozz.sudokusolver.model;

import cl.bozz.sudokusolver.utils.ConstraintUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public record BoardState(Set<Integer> values, Set<Integer> options, Set<Constraint> constraints, BoardState parent) {
    public static final BoardState EMPTY = new BoardState(
            new HashSet<>(),
            IntStream.range(0, CellValues.values().length).boxed().collect(Collectors.toSet()),
            ConstraintUtils.createEmptyConstraints(),
            null
    );

    @Override
    public String toString() {
        return values.stream()
                .sorted()
                .map(n -> CellValues.values()[n].name())
                .collect(Collectors.joining(","));
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
