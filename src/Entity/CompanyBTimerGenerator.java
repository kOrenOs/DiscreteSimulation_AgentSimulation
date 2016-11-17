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
public class CompanyBTimerGenerator implements Generator {

    ExponentialRNG timeGenerator = null;
    EmpiricRNG loadGenerator = null;

    public CompanyBTimerGenerator(Random seedGenerator) {
        loadGenerator = new EmpiricRNG(new Random(seedGenerator.nextLong()),
                new EmpiricPair(new UniformDiscreteRNG(6, 6), 0.0003509),
                new EmpiricPair(new UniformDiscreteRNG(7, 7), 0.0010526),
                new EmpiricPair(new UniformDiscreteRNG(8, 8), 0.0028070),
                new EmpiricPair(new UniformDiscreteRNG(9, 9), 0.0031579),
                new EmpiricPair(new UniformDiscreteRNG(10, 10), 0.0045614),
                new EmpiricPair(new UniformDiscreteRNG(11, 11), 0.0073684),
                new EmpiricPair(new UniformDiscreteRNG(12, 12), 0.0070175),
                new EmpiricPair(new UniformDiscreteRNG(13, 13), 0.0077193),
                new EmpiricPair(new UniformDiscreteRNG(14, 14), 0.0115789),
                new EmpiricPair(new UniformDiscreteRNG(15, 15), 0.0105263),
                new EmpiricPair(new UniformDiscreteRNG(16, 16), 0.0638596),
                new EmpiricPair(new UniformDiscreteRNG(17, 17), 0.1828070),
                new EmpiricPair(new UniformDiscreteRNG(18, 18), 0.1940351),
                new EmpiricPair(new UniformDiscreteRNG(19, 19), 0.2098246),
                new EmpiricPair(new UniformDiscreteRNG(20, 20), 0.2224561),
                new EmpiricPair(new UniformDiscreteRNG(21, 21), 0.0708772),
                new EmpiricPair(new UniformDiscreteRNG(6, 21), 0.0708772));

        timeGenerator = new ExponentialRNG(39.1, new Random(seedGenerator.nextLong()), 0.700);
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
