 If you are dealing with dynamic data, you cannot cache for long periods of time, such as days, hours or sometimes even minutes because data becomes stale too quickly. That doesn't mean, however, that such data shouldn't be cached at all.
	
	dynamic data-sets
 f you have low load-levels, there's no sense in caching something for several seconds, as most clients won't be able to reach cached data. The rate of cache misses compared to cache hits will be very high. However, when the load increases (let's say to hundreds of requests-per-second) even a five-second cache will be a lifesaver, satisfying thousands of requests from cache, rather than origin, providing very effective protection for the backend systems by avoiding database hits etc. The amazing thing about this type of caching is that it becomes more effective as the load on the system increases 
	
	Nearly-static data-sets
	
 The second type of caching that can and should be used extensively deals with nearly-static data. In any application you have plenty of such data-sets: lists of countries, states, cities (any domain), insurance providers (healthcare), podcasts, series and topics (news media), currencies (banking) etc. These lists do change and generally we have no idea when they will change but we do know that they change quite infrequently. Nearly-static data-sets are very effective targets for long-term caching.


We will have to be sure that timezone conversions are done properly by every participant of that exchange.
We will need to assume clocks on the Chicago server and Melbourne client are ideally synchronized (generally, a pipe dream).
For the client to leverage the cache, a response from Chicago server to Melbourne client will need to arrive in less than two seconds, otherwise the cache will already be invalid by the time the response is received. Considering the distance between Chicago and Melbourne, the theoretical limitation of the speed of light and the actual speed of data transmission on the public Web (which is much slower), this goal may be unattainable.

	In distributed systems, deployed at large distances (such as the Web), the above assumptions are so unrealistic that using date-based caching instructions, such as the Expires header, is highly ineffective. The same is true for the combination of Last-Modified and If-Modified-Since headers, which also rely on a shared understanding of date-time.

	Instead, when caching resources for short periods of time you should be using HTTP caching instructions that do not rely on a shared understanding of time, such as cache-control: max-age and ETags

 
 Cache-Control: time and manner a file shud be cached
	
	max-age
 	Cache-control:max-age=6543,public
 
 	Will stay in cache for these many seconds
 Example for logo image on every page, should not download every time
 
Common max-age values are:

One minute: max-age=60
One hour: max-age=3600
One day: max-age=86400
One week: max-age=604800
One month: max-age=2628000
One year: max-age=31536000
	
	public
	
 The Cache-Control header above states "public". This means that this file may be publicly cached
 The "public" response directive indicates that any cache MAY store the response, even if the response would normally be non-cacheable or cacheable only within a private cache.

In essence, if you want something cached for page speed reasons, and it is not private or time sensitive then you should use the public directive.

	private
The private directive means it is specific to one person. An example would be a Twitter page. When you go to Twitter you see one thing and another person going to the same url sees something else. Even though the information on that page is public and not sensitive, it is specific to one person.
 The "private" response directive indicates that the response message is intended for a single user and MUST NOT be stored by a shared cache. A private cache MAY store the response and reuse it for later requests, even if the response would normally be non-cacheable.
 
	no-store
The no-store directive is the strongest indication that something should never ever be stored in any caching mechanism whatsoever.
The "no-store" response directive indicates that a cache MUST NOT store any part of either the immediate request or response. This directive applies to both private and shared caches.
On the contrary, “no-store” is simpler. This is the case because it disallows browsers and all intermediate caches from storing any versions of returned responses i.e. responses containing private/personal information or banking data. Every time users request this asset, requests are sent to the server. The assets are downloaded every time

	no-cache
“No-cache” shows that returned responses can’t be used for subsequent requests to the same URL before checking if server responses have changed. If a proper ETag (validation token) is present as a result, no-cache incurs a roundtrip in an effort to validate cached responses. Caches can however eliminate downloads if the resources haven’t changed. In other words, web browsers might cache the assets but they have to check on every request if the assets have changed (304 response if nothing has changed).

	s-maxage
The “s-” stands for shared as in “shared cache”. This directive is explicitly for CDNs among other intermediary caches. This directive overrides the max-age directive and expires header field when present. KeyCDN also obeys this directive.


	must-revalidate
The must-revalidate directive is used to tell a cache that it must first revalidate an asset with the origin after it becomes stale. The asset must not be delivered to the client without doing an end-to-end revalidation. In short, stale assets must first be verified and expired assets should not be used.

	proxy-revalidate
The proxy-revalidate directive is the same as the must-revalidate directive, however, it only applies to shared caches such as proxies. It is useful in the event that a proxy services many users that need to be authenticated one-by-one. A response to an authenticated request can be stored in the user’s cache without needing to revalidate it each time as they are known and have already been authenticated. However, proxy-revalidate allows proxies to still revalidate for new users that have not been authenticated yet.

	no-transform
The no-transform directive tells any intermediary such as a proxy or cache server to not make any modifications whatsoever to the original asset. The Content-Encoding, Content-Range, and Content-Type headers must remain unchanged. This can occur if a non-transparent proxy decides to make modifications to assets in order to save space. However, this can cause serious problems in the event that the asset must remain identical to the original entity-body although must also pass through the proxy.

	Pragma
The old “pragma” header accomplishes many things most of them characterized by newer implementations. We are however most concerned with the pragma: no-cache directive which is interpreted by newer implementations as cache-control: no-cache

	file types to be cached

Images - (png, jpg, gif, etc.) Images don't really change so they can have a long cache time period (a year)
CSS - CSS files tend to change more often than other files, a shorter time period may be needed (week or month)
ico (favicon) - rarely changed (a year)
JS - Javascripts for the most part are not changing very often so a longer cache time is normally possible (a month)

	URL fingerprinting
One thing I would mention is that if you do change something (like a CSS file) and that file is cached, you should consider changing the name of the file so that your updated CSS file is seen by all users.

This is called URL fingerprinting.

Getting a fresh (not cached) file resource is possible by having a unique name. An example would be if our css file was named "main.css" we could name it "main_1.css" instead. The next time we change it we can call it "main_2.css". This is useful for files that change occasionally.


filesMatch ".(css|jpg|jpeg|png|gif|js|ico)$">
Header set Cache-Control "max-age=2628000, public"
</filesMatch>


# One year for image files
<filesMatch ".(jpg|jpeg|png|gif|ico)$">
Header set Cache-Control "max-age=31536000, public"
</filesMatch>
# One month for css and js
<filesMatch ".(css|js)$">
Header set Cache-Control "max-age=2628000, public"
</filesMatch>

 HTTP caching revolves around the Cache-Control response header and, subsequently, conditional request headers (such as Last-Modified and ETag). Cache-Control advises private (for example, browser) and public (for example, proxy) caches on how to cache and re-use responses. An ETag header is used to make a conditional request that may result in a 304 (NOT_MODIFIED) without a body, if the content has not changed. ETag can be seen as a more sophisticated successor to the Last-Modified header.
 
 CacheControl provides support for configuring settings related to the Cache-Control header and is accepted as an argument in a number of places:

WebContentInterceptor

WebContentGenerator

Controllers

Static Resources

While RFC 7234 describes all possible directives for the Cache-Control response header, the CacheControl type takes a use case-oriented approach that focuses on the common scenarios:

// Cache for an hour - "Cache-Control: max-age=3600"
CacheControl ccCacheOneHour = CacheControl.maxAge(1, TimeUnit.HOURS);

// Prevent caching - "Cache-Control: no-store"
CacheControl ccNoStore = CacheControl.noStore();

// Cache for ten days in public and private caches,
// public caches should not transform the response
// "Cache-Control: max-age=864000, public, no-transform"
CacheControl ccCustom = CacheControl.maxAge(10, TimeUnit.DAYS).noTransform().cachePublic();
WebContentGenerator also accept a simpler cachePeriod property (defined in seconds) that works as follows:

A -1 value does not generate a Cache-Control response header.

A 0 value prevents caching by using the 'Cache-Control: no-store' directive.

An n > 0 value caches the given response for n seconds by using the 'Cache-Control: max-age=n' directive.


18
down vote
accepted
org.springframework.web.servlet.support.WebContentGenerator, which is the base class for all Spring controllers has quite a few methods dealing with cache headers:

The org.springframework.web.servlet.mvc.WebContentInterceptor class allows you to define default caching behaviour, plus path-specific overrides (with the same path-matcher behaviour used elsewhere). The steps for me were:

Ensure my instance of org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter does not have the "cacheSeconds" property set.
Add an instance of WebContentInterceptor:

<mvc:interceptors>
...
<bean class="org.springframework.web.servlet.mvc.WebContentInterceptor" p:cacheSeconds="0" p:alwaysUseFullPath="true" >
    <property name="cacheMappings">
        <props>
            <!-- cache for one month -->
            <prop key="/cache/me/**">2592000</prop>
            <!-- don't set cache headers -->
            <prop key="/cache/agnostic/**">-1</prop>
        </props>
    </property>
</bean>
...
</mvc:interceptors>
After these changes, responses under /foo included headers to discourage caching, responses under /cache/me included headers to encourage caching, and responses under /cache/agnostic included no cache-related headers.

If using a pure Java configuration:

@EnableWebMvc
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
  /* Time, in seconds, to have the browser cache static resources (one week). */
  private static final int BROWSER_CACHE_CONTROL = 604800;

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry
     .addResourceHandler("/images/**")
     .addResourceLocations("/images/")
     .setCachePeriod(BROWSER_CACHE_CONTROL);
  }
}

