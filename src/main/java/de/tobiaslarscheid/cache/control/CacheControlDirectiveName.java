package de.tobiaslarscheid.cache.control;

/**
 * This reflects all cache-control directives you could set through
 * {@link javax.ws.rs.core.CacheControl}
 */
public enum CacheControlDirectiveName {
	MAX_AGE, MUST_REVALIDATE, NO_CACHE, NO_STORE, NO_TRANSFORM, PRIVATE, PROXY_REVALIDATE, S_MAX_AGE;
}
