package com.batch.SpringBatchTasklet.util;

import com.batch.SpringBatchTasklet.persistence.entities.Person;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailSending {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(Person person) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        message.setFrom("app@app.com");
        message.setRecipients(MimeMessage.RecipientType.TO, person.getEmail());
        message.setSubject("Account Created Successfully");

        String HTML = """
                <h1>Account Created!</h1>
                <p>
                Dear""" + " " + person.getName() + " " + person.getLastName() + """
                , your account was created successfully, please activate your account 
                 by clicking <a href="http://localhost:8080/auth/activate?id=""" + person.getId() + """
                "\\>here</a></p>
                """;

        message.setContent(HTML, "text/html; charset=utf-8");

        mailSender.send(message);

        log.info("Enviando EMAIL a " + person.getEmail());
    }

}