@Controller
public class EmployeeController {

    @RequestMapping(value = "/find/employer/{employerId}", method = RequestMethod.GET)
    public List getEmployees(@PathVariable("employerId") Long employerId, final HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache");
        return employeeService.findEmployeesForEmployer(employerId);
    }

}
In your controller, you can set response headers directly.

response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
response.setHeader("Pragma", "no-cache");
response.setDateHeader("Expires", 0);
Code above shows exactly what you want to achive. You have to do two things. Add "final HttpServletResponse response" as your parameter. And then set header Cache-Control to no-cache.


Starting with Spring 4.2 you can do this:

import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class CachingController {
    @RequestMapping(method = RequestMethod.GET, path = "/cachedapi")
    public ResponseEntity<MyDto> getPermissions() {

        MyDto body = new MyDto();

        return ResponseEntity.ok()
            .cacheControl(CacheControl.maxAge(20, TimeUnit.SECONDS))
            .body(body);
    }
}


CacheControl object is a builder with many configuration options


you can define a anotation for this: @CacheControl(isPublic = true, maxAge = 300, sMaxAge = 300), then render this anotation to HTTP Header with Spring MVC interceptor. or do it dynamic:

int age = calculateLeftTiming();
String cacheControlValue = CacheControlHeader.newBuilder()
      .setCacheType(CacheType.PUBLIC)
      .setMaxAge(age)
      .setsMaxAge(age).build().stringValue();
