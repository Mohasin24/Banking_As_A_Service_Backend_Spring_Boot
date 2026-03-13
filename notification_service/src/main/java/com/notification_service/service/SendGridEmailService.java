package com.notification_service.service;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SendGridEmailService {

    @Value("${twilio.sendgrid.from-email}")
    private String fromEmail;

    private final SendGrid sendGrid;

    @Autowired
    public SendGridEmailService(SendGrid sendGrid){
        this.sendGrid=sendGrid;
    }

    public void sendSingleEmail(String toEmail, String subject, String body){

        Email from = new Email(this.fromEmail);

        Email to = new Email(toEmail);

        Content content = new Content("text/html",body);
        System.err.println(content.toString());

        Mail mail = new Mail(from,subject,to,content);

        Response response = sendEmail(mail);
        System.err.println(response.getStatusCode());
        System.err.println(mail.getSubject());
//        System.err.println(response.getHeaders());
    }

    /// ///////// Private Methods //////////////

    private Response sendEmail(Mail mail) {
        try {

            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            // perform the request and send the email
            Response response = sendGrid.api(request);
            int statusCode = response.getStatusCode();
            // if the status code is not 2xx
            if (statusCode < 200 || statusCode >= 300) {
                throw new RuntimeException(response.getBody());
            }

            return response;
        } catch (IOException e) {
            // log the error message in case of network failures
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}
