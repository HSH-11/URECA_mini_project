package app.phone.dao;

import app.phone.common.DBManager;
import app.phone.dto.CouponDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CouponDAO {
    public List<CouponDTO> getAllCoupons() {
        List<CouponDTO> coupons = new ArrayList<>();
        String sql = "SELECT * FROM Coupons";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
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

    public void addCoupon(CouponDTO coupon) {
        String sql = "INSERT INTO Coupons (coupon_name, discount_rate) VALUES (?, ?)";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, coupon.getCouponName());
            pstmt.setDouble(2, coupon.getDiscountRate());
    
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
