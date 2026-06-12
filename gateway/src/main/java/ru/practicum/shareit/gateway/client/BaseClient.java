package ru.practicum.shareit.gateway.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.gateway.exception.GatewayException;

@Component
public class BaseClient {

	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper;
	private final String serverUrl;

	public BaseClient(
			RestTemplate restTemplate,
			ObjectMapper objectMapper,
			@Value("${shareit.server-url}") String serverUrl
	) {
		this.restTemplate = restTemplate;
		this.objectMapper = objectMapper;
		this.serverUrl = serverUrl;
	}

	protected <T> T get(String path, HttpHeaders headers, Class<T> responseType) {
		return exchange(path, HttpMethod.GET, null, headers, responseType);
	}

	protected <T> T get(String path, HttpHeaders headers, ParameterizedTypeReference<T> responseType) {
		return exchange(path, HttpMethod.GET, null, headers, responseType);
	}

	protected <T> T post(String path, Object body, HttpHeaders headers, Class<T> responseType) {
		return exchange(path, HttpMethod.POST, body, headers, responseType);
	}

	protected <T> T post(String path, Object body, HttpHeaders headers, ParameterizedTypeReference<T> responseType) {
		return exchange(path, HttpMethod.POST, body, headers, responseType);
	}

	protected <T> T patch(String path, Object body, HttpHeaders headers, Class<T> responseType) {
		return exchange(path, HttpMethod.PATCH, body, headers, responseType);
	}

	protected <T> T patch(String path, Object body, HttpHeaders headers, ParameterizedTypeReference<T> responseType) {
		return exchange(path, HttpMethod.PATCH, body, headers, responseType);
	}

	protected <T> T delete(String path, HttpHeaders headers, Class<T> responseType) {
		return exchange(path, HttpMethod.DELETE, null, headers, responseType);
	}

	private <T> T exchange(
			String path,
			HttpMethod method,
			Object body,
			HttpHeaders headers,
			Class<T> responseType
	) {
		try {
			ResponseEntity<T> response = restTemplate.exchange(
					URI.create(serverUrl + path),
					method,
					new HttpEntity<>(body, headers),
					responseType);
			return response.getBody();
		} catch (HttpStatusCodeException ex) {
			throw new GatewayException(ex.getStatusCode().value(), extractMessage(ex.getResponseBodyAsString()));
		}
	}

	private <T> T exchange(
			String path,
			HttpMethod method,
			Object body,
			HttpHeaders headers,
			ParameterizedTypeReference<T> responseType
	) {
		try {
			ResponseEntity<T> response = restTemplate.exchange(
					URI.create(serverUrl + path),
					method,
					new HttpEntity<>(body, headers),
					responseType);
			return response.getBody();
		} catch (HttpStatusCodeException ex) {
			throw new GatewayException(ex.getStatusCode().value(), extractMessage(ex.getResponseBodyAsString()));
		}
	}

	private String extractMessage(String responseBody) {
		if (responseBody == null || responseBody.isBlank()) {
			return "Upstream request failed";
		}
		try {
			JsonNode node = objectMapper.readTree(responseBody);
			JsonNode error = node.get("error");
			if (error != null && !error.isNull()) {
				return error.asText();
			}
		} catch (Exception ignored) {
			return responseBody;
		}
		return responseBody;
	}

	protected HttpHeaders jsonHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}

	protected HttpHeaders jsonHeaders(Long userId) {
		HttpHeaders headers = jsonHeaders();
		headers.set("X-Sharer-User-Id", String.valueOf(userId));
		return headers;
	}
}
