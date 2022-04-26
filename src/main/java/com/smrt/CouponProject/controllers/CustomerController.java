package com.smrt.CouponProject.controllers;

import com.smrt.CouponProject.beans.Category;
import com.smrt.CouponProject.beans.LoginDetails;
import com.smrt.CouponProject.beans.UserDetails;
import com.smrt.CouponProject.exceptions.JwtException;
import com.smrt.CouponProject.exceptions.LoginException;
import com.smrt.CouponProject.exceptions.PurchaseException;
import com.smrt.CouponProject.services.CustomerService;
import com.smrt.CouponProject.jwt.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("customer")
@RequiredArgsConstructor
public class CustomerController{

    private final CustomerService customerService;
    private final JWTUtils jwtUtils;
    private String role = "Customer";

    /**
     * logs customer into the system.
     * @param loginDetails email and password.
     * @return JWT that authorizes use of customerController methods.
     * @throws LoginException if customerID doesn't exist.
     */
    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginDetails loginDetails) throws LoginException {
        int customerID = customerService.login(loginDetails.getEmail(), loginDetails.getPassword());
        if (customerID == 0) {
            throw new LoginException("invalid user");
        }
        return new ResponseEntity<>(jwtUtils.generateToken(new UserDetails(loginDetails.getEmail(), loginDetails.getPassword(), this.role, customerID)), HttpStatus.OK);
    }

    /**
     * purchase a coupon.
     * @param token is taken from the requestEntity's header, and used to validate the request
     * @param couponId ID of coupon.
     * @throws PurchaseException if couponID doesn't exist.
     * @throws LoginException when role doesn't fit.
     * @throws JwtException when JWT isn't valid.
     */
    @PostMapping("purchaseCoupon/{couponId}")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void purchaseCoupon(@RequestHeader(name = "Authorization") String token, @PathVariable int couponId) throws PurchaseException, JwtException, LoginException {
        UserDetails userDetails = jwtUtils.validateToken(token);
        if (!userDetails.getRole().equals(role)) {
            throw new LoginException("Invalid user");
        }
        customerService.purchaseCoupon(userDetails.getId(),couponId);
    }

    /**
     * get all coupons owned by customer.
     * @param token is taken from the requestEntity's header, and used to validate the request
     * @return list of all coupons owned by customer.
     * @throws LoginException when role doesn't fit.
     * @throws JwtException when JWT isn't valid.
     */
    @GetMapping("customerCoupons")
    public ResponseEntity<?> getCustomerCoupons(@RequestHeader(name = "Authorization") String token) throws JwtException, LoginException {
        UserDetails userDetails = jwtUtils.validateToken(token);
        if (!userDetails.getRole().equals(role)) {
            throw new LoginException("Invalid user");
        }
        return new ResponseEntity<>(customerService.getCustomerCoupons(userDetails.getId()), HttpStatus.ACCEPTED);
    }

    /**
     * get all coupons owned by customer, filtered by category.
     * @param token is taken from the requestEntity's header, and used to validate the request
     * @param categoryId category's ID
     * @return list of coupons owned by customer, filtered by category.
     * @throws LoginException when role doesn't fit.
     * @throws JwtException when JWT isn't valid.
     */
    @GetMapping("customerCouponsByCategory/{categoryId}")
    public ResponseEntity<?> getCustomerCouponsByCategory(@RequestHeader(name = "Authorization") String token, @PathVariable Category categoryId) throws JwtException, LoginException {
        UserDetails userDetails = jwtUtils.validateToken(token);
        if (!userDetails.getRole().equals(role)) {
            throw new LoginException("Invalid user");
        }
        return new ResponseEntity<>(customerService.getCustomerCouponsByCategory(userDetails.getId(),categoryId), HttpStatus.OK);
    }

    /**
     * get coupons owned by customer, till a certain price.
     * @param token is taken from the requestEntity's header, and used to validate the request
     * @param maxPrice max price.
     * @return list of coupons owned by customer, till a certain price.
     * @throws LoginException when role doesn't fit.
     * @throws JwtException when JWT isn't valid.
     */
    @GetMapping("customerCouponsTillMaxPrice/{maxPrice}")
    public ResponseEntity<?> getCustomerCouponsTillMaxPrice(@RequestHeader(name = "Authorization") String token, @PathVariable double maxPrice) throws JwtException, LoginException {
        UserDetails userDetails = jwtUtils.validateToken(token);
        if (!userDetails.getRole().equals(role)) {
            throw new LoginException("Invalid user");
        }
        return new ResponseEntity<>(customerService.getCustomerCouponsTillMaxPrice(userDetails.getId(),maxPrice), HttpStatus.ACCEPTED);

    }

    /**
     * get customer details.
     * @param token is taken from the requestEntity's header, and used to validate the request, and fetch the customer's details.
     * @return customer details.
     * @throws LoginException when role doesn't fit.
     * @throws JwtException when JWT isn't valid.
     */
    @GetMapping("customerDetails")
    public ResponseEntity<?> customerDetails(@RequestHeader(name = "Authorization") String token) throws JwtException, LoginException {
        UserDetails userDetails = jwtUtils.validateToken(token);
        if (!userDetails.getRole().equals(role)) {
            throw new LoginException("Invalid user");
        }
        return new ResponseEntity<>(customerService.getCustomerDetails(userDetails.getId()), HttpStatus.OK);
    }
}
