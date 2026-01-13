// SOLID Principles - Dependency Inversion Principle Example
// Bad Example: High-level module depends on low-level module
public class EmailServiceBad {
    private final GmailMailerBad mailer;

    public EmailServiceBad() {
        this.mailer = new GmailMailerBad(); // Direct dependency
    }

    public void sendEmail(String message) {
        mailer.send(message);
    }
}

public class GmailMailerBad {
    public void send(String message) {
        System.out.println("Sending via Gmail: " + message);
    }
}

// Good Example: Depends on abstractions
public interface MessageService {
    void sendMessage(String message);
}

public interface Mailer {
    void send(String message);
}

public class GmailMailer implements Mailer {
    @Override
    public void send(String message) {
        System.out.println("Sending via Gmail: " + message);
    }
}

public class OutlookMailer implements Mailer {
    @Override
    public void send(String message) {
        System.out.println("Sending via Outlook: " + message);
    }
}

public class EmailService implements MessageService {
    private final Mailer mailer;

    public EmailService(Mailer mailer) {
        this.mailer = mailer;
    }

    @Override
    public void sendMessage(String message) {
        mailer.send(message);
    }
}

public class NotificationService {
    private final MessageService messageService;

    public NotificationService(MessageService messageService) {
        this.messageService = messageService;
    }

    public void notify(String message) {
        messageService.sendMessage(message);
    }
}

// Factory for creating services
public class ServiceFactory {
    public static NotificationService createNotificationService(Mailer mailer) {
        MessageService emailService = new EmailService(mailer);
        return new NotificationService(emailService);
    }
}