package dao;

import model.HoaDon;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HoaDonDAO {

    public List<HoaDon> getAll() {
        List<HoaDon> list = new ArrayList<>();
        String sql = "SELECT hd.MaHoaDon, hd.ThoiGian, hd.MaKH, hd.MaNhanVien, hd.MaCuaHang, " +
                     "kh.HoTen AS TenKH, nv.HoTen AS TenNV, " +
                     "IFNULL(SUM(ct.SoLuong * ct.DonGia * (1 - ct.GiamGia/100)), 0) AS TongTien " +
                     "FROM HoaDonBanHang hd " +
                     "LEFT JOIN KhachHang kh ON hd.MaKH = kh.MaKH " +
                     "LEFT JOIN NhanVien nv ON hd.MaNhanVien = nv.MaNhanVien " +
                     "LEFT JOIN CT_HoaDonBanHang ct ON hd.MaHoaDon = ct.MaHoaDon " +
                     "GROUP BY hd.MaHoaDon ORDER BY hd.ThoiGian DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setMaHoaDon(rs.getInt("MaHoaDon"));
                hd.setNgayLap(rs.getDate("ThoiGian"));
                hd.setMaKH(rs.getInt("MaKH"));
                hd.setTenKH(rs.getString("TenKH"));
                hd.setMaNhanVien(rs.getInt("MaNhanVien"));
                hd.setTenNV(rs.getString("TenNV"));
                hd.setMaCuaHang(rs.getInt("MaCuaHang"));
                hd.setTongTien(rs.getDouble("TongTien"));
                list.add(hd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<HoaDon> search(String keyword) {
        List<HoaDon> list = new ArrayList<>();
        String sql = "SELECT hd.MaHoaDon, hd.ThoiGian, hd.MaKH, hd.MaNhanVien, hd.MaCuaHang, " +
                     "kh.HoTen AS TenKH, nv.HoTen AS TenNV, " +
                     "IFNULL(SUM(ct.SoLuong * ct.DonGia * (1 - ct.GiamGia/100)), 0) AS TongTien " +
                     "FROM HoaDonBanHang hd " +
                     "LEFT JOIN KhachHang kh ON hd.MaKH = kh.MaKH " +
                     "LEFT JOIN NhanVien nv ON hd.MaNhanVien = nv.MaNhanVien " +
                     "LEFT JOIN CT_HoaDonBanHang ct ON hd.MaHoaDon = ct.MaHoaDon " +
                     "WHERE kh.HoTen LIKE ? OR nv.HoTen LIKE ? " +
                     "GROUP BY hd.MaHoaDon ORDER BY hd.ThoiGian DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String kw = "%" + keyword + "%";
            ps.setString(1, kw);
            ps.setString(2, kw);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setMaHoaDon(rs.getInt("MaHoaDon"));
                hd.setNgayLap(rs.getDate("ThoiGian"));
                hd.setMaKH(rs.getInt("MaKH"));
                hd.setTenKH(rs.getString("TenKH"));
                hd.setMaNhanVien(rs.getInt("MaNhanVien"));
                hd.setTenNV(rs.getString("TenNV"));
                hd.setMaCuaHang(rs.getInt("MaCuaHang"));
                hd.setTongTien(rs.getDouble("TongTien"));
                list.add(hd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public String getChiTietGiamGia(int maHoaDon) {
        StringBuilder sb = new StringBuilder();
        String sql = "SELECT hh.TenHangHoa, ct.GiamGia FROM CT_HoaDonBanHang ct " +
                     "JOIN DanhMucHangHoa hh ON ct.MaHangHoa = hh.MaHangHoa " +
                     "WHERE ct.MaHoaDon = ? AND ct.GiamGia > 0";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maHoaDon);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                sb.append(rs.getString("TenHangHoa"))
                  .append(": -").append(rs.getDouble("GiamGia")).append("%  ");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.length() > 0 ? sb.toString() : "Không có";
    }
}
