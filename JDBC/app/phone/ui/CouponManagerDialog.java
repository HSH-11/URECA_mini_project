package app.phone.ui;

import app.phone.dao.CouponDAO;
import app.phone.dto.CouponDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CouponManagerDialog extends JDialog {
    private JTable table;
    private DefaultTableModel model;

    public CouponManagerDialog(Frame owner) {
        super(owner, "쿠폰 관리", true);
        setSize(500, 400);
        setLocationRelativeTo(owner);

        model = new DefaultTableModel(new String[]{"쿠폰ID", "쿠폰명", "할인율", "유효기간"}, 0);
        table = new JTable(model);

        loadCoupons();

        JButton btnAdd = new JButton("쿠폰 추가");
        btnAdd.addActionListener(e -> addCoupon());

        JPanel panel = new JPanel();
        panel.add(btnAdd);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);
    }

    private void loadCoupons() {
        CouponDAO dao = new CouponDAO();
        List<CouponDTO> coupons = dao.getAllCoupons();

        model.setRowCount(0);
        for (CouponDTO coupon : coupons) {
            model.addRow(new Object[]{
                coupon.getCouponId(),
                coupon.getCouponName(),
                coupon.getDiscountRate()+ "%"
                
            });
        }
    }

    private void addCoupon() {
        String name = JOptionPane.showInputDialog(this, "쿠폰명:");
        if (name == null) return;

        String discountStr = JOptionPane.showInputDialog(this, "할인율 (%):");
        if (discountStr == null) return;
        double discountRate = Double.parseDouble(discountStr);

        CouponDTO coupon = new CouponDTO();
        coupon.setCouponName(name);
        coupon.setDiscountRate(discountRate);
        

        CouponDAO dao = new CouponDAO();
        dao.addCoupon(coupon);
        loadCoupons();
    }
}
