package cl.bozz.sudokusolver.algorithm;

import cl.bozz.sudokusolver.algorithm.model.ExactCoverConstraint;
import cl.bozz.sudokusolver.algorithm.model.ExactCoverStep;
import cl.bozz.sudokusolver.algorithm.utils.ExactCoverConstraintUtils;
import cl.bozz.sudokusolver.algorithm.utils.ExactCoverStepUtils;

import java.util.Set;

/**
 * Default implementation of Knuth's Algorithm X.
 */
public class KnuthAlgorithmX extends AbstractKnuthAlgorithmX {

    public KnuthAlgorithmX(final boolean stopAtFirstResult) {
        super(stopAtFirstResult);
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
        return ExactCoverStepUtils.updateStep(step, option);
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
