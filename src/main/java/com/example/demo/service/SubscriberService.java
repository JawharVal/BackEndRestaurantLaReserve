package com.example.demo.service;

import com.example.demo.dto.SubscriberDTO;
import com.example.demo.model.Subscriber;

import java.util.List;
import java.util.Optional;

public interface SubscriberService {
    Subscriber saveSubscriber(SubscriberDTO subscriberDTO);
    List<Subscriber> findAllSubscribers();
    Optional<Subscriber> findSubscriberByEmail(String email);
    void deleteSubscriberByEmail(String email);
    void sendMonthlyEmails();
}
