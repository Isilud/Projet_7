package com.nnk.springboot.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.nnk.springboot.model.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;

@Service
public class CurvePointService {

    private final CurvePointRepository curvePointRepository;

    public CurvePointService(CurvePointRepository curvePointRepository) {
        this.curvePointRepository = curvePointRepository;
    }

    public List<CurvePoint> getAllCurvePoint() {
        return curvePointRepository.findAll();
    }

    public void saveCurvePoint(CurvePoint curve) {
        curvePointRepository.save(curve);
    }

    public void deleteCurvePoint(CurvePoint curve) {
        curvePointRepository.delete(curve);
    }

    public Optional<CurvePoint> getCurvePoint(Integer id) {
        return curvePointRepository.findById(id);
    }

}
