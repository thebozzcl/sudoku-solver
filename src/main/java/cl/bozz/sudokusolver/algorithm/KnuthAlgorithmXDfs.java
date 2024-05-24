package cl.bozz.sudokusolver.algorithm;

import cl.bozz.sudokusolver.model.ExactCoverConstraint;
import cl.bozz.sudokusolver.model.ExactCoverStep;
import cl.bozz.sudokusolver.utils.ExactCoverConstraintUtils;
import cl.bozz.sudokusolver.utils.ExactCoverStepUtils;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
public class KnuthAlgorithmXDfs implements ExactCoverAlgorithm {
    final boolean stopAtFirstResult;

    private static final Duration UPDATE_INTERVAL = Duration.ofSeconds(5);

    @Override
    public Set<ExactCoverStep> runAlgorithm(final ExactCoverStep initialStep) {


        final Stack<ExactCoverStep> processingStack = new Stack<>();
        processingStack.push(initialStep);

        final Set<ExactCoverStep> completeBoards = new HashSet<>();
        final AtomicInteger cacheHits = new AtomicInteger(0);
        final AtomicInteger culledByConstraintOptions = new AtomicInteger(0);
        final Set<String> processedHashes = new HashSet<>();

        Instant nextUpdate = Instant.now().plus(UPDATE_INTERVAL);

        while(!processingStack.isEmpty()) {
            final ExactCoverStep currentStep = processingStack.pop();
            processedHashes.add(currentStep.toString());

            final Instant now = Instant.now();
            if (now.isAfter(nextUpdate)) {
                System.out.println("Processed: " + processedHashes.size());
                System.out.println("Cache Hits: " + cacheHits.get());
                System.out.println("Culled By Constraint: " + culledByConstraintOptions.get());
                System.out.println("Solutions: " + completeBoards.size());
                System.out.println();

                nextUpdate = now.plus(UPDATE_INTERVAL);
            }

            final Set<ExactCoverConstraint> options = ExactCoverConstraintUtils.getLowestCardinalityConstraints(currentStep.constraints(), currentStep.options());
            if (options.isEmpty()) {
                culledByConstraintOptions.incrementAndGet();
                continue;
            }

            options.forEach(constraint -> {
                for (final int i : currentStep.options()) {
                    if (stopAtFirstResult && !completeBoards.isEmpty()) {
                        return;
                    }

                    if (!constraint.acceptedValues()[i]) {
                        continue;
                    }

                    final ExactCoverStep newStep = ExactCoverStepUtils.updateState(currentStep, i);
                    if (processedHashes.contains(newStep.toString())) {
                        cacheHits.incrementAndGet();
                        return;
                    }

                    if (newStep.constraints().isEmpty() && newStep.options().isEmpty()) {
                        processedHashes.add(newStep.toString());
                        completeBoards.add(newStep);
                        System.out.println("Found solution: " + newStep);
                        System.out.println();
                    } else if (!newStep.constraints().isEmpty() && !newStep.options().isEmpty()) {
                        processingStack.push(newStep);
                    }
                }
            });

            if (stopAtFirstResult && !completeBoards.isEmpty()) {
                break;
            }
        }

        return completeBoards;
    }
}
