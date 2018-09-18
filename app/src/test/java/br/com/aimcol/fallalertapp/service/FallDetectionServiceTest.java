package br.com.aimcol.fallalertapp.service;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.aimcol.fallalertapp.util.AccelerometerAxis;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FallDetectionServiceTest {

    private List<Map<AccelerometerAxis, Double>> accelerometerValues = new ArrayList<>();
    private FallDetectionService fallDetectionService = new FallDetectionService();

    protected void addToList(double x, double y, double z) {
        Map<AccelerometerAxis, Double> map = new HashMap<>();
        map.put(AccelerometerAxis.X, x);
        map.put(AccelerometerAxis.Y, y);
        map.put(AccelerometerAxis.Z, z);
        this.accelerometerValues.add(map);
    }

    /* Regex for preparing the test
        find:
            [0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9], (.*)
        replace:
            this.fallDetectionServiceTest.addToList($1);
     */
    public void prepareForTest() {
        this.accelerometerValues = new ArrayList<>();
    }

    /*
     * Test for dataset pat1
     */
    @Test
    public void testPat1FallFOL_acc_1_1() {
        FallDetectionServiceTestCasePat1 fallDetectionServiceTestCasePat1;
        fallDetectionServiceTestCasePat1 = new FallDetectionServiceTestCasePat1(this);
        fallDetectionServiceTestCasePat1.testPat1FallFOL_acc_1_1();
        assertTrue(this.fallDetectionService.testFallDetection(this.accelerometerValues));
    }

    @Test
    public void testPat1FallFOL_acc_1_2() {
        FallDetectionServiceTestCasePat1 fallDetectionServiceTestCasePat1;
        fallDetectionServiceTestCasePat1 = new FallDetectionServiceTestCasePat1(this);
        fallDetectionServiceTestCasePat1.testPat1FallFOL_acc_1_2();
        assertTrue(this.fallDetectionService.testFallDetection(this.accelerometerValues));
    }

    @Test
    public void testPat1FallFOL_acc_1_3() {
        FallDetectionServiceTestCasePat1 fallDetectionServiceTestCasePat1;
        fallDetectionServiceTestCasePat1 = new FallDetectionServiceTestCasePat1(this);
        fallDetectionServiceTestCasePat1.testPat1FallFOL_acc_1_3();
        assertTrue(this.fallDetectionService.testFallDetection(this.accelerometerValues));
    }


    /*
     * Test for dataset pat2
     */
    @Test
    public void testPat1FallBSC_acc_2_1() {
        FallDetectionServiceTestCasePat2 fallDetectionServiceTestCasePat2;
        fallDetectionServiceTestCasePat2 = new FallDetectionServiceTestCasePat2(this);
        fallDetectionServiceTestCasePat2.testPat1FallBSC_acc_2_1();
        assertTrue(this.fallDetectionService.testFallDetection(this.accelerometerValues));
    }

    @Test
    public void testPat1FallBSC_acc_2_2() {
        FallDetectionServiceTestCasePat2 fallDetectionServiceTestCasePat2;
        fallDetectionServiceTestCasePat2 = new FallDetectionServiceTestCasePat2(this);
        fallDetectionServiceTestCasePat2.testPat1FallBSC_acc_2_2();
        assertTrue(this.fallDetectionService.testFallDetection(this.accelerometerValues));
    }

    @Test
    public void testPat1FallBSC_acc_2_3() {
        FallDetectionServiceTestCasePat2 fallDetectionServiceTestCasePat2;
        fallDetectionServiceTestCasePat2 = new FallDetectionServiceTestCasePat2(this);
        fallDetectionServiceTestCasePat2.testPat1FallBSC_acc_2_3();
        assertTrue(this.fallDetectionService.testFallDetection(this.accelerometerValues));
    }

    @Test
    public void testPat1ADLCSI_acc_2_1() {
        FallDetectionServiceTestCasePat2 fallDetectionServiceTestCasePat2;
        fallDetectionServiceTestCasePat2 = new FallDetectionServiceTestCasePat2(this);
        fallDetectionServiceTestCasePat2.testPat1ADLCSI_acc_2_1();
        assertFalse(this.fallDetectionService.testFallDetection(this.accelerometerValues));
    }

    @Test
    public void testPat1ADLCSI_acc_2_2() {
        FallDetectionServiceTestCasePat2 fallDetectionServiceTestCasePat2;
        fallDetectionServiceTestCasePat2 = new FallDetectionServiceTestCasePat2(this);
        fallDetectionServiceTestCasePat2.testPat1ADLCSI_acc_2_2();
        assertFalse(this.fallDetectionService.testFallDetection(this.accelerometerValues));
    }

    @Test
    public void testPat1ADLCSI_acc_2_3() {
        FallDetectionServiceTestCasePat2 fallDetectionServiceTestCasePat2;
        fallDetectionServiceTestCasePat2 = new FallDetectionServiceTestCasePat2(this);
        fallDetectionServiceTestCasePat2.testPat1ADLCSI_acc_2_3();
        assertFalse(this.fallDetectionService.testFallDetection(this.accelerometerValues));
    }

    @Test
    public void testPat1ADLCSI_acc_2_4() {
        FallDetectionServiceTestCasePat2 fallDetectionServiceTestCasePat2;
        fallDetectionServiceTestCasePat2 = new FallDetectionServiceTestCasePat2(this);
        fallDetectionServiceTestCasePat2.testPat1ADLCSI_acc_2_4();
        assertFalse(this.fallDetectionService.testFallDetection(this.accelerometerValues));
    }

    @Test
    public void testPat1ADLCSI_acc_2_5() {
        FallDetectionServiceTestCasePat2 fallDetectionServiceTestCasePat2;
        fallDetectionServiceTestCasePat2 = new FallDetectionServiceTestCasePat2(this);
        fallDetectionServiceTestCasePat2.testPat1ADLCSI_acc_2_5();
        assertFalse(this.fallDetectionService.testFallDetection(this.accelerometerValues));
    }

    @Test
    public void testPat1ADLCSI_acc_2_6() {
        FallDetectionServiceTestCasePat2 fallDetectionServiceTestCasePat2;
        fallDetectionServiceTestCasePat2 = new FallDetectionServiceTestCasePat2(this);
        fallDetectionServiceTestCasePat2.testPat1ADLCSI_acc_2_6();
        assertFalse(this.fallDetectionService.testFallDetection(this.accelerometerValues));
    }

    @Test
    public void testPat1ADLCSO_acc_2_1() {
        FallDetectionServiceTestCasePat2 fallDetectionServiceTestCasePat2;
        fallDetectionServiceTestCasePat2 = new FallDetectionServiceTestCasePat2(this);
        fallDetectionServiceTestCasePat2.testPat1ADLCSO_acc_2_1();
        assertFalse(this.fallDetectionService.testFallDetection(this.accelerometerValues));
    }

    @Test
    public void testPat1ADLCSO_acc_2_2() {
        FallDetectionServiceTestCasePat2 fallDetectionServiceTestCasePat2;
        fallDetectionServiceTestCasePat2 = new FallDetectionServiceTestCasePat2(this);
        fallDetectionServiceTestCasePat2.testPat1ADLCSO_acc_2_2();
        assertFalse(this.fallDetectionService.testFallDetection(this.accelerometerValues));
    }

    @Test
    public void testPat1ADLCSO_acc_2_3() {
        FallDetectionServiceTestCasePat2 fallDetectionServiceTestCasePat2;
        fallDetectionServiceTestCasePat2 = new FallDetectionServiceTestCasePat2(this);
        fallDetectionServiceTestCasePat2.testPat1ADLCSO_acc_2_3();
        assertFalse(this.fallDetectionService.testFallDetection(this.accelerometerValues));
    }

    @Test
    public void testPat1ADLCSO_acc_2_4() {
        FallDetectionServiceTestCasePat2 fallDetectionServiceTestCasePat2;
        fallDetectionServiceTestCasePat2 = new FallDetectionServiceTestCasePat2(this);
        fallDetectionServiceTestCasePat2.testPat1ADLCSO_acc_2_4();
        assertFalse(this.fallDetectionService.testFallDetection(this.accelerometerValues));
    }

    @Test
    public void testPat1ADLCSO_acc_2_5() {
        FallDetectionServiceTestCasePat2 fallDetectionServiceTestCasePat2;
        fallDetectionServiceTestCasePat2 = new FallDetectionServiceTestCasePat2(this);
        fallDetectionServiceTestCasePat2.testPat1ADLCSO_acc_2_5();
        assertFalse(this.fallDetectionService.testFallDetection(this.accelerometerValues));
    }

    @Test
    public void testPat1ADLCSO_acc_2_6() {
        FallDetectionServiceTestCasePat2 fallDetectionServiceTestCasePat2;
        fallDetectionServiceTestCasePat2 = new FallDetectionServiceTestCasePat2(this);
        fallDetectionServiceTestCasePat2.testPat1ADLCSO_acc_2_6();
        assertFalse(this.fallDetectionService.testFallDetection(this.accelerometerValues));
    }
}
