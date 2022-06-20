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

package io.github.selcukes.commons.helper;

import io.github.selcukes.commons.exception.SelcukesException;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@UtilityClass
public class XmlHelper {

    public static Document toXml(InputStream inputStream) {
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            builderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            builderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            builderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            return builder.parse(new InputSource(inputStream));
        } catch (Exception e) {
            throw new SelcukesException("Failed parsing to XML Document : ", e);
        }
    }

    public static List<String> filterElements(NodeList nodeList, String matcher) {
        return IntStream.range(0, nodeList.getLength())
            .mapToObj(nodeList::item)
            .filter(Element.class::isInstance)
            .map(Element.class::cast)
            .map(e -> e.getChildNodes().item(0).getNodeValue())
            .filter(nodeValue -> nodeValue.contains(matcher))
            .collect(Collectors.toList());
    }

    @SneakyThrows
    public static NodeList getNodes(Document xmlDocument, String expression) {
        XPath xPath = XPathFactory.newInstance().newXPath();
        return (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);
    }
}