if (StringUtils.isNotBlank(cacheControlValue)) {
    response.addHeader("Cache-Control", cacheControlValue);
}


BTW: I just found that Spring MVC has build-in support for cache control: Google WebContentInterceptor or CacheControlHandlerInterceptor or CacheControl


3
down vote
I know this is a really old one, but those who are googling, this might help:

@Override
protected void addInterceptors(InterceptorRegistry registry) {

    WebContentInterceptor interceptor = new WebContentInterceptor();

    Properties mappings = new Properties();
    mappings.put("/", "2592000");
    mappings.put("/admin", "-1");
    interceptor.setCacheMappings(mappings);

    registry.addInterceptor(interceptor);
}

https://www.logicbig.com/tutorials/spring-framework/spring-web-mvc/cache-control.html

Clicking on 'test1' link will retrieve the page from browser's local cache, but clicking on reload button (or F5) will reload the page from server. This happens if we are on the same tab. If we open new tab or new browser window and enter the url in the address bar, the page will be retrieved from the cache. This behavior is consistent in the latest versions of Chrome, FireFox and Edge browsers.
Default Cache-Control
According to the specification, if there's no cache-control header specified by the response, the browser will always cache the contents. It will probably be cached for a long period or may be for good, unless user uses F5 or Ctrl+F5 (hard refresh) or clears the cache manually by using browser settings.

https://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.9

 Cache-Control   = "Cache-Control" ":" 1#cache-directive
    cache-directive = cache-request-directive
         | cache-response-directive
    cache-request-directive =
           "no-cache"                          ; Section 14.9.1
         | "no-store"                          ; Section 14.9.2
         | "max-age" "=" delta-seconds         ; Section 14.9.3, 14.9.4
         | "max-stale" [ "=" delta-seconds ]   ; Section 14.9.3
         | "min-fresh" "=" delta-seconds       ; Section 14.9.3
         | "no-transform"                      ; Section 14.9.5
         | "only-if-cached"                    ; Section 14.9.4
         | cache-extension                     ; Section 14.9.6
     cache-response-directive =
           "public"                               ; Section 14.9.1
         | "private" [ "=" <"> 1#field-name <"> ] ; Section 14.9.1
         | "no-cache" [ "=" <"> 1#field-name <"> ]; Section 14.9.1
         | "no-store"                             ; Section 14.9.2
         | "no-transform"                         ; Section 14.9.5
         | "must-revalidate"                      ; Section 14.9.4
         | "proxy-revalidate"                     ; Section 14.9.4
         | "max-age" "=" delta-seconds            ; Section 14.9.3
         | "s-maxage" "=" delta-seconds           ; Section 14.9.3
         | cache-extension                        ; Section 14.9.6
    cache-extension = token [ "=" ( token | quoted-string ) ]
When a directive appears without any 1#field-name parameter, the directive applies to the entire request or response. When such a directive appears with a 1#field-name parameter, it applies only to the named field or fields, and not to the rest of the request or response. This mechanism supports extensibility; implementations of future versions of the HTTP protocol might apply these directives to header fields not defined in HTTP/1.1.

@Override
public void addResourceHandlers(ResourceHandlerRegistry registry) {
    // This is just an example
    registry.addResourceHandler("/img/**")
            .addResourceLocations("classpath:/static/images/")
            .setCachePeriod(12);
}
We could also override the Spring Security default settings. If we want to deactivate the "no cache control" policy for our API, we can change the ApiSecurityConfiguration class...

https://www.logicbig.com/tutorials/spring-framework/spring-web-mvc/cache-control.html