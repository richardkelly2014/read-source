package com.google.client.http;

import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class HttpMediaType {

    /** Matches a valid media type or '*' (examples: "text" or "*"). */
    private static final Pattern TYPE_REGEX;

    /** Matches a valid token which might be used as a type, key parameter or key value. */
    private static final Pattern TOKEN_REGEX;

    /** The pattern matching the full HTTP media type string. */
    private static final Pattern FULL_MEDIA_TYPE_REGEX;

    /** The pattern matching a single parameter (key, value) at a time. */
    private static final Pattern PARAMETER_REGEX;

    /** The main type of the media type, for example {@code "text"}. */
    private String type = "application";

    /** The sub type of the media type, for example {@code "plain"}. */
    private String subType = "octet-stream";

    /** Additional parameters to the media type, for example {@code "charset=utf-8"}. */
    private final SortedMap<String, String> parameters = new TreeMap<String, String>();

    /** The last build result or {@code null}. */
    private String cachedBuildResult;

    static {
        // TYPE_REGEX: Very restrictive regex accepting valid types and '*' for e.g. "text/*".
        // http://tools.ietf.org/html/rfc4288#section-4.2
        TYPE_REGEX = Pattern.compile("[\\w!#$&.+\\-\\^_]+|[*]");

        // TOKEN_REGEX: Restrictive (but less than TYPE_REGEX) regex accepting valid tokens.
        // http://tools.ietf.org/html/rfc2045#section-5.1
        TOKEN_REGEX =
                Pattern.compile("[\\p{ASCII}&&[^\\p{Cntrl} ;/=\\[\\]\\(\\)\\<\\>\\@\\,\\:\\\"\\?\\=]]+");

        // FULL_MEDIA_TYPE_REGEX: Unrestrictive regex matching the general structure of the media type.
        // Used to split a Content-Type string into different parts. Unrestrictive so that invalid char
        // detection can be done on a per-type/parameter basis.
        String typeOrKey = "[^\\s/=;\"]+"; // only disallow separators
        String wholeParameterSection = ";.*";
        FULL_MEDIA_TYPE_REGEX = Pattern.compile(
                "\\s*(" + typeOrKey + ")/(" + typeOrKey + ")" + // main type (G1)/sub type (G2)
                        "\\s*(" + wholeParameterSection + ")?", Pattern.DOTALL); // parameters (G3) or null

        // PARAMETER_REGEX: Semi-restrictive regex matching each parameter in the parameter section.
        // We also allow multipart values here (http://www.w3.org/Protocols/rfc1341/7_2_Multipart.html)
        // although those do not fully conform to the HTTP spec.
        String quotedParameterValue = "\"([^\"]*)\"";
        String unquotedParameterValue = "[^\\s;\"]*";
        String parameterValue =  quotedParameterValue + "|" + unquotedParameterValue;
        PARAMETER_REGEX = Pattern.compile("\\s*;\\s*(" + typeOrKey + ")" + // parameter key (G1)
                "=(" + parameterValue + ")"); // G2 (if quoted) and else G3
    }


    public HttpMediaType(String type, String subType) {
        setType(type);
        setSubType(subType);
    }

    public HttpMediaType(String mediaType) {
        fromString(mediaType);
    }

    public HttpMediaType setType(String type) {

        boolean flag = TYPE_REGEX.matcher(type).matches();

        this.type = type;
        cachedBuildResult = null;
        return this;
    }

    public String getType() {
        return type;
    }

    public HttpMediaType setSubType(String subType) {
        boolean flag =  TYPE_REGEX.matcher(subType).matches();

        this.subType = subType;
        cachedBuildResult = null;
        return this;
    }

    public String getSubType() {
        return subType;
    }

    private HttpMediaType fromString(String combinedType) {
        Matcher matcher = FULL_MEDIA_TYPE_REGEX.matcher(combinedType);

        boolean flag = matcher.matches();


        setType(matcher.group(1));
        setSubType(matcher.group(2));
        String params = matcher.group(3);
        if (params != null) {
            matcher = PARAMETER_REGEX.matcher(params);
            while (matcher.find()) {
                // 1=key, 2=valueWithQuotes, 3=valueWithoutQuotes
                String key = matcher.group(1);
                String value = matcher.group(3);
                if (value == null) {
                    value = matcher.group(2);
                }
                setParameter(key, value);
            }
        }
        return this;
    }

    public HttpMediaType setParameter(String name, String value) {
        if (value == null) {
            removeParameter(name);
            return this;
        }
        boolean flag = TOKEN_REGEX.matcher(name).matches();
        cachedBuildResult = null;
        parameters.put(name.toLowerCase(Locale.US), value);
        return this;
    }

    public String getParameter(String name) {
        return parameters.get(name.toLowerCase(Locale.US));
    }

    public HttpMediaType removeParameter(String name) {
        cachedBuildResult = null;
        parameters.remove(name.toLowerCase(Locale.US));
        return this;
    }

    public void clearParameters() {
        cachedBuildResult = null;
        parameters.clear();
    }

    public Map<String, String> getParameters() {
        return Collections.unmodifiableMap(parameters);
    }

    static boolean matchesToken(String value) {
        return TOKEN_REGEX.matcher(value).matches();
    }

    private static String quoteString(String unquotedString) {
        String escapedString = unquotedString.replace("\\", "\\\\"); // change \ to \\
        escapedString = escapedString.replace("\"", "\\\""); // change " to \"
        return "\"" + escapedString + "\"";
    }

    public String build() {
        if (cachedBuildResult != null) {
            return cachedBuildResult;
        }

        StringBuilder str = new StringBuilder();
        str.append(type);
        str.append('/');
        str.append(subType);
        if (parameters != null) {
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                String value = entry.getValue();
                str.append("; ");
                str.append(entry.getKey());
                str.append("=");
                str.append(!matchesToken(value) ? quoteString(value) : value);
            }
        }
        cachedBuildResult = str.toString();
        return cachedBuildResult;
    }

    @Override
    public String toString() {
        return build();
    }

    /**
     * Returns {@code true} if the specified media type has both the same type and subtype, or
     * {@code false} if they don't match or the media type is {@code null}.
     */
    public boolean equalsIgnoreParameters(HttpMediaType mediaType) {
        return mediaType != null && getType().equalsIgnoreCase(mediaType.getType())
                && getSubType().equalsIgnoreCase(mediaType.getSubType());
    }

    /**
     * Returns {@code true} if the two specified media types have the same type and subtype, or if
     * both types are {@code null}.
     */
    public static boolean equalsIgnoreParameters(String mediaTypeA, String mediaTypeB) {
        // TODO(mlinder): Make the HttpMediaType.isSameType implementation more performant.
        return (mediaTypeA == null && mediaTypeB == null) || mediaTypeA != null && mediaTypeB != null
                && new HttpMediaType(mediaTypeA).equalsIgnoreParameters(new HttpMediaType(mediaTypeB));
    }

    /**
     * Sets the charset parameter of the media type.
     *
     * @param charset new value for the charset parameter or {@code null} to remove
     */
    public HttpMediaType setCharsetParameter(Charset charset) {
        setParameter("charset", charset == null ? null : charset.name());
        return this;
    }

    /**
     * Returns the specified charset or {@code null} if unset.
     */
    public Charset getCharsetParameter() {
        String value = getParameter("charset");
        return value == null ? null : Charset.forName(value);
    }

    @Override
    public int hashCode() {
        return build().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof HttpMediaType)) {
            return false;
        }

        HttpMediaType otherType = (HttpMediaType) obj;

        return equalsIgnoreParameters(otherType) && parameters.equals(otherType.parameters);
    }

}
