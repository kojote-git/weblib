package com.jkojote.weblib;


public class ActionRunner {

    public void report(int times, Action action) {
        System.out.println("Time elapsed in average: " + timeElapsed(times, action) + " ms");
    }

    public float timeElapsed(int times, Action action) {
        int i = 0;
        long start = 0, end = 0, total = 0;
        while (i < times) {
            start = System.currentTimeMillis();
            action.execute();
            end = System.currentTimeMillis();
            total += end - start;
            i++;
        }
        return ((float) total) / times;
    }
}
