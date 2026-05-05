package ru.nsu.pronin.XML1;

import ru.nsu.pronin.XML1.data.PeopleInfo;
import ru.nsu.pronin.XML1.data.Person;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        String inputPath = (args.length >= 1) ? args[0] : "people.xml";
        String outputPath = (args.length >= 2) ? args[1] : "normalized.xml";
        String warningsPath = (args.length >= 3) ? args[2] : "warnings.txt";

        Parser parser = new Parser();
        PeopleInfo ppl = parser.readXML(inputPath);

        Collector collector = new Collector(ppl);
        Map<String, Person> result = collector.merge();
        System.out.println("Unique persons: " + result.size());

        Validator validator = new Validator();
        List<Validator.ValidationError> errors = validator.validatePersons(result);
        writeWarnings(errors, warningsPath);

        Writer writer = new Writer();
        try {
            writer.writePrettyXML(outputPath, result);
        } catch (ParserConfigurationException | TransformerException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Output: " + Path.of(outputPath).toAbsolutePath());
        System.out.println("Warnings: " + Path.of(warningsPath).toAbsolutePath());
    }

    private static void writeWarnings(List<Validator.ValidationError> errors, String warningsPath) {
        List<String> lines = new ArrayList<>();
        for (Validator.ValidationError e : errors) {
            lines.add(e.toString());
        }
        lines.sort(Comparator.naturalOrder());
        try {
            Files.write(Path.of(warningsPath), lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write warnings file", e);
        }
    }
}