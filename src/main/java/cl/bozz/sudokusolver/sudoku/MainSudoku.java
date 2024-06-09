package cl.bozz.sudokusolver.sudoku;

import cl.bozz.sudokusolver.algorithm.KnuthAlgorithmX;
import cl.bozz.sudokusolver.algorithm.model.ExactCoverStep;

import java.io.IOException;
import java.util.Set;

public class MainSudoku {

    public static void main(final String[] args) throws IOException {
        final ExactCoverStep initialStep = SudokuUtils.readSudokuFromResource(args[0]);

        final Set<ExactCoverStep> completeSteps = new KnuthAlgorithmX(true).runAlgorithm(initialStep);

        completeSteps.stream()
                .map(SudokuUtils::toPrettyString)
                .forEach(System.out::println);
    }
}
