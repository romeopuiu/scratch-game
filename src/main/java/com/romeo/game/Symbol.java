package com.romeo.game;



/**
 * Represents a symbol in a scratch game. Each symbol has a reward multiplier,
 * type, extra value and impact
 */
public class Symbol {
    public double rewardMultiplier;

    // The type of the symbol
    private String type;

    // An extra value associated with the symbol
    private int extra;

    // The impact of the symbol
    private String impact;

    public double getRewardMultiplier() {
        return rewardMultiplier;
    }

    public void setRewardMultiplier(double rewardMultiplier) {
        this.rewardMultiplier = rewardMultiplier;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getExtra() {
        return extra;
    }

    public void setExtra(int extra) {
        this.extra = extra;
    }

    public String getImpact() {
        return impact;
    }

    public void setImpact(String impact) {
        this.impact = impact;
    }
}
