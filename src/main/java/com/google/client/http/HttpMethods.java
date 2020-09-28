package com.google.client.http;

/**
 * HTTP request method constants specified in <a
 * href="http://tools.ietf.org/html/rfc2616#section-5.1.1">RFC 2616 Section 5.1.1</a>.
 *
 * @since 1.12
 * @author Yaniv Inbar
 */
public final class HttpMethods {

  /** HTTP CONNECT method. */
  public static final String CONNECT = "CONNECT";

  /** HTTP DELETE method. */
  public static final String DELETE = "DELETE";

  /** HTTP GET method. */
  public static final String GET = "GET";

  /** HTTP HEAD method. */
  public static final String HEAD = "HEAD";

  /** HTTP OPTIONS method. */
  public static final String OPTIONS = "OPTIONS";

  /**
   * HTTP PATCH method.
   *
   * @since 1.14
   */
  public static final String PATCH = "PATCH";

  /** HTTP POST method. */
  public static final String POST = "POST";

  /** HTTP PUT method. */
  public static final String PUT = "PUT";

  /** HTTP TRACE method. */
  public static final String TRACE = "TRACE";

  private HttpMethods() {
  }
}
