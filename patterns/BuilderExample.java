// Builder Pattern Example
// Product class with many optional fields
public class HttpRequest {
    private final String url;           // Required field
    private final String method;        // Required field
    private final Map<String, String> headers;  // Optional
    private final Map<String, String> queryParams; // Optional
    private final int timeout;          // Optional, with default
    private final String authToken;     // Optional

    // Private constructor ensures objects are created only through Builder
    private HttpRequest(Builder builder) {
        this.url = builder.url;
        this.method = builder.method;
        this.headers = builder.headers != null ? new HashMap<>(builder.headers) : new HashMap<>();
        this.queryParams = builder.queryParams != null ? new HashMap<>(builder.queryParams) : new HashMap<>();
        this.timeout = builder.timeout;
        this.authToken = builder.authToken;
    }

    // Getters omitted for brevity
    public String getUrl() { return url; }
    public String getMethod() { return method; }
    public Map<String, String> getHeaders() { return new HashMap<>(headers); }

    // Static inner Builder class
    public static class Builder {
        // Required fields
        private final String url;
        private final String method;

        // Optional fields with defaults
        private Map<String, String> headers = new HashMap<>();
        private Map<String, String> queryParams = new HashMap<>();
        private int timeout = 5000; // Default 5 seconds
        private String authToken = null;

        // Constructor with required parameters
        public Builder(String url, String method) {
            this.url = url;
            this.method = method;
        }

        // Fluent methods for optional parameters
        public Builder addHeader(String key, String value) {
            this.headers.put(key, value);
            return this; // Return this for method chaining
        }

        public Builder addQueryParam(String key, String value) {
            this.queryParams.put(key, value);
            return this;
        }

        public Builder timeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder authToken(String authToken) {
            this.authToken = authToken;
            return this;
        }

        // Build method validates and creates the object
        public HttpRequest build() {
            // Validation logic
            if (url == null || url.trim().isEmpty()) {
                throw new IllegalArgumentException("URL cannot be null or empty");
            }
            if (method == null || method.trim().isEmpty()) {
                throw new IllegalArgumentException("HTTP method cannot be null or empty");
            }
            return new HttpRequest(this);
        }
    }
}

// Usage example
public class HttpClient {
    public static void main(String[] args) {
        // Build a complex request fluently
        HttpRequest request = new HttpRequest.Builder("https://api.example.com/users", "GET")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer token123")
                .addQueryParam("page", "1")
                .addQueryParam("limit", "10")
                .timeout(10000)
                .authToken("token123")
                .build();

        System.out.println("Request URL: " + request.getUrl());
        System.out.println("Request Method: " + request.getMethod());
        System.out.println("Headers: " + request.getHeaders());
    }
}