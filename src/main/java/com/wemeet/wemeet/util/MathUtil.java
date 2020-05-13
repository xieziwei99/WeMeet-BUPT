package com.wemeet.wemeet.util;

/**
 * @author xieziwei99
 * 2020-05-13
 */
public class MathUtil {

    /**
     * 不精确，依据是 1 度大约是这么些千米
     *
     * @param meter 单位是米
     * @return 单位是度
     */
    public static double meterToLatitude(double meter) {
        double longitude = meterToLongitude(meter) * Math.PI / 180;
        return meter / 1000 / (111.320 * Math.cos(longitude));
    }

    /**
     * 不精确，依据是 1 度大约是这么些千米
     *
     * @param meter 单位是米
     * @return 单位是度
     */
    public static double meterToLongitude(double meter) {
        return meter / 1000 / 110.574;
    }
}
