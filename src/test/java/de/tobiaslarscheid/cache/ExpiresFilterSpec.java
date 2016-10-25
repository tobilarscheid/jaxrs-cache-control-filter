package de.tobiaslarscheid.cache;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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

import de.tobiaslarscheid.cache.expires.ExpiresFilter;
import de.tobiaslarscheid.cache.expires.interfaces.Expires;

@RunWith(MockitoJUnitRunner.class)
public class ExpiresFilterSpec {
	@Mock
	private ContainerRequestContext requestCtx;
	@Mock
	private ContainerResponseContext responseCtx;
	@Mock
	private Expires expires;
	private ExpiresFilter expiresFilter = new ExpiresFilter();

	private void prepareMocks() {
		Mockito.when(requestCtx.getMethod()).thenReturn(HttpMethod.GET);
		Mockito.when(responseCtx.getStatus()).thenReturn(200);
		prepareExpires();
		Mockito.when(responseCtx.getEntityAnnotations()).thenReturn(new Annotation[] { expires });
	}

	private void prepareExpires() {
		Mockito.when(expires.value()).thenReturn(DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now()));
	}

	private MultivaluedMap<String, Object> prepareHeaders() {
		MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
		Mockito.when(responseCtx.getHeaders()).thenReturn(headers);
		return headers;
	}

	@Test
	public void shouldAppendExpiresIfSet() throws IOException {
		prepareMocks();
		MultivaluedMap<String, Object> headers = prepareHeaders();

		expiresFilter.filter(requestCtx, responseCtx);
		assertThat(headers.containsKey(HttpHeaders.EXPIRES), is(true));
		assertThat(headers.getFirst(HttpHeaders.EXPIRES), is(expires.value()));
	}

	@Test
	public void shouldDoNothingForNon200ResponseCode() throws IOException {
		prepareMocks();
		MultivaluedMap<String, Object> headers = prepareHeaders();

		Mockito.when(responseCtx.getStatus()).thenReturn(400);

		expiresFilter.filter(requestCtx, responseCtx);

		assertThat(headers.containsKey(HttpHeaders.EXPIRES), is(false));
	}

	@Test
	public void shouldDoNothingForNonGETMethod() throws IOException {
		prepareMocks();
		MultivaluedMap<String, Object> headers = prepareHeaders();

		Mockito.when(requestCtx.getMethod()).thenReturn(HttpMethod.POST);

		expiresFilter.filter(requestCtx, responseCtx);

		assertThat(headers.containsKey(HttpHeaders.EXPIRES), is(false));
	}
}
