package cl.bozz.sudokusolver.utils;

import cl.bozz.sudokusolver.model.CellValues;
import cl.bozz.sudokusolver.model.Constraint;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@UtilityClass
public class ConstraintUtils {
    private Boolean[] getEmptyAcceptedValues() {
        final Boolean[] empty = new Boolean[CellValues.values().length];
        Arrays.fill(empty, false);
        return empty;
    }

    public Constraint rowColumnConstraint(final int row, final int col) {
        final Boolean[] values = getEmptyAcceptedValues();

        Arrays.stream(CellValues.values())
                .filter(v -> v.getRow() == row && v.getCol() == col)
                .mapToInt(v -> CellValues.valueOf(v.name()).ordinal())
                .forEach(i -> values[i] = true);

        return new Constraint(String.format("R%dC%d", row, col), values);
    }

    public Constraint rowNumberConstraint(final int row, final int val) {
        final Boolean[] values = getEmptyAcceptedValues();

        Arrays.stream(CellValues.values())
                .filter(v -> v.getRow() == row && v.getValue() == val)
                .mapToInt(v -> CellValues.valueOf(v.name()).ordinal())
                .forEach(i -> values[i] = true);

        return new Constraint(String.format("R%dV%d", row, val), values);
    }

    public Constraint columnNumberConstraint(final int col, final int val) {
        final Boolean[] values = getEmptyAcceptedValues();

        Arrays.stream(CellValues.values())
                .filter(v -> v.getCol() == col && v.getValue() == val)
                .mapToInt(v -> CellValues.valueOf(v.name()).ordinal())
                .forEach(i -> values[i] = true);

        return new Constraint(String.format("C%dV%d", col, val), values);
    }

    public Constraint boxNumberConstraint(final int box, final int val) {
        final Boolean[] values = getEmptyAcceptedValues();

        Arrays.stream(CellValues.values())
                .filter(v -> v.getBox() == box && v.getValue() == val)
                .mapToInt(v -> CellValues.valueOf(v.name()).ordinal())
                .forEach(i -> values[i] = true);

        return new Constraint(String.format("B%dV%d", box, val), values);
    }

    public Set<Constraint> createEmptyConstraints() {
        final Set<Constraint> constraints = new HashSet<>();

        IntStream.range(1, 10).forEach(i -> IntStream.range(1, 10)
                .forEach(j -> {
                    constraints.add(rowColumnConstraint(i, j));
                    constraints.add(columnNumberConstraint(i, j));
                    constraints.add(rowNumberConstraint(i, j));
                    constraints.add(boxNumberConstraint(i, j));
                })
        );

        return constraints;
    }

    public Set<Constraint> findConstraintsWithValue(final Set<Constraint> constraints, final int value) {
        return constraints.stream()
                .filter(constraint -> constraint.acceptedValues()[value])
                .collect(Collectors.toSet());
    }

    public Set<Constraint> getLowestCardinalityConstraints(final Set<Constraint> constraints, final Set<Integer> options) {
        return constraints.stream()
                .collect(Collectors.groupingBy(constraint -> ConstraintUtils.getConstraintCardinality(constraint, options), Collectors.toSet()))
                .entrySet().stream()
                .min(Comparator.comparingInt(Map.Entry::getKey))
                .map(Map.Entry::getValue)
                .orElse(new HashSet<>());
    }

    private int getConstraintCardinality(final Constraint constraint, final Set<Integer> options) {
        return (int) options.stream()
                .filter(o -> constraint.acceptedValues()[o])
                .count();
    }
}
