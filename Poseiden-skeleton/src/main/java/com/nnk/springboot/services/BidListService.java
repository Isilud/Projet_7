package com.nnk.springboot.services;

import java.util.List;
import java.util.Optional;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;

public class BidListService {

    private BidListRepository bidListRepository;

    public BidListService(BidListRepository bidListRepository) {
        this.bidListRepository = bidListRepository;
    }

    public List<BidList> getAllBidList() {
        return bidListRepository.findAll();
    }

    public void saveBidList(BidList bid) {
        bidListRepository.save(bid);
    }

    public void deleteBidList(BidList bid) {
        bidListRepository.delete(bid);
    }

    public Optional<BidList> getBidList(Integer id) {
        return bidListRepository.findById(id);
    }

}
