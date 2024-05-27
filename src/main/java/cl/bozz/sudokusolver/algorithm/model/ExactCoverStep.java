package cl.bozz.sudokusolver.algorithm.model;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents partial solution to be explored by the algorithm
 * @param values Choices made so far
 * @param options Remaining choices
 * @param constraints Rules used to pick new values and exclude incompatible options afterward
 * @param parent Parent state
 */
public record ExactCoverStep(Set<Integer> values, Set<Integer> options, Set<ExactCoverConstraint> constraints, ExactCoverStep parent) {

    @Override
    public String toString() {
        return "[" + values.stream()
                .sorted()
                .map(Object::toString)
                .collect(Collectors.joining(", ")) + "]";
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
