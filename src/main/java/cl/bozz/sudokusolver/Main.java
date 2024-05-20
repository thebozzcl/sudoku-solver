package cl.bozz.sudokusolver;

import cl.bozz.sudokusolver.model.BoardState;
import cl.bozz.sudokusolver.model.Constraint;
import cl.bozz.sudokusolver.utils.BoardStateUtils;
import cl.bozz.sudokusolver.utils.ConstraintUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class Main {
    public static void main(final String[] args) throws IOException {
        final BoardState initialBoard = getInitialState(args[0]);

        final Stack<BoardState> processingStack = new Stack<>();
        processingStack.push(initialBoard);

        //final Set<BoardState> completeBoards = new HashSet<>();
        final Set<Integer> processedHashes = new HashSet<>();

        while(!processingStack.isEmpty()) {
            final BoardState currentBoard = processingStack.pop();
            processedHashes.add(currentBoard.hashCode());

            final Set<Constraint> options = ConstraintUtils.getLowestCardinalityConstraints(currentBoard.constraints(), currentBoard.options());
            if (options.isEmpty()) {
                continue;
            }

            options.forEach(constraint -> {
                for (final int i : currentBoard.options()) {
                    if (!constraint.acceptedValues()[i]) {
                        continue;
                    }

                    final BoardState newBoard = BoardStateUtils.updateBoardState(currentBoard, i);
                    if (processedHashes.contains(newBoard.hashCode())) {
                        return;
                    }

                    if (!newBoard.constraints().isEmpty() && newBoard.values().size() < 81) {
                        processingStack.push(newBoard);
                    } else if (newBoard.constraints().isEmpty() && newBoard.values().size() == 81) {
                        processedHashes.add(newBoard.hashCode());
                        //completeBoards.add(newBoard);
                        System.out.println(newBoard);
                        System.out.println(BoardStateUtils.toPrettyString(newBoard));
                        System.out.println();
                    }
                }
            });
        }
    }

    private static BoardState getInitialState(final String resourceName) throws IOException {
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

        BoardState boardState = BoardState.EMPTY;
        for (final Integer setValue : setValues) {
            boardState = BoardStateUtils.updateBoardState(boardState, setValue);
        }
        return boardState;
    }
}
