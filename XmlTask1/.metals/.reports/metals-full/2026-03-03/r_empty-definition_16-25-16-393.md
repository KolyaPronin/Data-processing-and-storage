error id: file://<WORKSPACE>/src/main/java/ru/nsu/shelestov/XML1/Main.java:_empty_/WriterJAXB#
file://<WORKSPACE>/src/main/java/ru/nsu/shelestov/XML1/Main.java
empty definition using pc, found symbol in pc: _empty_/WriterJAXB#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 1008
uri: file://<WORKSPACE>/src/main/java/ru/nsu/shelestov/XML1/Main.java
text:
```scala
package ru.nsu.shelestov.XML1;

import org.xml.sax.SAXException;
import data.ru.nsu.pronin.XML1.PeopleInfo;
import data.ru.nsu.pronin.XML1.Person;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Parser parser = new Parser();
        PeopleInfo ppl = parser.readXML("people.xml");

        Collector collector = new Collector(ppl);
        Map<String, Person> result = collector.merge();
        System.out.println("Report: people number: " + result.size());

        Validator validator = new Validator();
        validator.validatePersons(result);

        Writer writer = new Writer();
        try {
            writer.writePrettyXML("output.xml", result);
        } catch (ParserConfigurationException | TransformerException e) {
            throw new RuntimeException(e);
        }
        try {
            @@WriterJAXB.write(result);
        } catch (JAXBException | SAXException e) {
            throw new RuntimeException(e);
        }
    }
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: _empty_/WriterJAXB#