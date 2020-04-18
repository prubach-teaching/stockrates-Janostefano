package sgh;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class StockData {

    public static void getAndProcessChange(String stock) throws IOException {
        String filePath = "data_in/" + stock + ".csv";
        File stocksFile = new File("./" + filePath);
        //TODO HINT: You might need to check if the file doesn't already exist...
        if (!stocksFile.exists()) {
            download("https://query1.finance.yahoo.com/v7/finance/download/" + stock +
                            "?period1=1554504399&period2=1586126799&interval=1d&events=history",
                    filePath);
        }
        //TODO Your code here
        FileWriter changeFile = new FileWriter("./data_out/" + stock + ".csv");
        changeFile.write("Date,Open,High,Low,Close,Adj Close,Volume,Change\n");
        Scanner scanner = new Scanner(stocksFile);
        scanner.nextLine();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] lineArr = line.split(",");

            Double openValue = Double.parseDouble(lineArr[1]);
            Double closeValue = Double.parseDouble(lineArr[4]);

            for (String value : lineArr) {
                changeFile.write(value + ",");
            }
            changeFile.write(((closeValue - openValue) / openValue) * 100 + "\n");
        }
        scanner.close();
        changeFile.close();
    }

    public static void download(String url, String fileName) throws IOException {
        try (InputStream in = URI.create(url).toURL().openStream()) {
            Files.copy(in, Paths.get(fileName));
        }
    }

    public static void main(String[] args) throws IOException {
        String[] stocks = new String[]{"IBM", "MSFT", "GOOG"};
        for (String s : stocks) {
            getAndProcessChange(s);
        }
    }
}
