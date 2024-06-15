package cl.bozz.sudokusolver.v1.linkedinqueens;

public record LinkedInQueensValue(int number, int row, int col) {
    @Override
    public String toString() {
        return String.format("[%d] R%dC%d", number, row, col);
    }
}
