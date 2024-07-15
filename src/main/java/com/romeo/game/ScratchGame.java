package com.romeo.game;


public class ScratchGame {
    public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println(
                    "Please give in the proper format like : java -jar build_scratch_game-0.0.1-jar-with-dependencies.jar config.json 100");
            return;
        }

        var configFile = args[0];
        var bettingAmount = Integer.parseInt(args[1]);

        LoadConfigurationFile.loadConfig(configFile);

        var matrix = MatrixGenerator.generateRandomMatrix();

        var appliedWinningCombinations = ScratchGameUtility.findAppliedWinningCombinations(matrix);

        ScratchGameUtility.calculateStandardSymbolReward(appliedWinningCombinations, bettingAmount);

        var appliedBonusSymbol = ScratchGameUtility.findAppliedBonusSymbols(matrix);

        System.out.println("{\n\"matrix\": " + matrix + ",");
        System.out.println("\"reward\": " + ScratchGameUtility.reward + ",");
        System.out.println("\"applied_winning_combinations\": " + appliedWinningCombinations + ",");
        System.out.println("\"applied_bonus_symbol\": " + appliedBonusSymbol + "\n}");
    }
}
