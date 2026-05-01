package dao;

import model.NhanVien;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NhanVienDAO {

    public List<NhanVien> getAll() {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT nv.MaNhanVien, nv.HoTen, nv.SoDT, nv.NgaySinh, nv.DiaChi, nv.MaCuaHang, ch.DiaChi AS DiaChiCH " +
                     "FROM NhanVien nv LEFT JOIN CuaHang ch ON nv.MaCuaHang = ch.MaCuaHang ORDER BY nv.MaNhanVien";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                NhanVien nv = new NhanVien(
                    rs.getInt("MaNhanVien"),
                    rs.getString("HoTen"),
                    rs.getString("SoDT"),
                    rs.getString("DiaChi"),
                    rs.getInt("MaCuaHang")
                );
                nv.setNgaySinh(rs.getString("NgaySinh"));
                nv.setTenCuaHang(rs.getString("DiaChiCH"));
                list.add(nv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<NhanVien> search(String keyword) {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT nv.*, ch.DiaChi AS DiaChiCH FROM NhanVien nv " +
                     "LEFT JOIN CuaHang ch ON nv.MaCuaHang = ch.MaCuaHang " +
                     "WHERE nv.HoTen LIKE ? OR nv.SoDT LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String kw = "%" + keyword + "%";
            ps.setString(1, kw);
            ps.setString(2, kw);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                NhanVien nv = new NhanVien(
                    rs.getInt("MaNhanVien"),
                    rs.getString("HoTen"),
                    rs.getString("SoDT"),
                    rs.getString("DiaChi"),
                    rs.getInt("MaCuaHang")
                );
                nv.setNgaySinh(rs.getString("NgaySinh"));
                nv.setTenCuaHang(rs.getString("DiaChiCH"));
                list.add(nv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(NhanVien nv) {
        String sql = "INSERT INTO NhanVien (HoTen, SoDT, NgaySinh, DiaChi, MaCuaHang) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nv.getHoTen());
            ps.setString(2, nv.getSoDT());
            ps.setString(3, nv.getNgaySinh());
            ps.setString(4, nv.getDiaChi());
            ps.setInt(5, nv.getMaCuaHang());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(NhanVien nv) {
        String sql = "UPDATE NhanVien SET HoTen=?, SoDT=?, NgaySinh=?, DiaChi=?, MaCuaHang=? WHERE MaNhanVien=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nv.getHoTen());
            ps.setString(2, nv.getSoDT());
            ps.setString(3, nv.getNgaySinh());
            ps.setString(4, nv.getDiaChi());
            ps.setInt(5, nv.getMaCuaHang());
            ps.setInt(6, nv.getMaNhanVien());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int maNV) {
        String sql = "DELETE FROM NhanVien WHERE MaNhanVien=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maNV);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
