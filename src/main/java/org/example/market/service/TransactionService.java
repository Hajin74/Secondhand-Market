package org.example.market.service;

import lombok.RequiredArgsConstructor;
import org.example.market.repository.jpa.OrderJpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final OrderJpaRepository orderJpaRepository;
}
