package com.example.modulith.products;

import com.example.modulith.orders.OrderPlacedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

@Service
public class Products {

  private static final Logger log = LoggerFactory.getLogger(Products.class);

  @ApplicationModuleListener
  void on(OrderPlacedEvent ope) throws InterruptedException {
    log.info("starting [ {} ]", ope);
    Thread.sleep(10_000);
    log.info("stopping [ {} ]", ope);
  }
}
