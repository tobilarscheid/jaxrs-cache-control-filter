# JAX-RS Cache Control Filter

JAX-RS cache control finally became easy! Add `Cache-Control` or `Expires` headers to all your responses with a simple annotation.

To get started, add the following dependency from jitpack:

https://jitpack.io/#tobilarscheid/jaxrs-cache-control-filter/v-1.0.0

## Cache-Control

For Cache-Control, annotate your service Method with `@CacheControlled` and pass all the `@CacheControlDirectives` you want to use as an array.

```java
@CacheControlled({@CacheControlDirective(name=MAX_AGE, value="100")})
public Response getMyEntity(){
    MyEntity e = new MyEntity();
    return Response.ok(e).build();
}
```

The `Cache-Control` header is automatically added to your Response using [JAX-RS CacheControl](http://docs.oracle.com/javaee/7/api/javax/ws/rs/core/CacheControl.html).

## Expires

For Expires, annotate your service Method with `@Expires` and pass a `RFC 1123` Datetime describing when your resource expires.

```java
@Expires("Tue, 3 Jun 2008 11:05:30 GMT")
public Response getMyEntity(){
    MyEntity e = new MyEntity();
    return Response.ok(e).build();
}
```

The `Expires`header is automatically added to your Response.

## Hints
  - All the functionality is only applied if your Response´s status code is `200` and the Request Method was `GET`.
  - If you don't use class path scanning (hint: [you shouldn't](https://blogs.oracle.com/japod/entry/when_to_use_jax_rs)), you need to list `de.tobiaslarscheid.cache.CacheControlFilter.class` and/or `de.tobiaslarscheid.cache.ExpiresFilter.class` in your Rest Application. (Or whereever else you register your resource classes)
  - If you want to add ETags to complete your caching, you should have a look at [JAX-RS ETag Filter](https://github.com/tobilarscheid/jaxrs-etag-filter)

License
----

MIT