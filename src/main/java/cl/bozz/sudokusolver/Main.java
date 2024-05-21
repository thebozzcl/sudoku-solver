package cl.bozz.sudokusolver;

import cl.bozz.sudokusolver.model.ExactCoverState;
import cl.bozz.sudokusolver.model.Constraint;
import cl.bozz.sudokusolver.utils.ExactCoverStateUtils;
import cl.bozz.sudokusolver.utils.ConstraintUtils;
import cl.bozz.sudokusolver.utils.SudokuUtils;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private static final Duration UPDATE_INTERVAL = Duration.ofSeconds(5);

    public static void main(final String[] args) throws IOException {
        final ExactCoverState initialBoard = SudokuUtils.readSudokuFromResource(args[0]);

        final Stack<ExactCoverState> processingStack = new Stack<>();
        processingStack.push(initialBoard);

        final Set<ExactCoverState> completeBoards = new HashSet<>();
        final AtomicInteger cacheHits = new AtomicInteger(0);
        final Set<Integer> processedHashes = new HashSet<>();

        Instant nextUpdate = Instant.now().plus(UPDATE_INTERVAL);

        while(!processingStack.isEmpty()) {
            final ExactCoverState currentBoard = processingStack.pop();
            processedHashes.add(currentBoard.hashCode());

            final Instant now = Instant.now();
            if (now.isAfter(nextUpdate)) {
                System.out.println("Processed: " + processedHashes.size());
                System.out.println("Cache Hits: " + cacheHits.get());
                System.out.println("Solutions: " + completeBoards.size());
                System.out.println("Current: ");
                System.out.println(SudokuUtils.toPrettyString(currentBoard));
                System.out.println();

                nextUpdate = now.plus(UPDATE_INTERVAL);
            }

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
                        cacheHits.incrementAndGet();
                        return;
                    }

                    if (!newBoard.constraints().isEmpty() && newBoard.values().size() < 81) {
                        processingStack.push(newBoard);
                    } else if (newBoard.constraints().isEmpty() && newBoard.values().size() == 81) {
                        processedHashes.add(newBoard.hashCode());
                        completeBoards.add(newBoard);
                        System.out.println(newBoard);
                        System.out.println(SudokuUtils.toPrettyString(newBoard));
                        System.out.println();
                    }
                }
            });
        }
    }
}
