package cl.bozz.sudokusolver.linkedinqueens;

import cl.bozz.sudokusolver.algorithm.model.ExactCoverConstraint;
import cl.bozz.sudokusolver.sudoku.SudokuUtils;
import lombok.experimental.UtilityClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@UtilityClass
public class LinkedInQueensUtils {
    public Set<LinkedInQueensValue> readLinkedInQueensValuesFromResource(final String resourceName) throws IOException {
        final Set<LinkedInQueensValue> values = new HashSet<>();
        int length = 0;

        try (
                final InputStream inputStream = SudokuUtils.class.getClassLoader().getResourceAsStream(resourceName);
                final InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                final BufferedReader reader = new BufferedReader(streamReader)
        ) {
            int n = 0;
            int row = 1;
            String line;
            while ((line = reader.readLine()) != null) {
                if (length == 0) {
                    length = line.length();
                }
                for (int col = 1; col <= length; col++) {
                    values.add(new LinkedInQueensValue(n++, row, col));
                }
                row++;
            }
        }

        return values;
    }

    public Map<Character, Set<LinkedInQueensValue>> readLinkedInQueensGroupsFromResource(
            final String resourceName,
            final Set<LinkedInQueensValue> values
    ) throws IOException {
        final Map<Character, Set<Integer>> groupsAsInts = new HashMap<>();
        int length = 0;

        try (
                final InputStream inputStream = SudokuUtils.class.getClassLoader().getResourceAsStream(resourceName);
                final InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                final BufferedReader reader = new BufferedReader(streamReader)
        ) {
            int n = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                if (length == 0) {
                    length = line.length();
                }
                for (int col = 1; col <= length; col++) {
                    final char label = line.charAt(col - 1);

                    if (!groupsAsInts.containsKey(label)) {
                        groupsAsInts.put(label, new HashSet<>());
                    }

                    groupsAsInts.get(label).add(n++);
                }
            }
        }

        return groupsAsInts.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .map(n -> values.stream()
                                        .filter(v -> v.number() == n)
                                        .findFirst().orElseThrow()
                                ).collect(Collectors.toSet())
                ));
    }

    public Set<ExactCoverConstraint> createConstraints(final Set<LinkedInQueensValue> values, final Map<Character, Set<LinkedInQueensValue>> groups) {
        final int length = values.stream()
                .mapToInt(LinkedInQueensValue::row)
                .max().orElseThrow();

        final Set<ExactCoverConstraint> constraints = new HashSet<>();

        // Row/column constraints
        IntStream.range(1, length + 1).forEach(i -> {
            constraints.add(rowConstraint(i, values));
            constraints.add(columnConstraint(i, values));
        });

        // Group constraints
        groups.forEach((label, group) -> constraints.add(groupConstraint(label, values.size(), group)));

        return constraints;
    }

    public ExactCoverConstraint rowConstraint(final int row, final Set<LinkedInQueensValue> values) {
        final Boolean[] acceptedValues = new Boolean[values.size()];
        Arrays.fill(acceptedValues, false);
        values.stream()
                .filter(v -> v.row() == row)
                .map(LinkedInQueensValue::number)
                .forEach(v -> acceptedValues[v] = true);

        return new ExactCoverConstraint(
                "R" + row,
                acceptedValues
        );
    }

    public ExactCoverConstraint columnConstraint(final int col, final Set<LinkedInQueensValue> values) {
        final Boolean[] acceptedValues = new Boolean[values.size()];
        Arrays.fill(acceptedValues, false);
        values.stream()
                .filter(v -> v.col() == col)
                .map(LinkedInQueensValue::number)
                .forEach(v -> acceptedValues[v] = true);

        return new ExactCoverConstraint(
                "R" + col,
                acceptedValues
        );
    }

    public ExactCoverConstraint groupConstraint(final char label, final int valuesLength, final Set<LinkedInQueensValue> group) {
        final Boolean[] acceptedValues = new Boolean[valuesLength];
        Arrays.fill(acceptedValues, false);
        group.stream()
                .map(LinkedInQueensValue::number)
                .forEach(v -> acceptedValues[v] = true);

        return new ExactCoverConstraint(
                "G" + label,
                acceptedValues
        );
    }

    public Map<Integer, Set<Integer>> exclusionaryValues(final Set<LinkedInQueensValue> values) {
        return values.stream().collect(Collectors.toMap(
                LinkedInQueensValue::number,
                root -> values.stream()
                        .filter(v -> (v.row() >= root.row() - 1 && v.row() <= root.row() + 1) && (v.col() >= root.col() - 1 && v.col() <= root.col() + 1))
                        .filter(v -> v.row() != root.row() || v.col() != root.col())
                        .map(LinkedInQueensValue::number)
                        .collect(Collectors.toSet())
        ));
    }

    public String toPrettyString(final int length, final Set<Integer> values, final Map<Character, Set<LinkedInQueensValue>> groups) {
        final char[] rawResult = new char[length * length];

        groups.forEach((label, group) -> {
           group.stream()
                   .mapToInt(LinkedInQueensValue::number)
                   .forEach(v -> rawResult[v] = label);
        });
        values.forEach(v -> rawResult[v] = 'X');

        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rawResult.length; i++) {
            sb.append(rawResult[i]);
            if (i % length == length - 1 && i < rawResult.length - 1) {
                sb.append('\n');
            }
        }

        return sb.toString();
    }
}
