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

import com.nnk.springboot.model.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import com.nnk.springboot.services.CurvePointService;

@ExtendWith(MockitoExtension.class)
public class CurvePointServiceTest {

    @Mock
    private CurvePointRepository curvePointRepository;

    public CurvePointService curvePointService;

    CurvePoint defaultCurvePoint;

    @BeforeEach
    public void setup() {
        defaultCurvePoint = new CurvePoint(10, 5, 5.);
        curvePointService = new CurvePointService(curvePointRepository);
    }

    @Test
    public void getAllCurvePointTest() {
        List<CurvePoint> allCurvePoint = new ArrayList<CurvePoint>();
        allCurvePoint.add(defaultCurvePoint);

        when(curvePointRepository.findAll()).thenReturn(allCurvePoint);
        curvePointService.getAllCurvePoint();

        verify(curvePointRepository).findAll();
    }

    @Test
    public void getCurvePointTest() {
        when(curvePointRepository.findById(1)).thenReturn(Optional.of(defaultCurvePoint));
        curvePointService.getCurvePoint(1);

        verify(curvePointRepository).findById(1);
    }

    @Test
    public void saveCurvePointTest() {
        curvePointService.saveCurvePoint(defaultCurvePoint);

        verify(curvePointRepository).save(defaultCurvePoint);
    }

    @Test
    public void deleteCurvePointTest() {
        curvePointService.deleteCurvePoint(defaultCurvePoint);

        verify(curvePointRepository).delete(defaultCurvePoint);
    }

}
