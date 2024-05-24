package cl.bozz.sudokusolver.algorithm;

import cl.bozz.sudokusolver.model.ExactCoverStep;

import java.util.Set;

public interface ExactCoverAlgorithm {
    Set<ExactCoverStep> runAlgorithm(ExactCoverStep initialStep);
}
