// Chain of Responsibility Pattern Example
// Handler interface
public interface RequestHandler {
    void setNext(RequestHandler nextHandler); // Set the next handler in chain
    void handle(Request request); // Process the request or pass it along
}

// Abstract base handler with common functionality
public abstract class AbstractRequestHandler implements RequestHandler {
    protected RequestHandler nextHandler; // Next handler in the chain

    @Override
    public void setNext(RequestHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    // Template method for handling requests
    @Override
    public void handle(Request request) {
        if (canHandle(request)) {
            processRequest(request);
        }
        if (nextHandler != null) {
            nextHandler.handle(request); // Pass to next handler regardless
        }
    }

    // Subclasses implement these
    protected abstract boolean canHandle(Request request);
    protected abstract void processRequest(Request request);
}

// Concrete handlers

// Authentication handler
public class AuthenticationHandler extends AbstractRequestHandler {
    @Override
    protected boolean canHandle(Request request) {
        return request.getHeaders().containsKey("Authorization");
    }

    @Override
    protected void processRequest(Request request) {
        String authHeader = request.getHeaders().get("Authorization");
        if (authHeader == null || !isValidToken(authHeader)) {
            throw new SecurityException("Invalid or missing authentication token");
        }
        System.out.println("[AUTH] User authenticated successfully");
        request.setAuthenticatedUser(extractUserFromToken(authHeader));
    }

    private boolean isValidToken(String token) {
        // Simplified token validation
        return token.startsWith("Bearer ") && token.length() > 10;
    }

    private String extractUserFromToken(String token) {
        // Simplified user extraction
        return "user123";
    }
}

// Logging handler
public class LoggingHandler extends AbstractRequestHandler {
    @Override
    protected boolean canHandle(Request request) {
        return true; // Always log requests
    }

    @Override
    protected void processRequest(Request request) {
        System.out.println("[LOG] Request: " + request.getMethod() + " " + request.getPath() +
                          " from " + request.getRemoteAddress());
    }
}

// Rate limiting handler
public class RateLimitingHandler extends AbstractRequestHandler {
    private final Map<String, Integer> requestCounts = new ConcurrentHashMap<>();
    private final int maxRequestsPerMinute = 100;

    @Override
    protected boolean canHandle(Request request) {
        return true; // Check rate limit for all requests
    }

    @Override
    protected void processRequest(Request request) {
        String clientId = request.getRemoteAddress(); // Use IP as client identifier
        int currentCount = requestCounts.getOrDefault(clientId, 0);

        if (currentCount >= maxRequestsPerMinute) {
            throw new RuntimeException("Rate limit exceeded for client: " + clientId);
        }

        requestCounts.put(clientId, currentCount + 1);
        System.out.println("[RATE LIMIT] Request allowed for " + clientId + " (count: " + (currentCount + 1) + ")");
    }
}

// Caching handler
public class CachingHandler extends AbstractRequestHandler {
    private final Map<String, Response> cache = new ConcurrentHashMap<>();

    @Override
    protected boolean canHandle(Request request) {
        return "GET".equals(request.getMethod()); // Only cache GET requests
    }

    @Override
    protected void processRequest(Request request) {
        String cacheKey = request.getMethod() + ":" + request.getPath();

        if (cache.containsKey(cacheKey)) {
            System.out.println("[CACHE] Cache hit for " + cacheKey);
            request.setCachedResponse(cache.get(cacheKey));
            // Don't call next handler - return cached response
            return;
        }

        System.out.println("[CACHE] Cache miss for " + cacheKey);
        // Continue to next handler, response will be cached on the way back
    }

    // Method to cache responses (called by response handler)
    public void cacheResponse(String cacheKey, Response response) {
        cache.put(cacheKey, response);
        System.out.println("[CACHE] Response cached for " + cacheKey);
    }
}

// Business logic handler (end of chain)
public class BusinessLogicHandler extends AbstractRequestHandler {
    @Override
    protected boolean canHandle(Request request) {
        return true; // Handle all remaining requests
    }

