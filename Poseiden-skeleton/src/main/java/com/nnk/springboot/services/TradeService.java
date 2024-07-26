package com.nnk.springboot.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.nnk.springboot.model.Trade;
import com.nnk.springboot.repositories.TradeRepository;

@Service
public class TradeService {

    private final TradeRepository tradeRepository;

    public TradeService(TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
    }

    public List<Trade> getAllTrade() {
        return tradeRepository.findAll();
    }

    public void saveTrade(Trade trade) {
        tradeRepository.save(trade);
    }

    public void deleteTrade(Trade trade) {
        tradeRepository.delete(trade);
    }

    public Optional<Trade> getTrade(Integer id) {
        return tradeRepository.findById(id);
    }

}
