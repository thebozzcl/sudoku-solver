package cl.bozz.sudokusolver;

import cl.bozz.sudokusolver.model.ExactCoverState;
import cl.bozz.sudokusolver.model.Constraint;
import cl.bozz.sudokusolver.utils.ExactCoverStateUtils;
import cl.bozz.sudokusolver.utils.ConstraintUtils;
import cl.bozz.sudokusolver.utils.SudokuUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class Main {
    public static void main(final String[] args) throws IOException {
        final ExactCoverState initialBoard = SudokuUtils.readSudokuFromResource(args[0]);

        final Stack<ExactCoverState> processingStack = new Stack<>();
        processingStack.push(initialBoard);

        //final Set<BoardState> completeBoards = new HashSet<>();
        final Set<Integer> processedHashes = new HashSet<>();

        while(!processingStack.isEmpty()) {
            final ExactCoverState currentBoard = processingStack.pop();
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

                    final ExactCoverState newBoard = ExactCoverStateUtils.updateBoardState(currentBoard, i);
                    if (processedHashes.contains(newBoard.hashCode())) {
                        return;
                    }

                    if (!newBoard.constraints().isEmpty() && newBoard.values().size() < 81) {
                        processingStack.push(newBoard);
                    } else if (newBoard.constraints().isEmpty() && newBoard.values().size() == 81) {
                        processedHashes.add(newBoard.hashCode());
                        //completeBoards.add(newBoard);
                        System.out.println(newBoard);
                        System.out.println(SudokuUtils.toPrettyString(newBoard));
                        System.out.println();
                    }
                }
            });
        }
    }
}