    @Override
    protected void processRequest(Request request) {
        System.out.println("[BUSINESS] Processing business logic for " + request.getPath());

        // Simulate business logic
        String responseData = "Processed request for user: " + request.getAuthenticatedUser();

        Response response = new Response(200, responseData, Map.of("Content-Type", "application/json"));

        // Cache the response if it was a GET request
        if ("GET".equals(request.getMethod())) {
            String cacheKey = request.getMethod() + ":" + request.getPath();
            // In a real implementation, you'd get the caching handler from the chain
            // For simplicity, we'll assume it's available
        }

        request.setResponse(response);
    }
}

// Supporting classes
public class Request {
    private final String method;
    private final String path;
    private final String remoteAddress;
    private final Map<String, String> headers;
    private String authenticatedUser;
    private Response response;
    private Response cachedResponse;

    public Request(String method, String path, String remoteAddress, Map<String, String> headers) {
        this.method = method;
        this.path = path;
        this.remoteAddress = remoteAddress;
        this.headers = new HashMap<>(headers);
    }

    // Getters and setters
    public String getMethod() { return method; }
    public String getPath() { return path; }
    public String getRemoteAddress() { return remoteAddress; }
    public Map<String, String> getHeaders() { return new HashMap<>(headers); }
    public String getAuthenticatedUser() { return authenticatedUser; }
    public void setAuthenticatedUser(String user) { authenticatedUser = user; }
    public Response getResponse() { return response; }
    public void setResponse(Response response) { this.response = response; }
    public Response getCachedResponse() { return cachedResponse; }
    public void setCachedResponse(Response cachedResponse) { this.cachedResponse = cachedResponse; }
}

public class Response {
    private final int statusCode;
    private final String body;
    private final Map<String, String> headers;

    public Response(int statusCode, String body, Map<String, String> headers) {
        this.statusCode = statusCode;
        this.body = body;
        this.headers = new HashMap<>(headers);
    }

    public int getStatusCode() { return statusCode; }
    public String getBody() { return body; }
    public Map<String, String> getHeaders() { return new HashMap<>(headers); }
}

// Chain builder and executor
public class RequestProcessor {
    private final RequestHandler chainHead;

    public RequestProcessor() {
        // Build the chain: Logging -> Rate Limiting -> Authentication -> Caching -> Business Logic
        RequestHandler logging = new LoggingHandler();
        RequestHandler rateLimiting = new RateLimitingHandler();
        RequestHandler authentication = new AuthenticationHandler();
        RequestHandler caching = new CachingHandler();
        RequestHandler businessLogic = new BusinessLogicHandler();

        // Link the chain
        logging.setNext(rateLimiting);
        rateLimiting.setNext(authentication);
        authentication.setNext(caching);
        caching.setNext(businessLogic);

        this.chainHead = logging;
    }

    public Response processRequest(Request request) {
        try {
            chainHead.handle(request);

            // Return cached response if available, otherwise the processed response
            return request.getCachedResponse() != null ?
                   request.getCachedResponse() : request.getResponse();

        } catch (Exception e) {
            System.err.println("Request processing failed: " + e.getMessage());
            return new Response(500, "Internal Server Error: " + e.getMessage(), Map.of());
        }
    }
}

// Usage example
public class ChainOfResponsibilityDemo {
    public static void main(String[] args) {
        RequestProcessor processor = new RequestProcessor();

        // Simulate requests
        Map<String, String> headers1 = Map.of("Authorization", "Bearer valid-token-123");
        Request request1 = new Request("GET", "/api/users", "192.168.1.1", headers1);

        Response response1 = processor.processRequest(request1);
        System.out.println("Response 1: " + response1.getStatusCode() + " - " + response1.getBody());

        // Second request to same endpoint (should use cache)
        Request request2 = new Request("GET", "/api/users", "192.168.1.1", headers1);
        Response response2 = processor.processRequest(request2);
        System.out.println("Response 2: " + response2.getStatusCode() + " - " + response2.getBody());

        // Request without authentication (should fail)
        Request request3 = new Request("GET", "/api/users", "192.168.1.1", Map.of());
        Response response3 = processor.processRequest(request3);
        System.out.println("Response 3: " + response3.getStatusCode() + " - " + response3.getBody());
    }
}