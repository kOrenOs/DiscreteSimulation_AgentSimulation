/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InputDataParse;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 *
 * @author korenciak.marek
 */
public class DataParser {

    public static void main(String[] args) {
        DataParser dp = new DataParser();
        dp.writeToFile();
    }

    private CompanyHolder comapnyA = new CompanyHolder("A");
    private CompanyHolder comapnyB = new CompanyHolder("B");
    private CompanyHolder comapnyC = new CompanyHolder("C");

    public DataParser() {
        Scanner scan = null;

        try {
            scan = new Scanner(new FileReader("inputFileS3.txt"));
        } catch (FileNotFoundException e) {
        }

        String read;
        String[] splited;
        Date tempDate = null;

        SimpleDateFormat formator = new SimpleDateFormat("dd.MM.yyyy HH:mm");

        while (scan.hasNext()) {
            read = scan.nextLine();
            splited = read.split(";");
            try {
                tempDate = formator.parse(splited[0]);
            } catch (ParseException e) {
            }

            switch (splited[1]) {
                case "A":
                    comapnyA.addData(tempDate, splited[2]);
                    break;
                case "B":
                    comapnyB.addData(tempDate, splited[2]);
                    break;
                case "C":
                    comapnyC.addData(tempDate, splited[2]);
                    break;
            }
        }
    }

    public void writeToFile() {
        comapnyA.writeToFile();
        comapnyB.writeToFile();
        comapnyC.writeToFile();
    }
}
