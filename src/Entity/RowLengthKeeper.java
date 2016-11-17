/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

/**
 *
 * @author korenciak.marek
 */
public class RowLengthKeeper {

    private double timeOfChange = 0;
    private double sum = 0;
    private double timeOfSleep = 0;
    private boolean stopped = false;
    private double value = 0;

    public void add(double paTime, double paValue) {
        addChange(paTime);
        value = paValue;
    }

    public double getStats(double paTime) {
        addChange(paTime);
        double temp = paTime - timeOfSleep;
        return sum / temp;
    }

    public void addChange(double paTime) {
        if (!stopped) {
            double temp = paTime - timeOfChange;
            sum += temp * value;
            timeOfChange = paTime;
        }
    }

    public void stopRow(double paTime) {
        if (!stopped) {
            addChange(paTime);
            timeOfChange = paTime;
            stopped = true;
        }
    }

    public void startRow(double paTime) {
        if (stopped) {
            timeOfSleep += paTime - timeOfChange;
            stopped = false;
            timeOfChange = paTime;
        }
    }

    public void reset() {
        timeOfChange = 0;
        timeOfSleep = 0;
        sum = 0;
        stopped = false;
    }
}
