package cl.bozz.sudokusolver.algorithm;

import cl.bozz.sudokusolver.algorithm.model.ExactCoverConstraint;
import cl.bozz.sudokusolver.algorithm.model.ExactCoverStep;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Abstract implementation of Knuth's Algorithm X.
 * <br />
 * The general loop is as follows:
 * <ol>
 *     <li>Find the constraints with the lowest cardinality (fewest accepted choices).</li>
 *     <li>For each constraint found in the previous step, get the available choices it can accept and:</li>
 *     <ol>
 *         <li>Create a new step by applying the current choice:</li>
 *         <ul>
 *             <li>Remove the current choice from the available options.</li>
 *             <li>Remove any constraints that accepted the current choice.</li>
 *             <li>Remove any choices accepted by the removed constraints - since constraints are mutually-exclusive
 *             choices, none of those choices are valid anymore.</li>
 *         </ul>
 *         <li>If the step is complete (no more options to choose and no more constraints to satisfy) finish.</li>
 *         <li>If the step is incomplete but valid (there are both choices to make and constraints to satisfy),
 *         process this new step recursively.</li>
 *     </ol>
 * <ol/>
 * For my implementation, I decided to go with a stack-based strategy instead of recursion.
 */
@RequiredArgsConstructor
public abstract class AbstractKnuthAlgorithmX implements ExactCoverAlgorithm {
    private final boolean stopAtFirstResult;

    private static final Duration UPDATE_INTERVAL = Duration.ofSeconds(5);

    @Override
    public Set<ExactCoverStep> runAlgorithm(ExactCoverStep initialStep) {

        final Set<ExactCoverStep> completeSteps = new HashSet<>();
        final AtomicInteger cacheHits = new AtomicInteger(0);
        final AtomicInteger culledByConstraintOptions = new AtomicInteger(0);
        final Set<String> queuedHashes = new HashSet<>();

        final Stack<ExactCoverStep> processingStack = new Stack<>();
        processingStack.push(initialStep);
        queuedHashes.add(initialStep.toString());
        Instant nextUpdate = Instant.now().plus(UPDATE_INTERVAL);

        while(!processingStack.isEmpty()) {
            final ExactCoverStep currentStep = processingStack.pop();

            final Instant now = Instant.now();
            if (now.isAfter(nextUpdate)) {
                System.out.println("In queue: " + processingStack.size());
                System.out.println("Hashes: " + queuedHashes.size());
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
                    if (queuedHashes.contains(newStep.toString())) {
                        cacheHits.incrementAndGet();
                        return;
                    }
                    queuedHashes.add(newStep.toString());

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

    protected abstract Set<ExactCoverConstraint> getLowestCardinalityConstraints(final ExactCoverStep step);

    protected abstract ExactCoverStep updateStep(final ExactCoverStep step, final int option);

    protected abstract boolean isComplete(final ExactCoverStep step);

    protected abstract boolean isValidIncomplete(final ExactCoverStep step);
}
