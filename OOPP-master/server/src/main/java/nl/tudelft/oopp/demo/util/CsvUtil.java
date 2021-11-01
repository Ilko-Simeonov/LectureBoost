package nl.tudelft.oopp.demo.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import nl.tudelft.oopp.demo.entities.CsvData;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;

public class CsvUtil<T extends CsvData> {

    /**
     * Rxdtcfyvjhbknlm.
     * @param headerList xdcfbjhnkm
     * @param csvDataList uhjnlk
     * @return cfgvjhbkn
     */
    public ByteArrayInputStream toCsvInputStream(List<String> headerList, List<T> csvDataList) {
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format)) {
            csvPrinter.printRecord(headerList);
            for (CsvData csvData : csvDataList) {
                csvPrinter.printRecord(csvData.getCsvData());
            }
            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Failed to import CSV Data" + e.getMessage());
        }
    }
}
