package cl.bozz.sudokusolver.linkedinqueens;

import cl.bozz.sudokusolver.algorithm.model.ExactCoverStep;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MainLinkedInQueens {

    public static void main(final String[] args) throws IOException {
        final Set<LinkedInQueensValue> values = LinkedInQueensUtils.readLinkedInQueensValuesFromResource(args[0]);
        final Map<Character, Set<LinkedInQueensValue>> groups = LinkedInQueensUtils.readLinkedInQueensGroupsFromResource(args[0], values);
        final Map<Integer, Set<Integer>> exclusionaryValues = LinkedInQueensUtils.exclusionaryValues(values);

        final ExactCoverStep initialBoard = new ExactCoverStep(
                new HashSet<>(),
                values.stream()
                        .map(LinkedInQueensValue::number)
                        .collect(Collectors.toSet()),
                LinkedInQueensUtils.createConstraints(values, groups),
                null
        );

        final Set<ExactCoverStep> completeBoards = new LinkedInQueensXDfs(true, exclusionaryValues).runAlgorithm(initialBoard);

        final int length = values.stream()
                .mapToInt(LinkedInQueensValue::row)
                .max().orElseThrow();
        completeBoards.stream()
                .map(board -> LinkedInQueensUtils.toPrettyString(length, board.values(), groups))
                .map(str -> str + '\n')
                .forEach(System.out::println);
    }
}
