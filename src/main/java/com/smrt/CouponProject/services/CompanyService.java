package com.smrt.CouponProject.services;

import com.smrt.CouponProject.beans.Category;
import com.smrt.CouponProject.beans.Company;
import com.smrt.CouponProject.beans.Coupon;
import com.smrt.CouponProject.exceptions.AdministrationException;
import com.smrt.CouponProject.exceptions.CompanyException;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@NoArgsConstructor
public class CompanyService extends ClientService {



    /**
     * checks if the login arguments are correct.
     * @param email    company email.
     * @param password company password.
     * @return company's ID.
     */
    public int login(String email, String password) {
        if (companyRepo.existsCompanyByEmailAndPassword(email, password)) {
            return companyRepo.getByEmail(email).getId();
        }
        return 0;
    }

    /**
     * this function creates a coupon and adds it to the database
     * @param companyID ID of company using this method
     * @param coupon the coupon you want to create.
     */
    public void addCoupon(int companyID,Coupon coupon) throws AdministrationException {
        coupon.setCompanyID(companyID);
        if (couponRepo.findByCompanyIDAndTitle(companyID,coupon.getTitle()).isPresent()){
            throw new AdministrationException("A coupon with this title already exists in your company.");
        }
        Company company = companyRepo.getById(companyID);
        List<Coupon> coupons = company.getCoupons();
        coupons.add(coupon);
        company.setCoupons(coupons);
        companyRepo.save(company);
    }

    /**
     * this function updates an existing company
     * @param companyID a company id
     * @param coupon specific coupon
     * @throws CompanyException if Coupon doesn't exist, or the coupon doesn't belong to your company.
     */
    public void updateCoupon(int companyID,Coupon coupon) throws CompanyException, AdministrationException {
        Optional<Coupon> coupon1 = couponRepo.findById(coupon.getId());
        if (coupon1.isEmpty()) {
            throw new CompanyException(COUPON_NOT_EXIST_EXCEPTION+UPDATE_EXCEPTION);
        }
        if (coupon1.get().getCompanyID()!=companyID){
            throw new CompanyException("Not your coupon.");
        }
        if (!(coupon.getTitle().equals(coupon1.get().getTitle()))&&couponRepo.existsCouponByCompanyIDAndTitle(companyID,coupon.getTitle())){
            throw new AdministrationException("A coupon with this name already exists in your company.");
        }
        coupon1.get().setAmount(coupon.getAmount());
        coupon1.get().setEndDate(coupon.getEndDate());
        coupon1.get().setCategory(coupon.getCategory());
        coupon1.get().setDescription(coupon.getDescription());
        coupon1.get().setImage(coupon.getImage());
        coupon1.get().setPrice(coupon.getPrice());
        coupon1.get().setStartDate(coupon.getStartDate());
        coupon1.get().setTitle(coupon.getTitle());
        couponRepo.saveAndFlush(coupon1.get());
    }

    /**
     * deletes an existing coupon, and all it's purchases.
     * @param companyID ID of company requesting the deletion.
     * @param couponID ID of coupon to be deleted.
     * @throws CompanyException if the coupon doesnt belong to your company.
     */
    public void deleteCoupon(int companyID,int couponID) throws CompanyException {
        Optional<Coupon> coupon1 = couponRepo.findById(couponID);
        if (coupon1.isEmpty()) {
            throw new CompanyException(COUPON_NOT_EXIST_EXCEPTION+UPDATE_EXCEPTION);
        }
        if (coupon1.get().getCompanyID()!=companyID){
            throw new CompanyException("Not your coupon.");
        }
        couponRepo.deleteById(couponID);
    }

    /**
     * get all company coupons.
     * @param companyID company's ID.
     * @return all coupons owned by this company.
     */
    public List<Coupon> getCompanyCoupons(int companyID) {
        return couponRepo.findByCompanyID(companyID);
    }

    /**
     * get company coupons filtered by category
     * @param companyID company's ID.
     * @param category coupon's category.
     * @return list of company coupons, that have the given category
     */
    public List<Coupon> getCompanyCouponsByCategory(int companyID,Category category) {
        return couponRepo.findByCompanyIDAndCategory(companyID, category);
    }

    /**
     * get company coupons with a specific price ceiling.
     * @param companyID company's ID.
     * @param maxPrice price ceiling.
     * @return list of coupons.
     */
    public List<Coupon> getCompanyCouponsTillMaxPrice(int companyID,double maxPrice)  {
        return couponRepo.findByCompanyIDAndPriceLessThanEqual(companyID, maxPrice);
    }

    /**
     *  get company details
     * @param companyID company's ID.
     * @return company details.
     */
    public Company getCompanyDetails(int companyID)  {
        return companyRepo.getById(companyID);
    }
}