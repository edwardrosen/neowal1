/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.processor;

import org.apache.camel.ContextTestSupport;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import static org.apache.camel.component.mock.MockEndpoint.assertIsSatisfied;

/**
 * @version $Revision: 1.1 $
 */
public class InterceptWithoutProceedRouteTest extends ContextTestSupport {
    private MockEndpoint a;
    private MockEndpoint b;

    public void testSendMatchingMessage() throws Exception {
        b.expectedMessageCount(1);

        template.sendBodyAndHeader("direct:start", "<matched/>", "foo", "bar");

        assertIsSatisfied(a, b);
    }

    public void testSendNotMatchingMessage() throws Exception {

        template.sendBodyAndHeader("direct:start", "<notMatched/>", "foo", "notMatchedHeaderValue");

        assertIsSatisfied(a, b);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        a = (MockEndpoint) resolveMandatoryEndpoint("mock:a");
        b = (MockEndpoint) resolveMandatoryEndpoint("mock:b");
    }

    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() {
                // we will disable the output to 'mock:a' since we never proceed()
                intercept().filter(header("foo").isEqualTo("bar")).to("mock:b");

                from("direct:start").to("mock:a");
            }
        };
    }
}