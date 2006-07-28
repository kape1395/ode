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
package org.apache.ode.bom.wsdl;

import org.apache.ode.bom.api.Constants;

import javax.wsdl.Definition;
import javax.wsdl.Types;
import javax.wsdl.extensions.ExtensionRegistry;
import javax.wsdl.factory.WSDLFactory;
import javax.xml.namespace.QName;

/**
 * Factory for {@link WSDLFactory} objects that are pre-configured to handle
 * BPEL 2.0 extension elements.
 */
public class WSDLFactoryBPEL11 extends WSDLFactoryImpl implements WSDLFactory4BPEL {

  public WSDLFactoryBPEL11() {
    super(Constants.NS_BPEL4WS_2003_03, Constants.NS_BPEL4WS_PARTNERLINK_2003_05);
  }

  public static WSDLFactory newInstance() {
    return new WSDLFactoryBPEL11();
  }

  public ExtensionRegistry newPopulatedExtensionRegistry() {
    ExtensionRegistry extRegistry;
    extRegistry = super.newPopulatedExtensionRegistry();
    extRegistry.registerDeserializer(Definition.class, new QName(_bpwsNS, "property"),
                             new PropertySerializer());
    extRegistry.registerDeserializer(Definition.class, new QName(_bpwsNS, "propertyAlias"),
                             new PropertyAliasSerializer_11());
    extRegistry.registerDeserializer(Types.class, XMLSchemaType.QNAME,
                             new XMLSchemaTypeSerializer());
    extRegistry.registerDeserializer(Definition.class, new QName(Constants.NS_BPEL4WS_PARTNERLINK_2003_05, "partnerLinkType"),
        new PartnerLinkTypeSerializer_1_1(Constants.NS_BPEL4WS_PARTNERLINK_2003_05));
    return extRegistry;

  }

}
