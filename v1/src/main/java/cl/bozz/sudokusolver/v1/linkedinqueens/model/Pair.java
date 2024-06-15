package cl.bozz.sudokusolver.v1.linkedinqueens.model;

public record Pair<T1, T2>(T1 left, T2 right) {
    @Override
    public String toString() {
        return String.format("(%s, %s)", left, right);
    }

    @Override
    public int hashCode() {
        final String hash1 = "" + left.hashCode();
        final String hash2 = "" + right.hashCode();
        return (hash1 + hash2).hashCode();
    }
}
