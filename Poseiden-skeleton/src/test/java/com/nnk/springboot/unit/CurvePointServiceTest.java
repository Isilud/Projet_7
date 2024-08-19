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
    private CurvePointRepository bidListRepository;

    public CurvePointService bidListService;

    CurvePoint defaultCurvePoint;

    @BeforeEach
    public void setup() {
        defaultCurvePoint = new CurvePoint(10, 5, 5.);
        bidListService = new CurvePointService(bidListRepository);
    }

    @Test
    public void getAllCurvePointTest() {
        List<CurvePoint> allCurvePoint = new ArrayList<CurvePoint>();
        allCurvePoint.add(defaultCurvePoint);

        when(bidListRepository.findAll()).thenReturn(allCurvePoint);
        bidListService.getAllCurvePoint();

        verify(bidListRepository).findAll();
    }

    @Test
    public void getCurvePointTest() {
        when(bidListRepository.findById(1)).thenReturn(Optional.of(defaultCurvePoint));
        bidListService.getCurvePoint(1);

        verify(bidListRepository).findById(1);
    }

    @Test
    public void saveCurvePointTest() {
        bidListService.saveCurvePoint(defaultCurvePoint);

        verify(bidListRepository).save(defaultCurvePoint);
    }

    @Test
    public void deleteCurvePointTest() {
        bidListService.deleteCurvePoint(defaultCurvePoint);

        verify(bidListRepository).delete(defaultCurvePoint);
    }

}
