package cl.bozz.sudokusolver.utils;

import cl.bozz.sudokusolver.model.ExactCoverState;
import cl.bozz.sudokusolver.model.Constraint;
import lombok.experimental.UtilityClass;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@UtilityClass
public class ExactCoverStateUtils {
    public static ExactCoverState emptyForEnum(final Class<? extends Enum<?>> enumClass, final Set<Constraint> constraints) {
        return new ExactCoverState(
                new HashSet<>(),
                IntStream.range(0, enumClass.getEnumConstants().length).boxed().collect(Collectors.toSet()),
                constraints,
                null
        );
    }

    public ExactCoverState updateBoardState(final ExactCoverState exactCoverState, final int update) {
        final Set<Integer> newState = new HashSet<>(exactCoverState.values());
        newState.add(update);

        final Set<Constraint> matchingConstraints = ConstraintUtils.findConstraintsWithValue(exactCoverState.constraints(), update);
        final Set<Integer> eliminatedOptions = matchingConstraints.stream()
                .flatMap(constraint -> IntStream.range(0, constraint.acceptedValues().length)
                        .filter(n -> constraint.acceptedValues()[n])
                        .boxed()
                )
                .collect(Collectors.toSet());
        final Set<Constraint> newConstraints = exactCoverState.constraints().stream()
                .filter(constraint -> !matchingConstraints.contains(constraint))
                .collect(Collectors.toSet());

        final Set<Integer> newOptions = exactCoverState.options().stream()
                .filter(n -> !eliminatedOptions.contains(n))
                .collect(Collectors.toSet());

        return new ExactCoverState(newState, newOptions, newConstraints, exactCoverState);
    }
}
