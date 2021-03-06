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
package org.raml.parser.rules;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.raml.parser.builder.AbstractRamlTestCase;
import org.raml.parser.rule.ValidationResult;
import org.raml.parser.visitor.IncludeInfo;

public class IncludeRulesTestCase extends AbstractRamlTestCase
{

    @Test
    public void include()
    {
        validateRamlNoErrors("org/raml/parser/rules/includes.yaml");
    }

    @Test
    public void includeNotFound()
    {
        List<ValidationResult> errors = validateRaml("org/raml/parser/rules/includes-bad.yaml");
        assertThat("Errors are not 1 " + errors, errors.size(), is(1));
        assertThat(errors.get(0).getMessage(), is("Include cannot be resolved org/raml/parser/rules/title2.txt"));
    }

    @Test
    public void includeWithError()
    {
        List<ValidationResult> errors = validateRaml("org/raml/parser/rules/includes-yaml-with-error.yaml");
        assertThat("Errors are not 1 " + errors, errors.size(), is(1));
        assertThat(errors.get(0).getMessage(), containsString("Unknown key: invalid"));

        String includedResource = "org/raml/parser/rules/included-with-error.yaml";
        assertThat(errors.get(0).getIncludeName(), is(includedResource));

        IncludeInfo includeInfo = errors.get(0).getIncludeContext().peek();
        assertThat(includeInfo.getStartMark().getLine() + 1, is(6));
        assertThat(includeInfo.getEndMark().getLine() + 1, is(6));
        assertThat(includeInfo.getIncludeName(), is(includedResource));
    }
}
