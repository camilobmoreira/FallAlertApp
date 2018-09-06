package br.com.aimcol.fallalertapp.service;

import org.junit.Test;

import static org.junit.Assert.*;

public class FallDetectionServiceTest {

    FallDetectionService fallDetectionService = new FallDetectionService();

    @Test
    public void testPart1FOL() {
        assertTrue(this.fallDetectionService.isFallDetected(0.90021986, -9.557653, -1.4939818));
        assertTrue(this.fallDetectionService.isFallDetected(0.7565677, -9.5385, -1.13964));
        assertTrue(this.fallDetectionService.isFallDetected(1.0151415, -9.490616, -1.292869));
        assertTrue(this.fallDetectionService.isFallDetected(0.9768343, -9.490616, -1.4556746));
        assertTrue(this.fallDetectionService.isFallDetected(0.9864111, -9.643845, -1.340753));
        assertTrue(this.fallDetectionService.isFallDetected(1.0726024, -9.959879, -1.1779473));
        assertTrue(this.fallDetectionService.isFallDetected(1.1779473, -10.055647, -0.33518824));
        assertTrue(this.fallDetectionService.isFallDetected(1.1300632, -9.969456, -0.36391866));
        assertTrue(this.fallDetectionService.isFallDetected(1.1013328, -9.959879, -0.30645782));
        assertTrue(this.fallDetectionService.isFallDetected(1.1779473, -10.0748005, -0.23942018));
        assertTrue(this.fallDetectionService.isFallDetected(1.283292, -10.208876, -0.2873042));
        assertTrue(this.fallDetectionService.isFallDetected(1.4269443, -10.314221, -0.45968673));
        assertTrue(this.fallDetectionService.isFallDetected(1.532289, -10.362105, -0.5650316));
        assertTrue(this.fallDetectionService.isFallDetected(1.6472107, -10.25676, -0.82360536));
        assertTrue(this.fallDetectionService.isFallDetected(1.7525556, -10.0748005, -1.0630256));
        assertTrue(this.fallDetectionService.isFallDetected(1.7525556, -9.797073, -1.3599066));
        assertTrue(this.fallDetectionService.isFallDetected(1.7717092, -9.32781, -1.6472107));
        assertTrue(this.fallDetectionService.isFallDetected(1.484405, -8.90643, -1.8291701));
        assertTrue(this.fallDetectionService.isFallDetected(1.1492168, -8.7053175, -1.8387469));
        assertTrue(this.fallDetectionService.isFallDetected(0.79487497, -8.791509, -1.8962077));
        assertTrue(this.fallDetectionService.isFallDetected(0.40222588, -9.212888, -2.1452048));
        assertTrue(this.fallDetectionService.isFallDetected(0.5267244, -9.346964, -2.4995465));
        assertTrue(this.fallDetectionService.isFallDetected(0.6991069, -9.624691, -2.585738));
        assertTrue(this.fallDetectionService.isFallDetected(1.0055647, -9.892841, -2.5187001));
        assertTrue(this.fallDetectionService.isFallDetected(1.1971009, -9.883265, -2.2697031));
        assertTrue(this.fallDetectionService.isFallDetected(1.1779473, -9.548077, -1.9632454));
        assertTrue(this.fallDetectionService.isFallDetected(1.2066777, -9.260772, -1.685518));
        assertTrue(this.fallDetectionService.isFallDetected(1.2354081, -9.174581, -1.3120226));
        assertTrue(this.fallDetectionService.isFallDetected(1.2162545, -9.203311, -0.90979666));
        assertTrue(this.fallDetectionService.isFallDetected(1.2066777, -9.203311, -0.7757214));
        assertTrue(this.fallDetectionService.isFallDetected(1.1492168, -9.212888, -0.93852705));
        assertTrue(this.fallDetectionService.isFallDetected(1.0630256, -9.212888, -1.6567876));
        assertTrue(this.fallDetectionService.isFallDetected(1.0630256, -9.212888, -1.9249382));
        assertTrue(this.fallDetectionService.isFallDetected(1.091756, -9.126697, -2.1547816));
        assertTrue(this.fallDetectionService.isFallDetected(1.1300632, -9.136273, -2.2026656));
        assertTrue(this.fallDetectionService.isFallDetected(1.13964, -9.14585, -2.087744));
        assertTrue(this.fallDetectionService.isFallDetected(1.2162545, -9.174581, -1.8387469));
        assertTrue(this.fallDetectionService.isFallDetected(1.3215994, -9.251195, -1.5897499));
        assertTrue(this.fallDetectionService.isFallDetected(1.4269443, -9.375694, -1.3024458));
        assertTrue(this.fallDetectionService.isFallDetected(1.436521, -9.500193, -0.92895025));
        assertTrue(this.fallDetectionService.isFallDetected(1.5227122, -9.682152, -0.4309563));
        assertTrue(this.fallDetectionService.isFallDetected(1.5610195, -9.739613, -0.2873042));
        assertTrue(this.fallDetectionService.isFallDetected(1.5897499, -9.77792, -0.15322891));
        assertTrue(this.fallDetectionService.isFallDetected(1.6376339, -9.787497, -0.05746084));
        assertTrue(this.fallDetectionService.isFallDetected(1.733402, -9.80665, 0.009576807));
        assertTrue(this.fallDetectionService.isFallDetected(1.7908629, -9.80665, 0.019153614));
        assertTrue(this.fallDetectionService.isFallDetected(1.8483237, -9.825804, 0.076614454));
        assertTrue(this.fallDetectionService.isFallDetected(1.8483237, -9.739613, 0.1340753));
        assertTrue(this.fallDetectionService.isFallDetected(1.8483237, -9.672575, 0.21068975));
        assertTrue(this.fallDetectionService.isFallDetected(1.8387469, -9.586384, 0.32561144));
        assertTrue(this.fallDetectionService.isFallDetected(1.9632454, -9.56723, 0.4309563));
        assertTrue(this.fallDetectionService.isFallDetected(2.0685902, -9.56723, 0.51714754));
        assertTrue(this.fallDetectionService.isFallDetected(2.231396, -9.5385, 0.7565677));
        assertTrue(this.fallDetectionService.isFallDetected(2.4133554, -9.586384, 0.93852705));
        assertTrue(this.fallDetectionService.isFallDetected(2.633622, -9.595961, 1.0821792));
        assertTrue(this.fallDetectionService.isFallDetected(2.9879637, -9.643845, 1.3024458));
        assertTrue(this.fallDetectionService.isFallDetected(3.1411927, -9.691729, 1.4269443));
        assertTrue(this.fallDetectionService.isFallDetected(3.1411927, -9.643845, 1.5705963));
        assertTrue(this.fallDetectionService.isFallDetected(3.217807, -9.691729, 1.6472107));
        assertTrue(this.fallDetectionService.isFallDetected(3.2465374, -9.662998, 1.6280571));
        assertTrue(this.fallDetectionService.isFallDetected(3.217807, -9.605537, 1.6089035));
        assertTrue(this.fallDetectionService.isFallDetected(3.016694, -9.337387, 1.4269443));
        assertTrue(this.fallDetectionService.isFallDetected(2.7868507, -8.925584, 1.2354081));
        assertTrue(this.fallDetectionService.isFallDetected(2.4995465, -8.820239, 0.94810385));
        assertTrue(this.fallDetectionService.isFallDetected(2.030283, -8.236053, 0.6799533));
        assertTrue(this.fallDetectionService.isFallDetected(1.5610195, -7.747637, 0.36391866));
        assertTrue(this.fallDetectionService.isFallDetected(1.1109096, -6.9144545, 0.05746084));
        assertTrue(this.fallDetectionService.isFallDetected(0.41180268, -5.315128, -0.32561144));
        assertTrue(this.fallDetectionService.isFallDetected(0.23942018, -4.453215, -0.58418524));
        assertTrue(this.fallDetectionService.isFallDetected(0.12449849, -3.2752678, -0.80445176));
        assertTrue(this.fallDetectionService.isFallDetected(0.10534488, -2.135628, -1.1492168));
        assertTrue(this.fallDetectionService.isFallDetected(0.2873042, -1.5610195, -1.484405));
        assertTrue(this.fallDetectionService.isFallDetected(0.6703765, -1.5514427, -1.8100165));
        assertTrue(this.fallDetectionService.isFallDetected(1.0726024, -1.7429788, -2.4037786));
        assertTrue(this.fallDetectionService.isFallDetected(1.4460979, -2.6431987, -2.815581));
        assertTrue(this.fallDetectionService.isFallDetected(1.5610195, -3.5817258, -3.169923));
        assertTrue(this.fallDetectionService.isFallDetected(1.091756, -4.673482, -2.9975405));
        assertTrue(this.fallDetectionService.isFallDetected(0.6512229, -6.8761473, -3.0358477));
        assertTrue(this.fallDetectionService.isFallDetected(0.51714754, -7.843405, -2.8060043));
        assertTrue(this.fallDetectionService.isFallDetected(0.47884035, -8.829816, -2.5761611));
        assertTrue(this.fallDetectionService.isFallDetected(0.5363012, -10.065224, -2.2984335));
        assertTrue(this.fallDetectionService.isFallDetected(0.7469909, -11.722012, -2.4037786));
        assertTrue(this.fallDetectionService.isFallDetected(0.8523358, -13.091495, -2.2026656));
        assertTrue(this.fallDetectionService.isFallDetected(0.90021986, -14.068329, -1.5610195));
        assertTrue(this.fallDetectionService.isFallDetected(0.90021986, -14.757859, -0.9959879));
        assertTrue(this.fallDetectionService.isFallDetected(1.0247183, -15.02601, -0.8714894));
        assertTrue(this.fallDetectionService.isFallDetected(1.1779473, -14.978126, -0.9576807));
        assertTrue(this.fallDetectionService.isFallDetected(1.340753, -14.930242, -1.2354081));
        assertTrue(this.fallDetectionService.isFallDetected(1.3311762, -14.978126, -1.5514427));
        assertTrue(this.fallDetectionService.isFallDetected(1.5131354, -14.757859, -2.1739352));
        assertTrue(this.fallDetectionService.isFallDetected(1.4652514, -14.451402, -2.432509));
        assertTrue(this.fallDetectionService.isFallDetected(1.2162545, -13.666103, -2.3750482));
        assertTrue(this.fallDetectionService.isFallDetected(1.0055647, -13.417107, -2.2218192));
        assertTrue(this.fallDetectionService.isFallDetected(0.7182605, -13.148955, -2.0111294));
        assertTrue(this.fallDetectionService.isFallDetected(0.48841715, -12.708423, -2.0207062));
        assertTrue(this.fallDetectionService.isFallDetected(0.2873042, -12.181698, -2.1068976));
        assertTrue(this.fallDetectionService.isFallDetected(0.17238252, -11.473015, -2.2122424));
        assertTrue(this.fallDetectionService.isFallDetected(0.095768064, -10.745177, -2.4037786));
        assertTrue(this.fallDetectionService.isFallDetected(0.10534488, -10.036493, -2.681506));
        assertTrue(this.fallDetectionService.isFallDetected(0.11492168, -9.222465, -2.9400797));
    }

