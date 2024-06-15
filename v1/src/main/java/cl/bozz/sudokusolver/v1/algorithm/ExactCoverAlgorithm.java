package cl.bozz.sudokusolver.v1.algorithm;

import cl.bozz.sudokusolver.v1.algorithm.model.ExactCoverStep;

import java.util.Set;

/**
 * Simple interface to hook up an exact cover algorithm
 */
public interface ExactCoverAlgorithm {
    Set<ExactCoverStep> runAlgorithm(ExactCoverStep initialStep);
}
