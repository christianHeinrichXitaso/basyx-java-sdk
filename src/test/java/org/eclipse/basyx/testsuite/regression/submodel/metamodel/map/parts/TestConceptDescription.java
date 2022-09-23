/*******************************************************************************
 * Copyright (C) 2021 the Eclipse BaSyx Authors
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 * SPDX-License-Identifier: MIT
 ******************************************************************************/
package org.eclipse.basyx.testsuite.regression.submodel.metamodel.map.parts;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.basyx.submodel.metamodel.api.dataspecification.IEmbeddedDataSpecification;
import org.eclipse.basyx.submodel.metamodel.api.identifier.IdentifierType;
import org.eclipse.basyx.submodel.metamodel.api.reference.IReference;
import org.eclipse.basyx.submodel.metamodel.api.reference.enums.KeyElements;
import org.eclipse.basyx.submodel.metamodel.map.dataspecification.EmbeddedDataSpecification;
import org.eclipse.basyx.submodel.metamodel.map.identifier.Identifier;
import org.eclipse.basyx.submodel.metamodel.map.parts.ConceptDescription;
import org.eclipse.basyx.submodel.metamodel.map.qualifier.AdministrativeInformation;
import org.eclipse.basyx.submodel.metamodel.map.qualifier.LangStrings;
import org.eclipse.basyx.submodel.metamodel.map.reference.Reference;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests constructor, setter and getter of {@link ConceptDescription} for their
 * correctness
 * 
 * @author haque
 *
 */
public class TestConceptDescription {
	private static final String CATE_STRING = "testCategory";
	private static final String ID_SHORT_STRING = "testIdShort";
	private static final LangStrings DESCRIPTION = new LangStrings("Eng", "test");
	private static final KeyElements KEY_ELEMENTS = KeyElements.ASSET;
	private static final boolean IS_LOCAL = false;
	private static final String VALUE = "testValue";
	private static final IdentifierType ID_TYPE = IdentifierType.CUSTOM;
	private static final Identifier IDENTIFIER = new Identifier(ID_TYPE, VALUE);
	private static final Reference REFERENCE = new Reference(IDENTIFIER, KEY_ELEMENTS, IS_LOCAL);

	private ConceptDescription description;

	@Before
	public void buildConceptDescription() {
		description = new ConceptDescription("testConceptDescriptionID", new Identifier(IdentifierType.IRDI, "testId"));
	}

	@Test
	public void testSetDataSpecificationReferences() {
		Collection<IReference> references = Collections.singleton(REFERENCE);
		description.setDataSpecificationReferences(references);
		assertEquals(references, description.getDataSpecificationReferences());
	}

	@Test
	public void testSetAdministration() {
		String versionString = "1.0";
		String revisionString = "4";
		AdministrativeInformation information = new AdministrativeInformation(versionString, revisionString);

		description.setAdministration(information);
		assertEquals(information, description.getAdministration());
	}

	@Test
	public void testSetIdentification() {
		description.setIdentification(ID_TYPE, ID_SHORT_STRING);
		Identifier identifier = new Identifier(ID_TYPE, ID_SHORT_STRING);
		assertEquals(identifier, description.getIdentification());
	}

	@Test
	public void testSetIsCaseOf() {
		Collection<IReference> references = Collections.singleton(REFERENCE);
		description.setIsCaseOf(Collections.singletonList(REFERENCE));
		assertEquals(references, description.getIsCaseOf());
	}

	@Test
	public void testSetIdShort() {
		description.setIdShort(ID_SHORT_STRING);
		assertEquals(ID_SHORT_STRING, description.getIdShort());
	}

	@Test
	public void testSetCategory() {
		description.setCategory(CATE_STRING);
		assertEquals(CATE_STRING, description.getCategory());
	}

	@Test
	public void testSetDescription() {
		description.setDescription(DESCRIPTION);
		assertEquals(DESCRIPTION, description.getDescription());
	}

	@Test
	public void testSetParent() {
		description.setParent(REFERENCE);
		assertEquals(REFERENCE, description.getParent());
	}

	@Test
	public void testSetEmbeddedDataSpecifications() {
		EmbeddedDataSpecification embeddedDataSpecification = new EmbeddedDataSpecification();
		Collection<IEmbeddedDataSpecification> specifications = Collections.singleton(embeddedDataSpecification);
		description.setEmbeddedDataSpecifications(specifications);
		assertEquals(specifications, description.getEmbeddedDataSpecifications());
	}
}
