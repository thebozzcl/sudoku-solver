package cl.bozz.sudokusolver.v1.wikiexample;

import cl.bozz.sudokusolver.v1.algorithm.model.ExactCoverConstraint;
import cl.bozz.sudokusolver.v1.algorithm.utils.ExactCoverConstraintUtils;
import cl.bozz.sudokusolver.v1.algorithm.model.ExactCoverStep;
import cl.bozz.sudokusolver.v1.algorithm.utils.ExactCoverStepUtils;
import lombok.experimental.UtilityClass;

import java.util.Set;

@UtilityClass
public class WikiExampleUtils {
    public Set<ExactCoverConstraint> wikiExampleConstraints() {
        return Set.of(
                ExactCoverConstraintUtils.fromAcceptedEnumValues(
                        "1",
                        Set.of(WikiExampleValues.A, WikiExampleValues.B),
                        WikiExampleValues.class
                ),
                ExactCoverConstraintUtils.fromAcceptedEnumValues(
                        "2",
                        Set.of(WikiExampleValues.E, WikiExampleValues.F),
                        WikiExampleValues.class
                ),
                ExactCoverConstraintUtils.fromAcceptedEnumValues(
                        "3",
                        Set.of(WikiExampleValues.D, WikiExampleValues.E),
                        WikiExampleValues.class
                ),
                ExactCoverConstraintUtils.fromAcceptedEnumValues(
                        "4",
                        Set.of(WikiExampleValues.A, WikiExampleValues.B, WikiExampleValues.C),
                        WikiExampleValues.class
                ),
                ExactCoverConstraintUtils.fromAcceptedEnumValues(
                        "5",
                        Set.of(WikiExampleValues.C, WikiExampleValues.D),
                        WikiExampleValues.class
                ),
                ExactCoverConstraintUtils.fromAcceptedEnumValues(
                        "6",
                        Set.of(WikiExampleValues.D, WikiExampleValues.E),
                        WikiExampleValues.class
                ),
                ExactCoverConstraintUtils.fromAcceptedEnumValues(
                        "7",
                        Set.of(WikiExampleValues.A, WikiExampleValues.C, WikiExampleValues.E, WikiExampleValues.F),
                        WikiExampleValues.class
                )
        );
    }

    public ExactCoverStep wikiExampleInitialStep() {
        return ExactCoverStepUtils.emptyForEnum(
                WikiExampleValues.class,
                wikiExampleConstraints()
        );
    }
}
