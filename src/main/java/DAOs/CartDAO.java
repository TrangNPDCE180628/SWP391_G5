package DAOs;

import Models.Cart;
import Models.ViewCartCustomer;
import Ultis.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {
// Thêm sản phẩm vào giỏ: nếu đã có thì cộng thêm số lượng

    public void addToCart(String cusId, String proId, int quantity) throws SQLException, ClassNotFoundException {
        Cart existing = getCartItem(cusId, proId);
        if (existing != null) {
            int newQuantity = existing.getQuantity() + quantity;
            updateQuantity(existing.getCartId(), newQuantity);
        } else {
            insertCart(cusId, proId, quantity);
        }
    }

    // Thêm sản phẩm vào giỏ
    public void insertCart(String cusId, String proId, int quantity) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Cart (cusId, proId, quantity) VALUES (?, ?, ?)";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cusId);
            stmt.setString(2, proId);
            stmt.setInt(3, quantity);
            stmt.executeUpdate();
        }
    }

    // Cập nhật số lượng trong giỏ
    public void updateQuantity(int cartId, int quantity) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Cart SET quantity = ? WHERE cartId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantity);
            stmt.setInt(2, cartId);
            stmt.executeUpdate();
        }
    }

    // Xóa sản phẩm trong giỏ theo cartId
    public void deleteCartItem(int cartId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Cart WHERE cartId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cartId);
            stmt.executeUpdate();
        }
    }

    // Xóa toàn bộ giỏ hàng của khách
    public void deleteAllByCusId(String cusId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Cart WHERE cusId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cusId);
            stmt.executeUpdate();
        }
    }

    // Lấy danh sách ViewCartCustomer theo cusId
    public List<ViewCartCustomer> getViewCartByCusId(String cusId) throws SQLException, ClassNotFoundException {
        List<ViewCartCustomer> list = new ArrayList<>();
        String sql = "SELECT * FROM ViewCartCustomer WHERE cusId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cusId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ViewCartCustomer vcc = new ViewCartCustomer(
                        rs.getInt("cartId"),
                        rs.getString("cusId"),
                        rs.getString("proId"),
                        rs.getString("proName"),
                        rs.getDouble("proPrice"),
                        rs.getString("proImageUrl"),
                        rs.getInt("quantity")
                );
                list.add(vcc);
            }
        }
        return list;
    }

    public int getTotalQuantityByCusId(String cusId) throws SQLException, ClassNotFoundException {
        List<ViewCartCustomer> cartList = getViewCartByCusId(cusId);
        int totalQuantity = 0;
        for (ViewCartCustomer item : cartList) {
            totalQuantity += item.getQuantity();
        }
        return totalQuantity;
    }

    // Kiểm tra xem sản phẩm đã tồn tại trong giỏ chưa (dùng để insert/update logic)
    public Cart getCartItem(String cusId, String proId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Cart WHERE cusId = ? AND proId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cusId);
            stmt.setString(2, proId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Cart(
                        rs.getInt("cartId"),
                        rs.getString("cusId"),
                        rs.getString("proId"),
                        rs.getInt("quantity")
                );
            }
        }
        return null;
    }

    // Xóa sản phẩm cụ thể khỏi giỏ hàng theo cusId và proId
    public void removeItem(String cusId, String proId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Cart WHERE cusId = ? AND proId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cusId);
            stmt.setString(2, proId);
            stmt.executeUpdate();
        }
    }

}
