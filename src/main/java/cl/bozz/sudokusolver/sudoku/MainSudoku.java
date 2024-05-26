package cl.bozz.sudokusolver.sudoku;

import cl.bozz.sudokusolver.algorithm.KnuthAlgorithmXDfs;
import cl.bozz.sudokusolver.algorithm.model.ExactCoverStep;

import java.io.IOException;
import java.util.Set;

public class MainSudoku {

    public static void main(final String[] args) throws IOException {
        final ExactCoverStep initialBoard = SudokuUtils.readSudokuFromResource(args[0]);

        final Set<ExactCoverStep> completeBoards = new KnuthAlgorithmXDfs(true).runAlgorithm(initialBoard);

        completeBoards.stream()
                .map(SudokuUtils::toPrettyString)
                .forEach(System.out::println);
    }
}
