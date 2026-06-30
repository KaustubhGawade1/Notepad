package service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.CloudDocument;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

public class DocumentApiClient {

    private static final String BASE_URL = "http://localhost:8080";
    private final HttpClient httpClient;
    private final Gson gson;
    private String jwtToken;

    public DocumentApiClient() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.gson = new Gson();
    }

    public boolean isAuthenticated() {
        return jwtToken != null && !jwtToken.isBlank();
    }

    public void login(String username, String password) throws IOException, InterruptedException {
        AuthResponse response = sendAuthRequest("/api/auth/login", new AuthRequest(username, password));
        this.jwtToken = response.token();
    }

    public void register(String username, String password) throws IOException, InterruptedException {
        AuthResponse response = sendAuthRequest("/api/auth/register", new AuthRequest(username, password));
        this.jwtToken = response.token();
    }

    public List<CloudDocument> listDocuments() throws IOException, InterruptedException {
        HttpRequest request = authenticatedRequest("/api/documents", "GET", null);
        String body = send(request);
        Type listType = new TypeToken<List<CloudDocument>>() {}.getType();
        return gson.fromJson(body, listType);
    }

    public CloudDocument getDocument(Long id) throws IOException, InterruptedException {
        HttpRequest request = authenticatedRequest("/api/documents/" + id, "GET", null);
        String body = send(request);
        return gson.fromJson(body, CloudDocument.class);
    }

    public CloudDocument saveDocument(String name, String content) throws IOException, InterruptedException {
        DocumentRequest requestBody = new DocumentRequest(name, content);
        HttpRequest request = authenticatedRequest("/api/documents", "POST", requestBody);
        String body = send(request);
        return gson.fromJson(body, CloudDocument.class);
    }

    private AuthResponse sendAuthRequest(String path, AuthRequest requestBody) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(requestBody)))
                .build();

        String body = send(request);
        return gson.fromJson(body, AuthResponse.class);
    }

    private HttpRequest authenticatedRequest(String path, String method, Object body) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + jwtToken);

        if (body != null) {
            builder.header("Content-Type", "application/json")
                    .method(method, HttpRequest.BodyPublishers.ofString(gson.toJson(body)));
        } else {
            builder.method(method, HttpRequest.BodyPublishers.noBody());
        }
        return builder.build();
    }

    private String send(HttpRequest request) throws IOException, InterruptedException {
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            return response.body();
        }
        throw new IOException("Cloud API request failed: " + response.statusCode() + " - " + response.body());
    }

    public static record AuthRequest(String username, String password) {}

    public static record AuthResponse(String token) {}

    public static record DocumentRequest(String name, String content) {}
}
