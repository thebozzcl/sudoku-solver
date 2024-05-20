package cl.bozz.sudokusolver.model;

public record Constraint(String name, Boolean[] acceptedValues) {
    @Override
    public String toString() {
        return name;
    }
}
