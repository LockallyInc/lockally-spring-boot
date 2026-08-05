package com.lockally.spring;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A transactional message to send through {@link LockallyMailSender}. Build one with
 * the fluent {@link #builder()}.
 */
public final class LockallyMessage {

    /** A file attachment. Raw bytes are base64-encoded when sent. */
    public static final class Attachment {
        private final String filename;
        private final String contentType;
        private final byte[] content;

        public Attachment(String filename, String contentType, byte[] content) {
            this.filename = filename;
            this.contentType = contentType;
            this.content = content;
        }

        public String getFilename() {
            return filename;
        }

        public String getContentType() {
            return contentType;
        }

        public byte[] getContent() {
            return content;
        }
    }

    private final String from;
    private final List<String> to;
    private final List<String> cc;
    private final List<String> bcc;
    private final List<String> replyTo;
    private final String subject;
    private final String text;
    private final String html;
    private final Map<String, String> headers;
    private final List<Attachment> attachments;

    private LockallyMessage(Builder b) {
        this.from = b.from;
        this.to = b.to;
        this.cc = b.cc;
        this.bcc = b.bcc;
        this.replyTo = b.replyTo;
        this.subject = b.subject;
        this.text = b.text;
        this.html = b.html;
        this.headers = b.headers;
        this.attachments = b.attachments;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getFrom() {
        return from;
    }

    public List<String> getTo() {
        return to;
    }

    public List<String> getCc() {
        return cc;
    }

    public List<String> getBcc() {
        return bcc;
    }

    public List<String> getReplyTo() {
        return replyTo;
    }

    public String getSubject() {
        return subject;
    }

    public String getText() {
        return text;
    }

    public String getHtml() {
        return html;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public static final class Builder {
        private String from;
        private final List<String> to = new ArrayList<>();
        private final List<String> cc = new ArrayList<>();
        private final List<String> bcc = new ArrayList<>();
        private final List<String> replyTo = new ArrayList<>();
        private String subject;
        private String text;
        private String html;
        private final Map<String, String> headers = new LinkedHashMap<>();
        private final List<Attachment> attachments = new ArrayList<>();

        public Builder from(String from) {
            this.from = from;
            return this;
        }

        public Builder to(String... addresses) {
            for (String a : addresses) {
                this.to.add(a);
            }
            return this;
        }

        public Builder cc(String... addresses) {
            for (String a : addresses) {
                this.cc.add(a);
            }
            return this;
        }

        public Builder bcc(String... addresses) {
            for (String a : addresses) {
                this.bcc.add(a);
            }
            return this;
        }

        public Builder replyTo(String... addresses) {
            for (String a : addresses) {
                this.replyTo.add(a);
            }
            return this;
        }

        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder html(String html) {
            this.html = html;
            return this;
        }

        public Builder header(String name, String value) {
            this.headers.put(name, value);
            return this;
        }

        public Builder attachment(String filename, String contentType, byte[] content) {
            this.attachments.add(new Attachment(filename, contentType, content));
            return this;
        }

        public LockallyMessage build() {
            return new LockallyMessage(this);
        }
    }
}
