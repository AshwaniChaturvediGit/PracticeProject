package com.springboot.dbank.tradeapp.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.dbank.tradeapp.model.Trade;

@Repository
public interface TradeRepository extends JpaRepository<Trade,String> {
}
