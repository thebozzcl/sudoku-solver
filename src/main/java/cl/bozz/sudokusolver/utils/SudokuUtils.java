package cl.bozz.sudokusolver.utils;

import cl.bozz.sudokusolver.Main;
import cl.bozz.sudokusolver.model.ExactCoverConstraint;
import cl.bozz.sudokusolver.model.SudokuCellValues;
import cl.bozz.sudokusolver.model.ExactCoverStep;
import lombok.experimental.UtilityClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@UtilityClass
public class SudokuUtils {
    public ExactCoverConstraint rowColumnConstraint(final int row, final int col) {
        return ExactCoverConstraintUtils.fromAcceptedEnumValues(
                String.format("R%dC%d", row, col),
                Arrays.stream(SudokuCellValues.values())
                        .filter(v -> v.getRow() == row && v.getCol() == col)
                        .collect(Collectors.toSet()),
                SudokuCellValues.class
        );
    }

    public ExactCoverConstraint rowNumberConstraint(final int row, final int val) {
        return ExactCoverConstraintUtils.fromAcceptedEnumValues(
                String.format("R%dV%d", row, val),
                Arrays.stream(SudokuCellValues.values())
                        .filter(v -> v.getRow() == row && v.getValue() == val)
                        .collect(Collectors.toSet()),
                SudokuCellValues.class
        );
    }

    public ExactCoverConstraint columnNumberConstraint(final int col, final int val) {
        return ExactCoverConstraintUtils.fromAcceptedEnumValues(
                String.format("C%dV%d", col, val),
                Arrays.stream(SudokuCellValues.values())
                        .filter(v -> v.getCol() == col && v.getValue() == val)
                        .collect(Collectors.toSet()),
                SudokuCellValues.class
        );
    }

    public ExactCoverConstraint boxNumberConstraint(final int box, final int val) {
        return ExactCoverConstraintUtils.fromAcceptedEnumValues(
                String.format("B%dV%d", box, val),
                Arrays.stream(SudokuCellValues.values())
                        .filter(v -> v.getBox() == box && v.getValue() == val)
                        .collect(Collectors.toSet()),
                SudokuCellValues.class
        );
    }

    public Set<ExactCoverConstraint> createEmptySudokuConstraints() {
        final Set<ExactCoverConstraint> constraints = new HashSet<>();

        IntStream.range(1, 10).forEach(i -> IntStream.range(1, 10)
                .forEach(j -> {
                    constraints.add(rowColumnConstraint(i, j));
                    constraints.add(columnNumberConstraint(i, j));
                    constraints.add(rowNumberConstraint(i, j));
                    constraints.add(boxNumberConstraint(i, j));
                })
        );

        return constraints;
    }

    public String toPrettyString(final ExactCoverStep exactCoverStep) {
        final List<String> lines = new java.util.ArrayList<>(List.of(
                ".........",
                ".........",
                ".........",
                ".........",
                ".........",
                ".........",
                ".........",
                ".........",
                "........."
        ));

        exactCoverStep.values().stream()
                .map(n -> SudokuCellValues.values()[n])
                .forEach(value -> {
                    char[] newLine = lines.get(value.getRow() - 1).toCharArray();
                    newLine[value.getCol() - 1] = (char) (value.getValue() + '1' - 1);
                    lines.set(
                            value.getRow() - 1,
                            String.valueOf(newLine)
                    );
                });

        return String.join("\n", lines);
    }

    public ExactCoverStep readSudokuFromResource(final String resourceName) throws IOException {
        int cellOffset = 0;
        final Set<Integer> setValues = new HashSet<>();
        try (
                final InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(resourceName);
                final InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                final BufferedReader reader = new BufferedReader(streamReader)
        ) {
            int c;
            while ((c = reader.read()) != -1) {
                if (c != '.') {
                    final int value = c - '1';
                    setValues.add(value + cellOffset);
                }
                cellOffset += 9;
            }
        }

        ExactCoverStep exactCoverStep = ExactCoverStepUtils.emptyForEnum(SudokuCellValues.class, SudokuUtils.createEmptySudokuConstraints());
        for (final Integer setValue : setValues) {
            exactCoverStep = ExactCoverStepUtils.updateState(exactCoverStep, setValue);
        }
        return exactCoverStep;
    }
}
