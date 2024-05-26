package cl.bozz.sudokusolver.algorithm.model;

public record ExactCoverConstraint(String name, Boolean[] acceptedValues) {
    @Override
    public String toString() {
        return name;
    }
}
