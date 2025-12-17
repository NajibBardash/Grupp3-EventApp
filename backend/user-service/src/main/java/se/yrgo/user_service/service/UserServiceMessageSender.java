package se.yrgo.user_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserServiceMessageSender {

        @Autowired
        private JmsTemplate jmsTemplate;

        public void sendMessage(String message) {
            jmsTemplate.convertAndSend("userServiceQueue", message);
        }
}
