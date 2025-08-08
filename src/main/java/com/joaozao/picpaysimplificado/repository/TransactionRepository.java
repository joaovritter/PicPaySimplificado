package com.joaozao.picpaysimplificado.repository;

import com.joaozao.picpaysimplificado.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository <Transaction, Long> {
}
