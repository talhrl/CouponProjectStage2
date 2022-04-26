package com.smrt.CouponProject.controllers;

import com.smrt.CouponProject.beans.Company;
import com.smrt.CouponProject.beans.Customer;
import com.smrt.CouponProject.beans.LoginDetails;
import com.smrt.CouponProject.beans.UserDetails;
import com.smrt.CouponProject.exceptions.AdministrationException;
import com.smrt.CouponProject.exceptions.JwtException;
import com.smrt.CouponProject.exceptions.LoginException;
import com.smrt.CouponProject.jwt.JWTUtils;
import com.smrt.CouponProject.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin")
@RequiredArgsConstructor
public class AdminController {
    private final String role = "Admin";
    private final AdminService adminService;
    private final JWTUtils jwtUtils;

    /**
     * verifies admin's loginDetails, and returns JWT with admin authorization upon success.
     * @param loginDetails username and password.
     * @return ResponseEntity with a JWT as it's body.
     * @throws LoginException if the userDetails aren't those of an admin.
     */
    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginDetails loginDetails) throws LoginException {
        if (!adminService.login(loginDetails.getEmail(),loginDetails.getPassword())) {
            throw new LoginException("invalid user");
        }
        return new ResponseEntity<>(jwtUtils.generateToken(new UserDetails(loginDetails.getEmail(),loginDetails.getPassword(),role,0)), HttpStatus.OK);
    }


    /**
     * Creates company, and inserts it into the Database.
     * @param token is taken from the requestEntity's header, and used to validate the request..
     * @param company A company.
     * @return Response entity with status: CREATED.
     * @throws LoginException when role doesn't fit.
     * @throws JwtException when JWT isn't valid.
     */
    @PostMapping("addCompany")
    public ResponseEntity<?> addCompany(@RequestHeader(name = "Authorization") String token, @RequestBody Company company) throws LoginException, JwtException {
        UserDetails userDetails=jwtUtils.validateToken(token);
        if (!userDetails.getRole().equals(role)) {
            throw new LoginException("Invalid User");
        }
        adminService.addCompany(company);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     *updates an existing company.
     * @param token is taken from the requestEntity's header, and used to validate the request.
     * @param company Updated company.
     * @throws AdministrationException if company doesn't exist.
     * @throws LoginException when role doesn't fit.
     * @throws JwtException when JWT isn't valid.
     */
    @PutMapping("updateCompany")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void updateCompany(@RequestHeader(name = "Authorization") String token, @RequestBody Company company) throws AdministrationException, LoginException, JwtException {
         UserDetails userDetails=jwtUtils.validateToken(token);
        if (!userDetails.getRole().equals(role)) {
            throw new LoginException("Invalid User");
        }
        adminService.updateCompany(company);
    }

    /**
     * deletes an existing company.
     * @param token is taken from the requestEntity's header, and used to validate the request.
     * @param companyId ID of the company you wish to delete.
     * @return Response entity with status:ACCEPTED.
     * @throws AdministrationException if company doesn't exist.
     * @throws LoginException when role doesn't fit.
     * @throws JwtException when JWT isn't valid.
     */
    @DeleteMapping("deleteCompany/{companyId}")
    public ResponseEntity<?> deleteCompany(@RequestHeader(name = "Authorization") String token, @PathVariable int companyId) throws AdministrationException, LoginException, JwtException {
         UserDetails userDetails=jwtUtils.validateToken(token);
        if (!userDetails.getRole().equals(role)) {
            throw new LoginException("Invalid User");
        }
        adminService.deleteCompany(companyId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    /**
     * fetches all companies from the database.
     * @param token is taken from the requestEntity's header, and used to validate the request.
     * @return responseEntity with status:OK.
     * @throws LoginException when role doesn't fit.
     * @throws JwtException when JWT isn't valid.
     */
    @GetMapping("allCompanies")
    public ResponseEntity<?> getAllCompanies(@RequestHeader(name = "Authorization") String token) throws LoginException, JwtException {
         UserDetails userDetails=jwtUtils.validateToken(token);
        if (!userDetails.getRole().equals(role)) {
            throw new LoginException("Invalid User");
        }
        return new ResponseEntity<>(adminService.getAllCompanies(), HttpStatus.OK);
    }

    /**
     * get a single company from the database.
     * @param token is taken from the requestEntity's header, and used to validate the request.
     * @param companyId ID of the company.
     * @return responseEntity with
     * @throws AdministrationException if company ID doesnt exist.
     * @throws LoginException when role doesn't fit.
     * @throws JwtException when JWT isn't valid.
     */
    @GetMapping("getCompany/{companyId}")
    public ResponseEntity<?> getOneCompany(@RequestHeader(name = "Authorization") String token, @PathVariable int companyId) throws AdministrationException, LoginException, JwtException {
         UserDetails userDetails=jwtUtils.validateToken(token);
        if (!userDetails.getRole().equals(role)) {
            throw new LoginException("Invalid User");
        }
        return new ResponseEntity<>(adminService.getOneCompany(companyId), HttpStatus.OK);
    }

    /**
     * adds a customer to the database.
     * @param token is taken from the requestEntity's header, and used to validate the request.
     * @param customer the customer you want to add to the database.
     * @return responseEntity with status:CREATED.
     * @throws LoginException when role doesn't fit.
     * @throws JwtException when JWT isn't valid.
     */
    @PostMapping("addCustomer")
    public ResponseEntity<?> addCustomer(@RequestHeader(name = "Authorization") String token, @RequestBody Customer customer) throws LoginException, JwtException {
         UserDetails userDetails=jwtUtils.validateToken(token);
        if (!userDetails.getRole().equals(role)) {
            throw new LoginException("Invalid User");
        }
        adminService.addCustomer(customer);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * updates an existing customer
     * @param token is taken from the requestEntity's header, and used to validate the request.
     * @param customer customer with updated fields.
     * @throws AdministrationException if customer ID doesnt exist.
     * @throws LoginException when role doesn't fit.
     * @throws JwtException when JWT isn't valid.
     */
    @PutMapping("updateCustomer")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void updateCompany(@RequestHeader(name = "Authorization") String token, @RequestBody Customer customer) throws AdministrationException, LoginException, JwtException {
         UserDetails userDetails=jwtUtils.validateToken(token);
        if (!userDetails.getRole().equals(role)) {
            throw new LoginException("Invalid User");
        }
        adminService.updateCustomer(customer);
    }

    /**
     * deletes an existing customer, and all his coupon purchases from the database.
     * @param token is taken from the requestEntity's header, and used to validate the request.
     * @param customerId ID of customer you wish to delete.
     * @return ResponseEntity with status:ACCEPTED.
     * @throws AdministrationException if customer ID doesnt exist.
     * @throws LoginException when role doesn't fit.
     * @throws JwtException when JWT isn't valid.
     */
    @DeleteMapping("deleteCustomer/{customerId}")
    public ResponseEntity<?> deleteCustomer(@RequestHeader(name = "Authorization") String token, @PathVariable int customerId) throws AdministrationException, LoginException, JwtException {
         UserDetails userDetails=jwtUtils.validateToken(token);
        if (!userDetails.getRole().equals(role)) {
            throw new LoginException("Invalid User");
        }
        adminService.deleteCustomer(customerId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    /**
     * fetches all customers from the database.
     * @param token is taken from the requestEntity's header, and used to validate the request.
     * @return a list of all existing customers.
     * @throws LoginException when role doesn't fit.
     * @throws JwtException when JWT isn't valid.
     */
    @GetMapping("allCustomers")
    public ResponseEntity<?> getAllCustomers(@RequestHeader(name = "Authorization") String token) throws  LoginException, JwtException {
         UserDetails userDetails=jwtUtils.validateToken(token);
        if (!userDetails.getRole().equals(role)) {
            throw new LoginException("Invalid User");
        }
        return new ResponseEntity<>(adminService.getAllCustomers(), HttpStatus.OK);
    }

    /**
     *  get a customer from the database.
     * @param token is taken from the requestEntity's header, and used to validate the request.
     * @param customerId ID of the customer.
     * @return a customer.
     * @throws AdministrationException if no customer with such ID exists in the database.
     * @throws LoginException when role doesn't fit.
     * @throws JwtException when JWT isn't valid.
     */
    @GetMapping("getCustomer/{customerId}")
    public ResponseEntity<?> getOneCustomer(@RequestHeader(name = "Authorization") String token, @PathVariable int customerId) throws AdministrationException, LoginException, JwtException {
         UserDetails userDetails=jwtUtils.validateToken(token);
        if (!userDetails.getRole().equals(role)) {
            throw new LoginException("Invalid User");
        }
        return new ResponseEntity<>(adminService.getOneCustomer(customerId), HttpStatus.OK);
    }
}
