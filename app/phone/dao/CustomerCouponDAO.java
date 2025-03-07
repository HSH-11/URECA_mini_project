package app.phone.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import app.phone.common.DBManager;
import app.phone.dto.CouponDTO;

public class CustomerCouponDAO {
    // 특정 고객의 보유 쿠폰 목록 조회
    public List<CouponDTO> getCustomerCoupons(int customerId) {
        List<CouponDTO> coupons = new ArrayList<>();
        String sql = "SELECT c.* " +
                "FROM CustomerCoupons cc " +
                "JOIN Coupons c ON cc.coupon_id = c.coupon_id " +
                "WHERE cc.customer_id = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                CouponDTO coupon = new CouponDTO();
                coupon.setCouponId(rs.getInt("coupon_id"));
                coupon.setCouponName(rs.getString("coupon_name"));
                coupon.setDiscountRate(rs.getDouble("discount_rate"));
                coupons.add(coupon);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return coupons;
    }

    // 고객에게 쿠폰 지급
    public void issueCouponToCustomer(int customerId, int couponId) {
        String sql = "INSERT INTO CustomerCoupons (customer_id, coupon_id, issued_date) VALUES (?, ?, NOW())";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customerId);
            pstmt.setInt(2, couponId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
