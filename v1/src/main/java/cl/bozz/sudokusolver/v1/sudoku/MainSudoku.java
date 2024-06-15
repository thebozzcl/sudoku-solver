package cl.bozz.sudokusolver.v1.sudoku;

import cl.bozz.sudokusolver.v1.algorithm.KnuthAlgorithmX;
import cl.bozz.sudokusolver.v1.algorithm.model.ExactCoverStep;

import java.io.IOException;
import java.util.Set;

public class MainSudoku {

    public static void main(final String[] args) throws IOException {
        final ExactCoverStep initialStep = SudokuUtils.readSudokuFromResource(args[0]);

        final Set<ExactCoverStep> completeSteps = new KnuthAlgorithmX(false).runAlgorithm(initialStep);

        completeSteps.stream()
                .map(SudokuUtils::toPrettyString)
                .forEach(System.out::println);
    }
}
