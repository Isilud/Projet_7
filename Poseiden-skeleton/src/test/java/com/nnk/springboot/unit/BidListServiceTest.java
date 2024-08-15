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

import com.nnk.springboot.model.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.services.BidListService;

@ExtendWith(MockitoExtension.class)
public class BidListServiceTest {

    @Mock
    private BidListRepository bidListRepository;

    public BidListService bidListService;

    BidList defaultBidList;

    @BeforeEach
    public void setup() {
        defaultBidList = new BidList("defaultAccount", "primary", 5.);
        bidListService = new BidListService(bidListRepository);
    }

    @Test
    public void getAllBidListTest() {
        List<BidList> allBidList = new ArrayList<BidList>();
        allBidList.add(defaultBidList);

        when(bidListRepository.findAll()).thenReturn(allBidList);
        bidListService.getAllBidList();

        verify(bidListRepository).findAll();
    }

    @Test
    public void getBidListTest() {
        when(bidListRepository.findById(1)).thenReturn(Optional.of(defaultBidList));
        bidListService.getBidList(1);

        verify(bidListRepository).findById(1);
    }

    @Test
    public void saveBidListTest() {
        bidListService.saveBidList(defaultBidList);

        verify(bidListRepository).save(defaultBidList);
    }

    @Test
    public void deleteBidListTest() {
        bidListService.deleteBidList(defaultBidList);

        verify(bidListRepository).delete(defaultBidList);
    }

}
