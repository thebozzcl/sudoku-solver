package cl.bozz.sudokusolver.utils;

import cl.bozz.sudokusolver.Main;
import cl.bozz.sudokusolver.model.Constraint;
import cl.bozz.sudokusolver.model.SudokuCellValues;
import cl.bozz.sudokusolver.model.ExactCoverState;
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
import java.util.stream.IntStream;

@UtilityClass
public class SudokuUtils {
    private Boolean[] getEmptyAcceptedValues() {
        final Boolean[] empty = new Boolean[SudokuCellValues.values().length];
        Arrays.fill(empty, false);
        return empty;
    }

    public Constraint rowColumnConstraint(final int row, final int col) {
        final Boolean[] values = getEmptyAcceptedValues();

        Arrays.stream(SudokuCellValues.values())
                .filter(v -> v.getRow() == row && v.getCol() == col)
                .mapToInt(Enum::ordinal)
                .forEach(i -> values[i] = true);

        return new Constraint(String.format("R%dC%d", row, col), values);
    }

    public Constraint rowNumberConstraint(final int row, final int val) {
        final Boolean[] values = getEmptyAcceptedValues();

        Arrays.stream(SudokuCellValues.values())
                .filter(v -> v.getRow() == row && v.getValue() == val)
                .mapToInt(Enum::ordinal)
                .forEach(i -> values[i] = true);

        return new Constraint(String.format("R%dV%d", row, val), values);
    }

    public Constraint columnNumberConstraint(final int col, final int val) {
        final Boolean[] values = getEmptyAcceptedValues();

        Arrays.stream(SudokuCellValues.values())
                .filter(v -> v.getCol() == col && v.getValue() == val)
                .mapToInt(Enum::ordinal)
                .forEach(i -> values[i] = true);

        return new Constraint(String.format("C%dV%d", col, val), values);
    }

    public Constraint boxNumberConstraint(final int box, final int val) {
        final Boolean[] values = getEmptyAcceptedValues();

        Arrays.stream(SudokuCellValues.values())
                .filter(v -> v.getBox() == box && v.getValue() == val)
                .mapToInt(Enum::ordinal)
                .forEach(i -> values[i] = true);

        return new Constraint(String.format("B%dV%d", box, val), values);
    }

    public Set<Constraint> createEmptyConstraints() {
        final Set<Constraint> constraints = new HashSet<>();

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

    public String toPrettyString(final ExactCoverState exactCoverState) {
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

        exactCoverState.values().stream()
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

    public ExactCoverState readSudokuFromResource(final String resourceName) throws IOException {
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

        ExactCoverState exactCoverState = ExactCoverStateUtils.emptyForEnum(SudokuCellValues.class, SudokuUtils.createEmptyConstraints());
        for (final Integer setValue : setValues) {
            exactCoverState = ExactCoverStateUtils.updateBoardState(exactCoverState, setValue);
        }
        return exactCoverState;
    }
}
