/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InputDataParse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 *
 * @author korenciak.marek
 */
public class IntervalHolder {

    String name = "";
    String time = "";
    String load = "";

    public IntervalHolder(String paName) {
        name = paName;
    }

    public void addTime(long paTime) {
        time += paTime + "\n";
    }

    public void addLoad(String paLoad) {
        load += paLoad + "\n";
    }

    public void writeToFile() {
        PrintWriter writerLoad = null;
        PrintWriter writerTime = null;
        try {
            writerLoad = new PrintWriter(new File("load_" + name + ".txt"));
            writerTime = new PrintWriter(new File("time_" + name + ".txt"));
        } catch (FileNotFoundException e) {
        }

        writerLoad.append(load);
        writerTime.append(time);

        writerLoad.close();
        writerTime.close();
    }
}
