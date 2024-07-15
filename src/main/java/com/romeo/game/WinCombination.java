package com.romeo.game;

import java.util.ArrayList;
import java.util.List;

public class WinCombination {

    // The reward multiplier associated with this win combination
    public double rewardMultiplier;

    // The count of occurrences required for this win combination
    public int count;

    // The group to which this win combination belongs
    public String group;

    // Conditions for triggering the win associated with this combination
    public String when;

    // A list of covered areas representing the positions on the scratch card where the win occurs.
    public List<List<String>> coveredAreas = new ArrayList<>();
}
