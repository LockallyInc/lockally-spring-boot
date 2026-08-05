package com.lockally.spring;

import com.lockally.sdk.model.V1SendPostRequest;
import com.lockally.sdk.model.V1SendPostRequestAttachmentsInner;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LockallyMailSenderTest {

    @Test
    void mapsMessageToSendRequest() {
        LockallyMessage msg = LockallyMessage.builder()
                .from("alerts@acme.com")
                .to("user@example.com")
                .cc("cc@example.com")
                .replyTo("reply@acme.com")
                .subject("Your code")
                .text("code 123")
                .html("<b>code 123</b>")
                .header("X-Campaign", "welcome")
                .attachment("invoice.pdf", "application/pdf", "BYTES".getBytes(StandardCharsets.UTF_8))
                .build();

        V1SendPostRequest req = LockallyMailSender.toRequest(msg);

        assertEquals("alerts@acme.com", req.getFrom());
        assertEquals(List.of("user@example.com"), req.getTo());
        assertEquals(List.of("cc@example.com"), req.getCc());
        assertEquals("Your code", req.getSubject());
        assertEquals("code 123", req.getText());
        assertEquals("<b>code 123</b>", req.getHtml());
        assertEquals("reply@acme.com", req.getHeaders().get("Reply-To"));
        assertEquals("welcome", req.getHeaders().get("X-Campaign"));

        List<V1SendPostRequestAttachmentsInner> atts = req.getAttachments();
        assertEquals(1, atts.size());
        assertEquals("invoice.pdf", atts.get(0).getFilename());
        assertEquals("application/pdf", atts.get(0).getContentType());
        assertEquals(Base64.getEncoder().encodeToString("BYTES".getBytes(StandardCharsets.UTF_8)),
                atts.get(0).getContentBase64());
    }

    @Test
    void omitsOptionalFieldsWhenAbsent() {
        LockallyMessage msg = LockallyMessage.builder()
                .from("a@acme.com")
                .to("b@example.com")
                .text("yo")
                .build();

        V1SendPostRequest req = LockallyMailSender.toRequest(msg);

        assertEquals(List.of("b@example.com"), req.getTo());
        assertEquals("yo", req.getText());
        assertNull(req.getHtml());
        // The generated model defaults these collections to empty (not null); the mapper
        // leaves them untouched when the message omits them.
        assertTrue(req.getCc().isEmpty());
        assertTrue(req.getHeaders().isEmpty());
        assertTrue(req.getAttachments().isEmpty());
    }

    @Test
    void buildsSenderFromApiKey() {
        LockallyMailSender sender = LockallyMailSender.fromApiKey("lk_test_abc", null);
        assertTrue(sender != null);
    }
}
