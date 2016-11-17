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
public class CompanyATimerGenerator implements Generator {

    ExponentialRNG timeGenerator = null;
    EmpiricRNG loadGenerator = null;

    public CompanyATimerGenerator(Random seedGenerator) {
        loadGenerator
                = new EmpiricRNG(new Random(seedGenerator.nextLong()),
                        new EmpiricPair(new UniformDiscreteRNG(5, 5), 0.0033085),
                        new EmpiricPair(new UniformDiscreteRNG(6, 6), 0.0128205),
                        new EmpiricPair(new UniformDiscreteRNG(7, 7), 0.0384615),
                        new EmpiricPair(new UniformDiscreteRNG(8, 8), 0.0500414),
                        new EmpiricPair(new UniformDiscreteRNG(9, 9), 0.0624483),
                        new EmpiricPair(new UniformDiscreteRNG(10, 10), 0.0744417),
                        new EmpiricPair(new UniformDiscreteRNG(11, 11), 0.0959471),
                        new EmpiricPair(new UniformDiscreteRNG(12, 12), 0.1066998),
                        new EmpiricPair(new UniformDiscreteRNG(13, 13), 0.1306865),
                        new EmpiricPair(new UniformDiscreteRNG(14, 14), 0.1488834),
                        new EmpiricPair(new UniformDiscreteRNG(15, 15), 0.1600496),
                        new EmpiricPair(new UniformDiscreteRNG(16, 16), 0.115798),
                        new EmpiricPair(new UniformDiscreteRNG(19, 19), 0.0004136),
                        new EmpiricPair(new UniformDiscreteRNG(5, 19), 0.0004136));

        timeGenerator = new ExponentialRNG(45.9, new Random(seedGenerator.nextLong()), 0.999);
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
