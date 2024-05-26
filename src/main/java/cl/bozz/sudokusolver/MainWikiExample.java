package cl.bozz.sudokusolver;

import cl.bozz.sudokusolver.algorithm.KnuthAlgorithmXDfs;
import cl.bozz.sudokusolver.model.ExactCoverStep;
import cl.bozz.sudokusolver.model.WikiExampleValues;
import cl.bozz.sudokusolver.utils.SudokuUtils;
import cl.bozz.sudokusolver.utils.WikiExampleUtils;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

public class MainWikiExample {

    public static void main(final String[] args) throws IOException {
        final ExactCoverStep initialBoard = WikiExampleUtils.wikiExampleInitialStep();

        final Set<ExactCoverStep> completeBoards = new KnuthAlgorithmXDfs(false).runAlgorithm(initialBoard);

        completeBoards.stream()
                .map(ExactCoverStep::values)
                .map(values -> values.stream().map(n -> WikiExampleValues.values()[n]).collect(Collectors.toSet()))
                .forEach(System.out::println);
    }
}
