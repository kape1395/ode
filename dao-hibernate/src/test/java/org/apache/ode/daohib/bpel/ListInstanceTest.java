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

package org.apache.ode.daohib.bpel;

import org.apache.ode.bpel.common.InstanceFilter;
import org.apache.ode.bpel.dao.BpelDAOConnection;
import org.apache.ode.daohib.SessionManager;
import org.apache.ode.daohib.sfwk.DAOConnectionFactoryImpl;
import junit.framework.TestCase;
import org.objectweb.jotm.Jotm;

import java.io.FileInputStream;
import java.util.*;

/**
 * Testing BpelDAOConnectionImpl.listInstance. We're just producing a lot
 * of different filter combinations and test if they execute ok. To really
 * test that the result is the one expected would take a huge test database
 * (with at least a process and an instance for every possible combination).
 */
public class ListInstanceTest extends TestCase {

  private Map<String, List> filterElmts;
  private ArrayList<String> order;

  private BpelDAOConnection daoConn;
  private Jotm jotm;

  protected void setUp() throws Exception {
    Properties hibProps = new Properties();
    hibProps.load(new FileInputStream("../dao-hibernate/src/hibernate/derby.properties"));

    jotm = new Jotm(true, false);

    SessionManager sessMgr = new SessionManager(hibProps, jotm.getTransactionManager());
    new DAOConnectionFactoryImpl(sessMgr);

    jotm.getTransactionManager().begin();

    BpelDAOConnectionFactoryImpl factoryImpl = new BpelDAOConnectionFactoryImpl(sessMgr);
    daoConn = factoryImpl.createConnection();

    buildFilterElements();
  }

  protected void tearDown() throws Exception {
    jotm.getTransactionManager().commit();
  }

  public void testListInstance() throws Exception {
    for (int index = 0; index < 7; index++) {
      StringBuffer filter = new StringBuffer();
      for (Map.Entry<String, List> entry : filterElmts.entrySet()) {
        filter.append(entry.getKey());
        filter.append(entry.getValue().get((index < entry.getValue().size()) ? index : index % entry.getValue().size()));
        InstanceFilter ifilter = new InstanceFilter(filter.toString(),
                order.get((index < order.size()) ? index : index % order.size()), 20);
        daoConn.instanceQuery(ifilter);
      }
    }
  }

  private void buildFilterElements() {
    filterElmts = new HashMap<String, List>();
    ArrayList<String> nameList = new ArrayList<String>();
    nameList.add("=Hello* ");
    nameList.add("=HelloWorld ");
    filterElmts.put("name", nameList);

    ArrayList<String> namespaceList = new ArrayList<String>();
    namespaceList.add("=http://ode* ");
    namespaceList.add("=http://ode ");
    filterElmts.put("namespace", namespaceList);

    ArrayList<String> statusList = new ArrayList<String>();
    statusList.add("=active ");
    statusList.add("=suspended ");
    statusList.add("=error ");
    statusList.add("=completed|terminated ");
    statusList.add("=faulted|terminated ");
    statusList.add("=error|active ");
    filterElmts.put("status", statusList);

    ArrayList<String> startedList = new ArrayList<String>();
    startedList.add(">=2005-11-29T15:11 ");
    startedList.add("<=2005-11-29T15:11 ");
    startedList.add("<2005-11-29T15:11 started>=2005-11-29T15:11 ");
    startedList.add(">2005-11-29T15:11 started<=2005-11-29T15:11 ");
    startedList.add("=2005-11-29T15:11 ");
    filterElmts.put("started", startedList);

    ArrayList<String> lastActiveList = new ArrayList<String>();
    lastActiveList.add(">=2005-11-29T15:11 ");
    lastActiveList.add("<=2005-11-29T15:11 ");
    lastActiveList.add("<2005-11-29T15:11 last-active>=2005-11-29T15:11 ");
    lastActiveList.add(">2005-11-29T15:11 last-active<=2005-11-29T15:11 ");
    lastActiveList.add("=2005-11-29T15:11 ");
    filterElmts.put("last-active", lastActiveList);

    order = new ArrayList<String>();
    order.add("pid");
    order.add("name pid");
    order.add("namespace -name");
    order.add("version -pid +name");
    order.add("status namespace");
    order.add("-started -version status");
    order.add("+last-active name -pid +version -status namespace");
  }

}
