/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import OSPRNG.EmpiricPair;
import OSPRNG.EmpiricRNG;
import OSPRNG.ExponentialRNG;
import OSPRNG.UniformDiscreteRNG;
import java.util.Random;

/**
 *
 * @author korenciak.marek
 */
public class CompanyCTimerGenerator implements Generator {

    ExponentialRNG timeGenerator = null;
    EmpiricRNG loadGenerator = null;

    public CompanyCTimerGenerator(Random seedGenerator) {
        loadGenerator
                = new EmpiricRNG(new Random(seedGenerator.nextLong()),
                        new EmpiricPair(new UniformDiscreteRNG(5, 5), 0.0004735),
                        new EmpiricPair(new UniformDiscreteRNG(6, 6), 0.0030777),
                        new EmpiricPair(new UniformDiscreteRNG(7, 7), 0.0037879),
                        new EmpiricPair(new UniformDiscreteRNG(8, 8), 0.0059186),
                        new EmpiricPair(new UniformDiscreteRNG(9, 9), 0.0104167),
                        new EmpiricPair(new UniformDiscreteRNG(10, 10), 0.0073390),
                        new EmpiricPair(new UniformDiscreteRNG(11, 11), 0.0163352),
                        new EmpiricPair(new UniformDiscreteRNG(12, 12), 0.0187027),
                        new EmpiricPair(new UniformDiscreteRNG(13, 13), 0.0184660),
                        new EmpiricPair(new UniformDiscreteRNG(14, 14), 0.0184659),
                        new EmpiricPair(new UniformDiscreteRNG(15, 15), 0.0217803),
                        new EmpiricPair(new UniformDiscreteRNG(16, 16), 0.0229640),
                        new EmpiricPair(new UniformDiscreteRNG(17, 17), 0.0187027),
                        new EmpiricPair(new UniformDiscreteRNG(18, 18), 0.0246212),
                        new EmpiricPair(new UniformDiscreteRNG(19, 19), 0.0262784),
                        new EmpiricPair(new UniformDiscreteRNG(20, 20), 0.0210701),
                        new EmpiricPair(new UniformDiscreteRNG(21, 21), 0.1439394),
                        new EmpiricPair(new UniformDiscreteRNG(22, 22), 0.2057292),
                        new EmpiricPair(new UniformDiscreteRNG(23, 23), 0.2249053),
                        new EmpiricPair(new UniformDiscreteRNG(24, 24), 0.1870265), 
                        new EmpiricPair(new UniformDiscreteRNG(5, 24), 0.1870265));

        timeGenerator = new ExponentialRNG(25.4, new Random(seedGenerator.nextLong()), 1.5);
    }

    @Override
    public double generateTime() {
        return timeGenerator.sample().doubleValue();
    }

    @Override
    public int generateLoad() {
        return loadGenerator.sample().intValue();
    }
}
