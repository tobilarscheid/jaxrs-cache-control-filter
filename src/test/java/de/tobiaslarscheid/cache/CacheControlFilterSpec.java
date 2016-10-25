package de.tobiaslarscheid.cache;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.lang.annotation.Annotation;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import de.tobiaslarscheid.cache.control.CacheControlDirectiveName;
import de.tobiaslarscheid.cache.control.CacheControlFilter;
import de.tobiaslarscheid.cache.control.interfaces.CacheControlDirective;
import de.tobiaslarscheid.cache.control.interfaces.CacheControlled;

@RunWith(MockitoJUnitRunner.class)
public class CacheControlFilterSpec {
	@Mock
	private ContainerRequestContext requestCtx;
	@Mock
	private ContainerResponseContext responseCtx;
	private CacheControlled cacheControlled;
	@Mock
	private CacheControlDirective maxAge;
	private CacheControlFilter cacheControlFilter = new CacheControlFilter();

	private void prepareMocks() {
		Mockito.when(requestCtx.getMethod()).thenReturn(HttpMethod.GET);
		Mockito.when(responseCtx.getStatus()).thenReturn(200);
		Mockito.when(responseCtx.getEntityAnnotations()).thenReturn(new Annotation[] { cacheControlled });
	}

	private void prepareCacheControl(CacheControlDirective[] directives) {
		cacheControlled = Mockito.mock(CacheControlled.class);
		Mockito.when(maxAge.name()).thenReturn(CacheControlDirectiveName.MAX_AGE);
		Mockito.when(maxAge.value()).thenReturn("42");
		Mockito.when(cacheControlled.value()).thenReturn(directives);
	}

	private MultivaluedMap<String, Object> prepareHeaders() {
		MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
		Mockito.when(responseCtx.getHeaders()).thenReturn(headers);
		return headers;
	}

	/**
	 * This test is not very extensive. It does not test all combinations, as
	 * they are expected to be properly handled by the used
	 * javax.ws.rs.core.CacheControl
	 */
	@Test
	public void shouldAppendCacheControlIfAnythingIsSet() throws IOException {
		prepareCacheControl(new CacheControlDirective[] { maxAge });
		prepareMocks();
		MultivaluedMap<String, Object> headers = prepareHeaders();

		cacheControlFilter.filter(requestCtx, responseCtx);

		assertThat(headers.containsKey(HttpHeaders.CACHE_CONTROL), is(true));
	}

	@Test
	public void shouldDoNothingForNon200ResponseCode() throws IOException {
		prepareMocks();
		prepareCacheControl(new CacheControlDirective[] { maxAge });
		MultivaluedMap<String, Object> headers = prepareHeaders();

		Mockito.when(responseCtx.getStatus()).thenReturn(400);

		cacheControlFilter.filter(requestCtx, responseCtx);

		assertThat(headers.containsKey(HttpHeaders.CACHE_CONTROL), is(false));
	}

	@Test
	public void shouldDoNothingForNonGETMethod() throws IOException {
		prepareMocks();
		prepareCacheControl(new CacheControlDirective[] { maxAge });
		MultivaluedMap<String, Object> headers = prepareHeaders();

		Mockito.when(requestCtx.getMethod()).thenReturn(HttpMethod.POST);

		cacheControlFilter.filter(requestCtx, responseCtx);

		assertThat(headers.containsKey(HttpHeaders.CACHE_CONTROL), is(false));
	}
}
