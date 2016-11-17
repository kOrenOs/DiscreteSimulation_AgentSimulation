/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import OSPStat.Stat;

/**
 *
 * @author korenciak.marek
 */
public class MachineUsage {

    private Stat statA;
    private Stat statB;
    private StatInterface statObject;

    public MachineUsage(StatInterface paStatObject) {
        statA = new Stat();
        statB = new Stat();
        statObject = paStatObject;
    }

    public double getStatA(double currentTime) {
        double time = statA.sampleSize() * statA.mean();
        double timeCounted = currentTime - statObject.getSleepTime()[0];
        time = time / timeCounted;
        if (time > 1) {
            time = 1;
        }

        return time;
    }

    public double getStatB(double currentTime) {
        double time = statB.sampleSize() * statB.mean();
        double timeCounted = currentTime - statObject.getSleepTime()[1];
        time = time / timeCounted;
        if (time > 1) {
            time = 1;
        }

        return time;
    }

    public void addSampleA(double sample) {
        statA.addSample(sample);
    }

    public void addSampleB(double sample) {
        statB.addSample(sample);
    }

    public void reset() {
        statA = new Stat();
        statB = new Stat();
    }
}
