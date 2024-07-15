package com.romeo.game;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class ScratchGameUtility {

    public static int columns;
    public static int rows;
    public static int reward;
    public static Map<String, Symbol> symbols = new HashMap<>();
    public static List<Probability> standardSymbolProbabilities = new ArrayList<>();
    public static Map<String, Integer> bonusSymbolProbabilities = new HashMap<>();
    public static Map<String, WinCombination> winCombinations = new HashMap<>();
    public static Random random = new Random();

    /**
     *
     * Calculates the reward for a bonus symbol if applied in the game matrix
     *
     * @param symbol The bonus symbol
     */
    private static void calculateBonusSymbolReward(String symbol) {
        if (reward != 0) {
            if (symbol.equalsIgnoreCase("10x") || symbol.equalsIgnoreCase("5x")) {
                double winReward = (reward * symbols.get(symbol).rewardMultiplier);
                reward = (int) winReward;
            } else if (symbol.equalsIgnoreCase("+1000") || symbol.equalsIgnoreCase("+500")) {
                double winReward = reward + symbols.get(symbol).getExtra();
                reward = (int) winReward;
            }
        }
    }

    /**
     *
     * Calculates the rewards for the applied standard symbol winning combinations
     *
     * @param appliedWinningCombinations A map containing applied winning
     *                                   combinations for each symbol
     * @param bettingAmount              The betting amount
     */
    public static void calculateStandardSymbolReward(Map<String, List<String>> appliedWinningCombinations,
                                                     int bettingAmount) {
        reward = 0;
        for (String symbol : appliedWinningCombinations.keySet()) {
            double symbolreward = 0.0;
            List<String> winCombinationList = appliedWinningCombinations.get(symbol);
            Symbol symbolData = symbols.get(symbol);
            if (symbolData != null) {
                double winReward = bettingAmount * symbolData.rewardMultiplier;
                int SymbolTotalReward = 1;
                for (String appliedList : winCombinationList) {
                    WinCombination winCombination = winCombinations.get(appliedList);
                    if (SymbolTotalReward == 1) {
                        winReward = winReward * winCombination.rewardMultiplier;
                        symbolreward += winReward;
                    } else {
                        symbolreward = (symbolreward * winCombination.rewardMultiplier);
                    }
                    SymbolTotalReward++;
                }
            } else {
                System.out.println("Symbol data not found for symbol: " + symbol);
            }
            reward += symbolreward;
        }
    }

    /**
     * Finds and returns a list of applied bonus symbols in the matrix
     *
     * @param matrix The game matrix
     * @return A list of applied bonus symbols
     */
    public static List<String> findAppliedBonusSymbols(List<List<String>> matrix) {
        List<String> bonusSymbols = new ArrayList<>();
        for (List<String> row : matrix) {
            for (String symbol : row) {
                if (!symbol.equalsIgnoreCase("MISS") && bonusSymbolProbabilities.keySet().contains(symbol)) {
                    bonusSymbols.add(symbol);
                }
                calculateBonusSymbolReward(symbol);
            }
        }
        return bonusSymbols;
    }

    /**
     *
     * Finds and returns the applied winning combinations for each symbol in the
     * matrix
     *
     * @param matrix The game matrix
     * @return A map containing applied winning combinations for each symbol
     */
    public static Map<String, List<String>> findAppliedWinningCombinations(List<List<String>> matrix) {
        Map<String, List<String>> appliedWinningCombinations = new HashMap<>();
        Map<String, Integer> symbolCounts = new HashMap<>();

        for (List<String> row : matrix) {
            for (String symbol : row) {
                symbolCounts.put(symbol, symbolCounts.getOrDefault(symbol, 0) + 1);
            }
        }
        for (String symbol : symbolCounts.keySet()) {
            int count = symbolCounts.get(symbol);

            if (count >= 3) {
                List<String> appliedCombinations = new ArrayList<>();
                for (String winCombinationKey : winCombinations.keySet()) {
                    WinCombination winCombination = winCombinations.get(winCombinationKey);

                    if (winCombination.count <= count) {
                        if (winCombination.when.equals("same_symbols")) {
                            if (symbol.equals("MISS")) {
                                continue;
                            }
                        } else if (winCombination.when.equals("linear_symbols")) {
                            boolean match = false;
                            for (List<String> area : winCombination.coveredAreas) {
                                match = true;
                                for (String cell : area) {
                                    int row = Integer.parseInt(cell.split(":")[0]);
                                    int column = Integer.parseInt(cell.split(":")[1]);
                                    if (!matrix.get(row).get(column).equals(symbol)) {
                                        match = false;
                                        break;
                                    }
                                }
                                if (match) {
                                    break;
                                }
                            }
                            if (!match) {
                                continue;
                            }
                        }
                        appliedCombinations.add(winCombinationKey);
                    }
                }
                appliedWinningCombinations.put(symbol, appliedCombinations);
            }
        }
        return appliedWinningCombinations;
    }
}
