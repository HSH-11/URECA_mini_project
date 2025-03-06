package app.phone.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import app.phone.dao.ProductDAO;
import app.phone.dto.ProductDTO;

public class AddProductDialog extends JDialog {
    private JTextField nameField, priceField, stockField;
    private JButton addButton, cancelButton;

    private DefaultTableModel tableModel;

    public AddProductDialog(JFrame parent, DefaultTableModel tableModel) {
        super(parent, "상품 등록", true);
        this.tableModel = tableModel;
        setSize(400, 300);
        setLocationRelativeTo(parent);

        setLayout(new GridLayout(5, 2));

        add(new JLabel("제품명:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("가격:"));
        priceField = new JTextField();
        add(priceField);

        add(new JLabel("재고 수량:"));
        stockField = new JTextField();
        add(stockField);


        addButton = new JButton("등록");
        cancelButton = new JButton("취소");

        add(addButton);
        add(cancelButton);

        addButton.addActionListener(this::addProduct);
        cancelButton.addActionListener(e -> dispose());
    }

    private void addProduct(ActionEvent e) {
        String name = nameField.getText();
        double price = Double.parseDouble(priceField.getText());
        int stock = Integer.parseInt(stockField.getText());
        
        // ProductDTO 객체 생성
        ProductDTO newProduct = new ProductDTO(0, name, price, stock);

        // ProductDAO 객체 생성 후 insertProduct 메서드 호출
        ProductDAO productDAO = new ProductDAO();
        productDAO.insertProduct(newProduct);  // insertProduct 호출

        // 추가된 상품을 테이블에 반영
        tableModel.addRow(new Object[]{newProduct.getProductId(), newProduct.getName(), newProduct.getPrice(), newProduct.getStockQuantity()});
        dispose();
    }

}
