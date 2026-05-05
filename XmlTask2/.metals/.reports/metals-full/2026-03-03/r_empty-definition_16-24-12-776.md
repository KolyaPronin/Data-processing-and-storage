error id: file://<WORKSPACE>/src/main/java/ru/nsu/shelestov/XML1/WriterJAXB.java:javax/xml/bind/JAXBException#
file://<WORKSPACE>/src/main/java/ru/nsu/shelestov/XML1/WriterJAXB.java
empty definition using pc, found symbol in pc: javax/xml/bind/JAXBException#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 221
uri: file://<WORKSPACE>/src/main/java/ru/nsu/shelestov/XML1/WriterJAXB.java
text:
```scala
package ru.nsu.shelestov.XML1;

import generated.People;
import org.xml.sax.SAXException;
import data.ru.nsu.pronin.XML2.Person;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.@@JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

public class WriterJAXB {
    public static void write(Map<String, Person> persons) throws JAXBException, SAXException {
        People people = Converter.build(persons);
        JAXBContext context = JAXBContext.newInstance(People.class);
        Marshaller marshaller = context.createMarshaller();

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        URL fis = WriterJAXB.class.getClassLoader().getResource("schema/people.xsd");
        Schema schema = sf.newSchema(fis);
        marshaller.setSchema(schema);

        marshaller.marshal(people, new File("bobiks.xml"));
    }
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: javax/xml/bind/JAXBException#