package core.basesyntax;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class WorkWithFile {
    private static final int FIRST_HALF_ARRAY = 0;
    private static final int SECOND_HALF_ARRAY = 1;
    private static final int SIZE_REPORT = 3;

    private String readFile(String fromFileName) {
        File startFile = new File(fromFileName);
        StringBuilder builder = null;
        if (!startFile.exists()) {
            throw new RuntimeException("File does not exist");
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(startFile))) {
            builder = new StringBuilder();
            String value = reader.readLine();
            while (value != null) {
                builder.append(value).append(System.lineSeparator());
                value = reader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Can't read file", e);
        }
        String lineOfFile = builder.toString();
        return lineOfFile;
    }

    private String operationWithFile(String lineOfFile) {
        String[] lineOfStartFile = lineOfFile.split(System.lineSeparator());
        int sumOfBuy = 0;
        int sumOfSupply = 0;
        for (int i = 0; i < lineOfStartFile.length; i++) {
            if (Character.isDigit(lineOfStartFile[i].charAt(0))) {
                String[] elementsStartWithNumber = lineOfStartFile[i].split("(?<=\\d)(?=\\D)");
                if (elementsStartWithNumber[SECOND_HALF_ARRAY].equals("buy")) {
                    sumOfBuy += Integer.parseInt(elementsStartWithNumber[FIRST_HALF_ARRAY]);
                } else if (elementsStartWithNumber[SECOND_HALF_ARRAY].equals("suplly")) {
                    sumOfSupply += Integer.parseInt(elementsStartWithNumber[FIRST_HALF_ARRAY]);
                }
            }
            String[] elementOfLine = lineOfStartFile[i].split(",");
            if (elementOfLine.length != 2) {
                throw new RuntimeException("Invalid input file format at line " + (i + 1));
            }
            String action = elementOfLine[FIRST_HALF_ARRAY];
            int quantity = 0;
            try {
                quantity = Integer.parseInt(elementOfLine[SECOND_HALF_ARRAY]);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Invalid input file format at line " + (i + 1), e);
            }
            if (action.equals("buy")) {
                sumOfBuy += quantity;
            } else if (action.equals("supply")) {
                sumOfSupply += quantity;
            } else {
                throw new RuntimeException("Invalid action in input file at line " + (i + 1));
            }
        }
        int result = sumOfSupply - sumOfBuy;
        String[] report = new String[SIZE_REPORT];
        report[0] = "supply," + Integer.toString(sumOfSupply);
        report[1] = "buy," + Integer.toString(sumOfBuy);
        report[2] = "result," + Integer.toString(result);
        return String.join(System.lineSeparator(), report);
    }

    private void closeFile(String report, String toFileName) {
        File finalFile = new File(toFileName);
        String[] readyReport = report.split(System.lineSeparator());
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(finalFile))) {
            for (int k = 0; k < readyReport.length; k++) {
                bufferedWriter.write(readyReport[k] + System.lineSeparator());
            }
        } catch (IOException e) {
            throw new RuntimeException("can't close file", e);
        }
    }

    public void getStatistic(String fromFileName, String toFileName) {
        String doneData = readFile(fromFileName);
        String readyReport = operationWithFile(doneData);
        closeFile(readyReport, toFileName);
    }
}
