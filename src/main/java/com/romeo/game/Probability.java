package com.romeo.game;

import java.util.Map;


/**
 * Represents the probabilities of symbols at a specific position in a matrix
 */
public class Probability {

    // The column index of the probability
    public int column;

    // The row index of the probability
    public int row;

    // The map containing symbol probabilities associated with their occurrences
    public Map<String, Integer> symbolProbabilities;

    public Probability(int column, int row, Map<String, Integer> symbolProbabilities) {
        this.column = column;
        this.row = row;
        this.symbolProbabilities = symbolProbabilities;
    }
}
