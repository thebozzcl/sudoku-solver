package cl.bozz.sudokusolver.v1.linkedinqueens;

import cl.bozz.sudokusolver.v1.algorithm.KnuthAlgorithmX;
import cl.bozz.sudokusolver.v1.algorithm.model.ExactCoverConstraint;
import cl.bozz.sudokusolver.v1.algorithm.model.ExactCoverStep;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MainLinkedInQueens {

    public static void main(final String[] args) throws IOException {
        final Set<LinkedInQueensValue> values = LinkedInQueensUtils.readLinkedInQueensValuesFromResource(args[0]);
        final Map<Character, Set<LinkedInQueensValue>> groups = LinkedInQueensUtils.readLinkedInQueensGroupsFromResource(args[0], values);
        final Set<ExactCoverConstraint> constraints = LinkedInQueensUtils.createConstraints(values, groups);

        final ExactCoverStep initialStep = new ExactCoverStep(
                new HashSet<>(),
                IntStream.range(0, constraints.stream().findFirst().orElseThrow().acceptedChoices().length)
                        .boxed()
                        .collect(Collectors.toSet()),
                constraints
        );

        final Set<ExactCoverStep> completeSteps = new KnuthAlgorithmX(true).runAlgorithm(initialStep);

        final int length = values.stream()
                .mapToInt(LinkedInQueensValue::row)
                .max().orElseThrow();
        completeSteps.stream()
                .map(board -> LinkedInQueensUtils.toPrettyString(length, board.choices(), groups))
                .map(str -> str + '\n')
                .forEach(System.out::println);
    }
}
