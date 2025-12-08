package com.bridgeX.ai;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class CsvService {

    private static final String CSV_FILE_PATH = "EX-HALF.dat";

    public List<Map<String, String>> readCsv() throws IOException {
        List<Map<String, String>> records = new ArrayList<>();
        
        ClassPathResource resource = new ClassPathResource(CSV_FILE_PATH);
        if (!resource.exists()) {
            throw new IOException("CSV file not found: " + CSV_FILE_PATH);
        }

        try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            for (CSVRecord csvRecord : csvParser) {
                records.add(csvRecord.toMap());
            }
        }
        return records;
    }
}
