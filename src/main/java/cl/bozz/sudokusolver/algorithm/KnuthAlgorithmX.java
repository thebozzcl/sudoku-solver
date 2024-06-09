package cl.bozz.sudokusolver.algorithm;

import cl.bozz.sudokusolver.algorithm.model.ExactCoverConstraint;
import cl.bozz.sudokusolver.algorithm.model.ExactCoverStep;
import cl.bozz.sudokusolver.algorithm.utils.ExactCoverConstraintUtils;
import cl.bozz.sudokusolver.algorithm.utils.ExactCoverStepUtils;
import cl.bozz.sudokusolver.algorithm.utils.NoRepeatStack;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Default implementation of Knuth's Algorithm X.
 */
@RequiredArgsConstructor
public class KnuthAlgorithmX implements ExactCoverAlgorithm {
    private final boolean stopAtFirstResult;

    private static final Duration UPDATE_INTERVAL = Duration.ofSeconds(5);

    @Override
    public Set<ExactCoverStep> runAlgorithm(ExactCoverStep initialStep) {

        final Set<ExactCoverStep> completeSteps = new HashSet<>();
        final AtomicInteger cacheHits = new AtomicInteger(0);
        final AtomicInteger culledByConstraintOptions = new AtomicInteger(0);

        final NoRepeatStack<ExactCoverStep> processingStack = new NoRepeatStack<>();
        processingStack.push(initialStep);
        Instant nextUpdate = Instant.now().plus(UPDATE_INTERVAL);

        while(!processingStack.isEmpty()) {
            final ExactCoverStep currentStep = processingStack.pop();

            final Instant now = Instant.now();
            if (now.isAfter(nextUpdate)) {
                System.out.println("In queue: " + processingStack.size());
                System.out.println("Hashes: " + processingStack.hashesSize());
                System.out.println("Cache Hits: " + cacheHits.get());
                System.out.println("Culled By Constraint: " + culledByConstraintOptions.get());
                System.out.println("Solutions: " + completeSteps.size());
                System.out.println();

                nextUpdate = now.plus(UPDATE_INTERVAL);
            }

            final Set<ExactCoverConstraint> lowestCardinalityConstraints = getLowestCardinalityConstraints(currentStep);
            if (lowestCardinalityConstraints.isEmpty()) {
                culledByConstraintOptions.incrementAndGet();
                continue;
            }

            lowestCardinalityConstraints.forEach(constraint -> {
                for (final int i : currentStep.options()) {
                    if (stopAtFirstResult && !completeSteps.isEmpty()) {
                        return;
                    }

                    if (!constraint.acceptedChoices()[i]) {
                        continue;
                    }

                    final ExactCoverStep newStep = updateStep(currentStep, i);
                    if (!processingStack.push(newStep)) {
                        cacheHits.incrementAndGet();
                        return;
                    }

                    if (isComplete(newStep)) {
                        completeSteps.add(newStep);
                        System.out.println("Found solution: " + newStep);
                        System.out.println();
                    } else if (isValidIncomplete(currentStep)) {
                        processingStack.push(newStep);
                    }
                }
            });

            if (stopAtFirstResult && !completeSteps.isEmpty()) {
                break;
            }
        }

        return completeSteps;
    }

    protected Set<ExactCoverConstraint> getLowestCardinalityConstraints(final ExactCoverStep step) {
        return ExactCoverConstraintUtils.getLowestCardinalityConstraints(
                step.constraints(),
                step.options()
        );
    }

    protected ExactCoverStep updateStep(final ExactCoverStep step, final int option) {
        return ExactCoverStepUtils.updateStep(step, option);
    }

    protected boolean isComplete(final ExactCoverStep step) {
        return step.constraints().isEmpty() && step.options().isEmpty();
    }

    protected boolean isValidIncomplete(final ExactCoverStep step) {
        return !step.constraints().isEmpty() && !step.options().isEmpty();
    }
}
