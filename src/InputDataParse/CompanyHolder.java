/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InputDataParse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author korenciak.marek
 */
public class CompanyHolder {

    Date lastDate = null;

    String load = "";
    String time = "";

    String name = "";

    ArrayList<IntervalHolder> holderList = new ArrayList<IntervalHolder>();

    public CompanyHolder(String paName) {
        name = paName;

        for (int i = 0; i < 24; i++) {
            holderList.add(new IntervalHolder(name + "_" + i));
        }
    }

    public void addData(Date paTime, String paLoad) {
        int hour = paTime.getHours();
        long timeGap = -1;

        IntervalHolder actualHolder = holderList.get(hour);

        if (lastDate == null) {
            lastDate = paTime;
        } else {
            timeGap = (paTime.getTime() - lastDate.getTime()) / 60000;
            lastDate = paTime;
        }

        if (timeGap != -1) {
            actualHolder.addTime(timeGap);
            time += timeGap + "\n";
        }
        actualHolder.addLoad(paLoad);

        load += paLoad + "\n";
    }

    public void writeToFile() {
        for (IntervalHolder holder : holderList) {
            holder.writeToFile();
        }

        PrintWriter writerLoad = null;
        try {
            writerLoad = new PrintWriter(new File("load_Company" + name + ".txt"));
        } catch (FileNotFoundException e) {
        }
        writerLoad.append(load);

        writerLoad.close();
        
        try {
            writerLoad = new PrintWriter(new File("time_Company" + name + ".txt"));
        } catch (FileNotFoundException e) {
        }
        writerLoad.append(time);

        writerLoad.close();
    }
}
