/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.ode.axis2;

import org.apache.ode.utils.DOMUtils;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axis2.AxisFault;
import org.w3c.dom.Element;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Utility methods to convert from/to AxiOM and DOM.
 */
public class OMUtils {

  public static Element toDOM(OMElement element) throws AxisFault {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try {
      element.serialize(baos);
      ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
      return DOMUtils.parse(bais).getDocumentElement();
    } catch (Exception e) {
      throw new AxisFault("Unable to read Axis input messag.e", e);
    }
  }

  public static OMElement toOM(Element element) throws AxisFault {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try {
      DOMUtils.serialize(element, baos);
      ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
      XMLStreamReader parser = XMLInputFactory.newInstance().createXMLStreamReader(bais);
      StAXOMBuilder builder = new StAXOMBuilder(parser);
      return builder.getDocumentElement();
    } catch (Exception e) {
      throw new AxisFault("Unable to read Axis input messag.e", e);
    }
  }

}
