package com.nnk.springboot.unit;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nnk.springboot.model.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import com.nnk.springboot.services.TradeService;

@ExtendWith(MockitoExtension.class)
public class TradeServiceTest {

    @Mock
    private TradeRepository tradeRepository;

    public TradeService tradeService;

    Trade defaultTrade;

    @BeforeEach
    public void setup() {
        defaultTrade = new Trade("account", "type", 10.);
        tradeService = new TradeService(tradeRepository);
    }

    @Test
    public void getAllTradeTest() {
        List<Trade> allTrade = new ArrayList<Trade>();
        allTrade.add(defaultTrade);

        when(tradeRepository.findAll()).thenReturn(allTrade);
        tradeService.getAllTrade();

        verify(tradeRepository).findAll();
    }

    @Test
    public void getTradeTest() {
        when(tradeRepository.findById(1)).thenReturn(Optional.of(defaultTrade));
        tradeService.getTrade(1);

        verify(tradeRepository).findById(1);
    }

    @Test
    public void saveTradeTest() {
        tradeService.saveTrade(defaultTrade);

        verify(tradeRepository).save(defaultTrade);
    }

    @Test
    public void deleteTradeTest() {
        tradeService.deleteTrade(defaultTrade);

        verify(tradeRepository).delete(defaultTrade);
    }

}
