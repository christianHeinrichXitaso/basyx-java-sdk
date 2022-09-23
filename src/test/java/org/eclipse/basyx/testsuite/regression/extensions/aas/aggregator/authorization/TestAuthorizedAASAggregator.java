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
package org.eclipse.basyx.testsuite.regression.extensions.aas.aggregator.authorization;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.basyx.aas.aggregator.api.IAASAggregator;
import org.eclipse.basyx.aas.metamodel.api.IAssetAdministrationShell;
import org.eclipse.basyx.aas.metamodel.map.AssetAdministrationShell;
import org.eclipse.basyx.aas.metamodel.map.descriptor.ModelUrn;
import org.eclipse.basyx.aas.metamodel.map.parts.Asset;
import org.eclipse.basyx.extensions.aas.aggregator.authorization.AuthorizedAASAggregator;
import org.eclipse.basyx.submodel.metamodel.api.identifier.IIdentifier;
import org.eclipse.basyx.testsuite.regression.extensions.shared.mqtt.AuthorizationContextProvider;
import org.eclipse.basyx.vab.exception.provider.ProviderException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Tests authorization with the AuthorizedAASAggregator
 *
 * @author jungjan, fried, fischer
 */
@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class TestAuthorizedAASAggregator {
	@Mock
	private IAASAggregator aggregatorMock;
	private AuthorizedAASAggregator testSubject;
	private AuthorizationContextProvider securityContextProvider = new AuthorizationContextProvider(AuthorizedAASAggregator.READ_AUTHORITY, AuthorizedAASAggregator.WRITE_AUTHORITY);

	@Before
	public void setUp() {
		testSubject = new AuthorizedAASAggregator(aggregatorMock);
	}

	@After
	public void tearDown() {
		securityContextProvider.clearContext();
		Mockito.verifyNoMoreInteractions(aggregatorMock);
	}

	private AssetAdministrationShell invokeCreateAAS() {
		final IIdentifier shellId = new ModelUrn("urn:test1");
		final AssetAdministrationShell shell = new AssetAdministrationShell("test", shellId, new Asset());
		testSubject.createAAS(shell);
		return shell;
	}

	@Test(expected = ProviderException.class)
	public void givenSecurityContextIsEmpty_whenCreateAAS_thenThrowProviderException() {
		securityContextProvider.setEmptySecurityContext();
		invokeCreateAAS();
	}

	@Test
	public void givenPrincipalHasWriteAuthority_whenCreateAAS_thenInvocationIsForwarded() {
		securityContextProvider.setSecurityContextWithWriteAuthority();
		AssetAdministrationShell shell = invokeCreateAAS();
		Mockito.verify(aggregatorMock).createAAS(shell);
	}

	@Test(expected = ProviderException.class)
	public void givenPrincipalIsMissingWriteAuthority_whenCreateAAS_thenThrowProviderException() {
		securityContextProvider.setSecurityContextWithoutAuthorities();
		invokeCreateAAS();
	}

	private IIdentifier invokeDeleteAAS() {
		final IIdentifier shellId = new ModelUrn("urn:test");
		testSubject.deleteAAS(shellId);
		return shellId;
	}

	@Test
	public void givenPrincipalHasWriteAuthority_whenDeleteAAS_thenInvocationIsForwarded() {
		securityContextProvider.setSecurityContextWithWriteAuthority();
		final IIdentifier shellId = invokeDeleteAAS();
		Mockito.verify(aggregatorMock).deleteAAS(shellId);
	}

	@Test(expected = ProviderException.class)
	public void givenPrincipalIsMissingWriteAuthority_whenDeleteAAS_thenThrowProviderException() {
		securityContextProvider.setSecurityContextWithoutAuthorities();
		invokeDeleteAAS();
	}

	@Test
	public void givenPrincipalHasReadAuthority_whenGetAAS_thenInvocationIsForwarded() {
		securityContextProvider.setSecurityContextWithReadAuthority();

		final IIdentifier shellId = new ModelUrn("urn:test1");
		final AssetAdministrationShell expectedShell = new AssetAdministrationShell("test", shellId, new Asset());
		Mockito.when(aggregatorMock.getAAS(shellId)).thenReturn(expectedShell);

		final IAssetAdministrationShell shell = testSubject.getAAS(shellId);

		Assert.assertEquals(expectedShell, shell);
	}

	@Test(expected = ProviderException.class)
	public void givenPrincipalIsMissingReadAuthority_whenGetAAS_thenThrowProviderException() {
		securityContextProvider.setSecurityContextWithoutAuthorities();

		final IIdentifier shellId = new ModelUrn("urn:test1");

		testSubject.getAAS(shellId);
	}

	@Test
	public void givenPrincipalHasReadAuthority_whenGetAASList_thenInvocationIsForwarded() {
		securityContextProvider.setSecurityContextWithReadAuthority();

		final IIdentifier shellId = new ModelUrn("urn:test1");
		final Collection<IAssetAdministrationShell> expectedAASDescriptorList = Collections.singletonList(new AssetAdministrationShell("test", shellId, new Asset()));
		Mockito.when(aggregatorMock.getAASList()).thenReturn(expectedAASDescriptorList);

		final Collection<IAssetAdministrationShell> shellList = testSubject.getAASList();

		Assert.assertEquals(expectedAASDescriptorList, shellList);
	}

	@Test(expected = ProviderException.class)
	public void givenPrincipalIsMissingReadAuthority_whenGetAASList_thenThrowProviderException() {
		securityContextProvider.setSecurityContextWithoutAuthorities();

		testSubject.getAASList();
	}
}
