package com.dumptruckman.dircbot.util;

import java.util.ArrayList;
import java.util.List;

public class DiceRolls {

    private int totalNumber = 0;

    void addDice(int number) {
        totalNumber += number;
    }

    public int getTotalNumber() {
        return totalNumber;
    }

    private List<String> rolls = new ArrayList<>();

    void addRoll(String rollString) {
        rolls.add(rollString);
    }

    public String getRollStrings() {
        StringBuilder buffer = new StringBuilder("[");
        for (int i = 0; i < rolls.size(); i++) {
            if (i != 0) {
                buffer.append("; ");
            }
            buffer.append(rolls.get(i));
        }
        buffer.append("]");
        return buffer.toString();
    }

    public void clearRollStrings() {
        rolls.clear();
    }
}
