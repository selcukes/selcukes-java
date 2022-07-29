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

    /**
     * It takes an InputStream and returns a Document
     *
     * @param inputStream The input stream to parse.
     * @return A Document object
     */
    public static Document parse(final InputStream inputStream) {
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

    /**
     * Given a stream of elements, return a stream of strings that are the values of the first child node of each element,
     * but only if the value contains the given matcher.
     *
     * @param elements a stream of elements
     * @param matcher  The string to match against.
     * @return A stream of strings
     */
    public static Stream<String> filterElements(final Stream<Element> elements, final String matcher) {
        return elements
                .map(element -> element.getChildNodes().item(0).getNodeValue())
                .filter(nodeValue -> nodeValue.contains(matcher));
    }


    /**
     * It takes an XML document and an XPath expression, and returns a stream of nodes that match the expression
     *
     * @param xmlDocument     The XML document to search.
     * @param xpathExpression The XPath expression to evaluate.
     * @return A stream of nodes
     */
    @SneakyThrows
    public static Stream<Node> selectNodes(final Document xmlDocument, final String xpathExpression) {
        var xPath = XPathFactory.newInstance().newXPath();
        var nodeList = (NodeList) xPath.compile(xpathExpression).evaluate(xmlDocument, XPathConstants.NODESET);
        return IntStream.range(0, nodeList.getLength())
                .mapToObj(nodeList::item);
    }

    /**
     * "Given an XML document and an XPath expression, return a stream of all the elements that match the expression."
     * <p>
     * The first line of the function is a call to the selectNodes function we just wrote. The second line is a filter that
     * only allows elements through. The third line is a map that casts the nodes to elements
     *
     * @param xmlDocument     The XML document to search.
     * @param xpathExpression The XPath expression to use to select the nodes.
     * @return A stream of elements
     */
    public static Stream<Element> selectElements(final Document xmlDocument, final String xpathExpression) {
        return selectNodes(xmlDocument, xpathExpression).filter(Element.class::isInstance)
                .map(Element.class::cast);
    }
}
