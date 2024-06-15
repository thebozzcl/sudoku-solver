package cl.bozz.sudokusolver.v1.algorithm.model;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents a step taken in the exact cover algorithm.
 * @param choices Choices that have been made so far
 * @param options Remaining options to choose from
 * @param constraints Sets of mutually-exclusive choices
 */
public record ExactCoverStep(Set<Integer> choices, Set<Integer> options, Set<ExactCoverConstraint> constraints) {

    @Override
    public String toString() {
        return "[" + choices.stream()
                .sorted()
                .map(Object::toString)
                .collect(Collectors.joining(", ")) + "]";
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
