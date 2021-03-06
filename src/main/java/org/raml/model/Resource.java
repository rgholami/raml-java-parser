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
package org.raml.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.raml.model.parameter.UriParameter;
import org.raml.parser.annotation.Key;
import org.raml.parser.annotation.Mapping;
import org.raml.parser.annotation.Parent;
import org.raml.parser.annotation.Scalar;
import org.raml.parser.annotation.Sequence;
import org.raml.parser.resolver.ResourceHandler;
import org.raml.parser.rule.TemplateReferenceRule;

public class Resource
{

    @Scalar
    private String displayName;

    @Scalar
    private String description;

    @Parent(property = "uri")
    private String parentUri;

    @Key
    private String relativeUri;

    @Mapping
    private Map<String, UriParameter> uriParameters = new HashMap<String, UriParameter>();

    @Mapping(handler = ResourceHandler.class, implicit = true)
    private Map<String, Resource> resources = new HashMap<String, Resource>();

    @Mapping(implicit = true)
    private Map<ActionType, Action> actions = new HashMap<ActionType, Action>();

    @Scalar(rule = TemplateReferenceRule.class)
    private TemplateReference type;

    @Sequence(rule = org.raml.parser.rule.TemplateReferenceSequenceRule.class)
    private List<TemplateReference> is = new ArrayList<TemplateReference>();

    @Sequence
    private List<String> securedBy = new ArrayList<String>();

    @Mapping(rule = org.raml.parser.rule.UriParametersRule.class)
    private Map<String, List<UriParameter>> baseUriParameters = new HashMap<String, List<UriParameter>>();

    public Resource()
    {
    }

    public void setRelativeUri(String relativeUri)
    {
        this.relativeUri = relativeUri;
    }

    public String getParentUri()
    {
        return parentUri;
    }

    public void setParentUri(String parentUri)
    {
        this.parentUri = parentUri;
    }

    public void setUriParameters(Map<String, UriParameter> uriParameters)
    {
        this.uriParameters = uriParameters;
    }

    public Map<ActionType, Action> getActions()
    {
        return actions;
    }

    public void setActions(Map<ActionType, Action> actions)
    {
        this.actions = actions;
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getRelativeUri()
    {
        return relativeUri;
    }

    public String getUri()
    {
        return parentUri + relativeUri;
    }

    public Action getAction(ActionType name)
    {
        return actions.get(name);
    }

    public Action getAction(String name)
    {
        return actions.get(ActionType.valueOf(name.toUpperCase()));
    }

    public Map<String, Resource> getResources()
    {
        return resources;
    }

    public void setResources(Map<String, Resource> resources)
    {
        this.resources = resources;
    }

    public Map<String, UriParameter> getUriParameters()
    {
        return uriParameters;
    }

    public List<TemplateReference> getIs()
    {
        return is;
    }

    public void setIs(List<TemplateReference> is)
    {
        this.is = is;
    }

    public TemplateReference getType()
    {
        return type;
    }

    public void setType(TemplateReference type)
    {
        this.type = type;
    }

    public List<String> getSecuredBy()
    {
        return securedBy;
    }

    public void setSecuredBy(List<String> securedBy)
    {
        this.securedBy = securedBy;
    }

    public Map<String, List<UriParameter>> getBaseUriParameters()
    {
        return baseUriParameters;
    }

    public void setBaseUriParameters(Map<String, List<UriParameter>> baseUriParameters)
    {
        this.baseUriParameters = baseUriParameters;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof Resource))
        {
            return false;
        }

        Resource resource = (Resource) o;

        return parentUri.equals(resource.parentUri) && relativeUri.equals(resource.relativeUri);

    }

    @Override
    public int hashCode()
    {
        int result = parentUri.hashCode();
        result = 31 * result + relativeUri.hashCode();
        return result;
    }

    @Override
    public String toString()
    {
        return "Resource{displayName='" + displayName + '\'' +
               ", uri='" + (parentUri != null ? getUri() : "-" + '\'') + '}';
    }

    public Resource getResource(String path)
    {
        for (Resource resource : resources.values())
        {
            if (path.startsWith(resource.getRelativeUri()))
            {
                if (path.length() == resource.getRelativeUri().length())
                {
                    return resource;
                }
                if (path.charAt(resource.getRelativeUri().length()) == '/')
                {
                    return resource.getResource(path.substring(resource.getRelativeUri().length()));
                }
            }
        }
        return null;
    }
}
