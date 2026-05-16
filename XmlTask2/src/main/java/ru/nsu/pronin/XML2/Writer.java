package ru.nsu.pronin.XML2;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ru.nsu.pronin.XML2.data.Person;
import ru.nsu.pronin.XML2.data.PersonToRole;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
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
        for (Person person: ppl.values()) {
            Element personElement = doc.createElement("person");
            personElement.setAttribute("id", person.getId());

            Element fullName = doc.createElement("fullName");
            fullName.appendChild(doc.createTextNode(person.getFullName()));
            personElement.appendChild(fullName);

            Element gender = doc.createElement("gender");
            gender.appendChild(doc.createTextNode(person.getGender()));
            personElement.appendChild(gender);

            List<PersonToRole> siblings = person.getSiblings();
            if (!siblings.isEmpty()) {
                Element siblingsElement = doc.createElement("siblings");
                for (PersonToRole sibling: siblings) {
                    String sibRole = normalizeSiblingRole(sibling.getRole(), sibling.getName(), ppl);
                    Element sib = doc.createElement(sibRole);
                    sib.appendChild(doc.createTextNode(sibling.getName()));
                    siblingsElement.appendChild(sib);
                }
                personElement.appendChild(siblingsElement);
            }

            List<PersonToRole> children = person.getChildren();
            if (!children.isEmpty()) {
                Element childrenElement = doc.createElement("children");
                for (PersonToRole child: children) {
                    String childRole = normalizeChildRole(child.getRole(), child.getName(), ppl);
                    Element childElement = doc.createElement(childRole);
                    childElement.appendChild(doc.createTextNode(child.getName()));
                    childrenElement.appendChild(childElement);
                }
                personElement.appendChild(childrenElement);
            }

            List<PersonToRole> parents = person.getParents();
            if (!parents.isEmpty()) {
                Element parentsElement = doc.createElement("parents");
                for (PersonToRole parent: parents) {
                    Element parentElement = doc.createElement(parent.getRole());
                    parentElement.appendChild(doc.createTextNode(parent.getName()));
                    parentsElement.appendChild(parentElement);
                }
                personElement.appendChild(parentsElement);
            }

            PersonToRole spouse = person.getSpouse();
            if (spouse != null) {
                Element spouseElement = doc.createElement(spouse.getRole());
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

    /**
     * Normalizes a sibling role to "brother" or "sister" based on the sibling's gender.
     * If the role is already specific ("brother"/"sister"), it is returned as-is.
     * Falls back to "sibling" if gender is unknown.
     */
    private String normalizeSiblingRole(String role, String siblingId, Map<String, Person> ppl) {
        if (role.equals("brother") || role.equals("sister")) {
            return role;
        }
        // role is generic ("sibling" / "siblings") — resolve by gender
        Person sibling = ppl.get(siblingId);
        if (sibling != null && sibling.getGender() != null) {
            char g = sibling.getGender().toLowerCase().charAt(0);
            if (g == 'm') return "brother";
            if (g == 'f') return "sister";
        }
        return "sibling";
    }

    /**
     * Normalizes a child role to "son" or "daughter" based on the child's gender.
     * If the role is already specific ("son"/"daughter"), it is returned as-is.
     * Falls back to "child" if gender is unknown.
     */
    private String normalizeChildRole(String role, String childId, Map<String, Person> ppl) {
        if (role.equals("son") || role.equals("daughter")) {
            return role;
        }
        // role is generic ("child" / "children") — resolve by gender
        Person child = ppl.get(childId);
        if (child != null && child.getGender() != null) {
            char g = child.getGender().toLowerCase().charAt(0);
            if (g == 'm') return "son";
            if (g == 'f') return "daughter";
        }
        return "child";
    }
}
