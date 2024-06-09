package cl.bozz.sudokusolver.wikiexample;

import cl.bozz.sudokusolver.algorithm.KnuthAlgorithmX;
import cl.bozz.sudokusolver.algorithm.model.ExactCoverStep;

import java.util.Set;
import java.util.stream.Collectors;

public class MainWikiExample {

    public static void main(final String[] args) {
        final ExactCoverStep initialStep = WikiExampleUtils.wikiExampleInitialStep();

        final Set<ExactCoverStep> completeSteps = new KnuthAlgorithmX(false).runAlgorithm(initialStep);

        completeSteps.stream()
                .map(ExactCoverStep::choices)
                .map(values -> values.stream().map(n -> WikiExampleValues.values()[n]).collect(Collectors.toSet()))
                .forEach(System.out::println);
    }
}
