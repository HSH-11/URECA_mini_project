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

public class EditProductDialog extends JDialog {
    private JTextField nameField, priceField, stockField;
    private JButton updateButton, cancelButton;

    private DefaultTableModel tableModel;
    private int selectedRow;

    public EditProductDialog(JFrame parent, DefaultTableModel tableModel, int selectedRow) {
        super(parent, "상품 수정", true);
        this.tableModel = tableModel;
        this.selectedRow = selectedRow;

        setSize(400, 300);
        setLocationRelativeTo(parent);

        setLayout(new GridLayout(5, 2));

        add(new JLabel("제품명:"));
        nameField = new JTextField((String) tableModel.getValueAt(selectedRow, 1));
        add(nameField);

        add(new JLabel("가격:"));
        priceField = new JTextField(String.valueOf(tableModel.getValueAt(selectedRow, 2)));
        add(priceField);

        add(new JLabel("재고 수량:"));
        stockField = new JTextField(String.valueOf(tableModel.getValueAt(selectedRow, 3)));
        add(stockField);

     
        updateButton = new JButton("수정");
        cancelButton = new JButton("취소");

        add(updateButton);
        add(cancelButton);

        updateButton.addActionListener(this::updateProduct);
        cancelButton.addActionListener(e -> dispose());
    }

    private void updateProduct(ActionEvent e) {
        String name = nameField.getText();
        double price = Double.parseDouble(priceField.getText());
        int stock = Integer.parseInt(stockField.getText());
       

        // DB에서 상품 수정하는 로직 작성
        ProductDTO updatedProduct = new ProductDTO(
            (int) tableModel.getValueAt(selectedRow, 0), name, price, stock);

        // ProductDAO의 인스턴스를 생성하여 updateProduct 호출
        ProductDAO productDAO = new ProductDAO();
        productDAO.updateProduct(updatedProduct);

        // 테이블 내용 갱신
        tableModel.setValueAt(updatedProduct.getName(), selectedRow, 1);
        tableModel.setValueAt(updatedProduct.getPrice(), selectedRow, 2);
        tableModel.setValueAt(updatedProduct.getStockQuantity(), selectedRow, 3);
       
        dispose();
    }

}
