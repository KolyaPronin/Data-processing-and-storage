package ru.nsu.pronin.XML1;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ru.nsu.pronin.XML1.data.Person;
import ru.nsu.pronin.XML1.data.PersonToRole;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Writer {

    public void writePrettyXML(String path, Map<String, Person> ppl) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();

        Element root = doc.createElement("people");
        root.setAttribute("count", String.valueOf(ppl.size()));
        doc.appendChild(root);
        List<Person> ordered = new ArrayList<>(ppl.values());
        ordered.sort(Comparator.comparing(Person::getId));

        for (Person person: ordered) {
            Element personElement = doc.createElement("person");
            personElement.setAttribute("id", person.getId());

            Element name = doc.createElement("name");
            if (person.getFirstName() != null && !person.getFirstName().isBlank()) {
                Element first = doc.createElement("first");
                first.appendChild(doc.createTextNode(person.getFirstName()));
                name.appendChild(first);
            }
            if (person.getLastName() != null && !person.getLastName().isBlank()) {
                Element last = doc.createElement("last");
                last.appendChild(doc.createTextNode(person.getLastName()));
                name.appendChild(last);
            }
            if (!name.hasChildNodes() && person.getFullName() != null && !person.getFullName().isBlank()) {
                Element full = doc.createElement("full");
                full.appendChild(doc.createTextNode(person.getFullName()));
                name.appendChild(full);
            }
            personElement.appendChild(name);

            Element gender = doc.createElement("gender");
            gender.appendChild(doc.createTextNode(person.getGender()));
            personElement.appendChild(gender);

            List<PersonToRole> siblings = person.getSiblings();
            if (!siblings.isEmpty()) {
                Element siblingsElement = doc.createElement("siblings");
                for (PersonToRole sibling: siblings) {
                    String role = sibling.getRole();
                    String tag = switch (role) {
                        case "brother" -> "brother";
                        case "sister" -> "sister";
                        default -> "sibling";
                    };
                    Element sib = doc.createElement(tag);
                    sib.appendChild(doc.createTextNode(sibling.getName()));
                    siblingsElement.appendChild(sib);
                }
                personElement.appendChild(siblingsElement);
            }

            List<PersonToRole> children = person.getChildren();
            if (!children.isEmpty()) {
                Element childrenElement = doc.createElement("children");
                for (PersonToRole child: children) {
                    String role = child.getRole();
                    String tag = switch (role) {
                        case "son" -> "son";
                        case "daughter" -> "daughter";
                        default -> "child";
                    };
                    Element childElement = doc.createElement(tag);
                    childElement.appendChild(doc.createTextNode(child.getName()));
                    childrenElement.appendChild(childElement);
                }
                personElement.appendChild(childrenElement);
            }

            List<PersonToRole> parents = person.getParents();
            if (!parents.isEmpty()) {
                Element parentsElement = doc.createElement("parents");
                for (PersonToRole parent: parents) {
                    String role = parent.getRole();
                    String tag = switch (role) {
                        case "mother" -> "mother";
                        case "father" -> "father";
                        default -> "parent";
                    };
                    Element parentElement = doc.createElement(tag);
                    parentElement.appendChild(doc.createTextNode(parent.getName()));
                    parentsElement.appendChild(parentElement);
                }
                personElement.appendChild(parentsElement);
            }

            PersonToRole spouse = person.getSpouse();
            if (spouse != null) {
                String role = spouse.getRole();
                String tag = switch (role) {
                    case "wife" -> "wife";
                    case "husband" -> "husband";
                    default -> "spouse";
                };
                Element spouseElement = doc.createElement(tag);
                spouseElement.appendChild(doc.createTextNode(spouse.getName()));
                personElement.appendChild(spouseElement);
            }
            root.appendChild(personElement);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(path));

        transformer.transform(source, result);
    }
}
