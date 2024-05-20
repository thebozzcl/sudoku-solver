package cl.bozz.sudokusolver.utils;

import cl.bozz.sudokusolver.model.SudokuCellValues;
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
