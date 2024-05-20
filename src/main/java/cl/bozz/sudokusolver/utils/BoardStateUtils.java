package cl.bozz.sudokusolver.utils;

import cl.bozz.sudokusolver.model.BoardState;
import cl.bozz.sudokusolver.model.CellValues;
import cl.bozz.sudokusolver.model.Constraint;
import lombok.experimental.UtilityClass;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@UtilityClass
public class BoardStateUtils {
    public BoardState updateBoardState(final BoardState boardState, final int update) {
        final Set<Integer> newState = new HashSet<>(boardState.values());
        newState.add(update);

        final Set<Constraint> matchingConstraints = ConstraintUtils.findConstraintsWithValue(boardState.constraints(), update);
        final Set<Integer> eliminatedOptions = matchingConstraints.stream()
                .flatMap(constraint -> IntStream.range(0, constraint.acceptedValues().length)
                        .filter(n -> constraint.acceptedValues()[n])
                        .boxed()
                )
                .collect(Collectors.toSet());
        final Set<Constraint> newConstraints = boardState.constraints().stream()
                .filter(constraint -> !matchingConstraints.contains(constraint))
                .collect(Collectors.toSet());

        final Set<Integer> newOptions = boardState.options().stream()
                .filter(n -> !eliminatedOptions.contains(n))
                .collect(Collectors.toSet());

        return new BoardState(newState, newOptions, newConstraints, boardState);
    }

    public String toPrettyString(final BoardState boardState) {
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

        boardState.values().stream()
                .map(n -> CellValues.values()[n])
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
}
