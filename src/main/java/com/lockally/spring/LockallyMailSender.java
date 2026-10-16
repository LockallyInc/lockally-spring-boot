package com.lockally.spring;

import com.lockally.sdk.ApiClient;
import com.lockally.sdk.ApiException;
import com.lockally.sdk.api.SendApi;
import com.lockally.sdk.model.V1SendPostRequest;
import com.lockally.sdk.model.V1SendPostRequestAttachmentsInner;

import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Sends transactional email through the Lockally API (POST /v1/send). Auto-configured
 * as a bean by the starter; inject it and call {@link #send(LockallyMessage)}.
 */
public class LockallyMailSender {

    private final SendApi api;

    public LockallyMailSender(SendApi api) {
        this.api = api;
    }

    /** Build a sender from an API key (and optional base URL override). */
    public static LockallyMailSender fromApiKey(String apiKey, String baseUrl) {
        ApiClient client = new ApiClient();
        client.setBearerToken(apiKey);
        if (baseUrl != null && !baseUrl.isEmpty()) {
            client.setBasePath(baseUrl.replaceAll("/+$", ""));
        }
        return new LockallyMailSender(new SendApi(client));
    }

    /** Send one message. Returns Lockally's message id on success. */
    public String send(LockallyMessage message) throws ApiException {
        // One idempotency key per logical send (required header, 24 h dedupe window).
        String key = "lk-" + UUID.randomUUID().toString().replace("-", "");
        return api.v1SendPost(key, toRequest(message)).getId().toString();
    }

    /** Map a {@link LockallyMessage} onto the /v1/send request. Public for testing. */
    public static V1SendPostRequest toRequest(LockallyMessage m) {
        V1SendPostRequest req = new V1SendPostRequest()
                .from(m.getFrom())
                .to(m.getTo());
        if (!m.getCc().isEmpty()) {
            req.setCc(m.getCc());
        }
        if (!m.getBcc().isEmpty()) {
            req.setBcc(m.getBcc());
        }
        if (m.getSubject() != null) {
            req.setSubject(m.getSubject());
        }
        if (m.getText() != null) {
            req.setText(m.getText());
        }
        if (m.getHtml() != null) {
            req.setHtml(m.getHtml());
        }

        Map<String, String> headers = new LinkedHashMap<>(m.getHeaders());
        if (!m.getReplyTo().isEmpty()) {
            // Lockally has no reply_to field — carry it as a header.
            headers.put("Reply-To", String.join(", ", m.getReplyTo()));
        }
        if (!headers.isEmpty()) {
            req.setHeaders(headers);
        }

        if (!m.getAttachments().isEmpty()) {
            req.setAttachments(m.getAttachments().stream()
                    .map(a -> new V1SendPostRequestAttachmentsInner()
                            .filename(a.getFilename())
                            .contentType(a.getContentType() != null
                                    ? a.getContentType() : "application/octet-stream")
                            .contentBase64(Base64.getEncoder().encodeToString(a.getContent())))
                    .collect(Collectors.toList()));
        }

        return req;
    }
}
