package com.romeo.game;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Loads the details from configuration file for the scratch game
 */
public class LoadConfigurationFile {


    /**
     * Loads the details from configuration file
     *
     * @param configFile
     */
    public static void loadConfig(String configFile) {
        try (InputStream inputStream = ScratchGame.class.getClassLoader().getResourceAsStream("config.json")) {
            if (inputStream == null) {
                throw new RuntimeException("config.json not found in classpath");
            }

            var parser = new JSONParser();
            var config = (JSONObject) parser.parse(new InputStreamReader(inputStream));

            ScratchGameUtility.columns = getConfigInteger(config, "columns", 3);
            ScratchGameUtility.rows = getConfigInteger(config, "rows", 3);

            loadSymbols(config);
            loadStandardSymbolProbabilities(config);
            loadBonusSymbolProbabilities(config);
            loadWinCombinations(config);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves an integer configuration value from the JSON object
     *
     * @param config       The JSON object containing the configuration
     * @param key          The key of the configuration value
     * @param defaultValue The default value to be used if the key is not found
     * @return The integer configuration value
     */
    private static int getConfigInteger(JSONObject config, String key, int defaultValue) {
        return config.containsKey(key) ? ((Long) config.get(key)).intValue() : defaultValue;
    }

    /**
     * Loads the symbols configuration from the JSON object
     *
     * @param config The JSON object containing the configuration
     */
    private static void loadSymbols(JSONObject config) {
        JSONObject symbolConfig = (JSONObject) config.get("symbols");
        for (Object key : symbolConfig.keySet()) {
            String symbolName = (String) key;
            JSONObject symbolData = (JSONObject) symbolConfig.get(symbolName);

            Symbol symbol = new Symbol();
            symbol.rewardMultiplier = getConfigDouble(symbolData, "reward_multiplier", 0.0);
            symbol.setType((String) symbolData.get("type"));
            symbol.setExtra(getConfigInteger(symbolData, "extra", 0));
            symbol.setImpact((String) symbolData.get("impact"));

            ScratchGameUtility.symbols.put(symbolName, symbol);
        }
    }

    /**
     * Retrieves a double configuration value from the JSON object
     *
     * @param object       The JSON object containing the configuration
     * @param key          The key of the configuration value
     * @param defaultValue The default value to be used if the key is not found
     * @return The double configuration value
     */
    private static double getConfigDouble(JSONObject object, String key, double defaultValue) {
        return object.containsKey(key) ? ((Number) object.get(key)).doubleValue() : defaultValue;
    }

    /**
     * Loads the standard symbol probabilities configuration from the JSON object
     *
     * @param config The JSON object containing the configuration
     */
    private static void loadStandardSymbolProbabilities(JSONObject config) {
        var probabilityConfig = (JSONObject) config.get("probabilities");
        var standardSymbolProbabilitiesArray = (JSONArray) probabilityConfig.get("standard_symbols");
        for (Object obj : standardSymbolProbabilitiesArray) {
            JSONObject probabilityData = (JSONObject) obj;
            var column = ((Long) probabilityData.get("column")).intValue();
            var row = ((Long) probabilityData.get("row")).intValue();
            Map<String, Integer> symbolProbabilities = getSymbolProbabilities(
                    (JSONObject) probabilityData.get("symbols"));
            ScratchGameUtility.standardSymbolProbabilities.add(new Probability(column, row, symbolProbabilities));
        }
    }

    /**
     * Retrieves symbol probabilities from the JSON object
     *
     * @param symbolProbabilitiesData The JSON object containing the symbol
     *                                probabilities
     * @return The map of symbol probabilities
     */
    private static Map<String, Integer> getSymbolProbabilities(JSONObject symbolProbabilitiesData) {
        Map<String, Integer> symbolProbabilities = new HashMap<>();
        for (Object symbol : symbolProbabilitiesData.keySet()) {
            symbolProbabilities.put((String) symbol, ((Long) symbolProbabilitiesData.get(symbol)).intValue());
        }
        return symbolProbabilities;
    }

    /**
     * Loads the bonus symbol probabilities configuration from the JSON object
     *
     * @param config The JSON object containing the configuration
     */
    private static void loadBonusSymbolProbabilities(JSONObject config) {
        var probabilityConfig = (JSONObject) config.get("probabilities");

        var bonusSymbolData = (JSONObject) probabilityConfig.get("bonus_symbols");
        var bonusSymbolProbabilitiesData = (JSONObject) bonusSymbolData.get("symbols");

        for (Object symbol : bonusSymbolProbabilitiesData.keySet()) {
            Object value = bonusSymbolProbabilitiesData.get(symbol);
            if (value instanceof Number) {
                int intValue = ((Number) value).intValue();
                ScratchGameUtility.bonusSymbolProbabilities.put((String) symbol, intValue);
            } else {
                throw new RuntimeException(
                        "Invalid value type for bonus symbol probability: " + value.getClass().getName());
            }
        }
    }

    /**
     * Loads the win combinations configuration from the JSON object
     *
     * @param config The JSON object containing the configuration
     */
    private static void loadWinCombinations(JSONObject config) {
        var winCombinationsConfig = (JSONObject) config.get("win_combinations");
        for (Object winCombinationName : winCombinationsConfig.keySet()) {
            JSONObject winCombinationData = (JSONObject) winCombinationsConfig.get(winCombinationName);
            WinCombination winCombination = new WinCombination();
            winCombination.rewardMultiplier = getConfigDouble(winCombinationData, "reward_multiplier", 0.0);
            Long countValue = (Long) winCombinationData.get("count");

            if (countValue != null) {
                winCombination.count = countValue.intValue();
            } else {
                // Handle the case where "count" is not present or is null
                winCombination.count = 0;
            }


            winCombination.group = (String) winCombinationData.get("group");
            winCombination.when = (String) winCombinationData.get("when");

            var coveredAreasArray = (JSONArray) winCombinationData.get("covered_areas");
            if (coveredAreasArray != null) {
                for (Object areaObj : coveredAreasArray) {
                    JSONArray areaArray = (JSONArray) areaObj;
                    List<String> area = new ArrayList<>();
                    for (Object cell : areaArray) {
                        area.add((String) cell);
                    }
                    winCombination.coveredAreas.add(area);
                }
            }

            ScratchGameUtility.winCombinations.put((String) winCombinationName, winCombination);
        }
    }
}
