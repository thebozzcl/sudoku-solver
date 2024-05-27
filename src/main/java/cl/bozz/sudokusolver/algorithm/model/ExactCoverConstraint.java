package cl.bozz.sudokusolver.algorithm.model;

/**
 * Represents a set of mutually exclusive choices to be made by the algorithm - once a value is picked, the remaining
 * choices, and all the other constraints that accept them, are removed from the child steps.
 * @param name
 * @param acceptedChoices A Boolean array, which should have an entry for each possible choice in the problem
 */
public record ExactCoverConstraint(String name, Boolean[] acceptedChoices) {
    @Override
    public String toString() {
        return name;
    }
}
