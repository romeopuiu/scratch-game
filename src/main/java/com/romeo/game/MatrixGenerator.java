package com.romeo.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/**
 * Generates a random matrix based on symbol probabilities.
 */
public class MatrixGenerator {

    /**
     * Generates a random matrix based on symbol probabilities.
     *
     * @return The generated matrix.
     */
    public static List<List<String>> generateRandomMatrix() {
        List<List<String>> matrix = new ArrayList<>();
        var random = new Random();
        var totalStandardProbabilities = ScratchGameUtility.standardSymbolProbabilities.stream()
                .mapToInt(prob -> prob.symbolProbabilities.values().stream().mapToInt(Integer::intValue).sum()).sum();

        var totalBonusProbabilities = ScratchGameUtility.bonusSymbolProbabilities.values().stream().mapToInt(Integer::intValue).sum();

        var symbolList = new ArrayList<>(ScratchGameUtility.symbols.keySet());
        for (int i = 0; i < 3; i++) {
            List<String> row = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                String symbol = generateRandomSymbol(symbolList, totalStandardProbabilities, totalBonusProbabilities,
                        random);
                row.add(symbol);
            }
            matrix.add(row);
        }

        return matrix;
    }

    /**
     * Generates a random symbol based on symbol probabilities.
     *
     * @param symbolList                 The list of available symbols.
     * @param totalStandardProbabilities The total standard symbol probabilities.
     * @param totalBonusProbabilities    The total bonus symbol probabilities.
     * @param random                     The random number generator.
     * @return The randomly generated symbol.
     */
    private static String generateRandomSymbol(List<String> symbolList, int totalStandardProbabilities,
                                               int totalBonusProbabilities, Random random) {
        var totalProbabilities = totalStandardProbabilities + totalBonusProbabilities;
        var randomNumber = random.nextInt(totalProbabilities) + 1;
        if (randomNumber <= totalStandardProbabilities) {
            for (Probability probability : ScratchGameUtility.standardSymbolProbabilities) {
                for (String symbol : symbolList) {
                    Integer symbolProbability = probability.symbolProbabilities.get(symbol);
                    if (symbolProbability != null) {
                        if (randomNumber <= symbolProbability) {
                            return symbol;
                        }
                        randomNumber -= symbolProbability;
                    }
                }
            }
        } else {
            randomNumber -= totalStandardProbabilities;
            for (String symbol : ScratchGameUtility.bonusSymbolProbabilities.keySet()) {
                if (randomNumber-- <= 0) {
                    return symbol;
                }
            }
        }

        // return a default symbol in case of null
        return "MISS";
    }
}
