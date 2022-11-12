package com.example.connect4;
public class State {
    /*
    board has 64 bits. We are concerned with 63 bits.
    Each column is represented by 9 bits. We have 7 columns.
    The first 3 bits called SE represents the index of the first empty slot.
    This index starts from 000 where the first row is empty and ends at 110 where no row is empty.
    The remaining 6 bits represent the colors of each slot.
    bits with smaller index than SE can be either 0 for RED or 1 for YELLOW.
    bits with greater index than SE contain garbage.
     */
    private final BitsArray board;

    /*
    false represents computer turns.
    true represents human turns.
     */
    private final boolean turn;

    public State(BitsArray board, boolean turn) {
        this.board = board;
        this.turn = turn;
    }

    public int getStateScore() {
        return getComputerScore() - getHumanScore();
    }

    public int getComputerScore() {
        final int coin = 0; //computer's coins are represented by 0's
        return getColScore(coin) + getRowScore(coin) + getLeftDiagonalScore(coin) + getRightDiagonalScore(coin);
    }

    public int getHumanScore() {
        final int coin = 1; //human's coins are represented by 1's
        return getColScore(coin) + getRowScore(coin) + getLeftDiagonalScore(coin) + getRightDiagonalScore(coin);
    }

    private int getColScore(int coin) {
        int score = 0;
        for(int i = 0; i < 7; i++) {
            int adj = 0;
            int colEmptySlot = getColEmptySlot(i);
            for(int j = 0; j < colEmptySlot; j++) {
                if(getIthJthCoin(j, i) == coin) adj++;
                else if(adj >= 4) {
                    score += adj - 4 + 1;
                    adj = 0;
                }
                else adj = 0;
            }
            if(adj >= 4) score += adj - 4 + 1;
        }
        return score;
    }

    private int getRowScore(int coin) {
        int score = 0;
        for(int i = 0; i < 6; i++) {
            int adj = 0;
            for(int j = 0; j < 7; j++) {
                int colEmptySlot = getColEmptySlot(j);
                if(i >= colEmptySlot || getIthJthCoin(i, j) != coin) {
                    if(adj >= 4) score += adj - 4 + 1;
                    adj = 0;
                }
                else adj++;
            }
            if(adj >= 4) score += adj - 4 + 1;
        }
        return score;
    }

    private int getLeftDiagonalScore(int coin) {
        int score = 0;
        for(int i = 0; i < 7; i++) {
            int row = 0, col = i;
            int adj = 0;
            while(row <= 5 && col >= 0) {
                int colEmptySlot = getColEmptySlot(col);
                if(row >= colEmptySlot || getIthJthCoin(row, col) != coin) {
                    if(adj >= 4) score += adj - 4 + 1;
                    adj = 0;
                }
                else adj++;
                row++;
                col--;
            }
            if(adj >= 4) score += adj - 4 + 1;
        }
        for(int i = 1; i < 6; i++) {
            int row = i, col = 6;
            int adj = 0;
            while(row <= 5 && col >= 0) {
                int colEmptySlot = getColEmptySlot(col);
                if(row >= colEmptySlot || getIthJthCoin(row, col) != coin) {
                    if(adj >= 4) score += adj - 4 + 1;
                    adj = 0;
                }
                else adj++;
                row++;
                col--;
            }
            if(adj >= 4) score += adj - 4 + 1;
        }
        return score;
    }

    private int getRightDiagonalScore(int coin) {
        int score = 0;
        for(int i = 0; i < 7; i++) {
            int row = 0, col = i;
            int adj = 0;
            while(row <= 5 && col <= 6) {
                int colEmptySlot = getColEmptySlot(col);
                if(row >= colEmptySlot || getIthJthCoin(row, col) != coin) {
                    if(adj >= 4) score += adj - 4 + 1;
                    adj = 0;
                }
                else adj++;
                row++;
                col++;
            }
            if(adj >= 4) score += adj - 4 + 1;
        }
        for(int i = 1; i < 6; i++) {
            int row = i, col = 0;
            int adj = 0;
            while(row <= 5 && col <= 6) {
                int colEmptySlot = getColEmptySlot(col);
                if(row >= colEmptySlot || getIthJthCoin(row, col) != coin) {
                    if(adj >= 4) score += adj - 4 + 1;
                    adj = 0;
                }
                else adj++;
                row++;
                col++;
            }
            if(adj >= 4) score += adj - 4 + 1;
        }
        return score;
    }

    private int getColEmptySlot(int column) {
        return board.getNthThreeBits(9 * column);
    }

    private int getIthJthCoin(int row, int col) {
        return board.getNthBit(9 * col + 3 + row);
    }

    public boolean isEndState() {
        for(int i = 0; i < 7; i++) {
            if(getColEmptySlot(i) < 6) return false;
        }
        return true;
    }
    public State[] generateChildStates() {
        State[] childStates = new State[7];
        for(int i = 0; i < 7; i++) {
            childStates[i] = generateChildState(i);
        }
        return childStates;
    }
    private State generateChildState(int column) {
        int colEmptySlot = getColEmptySlot(column);
        if(colEmptySlot > 5) return null;
        BitsArray bits = new BitsArray(board.getBits());
        bits.changeNthThreeBits(9 * column, colEmptySlot+1);
        bits.changeNthBit(9 * column + 3 + colEmptySlot, turn);
        return new State(bits, !turn);
    }

}