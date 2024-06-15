package cl.bozz.sudokusolver.v1.algorithm.utils;

import cl.bozz.sudokusolver.v1.algorithm.model.ExactCoverConstraint;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class ExactCoverConstraintUtils {
    public <E extends Enum<?>> ExactCoverConstraint fromAcceptedEnumValues(final String name, final Set<E> acceptedValues, final Class<E> enumClass) {
        final Boolean[] accepted = new Boolean[enumClass.getEnumConstants().length];
        Arrays.fill(accepted, false);
        acceptedValues.stream().mapToInt(Enum::ordinal).forEach(n -> accepted[n] = true);
        return new ExactCoverConstraint(name, accepted);
    }

    public Set<ExactCoverConstraint> findConstraintsWithValue(final Set<ExactCoverConstraint> constraints, final int value) {
        return constraints.stream()
                .filter(constraint -> constraint.acceptedChoices()[value])
                .collect(Collectors.toSet());
    }

    public Set<ExactCoverConstraint> getLowestCardinalityConstraints(final Set<ExactCoverConstraint> constraints, final Set<Integer> options) {
        return constraints.stream()
                .collect(Collectors.groupingBy(constraint -> ExactCoverConstraintUtils.getConstraintCardinality(constraint, options), Collectors.toSet()))
                .entrySet().stream()
                .min(Comparator.comparingInt(Map.Entry::getKey))
                .map(Map.Entry::getValue)
                .orElse(new HashSet<>());
    }

    private int getConstraintCardinality(final ExactCoverConstraint constraint, final Set<Integer> options) {
        return (int) options.stream()
                .filter(o -> constraint.acceptedChoices()[o])
                .count();
    }
}
