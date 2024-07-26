package com.nnk.springboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nnk.springboot.model.Trade;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Integer> {
}
