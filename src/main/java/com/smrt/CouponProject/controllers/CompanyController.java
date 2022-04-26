package com.smrt.CouponProject.controllers;


import com.smrt.CouponProject.beans.Category;
import com.smrt.CouponProject.beans.Coupon;
import com.smrt.CouponProject.beans.LoginDetails;
import com.smrt.CouponProject.beans.UserDetails;
import com.smrt.CouponProject.exceptions.AdministrationException;
import com.smrt.CouponProject.exceptions.CompanyException;
import com.smrt.CouponProject.exceptions.JwtException;
import com.smrt.CouponProject.exceptions.LoginException;
import com.smrt.CouponProject.services.AdminService;
import com.smrt.CouponProject.services.ClientService;
import com.smrt.CouponProject.services.CompanyService;
import com.smrt.CouponProject.jwt.JWTUtils;
import com.smrt.CouponProject.services.CustomerService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("company")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;
    private final JWTUtils jwtUtils;
    private String role = "Company";

    /**
     * Verify company's LoginDetails, and returns JWT with company authorization upon success.
     * @param loginDetails email and password
     * @return ResponseEntity with a jwt as it's body
     * @throws LoginException if the UserDetails aren't matching an existing company
     */
    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginDetails loginDetails) throws LoginException {
        int companyID = companyService.login(loginDetails.getEmail(), loginDetails.getPassword());
        if (companyID == 0) {
            throw new LoginException("invalid user");
        }
        return new ResponseEntity<>(jwtUtils.generateToken(new UserDetails(loginDetails.getEmail(), loginDetails.getPassword(), this.role, companyID)), HttpStatus.OK);
    }

    /**
     * add a coupon to the database.
     * @param token is taken from the requestEntity's header, and used to validate the request
     * @param coupon The coupon.
     * @return created status
     * @throws CompanyException if you can't add that coupon
     * @throws LoginException when role doesn't fit.
     * @throws JwtException when JWT isn't valid.
     */
    @PostMapping("addCoupon")
    public ResponseEntity<?> addCoupon(@RequestHeader(name = "Authorization") String token, @RequestBody Coupon coupon) throws LoginException, JwtException, AdministrationException {
        UserDetails userDetails = jwtUtils.validateToken(token);
        if (!userDetails.getRole().equals(role)) {
            throw new LoginException("Invalid User");
        }
        companyService.addCoupon(userDetails.getId(), coupon);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * update an existing coupon.
     * @param token is taken from the requestEntity's header, and used to validate the request
     * @param coupon coupon
     * @throws CompanyException if you can't update the coupon
     * @throws LoginException when role doesn't fit.
     * @throws JwtException when JWT isn't valid.
     */
    @PutMapping("updateCoupon")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void updateCoupon(@RequestHeader(name = "Authorization") String token, @RequestBody Coupon coupon) throws LoginException, CompanyException, JwtException, AdministrationException {
        UserDetails userDetails = jwtUtils.validateToken(token);
        if (!userDetails.getRole().equals(role)) {
            throw new LoginException("Invalid User");
        }
        companyService.updateCoupon(userDetails.getId(), coupon);
    }

    /**
     * delete an existing coupon sold by your company.
     * @param token is taken from the requestEntity's header, and used to validate the request
     * @param couponId ID of the coupon.
     * @throws CompanyException if coupon doesn't exist, or isn't yours to delete.
     * @throws LoginException when role doesn't fit.
     * @throws JwtException when JWT isn't valid.
     */
    @DeleteMapping("deleteCoupon/{couponId}")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void deleteCoupon(@RequestHeader(name = "Authorization") String token, @PathVariable int couponId) throws LoginException, CompanyException, JwtException {
        UserDetails userDetails = jwtUtils.validateToken(token);
        if (!userDetails.getRole().equals(role)) {
            throw new LoginException("Invalid User");
        }
        companyService.deleteCoupon(userDetails.getId(), couponId);
    }

    /**
     * get all coupons from the database.
     * @param token is taken from the requestEntity's header, and used to validate the request
     * @return all coupons owned by your company.
     * @throws LoginException when role doesn't fit.
     * @throws JwtException when JWT isn't valid.
     */
    @GetMapping("allCoupons")
    public ResponseEntity<?> getAllCoupons(@RequestHeader(name = "Authorization") String token) throws LoginException, JwtException {
        UserDetails userDetails = jwtUtils.validateToken(token);
        if (!userDetails.getRole().equals(role)) {
            throw new LoginException("Invalid User");
        }
        return new ResponseEntity<>(companyService.getCompanyCoupons(userDetails.getId()), HttpStatus.OK);

    }

    /**
     * get all company coupons by category.
     * @param token is taken from the requestEntity's header, and used to validate the request
     * @param categoryId ID of category.
     * @return list of coupons of this category that are owned by the company.
     * @throws LoginException when role doesn't fit.
     * @throws JwtException when JWT isn't valid.
     */
    @GetMapping("CouponsByCategory/{categoryId}")
    public ResponseEntity<?> getCouponsByCategory(@RequestHeader(name = "Authorization") String token, @PathVariable Category categoryId) throws LoginException, JwtException {
        UserDetails userDetails = jwtUtils.validateToken(token);
        if (!userDetails.getRole().equals(role)) {
            throw new LoginException("Invalid User");
        }
        return new ResponseEntity<>(companyService.getCompanyCouponsByCategory(userDetails.getId(), categoryId), HttpStatus.OK);
    }

    /**
     * get all company coupons that are under a certain price.
     * @param token is taken from the requestEntity's header, and used to validate the request
     * @param maxPrice price under which coupons get added to the list.
     * @return list of all coupons under maxPrice, that belong to the company.
     * @throws LoginException when role doesn't fit.
     * @throws JwtException when JWT isn't valid.
     */
    @GetMapping("couponsTillMaxPrice/{maxPrice}")
    public ResponseEntity<?> getCouponsTillMaxPrice(@RequestHeader(name = "Authorization") String token, @PathVariable double maxPrice) throws LoginException, JwtException {
        UserDetails userDetails = jwtUtils.validateToken(token);
        if (!userDetails.getRole().equals(role)) {
            throw new LoginException("Invalid User");
        }
        return new ResponseEntity<>(companyService.getCompanyCouponsTillMaxPrice(userDetails.getId(), maxPrice), HttpStatus.OK);
    }

    /**
     * get company details.
     * @param token is taken from the requestEntity's header, and used to validate the request
     * @return company details.
     * @throws LoginException when role doesn't fit.
     * @throws JwtException when JWT isn't valid.
     */
    @GetMapping("companyDetails")
    public ResponseEntity<?> companyDetails(@RequestHeader(name = "Authorization") String token) throws LoginException, JwtException {
        UserDetails userDetails = jwtUtils.validateToken(token);
        if (!userDetails.getRole().equals(role)) {
            throw new LoginException("Invalid User");
        }
        return new ResponseEntity<>(companyService.getCompanyDetails(userDetails.getId()), HttpStatus.OK);
    }
}
