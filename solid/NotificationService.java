// Interface for notification service - open for extension
public interface NotificationService {

    public void sendOTP(String medium);

    public void sendTransactionReport(String medium);

}