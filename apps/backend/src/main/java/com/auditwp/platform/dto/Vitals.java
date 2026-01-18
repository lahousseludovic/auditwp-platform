package com.auditwp.platform.dto;

import lombok.Data;

@Data
public class Vitals {

    /**
     * First Contentful Paint (ms)
     */
    private double fcp;

    /**
     * Largest Contentful Paint (ms)
     */
    private double lcp;

    /**
     * Cumulative Layout Shift (score)
     */
    private double cls;

    /**
     * Total Blocking Time (ms)
     */
    private double tbt;

    /**
     * Speed Index (ms)
     */
    private double speedIndex;

    /**
     * Time To Interactive (ms)
     */
    private double tti;
}