    @Test
    public void testPart2CSO() {
        assertFalse(this.fallDetectionService.isFallDetected(1.2641385, -1.7525556, 9.490616));
        assertFalse(this.fallDetectionService.isFallDetected(1.6950948, -1.733402, 8.992621));
        assertFalse(this.fallDetectionService.isFallDetected(1.6950948, -1.9153614, 9.11712));
        assertFalse(this.fallDetectionService.isFallDetected(1.4077905, -2.0015526, 9.251195));
        assertFalse(this.fallDetectionService.isFallDetected(0.90979666, -1.541866, 9.519346));
        assertFalse(this.fallDetectionService.isFallDetected(2.432509, -2.6527755, 11.281479));
        assertFalse(this.fallDetectionService.isFallDetected(2.2601264, -3.1124623, 10.5440645));
        assertFalse(this.fallDetectionService.isFallDetected(2.078167, -3.2752678, 9.586384));
        assertFalse(this.fallDetectionService.isFallDetected(2.1739352, -3.2752678, 8.896853));
        assertFalse(this.fallDetectionService.isFallDetected(2.27928, -3.1124623, 8.63828));
        assertFalse(this.fallDetectionService.isFallDetected(2.1643584, -2.9017725, 8.848969));
        assertFalse(this.fallDetectionService.isFallDetected(2.135628, -2.767697, 9.021352));
        assertFalse(this.fallDetectionService.isFallDetected(2.3558946, -2.7964275, 9.097966));
        assertFalse(this.fallDetectionService.isFallDetected(3.3901896, -3.4093432, 8.781932));
        assertFalse(this.fallDetectionService.isFallDetected(3.3135753, -4.060566, 8.695741));
        assertFalse(this.fallDetectionService.isFallDetected(3.1411927, -4.5202527, 8.676587));
        assertFalse(this.fallDetectionService.isFallDetected(2.7964275, -4.7022123, 8.7053175));
        assertFalse(this.fallDetectionService.isFallDetected(2.480393, -4.510676, 8.848969));
        assertFalse(this.fallDetectionService.isFallDetected(2.1547816, -4.0701427, 9.069236));
        assertFalse(this.fallDetectionService.isFallDetected(1.733402, -3.6008794, 9.251195));
        assertFalse(this.fallDetectionService.isFallDetected(1.1875241, -3.2082303, 9.021352));
        assertFalse(this.fallDetectionService.isFallDetected(0.60333884, -3.122039, 8.714894));
        assertFalse(this.fallDetectionService.isFallDetected(-0.1340753, -3.6391866, 8.66701));
        assertFalse(this.fallDetectionService.isFallDetected(-0.30645782, -4.0701427, 9.069236));
        assertFalse(this.fallDetectionService.isFallDetected(-0.33518824, -4.3861775, 9.203311));
        assertFalse(this.fallDetectionService.isFallDetected(-0.17238252, -4.711789, 9.241618));
        assertFalse(this.fallDetectionService.isFallDetected(-0.019153614, -4.8267107, 9.069236));
        assertFalse(this.fallDetectionService.isFallDetected(0.45968673, -4.7405195, 8.350976));
        assertFalse(this.fallDetectionService.isFallDetected(0.63206923, -4.5777135, 8.073248));
        assertFalse(this.fallDetectionService.isFallDetected(0.842759, -4.5298295, 7.9870567));
        assertFalse(this.fallDetectionService.isFallDetected(1.2354081, -4.4436383, 7.872135));
        assertFalse(this.fallDetectionService.isFallDetected(1.3694834, -4.453215, 7.8146744));
        assertFalse(this.fallDetectionService.isFallDetected(1.4173675, -4.654328, 7.7189064));
        assertFalse(this.fallDetectionService.isFallDetected(1.436521, -5.0278234, 7.0581064));
        assertFalse(this.fallDetectionService.isFallDetected(1.3790601, -5.535394, 6.655881));
        assertFalse(this.fallDetectionService.isFallDetected(1.3024458, -5.6790466, 6.397307));
        assertFalse(this.fallDetectionService.isFallDetected(1.733402, -5.363012, 5.9567738));
        assertFalse(this.fallDetectionService.isFallDetected(1.6950948, -5.1235914, 5.899313));
        assertFalse(this.fallDetectionService.isFallDetected(2.030283, -5.535394, 5.4779334));
        assertFalse(this.fallDetectionService.isFallDetected(1.9919758, -6.1100025, 5.4300494));
        assertFalse(this.fallDetectionService.isFallDetected(2.1068976, -6.4739213, 5.1906295));
        assertFalse(this.fallDetectionService.isFallDetected(2.3750482, -6.7133417, 4.6255975));
        assertFalse(this.fallDetectionService.isFallDetected(2.5187001, -7.2687964, 3.620033));
        assertFalse(this.fallDetectionService.isFallDetected(2.5761611, -7.4028716, 3.1603463));
        assertFalse(this.fallDetectionService.isFallDetected(2.4612393, -7.4507556, 2.9592333));
        assertFalse(this.fallDetectionService.isFallDetected(2.1260512, -7.642292, 3.0358477));
        assertFalse(this.fallDetectionService.isFallDetected(1.8004397, -7.929596, 3.3039982));
        assertFalse(this.fallDetectionService.isFallDetected(1.5035586, -8.207323, 3.7349546));
        assertFalse(this.fallDetectionService.isFallDetected(1.5514427, -8.101978, 3.323152));
        assertFalse(this.fallDetectionService.isFallDetected(1.7525556, -8.00621, 2.6623523));
        assertFalse(this.fallDetectionService.isFallDetected(2.0015526, -7.97748, 2.4516625));
        assertFalse(this.fallDetectionService.isFallDetected(2.3080103, -8.063671, 2.8060043));
        assertFalse(this.fallDetectionService.isFallDetected(2.6048915, -8.24563, 3.3039982));
        assertFalse(this.fallDetectionService.isFallDetected(2.5761611, -8.619126, 3.4955344));
        assertFalse(this.fallDetectionService.isFallDetected(2.1739352, -8.954314, 3.41892));
        assertFalse(this.fallDetectionService.isFallDetected(1.5993267, -8.983045, 3.2273839));
        assertFalse(this.fallDetectionService.isFallDetected(1.2737153, -8.599973, 3.41892));
        assertFalse(this.fallDetectionService.isFallDetected(1.1587936, -8.073248, 4.0318356));
        assertFalse(this.fallDetectionService.isFallDetected(1.1971009, -7.6710224, 4.5872903));
        assertFalse(this.fallDetectionService.isFallDetected(1.4939818, -7.2209125, 4.855441));
        assertFalse(this.fallDetectionService.isFallDetected(2.0111294, -6.8186865, 4.912902));
        assertFalse(this.fallDetectionService.isFallDetected(2.4037786, -6.397307, 4.9703627));
        assertFalse(this.fallDetectionService.isFallDetected(2.3942018, -6.215348, 5.0661306));
        assertFalse(this.fallDetectionService.isFallDetected(1.8195933, -6.0716953, 5.085284));
        assertFalse(this.fallDetectionService.isFallDetected(0.7374141, -5.889736, 5.0757074));
        assertFalse(this.fallDetectionService.isFallDetected(-0.47884035, -5.401319, 5.161899));
        assertFalse(this.fallDetectionService.isFallDetected(-1.7238252, -4.7022123, 5.506664));
        assertFalse(this.fallDetectionService.isFallDetected(-2.432509, -3.3327289, 5.9759274));
        assertFalse(this.fallDetectionService.isFallDetected(-2.231396, -0.22026655, 6.4739213));
        assertFalse(this.fallDetectionService.isFallDetected(-2.231396, -0.22026655, 6.4739213));
        assertFalse(this.fallDetectionService.isFallDetected(-1.5705963, 0.05746084, 8.408437));
        assertFalse(this.fallDetectionService.isFallDetected(-2.2984335, -0.8619126, 10.955867));
        assertFalse(this.fallDetectionService.isFallDetected(-3.4093432, -1.388637, 13.273455));
        assertFalse(this.fallDetectionService.isFallDetected(-4.5681367, -1.5993267, 13.40753));
        assertFalse(this.fallDetectionService.isFallDetected(-5.0374002, -1.2737153, 11.281479));
        assertFalse(this.fallDetectionService.isFallDetected(-5.439626, -0.4405331, 8.839393));
        assertFalse(this.fallDetectionService.isFallDetected(-5.046977, 0.6895301, 8.034941));
        assertFalse(this.fallDetectionService.isFallDetected(-3.7349546, 1.5705963, 9.442732));
        assertFalse(this.fallDetectionService.isFallDetected(-0.22984336, 1.8100165, 14.939818));
        assertFalse(this.fallDetectionService.isFallDetected(1.0630256, 1.484405, 15.907076));
        assertFalse(this.fallDetectionService.isFallDetected(1.3311762, 0.9576807, 14.738706));
        assertFalse(this.fallDetectionService.isFallDetected(0.10534488, 0.08619126, 13.196839));
        assertFalse(this.fallDetectionService.isFallDetected(-0.8714894, -0.08619126, 11.616667));
        assertFalse(this.fallDetectionService.isFallDetected(-1.1587936, 0.45010993, 10.352529));
        assertFalse(this.fallDetectionService.isFallDetected(-0.90979666, 0.9959879, 9.9790325));
        assertFalse(this.fallDetectionService.isFallDetected(-0.40222588, 1.3694834, 10.601525));
        assertFalse(this.fallDetectionService.isFallDetected(0.22984336, 1.388637, 11.434708));
        assertFalse(this.fallDetectionService.isFallDetected(0.6607997, 1.3215994, 12.258312));
        assertFalse(this.fallDetectionService.isFallDetected(0.8619126, 1.3599066, 12.545617));
        assertFalse(this.fallDetectionService.isFallDetected(0.62249243, 1.1971009, 11.109096));
        assertFalse(this.fallDetectionService.isFallDetected(-0.6991069, 0.58418524, 6.588843));
        assertFalse(this.fallDetectionService.isFallDetected(-1.2354081, 0.79487497, 6.83784));
        assertFalse(this.fallDetectionService.isFallDetected(-1.3790601, 1.2737153, 8.791509));
        assertFalse(this.fallDetectionService.isFallDetected(-1.484405, 2.0111294, 11.089942));
        assertFalse(this.fallDetectionService.isFallDetected(-0.9959879, 2.882619, 13.819332));
        assertFalse(this.fallDetectionService.isFallDetected(0.10534488, 3.715801, 16.682798));
        assertFalse(this.fallDetectionService.isFallDetected(0.5267244, 3.1507695, 16.567875));
        assertFalse(this.fallDetectionService.isFallDetected(-0.58418524, 1.9919758, 11.60709));
        assertFalse(this.fallDetectionService.isFallDetected(-1.3694834, 1.2162545, 5.583278));
        assertFalse(this.fallDetectionService.isFallDetected(-1.6567876, 0.31603462, 2.6719291));
    }
}
