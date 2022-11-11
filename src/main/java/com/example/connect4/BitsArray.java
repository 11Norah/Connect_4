package com.example.connect4;

public class BitsArray {
    private long bits;

    public BitsArray(long bits) {
        this.bits = bits;
    }

    public int getNthBit(int n) {
        return (int) ((bits & (1L << n)) >> n);
    }

    public int getNthThreeBits(int n) {
        return (int) ((bits & (7L << n)) >> n);
    }

    private void setNthBit(int n) {
        bits = bits | (1L << n);
    }

    private void clearNthBit(int n) {
        bits = bits & (~(1L << n));
    }

    public void changeNthBit(int n, boolean b) {
        if(b) setNthBit(n);
        else clearNthBit(n);
    }

    public void changeNthThreeBits(int n, int num) {
        if(num > 7 || num < 0) {
            System.out.println("Number should be between 0 and 7");
            return;
        }
        for(int i = 0; i < 3; i++) {
            boolean b = (num % 2 == 1);
            changeNthBit(n, b);
            num /= 2;
            n++;
        }
    }
}
