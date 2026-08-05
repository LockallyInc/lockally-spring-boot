# Lockally Spring Boot Starter

Official [Lockally](https://lockally.com) Spring Boot starter. Add the dependency, set
your API key, and inject `LockallyMailSender` to send transactional email.

## Install

Maven:

```xml
<dependency>
  <groupId>com.lockally</groupId>
  <artifactId>lockally-spring-boot-starter</artifactId>
  <version>0.1.0</version>
</dependency>
```

Gradle:

```groovy
implementation "com.lockally:lockally-spring-boot-starter:0.1.0"
```

## Configure

```properties
# application.properties
lockally.api-key=lk_live_xxx
# lockally.api-key=lk_test_xxx   # sandbox — no real send
# lockally.base-url=https://api.lockally.com   # optional override
```

The starter auto-configures a `LockallyMailSender` bean whenever `lockally.api-key` is set.

## Send

```java
import com.lockally.spring.LockallyMailSender;
import com.lockally.spring.LockallyMessage;
import org.springframework.stereotype.Service;

@Service
public class SignupService {
    private final LockallyMailSender mailer;

    public SignupService(LockallyMailSender mailer) {
        this.mailer = mailer;
    }

    public void welcome(String email) throws Exception {
        mailer.send(LockallyMessage.builder()
                .from("alerts@yourdomain.com")
                .to(email)
                .replyTo("support@yourdomain.com")
                .subject("Welcome")
                .text("Thanks for signing up.")
                .html("<b>Thanks for signing up.</b>")
                .build());
    }
}
```

### Mapping notes

- **Reply-To** is sent as a header (Lockally has no `reply_to` field).
- **Attachments** are base64-encoded as `{ filename, contentType, contentBase64 }`.
- One `Idempotency-Key` is generated per send (24 h dedupe window).

## License

MIT © Lockally
