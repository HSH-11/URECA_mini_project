package mybatis.ui;

import mybatis.common.MyBatisConfig;
import mybatis.dao.ProductDAO;
import mybatis.dto.ProductDTO;
import org.apache.ibatis.session.SqlSession;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProductUserView extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchWordField;
    private JButton searchButton, orderButton, listButton, checkOrderButton, checkCouponButton;

    public ProductUserView() {
        setTitle("상품 조회 및 주문");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"Product ID", "Name", "Price", "Stock"}, 0);
        table = new JTable(tableModel);

        listProducts(); 

        // 검색 필드 설정
        Dimension textFieldSize = new Dimension(400, 28);
        searchWordField = new JTextField();
        searchWordField.setPreferredSize(textFieldSize);

        searchButton = new JButton("검색");
        searchButton.addActionListener(e -> {
            String searchWord = searchWordField.getText();
            if (!searchWord.isBlank()) {
                listProducts(searchWord);
            }
        });

        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("제품명 검색"));
        searchPanel.add(searchWordField);
        searchPanel.add(searchButton);

        orderButton = new JButton("상품 주문");
        orderButton.addActionListener(e -> openOrderForm());

        listButton = new JButton("새로고침");
        listButton.addActionListener(e -> listProducts());

        checkOrderButton = new JButton("내 주문 내역 조회");
        checkOrderButton.addActionListener(e -> openOrderHistory());

        checkCouponButton = new JButton("내 쿠폰 조회");
        checkCouponButton.addActionListener(e -> openCouponHistory());

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(orderButton);
        bottomPanel.add(listButton);
        bottomPanel.add(checkOrderButton);
        bottomPanel.add(checkCouponButton);

        add(searchPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void listProducts() {
        clearTable();
        try (SqlSession session = MyBatisConfig.getSession()) {
            ProductDAO productDAO = session.getMapper(ProductDAO.class);
            List<ProductDTO> products = productDAO.getAllProducts();
            for (ProductDTO product : products) {
                tableModel.addRow(new Object[]{
                        product.getProductId(),
                        product.getName(),
                        product.getPrice(),
                        product.getStockQuantity()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "상품 데이터를 불러오는 데 실패했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listProducts(String searchWord) {
        clearTable();
        try (SqlSession session = MyBatisConfig.getSession()) {
            ProductDAO productDAO = session.getMapper(ProductDAO.class);
            List<ProductDTO> productList = productDAO.getProductsByName(searchWord);
            for (ProductDTO product : productList) {
                tableModel.addRow(new Object[]{
                        product.getProductId(),
                        product.getName(),
                        product.getPrice(),
                        product.getStockQuantity()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "검색 결과를 불러오는 데 실패했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearTable() {
        tableModel.setRowCount(0);
    }

    private void openOrderForm() {
        new OrderForm().setVisible(true);
    }

    private void openOrderHistory() {
        JOptionPane.showMessageDialog(this, "주문 내역 조회 기능은 아직 구현되지 않았습니다.");
    }

    private void openCouponHistory() {
        JOptionPane.showMessageDialog(this, "쿠폰 조회 기능은 아직 구현되지 않았습니다.");
    }
}
