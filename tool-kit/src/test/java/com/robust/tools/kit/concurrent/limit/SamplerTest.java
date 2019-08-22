package com.robust.tools.kit.concurrent.limit;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/8/1 9:54
 * @Version: 1.0
 */
public class SamplerTest {

    @Test
    public void select() {
        Sampler sampler = Sampler.create(5.5);
        int hits = 0;
        for (int i = 0; i < 10000; i++) {
            if (sampler.select()) {
                hits++;
            }
        }

        System.out.println("sample 5.5% in 10000 hits should close to 550, actual is " + hits);
        assertThat(hits).isBetween(500, 700);

        Sampler sampler2 = Sampler.create(0.5);
        hits = 0;
        for (int i = 0; i < 10000; i++) {
            if (sampler2.select()) {
                hits++;
            }
        }

        System.out.println("sample 0.5% in 10000 hits should close to 50, actual is " + hits);
        assertThat(hits).isBetween(20, 100);
    }

    @Test
    public void always() {
        Sampler sampler = Sampler.create(100.0);
        assertThat(sampler).isInstanceOf(Sampler.AlwaysSampler.class);
        int hits = 0;
        for (int i = 0; i < 10000; i++) {
            if (sampler.select()) {
                hits++;
            }
        }

        System.out.println("sample 100% in 10000 hits should be 10000, actual is " + hits);
        assertThat(hits).isEqualTo(10000);

        sampler = Sampler.create(0.0);
        assertThat(sampler).isInstanceOf(Sampler.NeverSampler.class);
        hits = 0;
        for (int i = 0; i < 10000; i++) {
            if (sampler.select()) {
                hits++;
            }
        }

        System.out.println("sample 0% in 10000 hits should be 0, actual is " + hits);
        assertThat(hits).isEqualTo(0);

        try {
            sampler = Sampler.create(-3.0);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }
}