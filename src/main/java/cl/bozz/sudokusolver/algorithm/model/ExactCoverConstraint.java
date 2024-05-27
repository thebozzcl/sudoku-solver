package cl.bozz.sudokusolver.algorithm.model;

/**
 * Represents a choice: the algorithm must choose one of the values accepted by this constraint, excluding the rest
 * from the candidate solution.
 * @param name
 * @param acceptedValues
 */
public record ExactCoverConstraint(String name, Boolean[] acceptedValues) {
    @Override
    public String toString() {
        return name;
    }
}
