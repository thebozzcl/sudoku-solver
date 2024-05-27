package cl.bozz.sudokusolver.linkedinqueens;

import cl.bozz.sudokusolver.algorithm.AbstractKnuthAlgorithmX;
import cl.bozz.sudokusolver.algorithm.model.ExactCoverConstraint;
import cl.bozz.sudokusolver.algorithm.model.ExactCoverStep;
import cl.bozz.sudokusolver.algorithm.utils.ExactCoverConstraintUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Custom implementation of Knuth's Algorithm X to be able to solve the LinkedIn Queens daily puzzle.
 * <br />
 * The only difference with the regular Algorithm X is the addition of "exclusionary choices". This
 * variation of the n-queens problem has a rule I wasn't able to represent as a constraint: there's
 * no restriction on putting queens in the same diagonal, but you can't place them next to each other.
 * <br />
 * The problem with this rule is that it's not really an "exact cover", or at least I haven't been able
 * to interpret it as such. Creating constraints to model this relation seems to always result in other
 * valid constraints being removed.
 * <br />
 * Until I find a set of exact cover constraints for this, I decided to take a shortcut and just have a
 * map that tells the algorithm which choices are made impossible after taking another but do not make
 * constraints unsatisfiable.
 */
public class LinkedInQueensXDfs extends AbstractKnuthAlgorithmX {
    final Map<Integer, Set<Integer>> exclusionaryChoices;

    public LinkedInQueensXDfs(
            final boolean stopAtFirstResult,
            final Map<Integer, Set<Integer>> exclusionaryChoices
    ) {
        super(stopAtFirstResult);
        this.exclusionaryChoices = exclusionaryChoices;
    }

    @Override
    protected Set<ExactCoverConstraint> getLowestCardinalityConstraints(final ExactCoverStep step) {
        return ExactCoverConstraintUtils.getLowestCardinalityConstraints(
                step.constraints(),
                step.options()
        );
    }

    @Override
    protected ExactCoverStep updateStep(final ExactCoverStep step, final int option) {
        final Set<Integer> newValues = new HashSet<>(step.choices());
        newValues.add(option);

        final Set<ExactCoverConstraint> matchingConstraints = ExactCoverConstraintUtils.findConstraintsWithValue(step.constraints(), option);
        final Set<Integer> eliminatedOptions = matchingConstraints.stream()
                .flatMap(constraint -> IntStream.range(0, constraint.acceptedChoices().length)
                        .filter(n -> constraint.acceptedChoices()[n])
                        .boxed()
                )
                .collect(Collectors.toSet());
        final Set<ExactCoverConstraint> newConstraints = step.constraints().stream()
                .filter(constraint -> !matchingConstraints.contains(constraint))
                .collect(Collectors.toSet());

        final Set<Integer> newOptions = step.options().stream()
                .filter(n -> !eliminatedOptions.contains(n))
                .filter(n -> !exclusionaryChoices.get(option).contains(n))
                .collect(Collectors.toSet());

        return new ExactCoverStep(newValues, newOptions, newConstraints);
    }

    @Override
    protected boolean isComplete(final ExactCoverStep step) {
        return step.constraints().isEmpty() && step.options().isEmpty();
    }

    @Override
    protected boolean isValidIncomplete(final ExactCoverStep step) {
        return !step.constraints().isEmpty() && !step.options().isEmpty();
    }
}
