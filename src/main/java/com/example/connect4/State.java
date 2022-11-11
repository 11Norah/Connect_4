package com.example.connect4;

import java.util.ArrayList;

public class State {
    /*
    board has 64 bits. We are concerned with 63 bits.
    Each column is represented by 9 bits. We have 7 columns.
    The first 3 bits called SE represents the index of the first empty slot.
    This index starts from 000 where the first row is empty and ends at 110 where no row is empty.
    The remaining 6 bits represent the colors of each slot.
    bits with smaller index than SE can be either 0 for RED or 1 for YELLOW.
    bits with greater index than SE contain garbage.
    The last bit is for turn.
     */
    private final BitsArray board;

    public State(BitsArray board) {
        this.board = board;
    }

    public boolean isEndState() {
        for(int i = 0; i < 7; i++) {
            int colEmptySlot = board.getNthThreeBits(9 * i);
            if(colEmptySlot < 6) return false;
        }
        return true;
    }

    public State[] generateChildStates() {
        ArrayList<State> childStatesList = new ArrayList<>();
        boolean turn = (board.getNthBit(63) == 1);
        for(int i = 0; i < 7; i++) {
            State nextState = generateChildState(i, turn);
            if(nextState == null) continue;
            childStatesList.add(nextState);
        }
        State[] childStates = new State[childStatesList.size()];
        int count = 0;
        for(State state : childStatesList) childStates[count++] = state;
        return childStates;
    }

    private State generateChildState(int column, boolean turn) {
        int colEmptySlot = board.getNthThreeBits(9 * column);
        if(colEmptySlot > 5) return null;
        BitsArray bits = new BitsArray(board.getBits());
        bits.changeNthThreeBits(9 * column, colEmptySlot+1);
        bits.changeNthBit(9 * column + 3 + colEmptySlot, turn);
        return new State(bits);
    }

}