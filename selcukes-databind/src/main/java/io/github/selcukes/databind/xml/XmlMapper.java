/*
 *  Copyright (c) Ramesh Babu Prudhvi.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.github.selcukes.databind.xml;


import io.github.selcukes.databind.exception.DataMapperException;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.InputStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@UtilityClass
public class XmlMapper {

    public static Document parse(InputStream inputStream) {
        try (inputStream) {
            var factory = DocumentBuilderFactory.newInstance();
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            var documentBuilder = factory.newDocumentBuilder();
            return documentBuilder.parse(new InputSource(inputStream));
        } catch (Exception e) {
            throw new DataMapperException("Failed to parse InputStream to XML Document : ", e);
        }
    }

    public static Stream<String> filterElements(Stream<Element> elements, String matcher) {
        return elements
            .map(element -> element.getChildNodes().item(0).getNodeValue())
            .filter(nodeValue -> nodeValue.contains(matcher));
    }

    @SneakyThrows
    public static Stream<Node> selectNodes(Document xmlDocument, String xpathExpression) {
        var xPath = XPathFactory.newInstance().newXPath();
        var nodeList = (NodeList) xPath.compile(xpathExpression).evaluate(xmlDocument, XPathConstants.NODESET);
        return IntStream.range(0, nodeList.getLength())
            .mapToObj(nodeList::item);
    }

    public static Stream<Element> selectElements(Document xmlDocument, String xpathExpression) {
        return selectNodes(xmlDocument, xpathExpression).filter(Element.class::isInstance)
            .map(Element.class::cast);
    }
}
