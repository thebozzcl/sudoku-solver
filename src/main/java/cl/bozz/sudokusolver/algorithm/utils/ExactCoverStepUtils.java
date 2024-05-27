package cl.bozz.sudokusolver.algorithm.utils;

import cl.bozz.sudokusolver.algorithm.model.ExactCoverStep;
import cl.bozz.sudokusolver.algorithm.model.ExactCoverConstraint;
import lombok.experimental.UtilityClass;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@UtilityClass
public class ExactCoverStepUtils {
    /**
     * Creates an empty exact cover step based on a set of enum choices and constraints
     * @param enumClass
     * @param constraints
     * @return
     */
    public static ExactCoverStep emptyForEnum(final Class<? extends Enum<?>> enumClass, final Set<ExactCoverConstraint> constraints) {
        return new ExactCoverStep(
                new HashSet<>(),
                IntStream.range(0, enumClass.getEnumConstants().length).boxed().collect(Collectors.toSet()),
                constraints
        );
    }

    /**
     * Default implementation of the state update function for Algorithm.
     * @param exactCoverStep
     * @param update
     * @return
     */
    public ExactCoverStep updateStep(final ExactCoverStep exactCoverStep, final int update) {
        final Set<Integer> newValues = new HashSet<>(exactCoverStep.choices());
        newValues.add(update);

        final Set<ExactCoverConstraint> matchingConstraints = ExactCoverConstraintUtils.findConstraintsWithValue(exactCoverStep.constraints(), update);
        final Set<Integer> eliminatedOptions = matchingConstraints.stream()
                .flatMap(constraint -> IntStream.range(0, constraint.acceptedChoices().length)
                        .filter(n -> constraint.acceptedChoices()[n])
                        .boxed()
                ).collect(Collectors.toSet());
        final Set<ExactCoverConstraint> newConstraints = exactCoverStep.constraints().stream()
                .filter(constraint -> !matchingConstraints.contains(constraint))
                .collect(Collectors.toSet());

        final Set<Integer> newOptions = exactCoverStep.options().stream()
                .filter(n -> !eliminatedOptions.contains(n))
                .collect(Collectors.toSet());

        return new ExactCoverStep(newValues, newOptions, newConstraints);
    }
}
