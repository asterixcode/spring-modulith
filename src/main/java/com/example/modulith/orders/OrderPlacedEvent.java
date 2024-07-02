package com.example.modulith.orders;

import org.springframework.modulith.events.Externalized;

@Externalized(target = AmqpIntegrationConfiguration.ORDERS_Q)
public record OrderPlacedEvent(int order) {}
