package cl.bozz.sudokusolver.linkedinqueens;

import cl.bozz.sudokusolver.algorithm.ExactCoverAlgorithm;
import cl.bozz.sudokusolver.algorithm.model.ExactCoverConstraint;
import cl.bozz.sudokusolver.algorithm.model.ExactCoverStep;
import cl.bozz.sudokusolver.algorithm.utils.ExactCoverConstraintUtils;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
public class LinkedInQueensXDfs implements ExactCoverAlgorithm {
    final boolean stopAtFirstResult;
    final Map<Integer, Set<Integer>> exclusionaryValues;

    private static final Duration UPDATE_INTERVAL = Duration.ofSeconds(5);

    @Override
    public Set<ExactCoverStep> runAlgorithm(final ExactCoverStep initialStep) {
        final Set<ExactCoverStep> completeBoards = new HashSet<>();
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
                System.out.println("Solutions: " + completeBoards.size());
                System.out.println();

                nextUpdate = now.plus(UPDATE_INTERVAL);
            }

            final Set<ExactCoverConstraint> optionsWithLowestCardinality = ExactCoverConstraintUtils.getLowestCardinalityConstraints(
                    currentStep.constraints(),
                    currentStep.options()
            );
            if (optionsWithLowestCardinality.isEmpty()) {
                culledByConstraintOptions.incrementAndGet();
                continue;
            }

            optionsWithLowestCardinality.forEach(constraint -> {
                for (final int i : currentStep.options()) {
                    if (stopAtFirstResult && !completeBoards.isEmpty()) {
                        return;
                    }

                    if (!constraint.acceptedValues()[i]) {
                        continue;
                    }

                    final ExactCoverStep newStep = updateStateAndExclude(currentStep, i);
                    if (queuedHashes.contains(newStep.toString())) {
                        cacheHits.incrementAndGet();
                        return;
                    }
                    queuedHashes.add(newStep.toString());

                    if (newStep.constraints().isEmpty() && newStep.options().isEmpty()) {
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

    private ExactCoverStep updateStateAndExclude(final ExactCoverStep exactCoverStep, final int update) {
        final Set<Integer> newValues = new HashSet<>(exactCoverStep.values());
        newValues.add(update);

        final Set<ExactCoverConstraint> matchingConstraints = ExactCoverConstraintUtils.findConstraintsWithValue(exactCoverStep.constraints(), update);
        final Set<Integer> eliminatedOptions = matchingConstraints.stream()
                .flatMap(constraint -> IntStream.range(0, constraint.acceptedValues().length)
                        .filter(n -> constraint.acceptedValues()[n])
                        .boxed()
                )
                .collect(Collectors.toSet());
        final Set<ExactCoverConstraint> newConstraints = exactCoverStep.constraints().stream()
                .filter(constraint -> !matchingConstraints.contains(constraint))
                .collect(Collectors.toSet());

        final Set<Integer> newOptions = exactCoverStep.options().stream()
                .filter(n -> !eliminatedOptions.contains(n))
                .filter(n -> !exclusionaryValues.get(update).contains(n))
                .collect(Collectors.toSet());

        return new ExactCoverStep(newValues, newOptions, newConstraints, exactCoverStep);
    }
}
