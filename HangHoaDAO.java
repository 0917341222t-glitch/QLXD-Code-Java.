package dao;

import model.HangHoa;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HangHoaDAO {

    public List<HangHoa> getAll() {
        List<HangHoa> list = new ArrayList<>();
        String sql = "SELECT MaHangHoa, TenHangHoa, DonVi, GiaBan, MoTa FROM DanhMucHangHoa ORDER BY MaHangHoa";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new HangHoa(
                    rs.getInt("MaHangHoa"),
                    rs.getString("TenHangHoa"),
                    rs.getString("DonVi"),
                    rs.getDouble("GiaBan"),
                    rs.getString("MoTa")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<HangHoa> search(String keyword) {
        List<HangHoa> list = new ArrayList<>();
        String sql = "SELECT * FROM DanhMucHangHoa WHERE TenHangHoa LIKE ? OR DonVi LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String kw = "%" + keyword + "%";
            ps.setString(1, kw);
            ps.setString(2, kw);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new HangHoa(
                    rs.getInt("MaHangHoa"),
                    rs.getString("TenHangHoa"),
                    rs.getString("DonVi"),
                    rs.getDouble("GiaBan"),
                    rs.getString("MoTa")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy tồn kho theo hàng hóa (query 11)
    public List<Object[]> getTonKho() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT hh.TenHangHoa, SUM(tk.SoLuongTon) AS TongTon " +
                     "FROM TonKho tk JOIN DanhMucHangHoa hh ON tk.MaHangHoa = hh.MaHangHoa " +
                     "GROUP BY tk.MaHangHoa, hh.TenHangHoa";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Object[]{rs.getString("TenHangHoa"), rs.getInt("TongTon")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(HangHoa hh) {
        String sql = "INSERT INTO DanhMucHangHoa (TenHangHoa, DonVi, GiaBan, MoTa) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hh.getTenHangHoa());
            ps.setString(2, hh.getDonVi());
            ps.setDouble(3, hh.getGiaBan());
            ps.setString(4, hh.getMoTa());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(HangHoa hh) {
        String sql = "UPDATE DanhMucHangHoa SET TenHangHoa=?, DonVi=?, GiaBan=?, MoTa=? WHERE MaHangHoa=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hh.getTenHangHoa());
            ps.setString(2, hh.getDonVi());
            ps.setDouble(3, hh.getGiaBan());
            ps.setString(4, hh.getMoTa());
            ps.setInt(5, hh.getMaHangHoa());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int maHH) {
        String sql = "DELETE FROM DanhMucHangHoa WHERE MaHangHoa=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maHH);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
