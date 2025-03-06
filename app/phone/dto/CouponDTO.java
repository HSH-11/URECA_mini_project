package app.phone.dto;

public class CouponDTO {
    private int couponId;
    private String couponName;
    private double discountRate; 


    public int getCouponId() { return couponId; }
    public void setCouponId(int couponId) { this.couponId = couponId; }

    public String getCouponName() { return couponName; }
    public void setCouponName(String couponName) { this.couponName = couponName; }

    public double getDiscountRate() { return discountRate; }
    public void setDiscountRate(double discountRate) { this.discountRate = discountRate; }

    @Override
    public String toString() {
        return String.format("%s (할인율: %.0f%%)", couponName, discountRate);
    }

}
