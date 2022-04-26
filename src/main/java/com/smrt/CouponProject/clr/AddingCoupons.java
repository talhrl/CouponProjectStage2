package com.smrt.CouponProject.clr;

import com.smrt.CouponProject.beans.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static com.smrt.CouponProject.utils.HttpUtils.*;
import static com.smrt.CouponProject.utils.DateUtils.*;
import static com.smrt.CouponProject.utils.NumberUtils.*;


@Component
@Order(3)
@RequiredArgsConstructor
public class AddingCoupons implements CommandLineRunner {


    private final RestTemplate myRest;
    private Map<String, Object> map;
    private HttpEntity<String> myRequest;
    private String myJWT;
    private final String addCouponURL = "http://localhost:8080/company/addCoupon";
    private final String companyLoginURL = "http://localhost:8080/company/login";

    @Override
    public void run(String... args) throws Exception {
        map = new HashMap<>();
        map.put("email", "smrt@smrt.com");
        map.put("password", "shiri_the_queen");
        myRequest = getRequest(map);
        myJWT = myRest.postForObject(companyLoginURL, myRequest, String.class);

        map = new HashMap<>();
        map.put("amount",getAmount());
        map.put("category","ELECTRICITY");
        map.put("description","A cheap computer");
        map.put("endDate",getEndDate());
        map.put("image","images/coupons/coupon");
        map.put("price",getPrice());
        map.put("startDate",getStartDate());
        map.put("title","Computer");
        myRequest = getRequest(map, myJWT);
        myRest.postForEntity(addCouponURL, myRequest, Object.class);

        map = new HashMap<>();
        map.put("category", Category.ELECTRICITY);
        map.put("title", "TV");
        map.put("description", "A cheap TV");
        map.put("startDate", getStartDate());
        map.put("endDate", getEndDate());
        map.put("amount", getAmount());
        map.put("price", getPrice());
        map.put("image", "http://images/image");
        myRequest = getRequest(map, myJWT);
        myRest.postForEntity(addCouponURL, myRequest, Object.class);

        map = new HashMap<>();
        map.put("category", Category.ELECTRICITY);
        map.put("title", "SoftwareEngineer");
        map.put("description", "A cheap worker");
        map.put("startDate", getStartDate());
        map.put("endDate", getEndDate());
        map.put("amount", getAmount());
        map.put("price", getPrice());
        map.put("image", "http://images/image");
        myRequest = getRequest(map, myJWT);
        myRest.postForEntity(addCouponURL, myRequest, Object.class);

        map = new HashMap<>();
        map.put("email", "matanshoes@gmail.com");
        map.put("password", "i_prefer_socks");
        myRequest = getRequest(map);
        myJWT = myRest.postForObject(companyLoginURL, myRequest, String.class);

        map = new HashMap<>();
        map.put("category", Category.FASHION);
        map.put("title", "Nike");
        map.put("description", "Cheap Nike shoes");
        map.put("startDate", getStartDate());
        map.put("endDate", getEndDate());
        map.put("amount", getAmount());
        map.put("price", getPrice());
        map.put("image", "http://images/image");
        myRequest = getRequest(map, myJWT);
        myRest.postForEntity(addCouponURL, myRequest, Object.class);

        map = new HashMap<>();
        map.put("category", Category.FASHION);
        map.put("title", "Adidas");
        map.put("description", "Cheap Adidas shoes");
        map.put("startDate", getStartDate());
        map.put("endDate", getEndDate());
        map.put("amount", getAmount());
        map.put("price", getPrice());
        map.put("image", "http://images/image");
        myRequest = getRequest(map, myJWT);
        myRest.postForEntity(addCouponURL, myRequest, Object.class);

        map = new HashMap<>();
        map.put("category", Category.FASHION);
        map.put("title", "Gali");
        map.put("description", "Cheap Gali shoes");
        map.put("startDate", getStartDate());
        map.put("endDate", getEndDate());
        map.put("amount", getAmount());
        map.put("price", getPrice());
        map.put("image", "http://images/image");
        myRequest = getRequest(map, myJWT);
        myRest.postForEntity(addCouponURL, myRequest, Object.class);

        map = new HashMap<>();
        map.put("email", "expelliarmus@j.k.rowling.com");
        map.put("password", "sirius_black");
        myRequest = getRequest(map);
        myJWT = myRest.postForObject(companyLoginURL, myRequest, String.class);

        map = new HashMap<>();
        map.put("category", Category.VACATION);
        map.put("title", "Hogwarts");
        map.put("description", "Vacation at Hogwarts");
        map.put("startDate", getStartDate());
        map.put("endDate", getEndDate());
        map.put("amount", getAmount());
        map.put("price", getPrice());
        map.put("image", "http://images/image");
        myRequest = getRequest(map, myJWT);
        myRest.postForEntity(addCouponURL, myRequest, Object.class);

        map = new HashMap<>();
        map.put("category", Category.VACATION);
        map.put("title", "Hogsmeade");
        map.put("description", "Vacation at Hogsmeade");
        map.put("startDate", getStartDate());
        map.put("endDate", getEndDate());
        map.put("amount", getAmount());
        map.put("price", getPrice());
        map.put("image", "http://images/image");
        myRequest = getRequest(map, myJWT);
        myRest.postForEntity(addCouponURL, myRequest, Object.class);

        map = new HashMap<>();
        map.put("category", Category.VACATION);
        map.put("title", "Azkaban");
        map.put("description", "Vacation at Azkaban");
        map.put("startDate", getStartDate());
        map.put("endDate", getEndDate());
        map.put("amount", getAmount());
        map.put("price", getPrice());
        map.put("image", "http://images/image");
        myRequest = getRequest(map, myJWT);
        myRest.postForEntity(addCouponURL, myRequest, Object.class);
    }
}
