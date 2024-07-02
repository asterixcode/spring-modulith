package com.example.modulith.orders;

import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
class OrdersController {
  private final Orders orders;

  OrdersController(Orders orders) {
    this.orders = orders;
  }

  @PostMapping
  void place(@RequestBody Order order) {
    this.orders.place(order);
  }
}

@Service
@Transactional
class Orders {
  
  private static final Logger log = LoggerFactory.getLogger(Orders.class);

  private final OrdersRepository ordersRepository;
  private final ApplicationEventPublisher publisher;

  Orders(OrdersRepository ordersRepository, ApplicationEventPublisher publisher) {
    this.ordersRepository = ordersRepository;
    this.publisher = publisher;
  }

  void place (Order order) {
    var saved = this.ordersRepository.save(order);
    log.info("Order saved: [ {} ]", saved);

    this.publisher.publishEvent(new OrderPlacedEvent(saved.id()));
  }
}

@Repository
interface OrdersRepository extends ListCrudRepository<Order, Integer> {}

@Table("orders")
record Order (@Id Integer id, Set<LineItem> lineItems){}

@Table("orders_line_items")
record LineItem (@Id Integer id, int quantity, int product) {}

@Configuration
class AmqpIntegrationConfiguration {

  static final String ORDERS_Q = "orders";

  @Bean
  Binding binding(Queue queue, Exchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with(ORDERS_Q).noargs();
  }

  @Bean
  Exchange exchange () {
    return ExchangeBuilder.directExchange(ORDERS_Q).build();
  }

  @Bean
  Queue queue() {
    return QueueBuilder.durable(ORDERS_Q).build();
  }
}
