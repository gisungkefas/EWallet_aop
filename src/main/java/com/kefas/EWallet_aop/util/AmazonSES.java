package com.kefas.EWallet_aop.util;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AmazonSES {
    //From env variables
    @Value("${awsAccessKeyId}")
    private String awsAccessKeyId;

    @Value("${awsSecretKey}")
    private String awsSecretKey;

    // This address must be verified with Amazon SES.
    final String FROM = "sikabeth2supply@gmail.com";

    // The HTML body for the email.
    final String HTMLBODY = """
            <h1>Please verify your email address</h1><p>Welcome to Sikabeth Wallet. To complete registration process and be able to log in, click on the following link: <a href='http://localhost:8086/api/v1/user/activate?token=$tokenValue'>Final step to complete your registration</a><br/><br/>\s
            $tokenValue
                        
            Thank you!""";

    // The email body for recipients with non-HTML email clients.
    final String TEXTBODY = """
            Please verify your email address. Welcome to Sikabeth Wallet. To complete registration process and be able to log in, open then the following URL in your browser window:  http://localhost:8086/api/v1/user/activate?token=$tokenValue\s
            $tokenValue
                        
            Thank you!""";

    final String PASSWORD_RESET_HTMLBODY = """
            <h1>A request to reset your password</h1><p>Hi, $firstName!</p> <p>Someone has requested to reset your password with Sample School. If it were not you, please ignore it. otherwise please click on the link below to set a new password: <a href='http://localhost:8086/api/v1/user/password-reset?token=$tokenValue'> Click this link to Reset Password</a><br/><br/>\s
            $tokenValue
                        
            Thank you!""";

    // The email body for recipients with non-HTML email clients.
    final String PASSWORD_RESET_TEXTBODY = """
            A request to reset your password Hi, $firstName! Someone has requested to reset your password with Sample School. If it were not you, please ignore it. otherwise please open the link below in your browser window to set a new password: https://http://localhost:8086/api/v1/user/password-reset?token=$tokenValue\s
            $tokenValue
                        
            Thank you!""";


    public void verifyEmail(String token, String email) {
        awsMasked();

        String SUBJECT = "One last step to complete your registration with Sikabeth Wallet";
        String htmlBodyWithToken = HTMLBODY.replace("$tokenValue", token);
        String textBodyWithToken = TEXTBODY.replace("$tokenValue", token);

        sendEmailResult(SUBJECT, htmlBodyWithToken, textBodyWithToken, email);

        System.out.println("Email sent!");

    }

    private void awsMasked() {
        // You can also set your keys this way. And it will work!
        System.setProperty("aws.accessKeyId", awsAccessKeyId);
        System.setProperty("aws.secretKey", awsSecretKey);
    }

    public boolean sendPasswordResetRequest(String firstName, String email, String token) {
        awsMasked();
        boolean returnValue = false;

        String PASSWORD_RESET_SUBJECT = "Password reset request";
        String htmlBodyWithToken = PASSWORD_RESET_HTMLBODY.replace("$tokenValue", token);
        htmlBodyWithToken = htmlBodyWithToken.replace("$firstName", firstName);

        String textBodyWithToken = PASSWORD_RESET_TEXTBODY.replace("$tokenValue", token);
        textBodyWithToken = textBodyWithToken.replace("$firstName", firstName);

        SendEmailResult result = sendEmailResult(PASSWORD_RESET_SUBJECT, htmlBodyWithToken, textBodyWithToken, email);

        if (result != null && (result.getMessageId() != null && !result.getMessageId().isEmpty())) {
            returnValue = true;
        }

        return returnValue;
    }

    public boolean sendTransaction(String message, String email) {
        awsMasked();
        boolean returnValue = false;

        SendEmailResult result = sendEmailResult("Transaction Update", message, message, email);

        if (result != null && (result.getMessageId() != null && !result.getMessageId().isEmpty())) {
            returnValue = true;
        }

        return returnValue;
    }

    private SendEmailResult sendEmailResult(String subject, String messageHtml, String messageText, String to) {
        SendEmailRequest request = new SendEmailRequest()
                .withDestination(
                        new Destination().withToAddresses(to))
                .withMessage(new Message()
                        .withBody(new Body()
                                .withHtml(new Content()
                                        .withCharset("UTF-8").withData(messageHtml))
                                .withText(new Content()
                                        .withCharset("UTF-8").withData(messageText)))
                        .withSubject(new Content()
                                .withCharset("UTF-8").withData(subject)))
                .withSource(FROM);

        return client().sendEmail(request);
    }

    private AmazonSimpleEmailService client() {
        return AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.US_EAST_2).build();
    }

}