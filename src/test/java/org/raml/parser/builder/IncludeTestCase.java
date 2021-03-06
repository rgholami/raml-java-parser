/*
 * Copyright (c) MuleSoft, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package org.raml.parser.builder;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.raml.model.ActionType.GET;

import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.raml.model.Raml;

public class IncludeTestCase extends AbstractRamlTestCase
{

    @Test
    public void testNotFound()
    {
        try
        {
            parseRaml("org/raml/include/include-not-found.yaml");
            fail();
        }
        catch (Exception e)
        {
            assertThat(e.getMessage(), startsWith("resource not found"));
        }
    }

    @Test
    public void testSingleLineString()
    {
        Raml raml = parseRaml("org/raml/include/include-non-yaml-single-line.yaml");
        assertThat(raml.getTitle(), is("included title"));
    }


    @Test
    public void testMultiLineString() throws Exception
    {
        Raml raml = parseRaml("org/raml/include/include-non-yaml-multi-line.yaml");
        String multiLine = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("org/raml/include/include-non-yaml-multi-line.txt"));
        assertThat(raml.getTitle(), is(multiLine));
    }

    @Test
    public void testYaml()
    {
        Raml raml = parseRaml("org/raml/include/include-yaml.yaml");
        assertThat(raml.getDocumentation().size(), is(2));
        assertThat(raml.getDocumentation().get(0).getTitle(), is("Home"));
        assertThat(raml.getDocumentation().get(0).getContent(), startsWith("Lorem ipsum"));
        assertThat(raml.getDocumentation().get(1).getTitle(), is("Section"));
        assertThat(raml.getDocumentation().get(1).getContent(), is("section content"));
    }

    @Test
    public void testRaml()
    {
        Raml raml = parseRaml("org/raml/include/include-raml.raml");
        assertThat(raml.getDocumentation().size(), is(2));
        assertThat(raml.getDocumentation().get(0).getTitle(), is("Home"));
        assertThat(raml.getDocumentation().get(0).getContent(), startsWith("Lorem ipsum"));
        assertThat(raml.getDocumentation().get(1).getTitle(), is("Section"));
        assertThat(raml.getDocumentation().get(1).getContent(), is("section content"));
    }

    @Test
    public void includeWithResourceTypeParam()
    {
        Raml raml = parseRaml("org/raml/include/include-with-params.yaml");
        assertThat(raml.getResources().get("/simple").getAction(GET).getDescription(), is("included title"));
    }

    @Test
    public void includeResourceTypeSequence()
    {
        Raml raml = parseRaml("org/raml/include/include-resource-type-sequence.yaml");
        assertThat(raml.getResources().get("/simple").getActions().size(), is(1));
        assertThat(raml.getResources().get("/simple").getAction(GET).getDescription(), is("super"));
    }

    @Test
    public void includeResourceType()
    {
        Raml raml = parseRaml("org/raml/include/include-resource-types.yaml");
        assertThat(raml.getResources().get("/simple").getActions().size(), is(1));
        assertThat(raml.getResources().get("/simple").getAction(GET).getDescription(), is("super"));
    }

    @Test
    @Ignore //use local http server
    public void testHttpScalarResource()
    {
        Raml raml = parseRaml("org/raml/include/include-http-non-yaml.yaml");
        assertThat(raml.getDocumentation().size(), is(1));
        assertThat(raml.getDocumentation().get(0).getTitle(), is("Home"));
        assertThat(raml.getDocumentation().get(0).getContent(), startsWith("Stop the point-to-point madness"));
    }

    @Test
    public void includeAction()
    {
        Raml raml = parseRaml("org/raml/include/include-action.yaml");
        assertThat(raml.getResources().get("/simple").getAction(GET).getDescription(), is("get something"));
    }
}
