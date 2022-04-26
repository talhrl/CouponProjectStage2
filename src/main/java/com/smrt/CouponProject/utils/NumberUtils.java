package com.smrt.CouponProject.utils;

public class NumberUtils {
    public static int getAmount() {
        return (int) (Math.random() * 16 + 10);
    }

    public static double getPrice() {
        return Math.random() * 91 + 10;
    }
}
