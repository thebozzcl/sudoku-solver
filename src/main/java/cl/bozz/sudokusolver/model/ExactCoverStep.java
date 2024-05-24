package cl.bozz.sudokusolver.model;

import java.util.Set;
import java.util.stream.Collectors;

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
