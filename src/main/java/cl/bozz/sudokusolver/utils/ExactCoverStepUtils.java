package cl.bozz.sudokusolver.utils;

import cl.bozz.sudokusolver.model.ExactCoverStep;
import cl.bozz.sudokusolver.model.ExactCoverConstraint;
import lombok.experimental.UtilityClass;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@UtilityClass
public class ExactCoverStepUtils {
    public static ExactCoverStep emptyForEnum(final Class<? extends Enum<?>> enumClass, final Set<ExactCoverConstraint> constraints) {
        return new ExactCoverStep(
                new HashSet<>(),
                IntStream.range(0, enumClass.getEnumConstants().length).boxed().collect(Collectors.toSet()),
                constraints,
                null
        );
    }

    public ExactCoverStep updateState(final ExactCoverStep exactCoverStep, final int update) {
        final Set<Integer> newValues = new HashSet<>(exactCoverStep.values());
        newValues.add(update);

        final Set<ExactCoverConstraint> matchingConstraints = ExactCoverConstraintUtils.findConstraintsWithValue(exactCoverStep.constraints(), update);
        final Set<Integer> eliminatedOptions = matchingConstraints.stream()
                .flatMap(constraint -> IntStream.range(0, constraint.acceptedValues().length)
                        .filter(n -> constraint.acceptedValues()[n])
                        .boxed()
                )
                .collect(Collectors.toSet());
        final Set<ExactCoverConstraint> newConstraints = exactCoverStep.constraints().stream()
                .filter(constraint -> !matchingConstraints.contains(constraint))
                .collect(Collectors.toSet());

        final Set<Integer> newOptions = exactCoverStep.options().stream()
                .filter(n -> !eliminatedOptions.contains(n))
                .collect(Collectors.toSet());

        return new ExactCoverStep(newValues, newOptions, newConstraints, exactCoverStep);
    }
}
