package com.example.connect4.heuristics;

import com.example.connect4.State;

public class Heuristic implements IHeuristic{

    private double getStateExpectedScore(State state) {
        return getExpectedScore(0, state) - getExpectedScore(1, state);
    }

    private double getExpectedScore(int coin, State state) {
        return getAllColsExpectedScore(coin, state) + getAllRowsExpectedScore(coin, state);
    }

    private double getAllColsExpectedScore(int coin, State state) {
        double expectedScore = 0;
        for(int col = 0; col < 7; col++) {
            expectedScore += getColExpectedScore(coin, col, state);
        }
        return expectedScore;
    }

    private double getAllRowsExpectedScore(int coin, State state) {
        double expectedScore = 0;
        for(int row = 0; row < 6; row++) {
            expectedScore += getRowExpectedScore(coin, row, state);
        }
        return expectedScore;
    }

    private double getColExpectedScore(int coin, int col, State state) {
        int colEmptySlot = state.getColEmptySlot(col);
        int cell = colEmptySlot - 1;
        int adj = 0;
        while(cell >= 0 && state.getIthJthCoin(cell, col) == coin && adj < 3) {
            adj++;
            cell--;
        }
        int numberOfEmptySlots = 6 - colEmptySlot;
        if(adj + numberOfEmptySlots < 4) return 0;
        else return (double) (adj+1) / 4;
    }

    private double getRowExpectedScore(int coin, int row, State state) {
        int numberOfEmptySlots = 0, leftAdj = 0, rightAdj = 0, col = 0;
        double expectedScore = 0;
        boolean emptyFlag = false;
        while(col < 7) {
            int emptySlotIndex = state.getColEmptySlot(col);
            if(row < emptySlotIndex && state.getIthJthCoin(row, col) == coin) {
                if(emptyFlag) rightAdj++;
                else leftAdj++;
            }
            else if(row < emptySlotIndex) {
                expectedScore += calculateSegScore(leftAdj, numberOfEmptySlots, rightAdj);
                leftAdj = 0; rightAdj = 0; numberOfEmptySlots = 0; emptyFlag = false;
            }
            else {
                if(rightAdj > 0) {
                    expectedScore += calculateSegScore(leftAdj, numberOfEmptySlots, rightAdj);
                    leftAdj = rightAdj;
                    rightAdj = 0;
                }
                emptyFlag = true;
                numberOfEmptySlots++;
            }
            col++;
        }
        expectedScore += calculateSegScore(leftAdj, numberOfEmptySlots, rightAdj);
        return expectedScore;
    }

    private double calculateSegScore(int leftAdj, int numberOfEmptySlots, int rightAdj) {
        if(leftAdj > 3) leftAdj = 3;
        if(rightAdj > 3) rightAdj = 3;
        if(numberOfEmptySlots == 0 || leftAdj + numberOfEmptySlots + rightAdj < 4) return 0;
        return (double) (Math.max(leftAdj, rightAdj) + 1) / 4;
    }

    @Override
    public int getHeuristic(State state) {
        return state.getStateScore() + Math.round((long)getStateExpectedScore(state));
    }
}
