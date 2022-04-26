package com.smrt.CouponProject.clr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smrt.CouponProject.beans.Company;
import com.smrt.CouponProject.beans.LoginDetails;
import com.smrt.CouponProject.repositories.CompanyRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.smrt.CouponProject.utils.HttpUtils.*;

@Component
@Order(1)
@RequiredArgsConstructor
public class AddingCompanies implements CommandLineRunner {
    private final RestTemplate myRest;
    private Map<String, Object> map;
    private HttpEntity<String> myRequest;
    private final String addCompanyURL = "http://localhost:8080/admin/addCompany";
    private final String adminLoginURL = "http://localhost:8080/admin/login";


    @Override
    public void run(String... args) throws Exception {
        map = new HashMap<>();
        map.put("email", "admin@admin.com");
        map.put("password", "admin");
        myRequest = getRequest(map);
        String myJWT = myRest.postForObject(adminLoginURL, myRequest, String.class);
        System.out.println(myJWT);

        map = new HashMap<>();  // company id 1
        map.put("name", "SMRT");
        map.put("email", "smrt@smrt.com");
        map.put("password", "shiri_the_queen");
        myRequest = getRequest(map, myJWT);
        myRest.postForEntity(addCompanyURL, myRequest, Object.class);

        map = new HashMap<>(); // company id 2
        map.put("name", "MatanShoes");
        map.put("email", "matanshoes@gmail.com");
        map.put("password", "i_prefer_socks");
        myRequest = getRequest(map, myJWT);
        myRest.postForEntity(addCompanyURL, myRequest, Object.class);

        map = new HashMap<>(); //company id 3
        map.put("name", "HarryPotter&Friends");
        map.put("email", "expelliarmus@j.k.rowling.com");
        map.put("password", "sirius_black");
        myRequest = getRequest(map, myJWT);
        myRest.postForEntity(addCompanyURL, myRequest, Object.class);
    }
}
