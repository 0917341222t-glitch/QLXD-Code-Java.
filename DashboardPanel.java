package view.panel;

import util.DBConnection;
import util.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;

public class DashboardPanel extends JPanel {

    public DashboardPanel() {
        setLayout(new BorderLayout(0, 0));
        setBackground(UITheme.BG_MAIN);
        initUI();
    }

    private void initUI() {
       
        JPanel titlePanel = UITheme.createTitlePanel(
            "🏠 Tổng Quan Hệ Thống",
            "Chuỗi Đại Lý Vật Liệu Xây Dựng — Dashboard"
        );
        add(titlePanel, BorderLayout.NORTH);

     
        JPanel content = new JPanel(new BorderLayout(0, 16));
        content.setBackground(UITheme.BG_MAIN);
        content.setBorder(new EmptyBorder(20, 20, 20, 20));

        
        JPanel statsRow = new JPanel(new GridLayout(1, 4, 16, 0));
        statsRow.setBackground(UITheme.BG_MAIN);

        int soKH      = getCount("SELECT COUNT(*) FROM KhachHang");
        int soNV      = getCount("SELECT COUNT(*) FROM NhanVien");
        int soHangHoa = getCount("SELECT COUNT(*) FROM DanhMucHangHoa");
        int soHoaDon  = getCount("SELECT COUNT(*) FROM HoaDonBanHang");

        statsRow.add(createStatCard("👥", "Khách Hàng", soKH, UITheme.PRIMARY));
        statsRow.add(createStatCard("👔", "Nhân Viên",  soNV, new Color(0x8E44AD)));
        statsRow.add(createStatCard("📦", "Hàng Hóa",  soHangHoa, UITheme.ACCENT));
        statsRow.add(createStatCard("🧾", "Hóa Đơn",   soHoaDon, UITheme.SUCCESS));

        content.add(statsRow, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel(new GridLayout(1, 2, 16, 0));
        infoPanel.setBackground(UITheme.BG_MAIN);

        infoPanel.add(createRecentPanel());
        infoPanel.add(createGuidePanel());

        content.add(infoPanel, BorderLayout.CENTER);
        add(content, BorderLayout.CENTER);
    }

    private JPanel createStatCard(String icon, String label, int value, Color color) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setBackground(UITheme.BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.BORDER_COLOR),
            new EmptyBorder(20, 20, 20, 20)
        ));

   
        JPanel bar = new JPanel();
        bar.setBackground(color);
        bar.setPreferredSize(new Dimension(0, 4));
        card.add(bar, BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(UITheme.BG_CARD);

        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        lblIcon.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblValue = new JLabel(String.valueOf(value));
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblValue.setForeground(color);
        lblValue.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(UITheme.FONT_LABEL);
        lblLabel.setForeground(UITheme.TEXT_MUTED);
        lblLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        center.add(lblIcon);
        center.add(Box.createVerticalStrut(8));
        center.add(lblValue);
        center.add(Box.createVerticalStrut(4));
        center.add(lblLabel);

        card.add(center, BorderLayout.CENTER);
        return card;
    }

    private JPanel createRecentPanel() {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(UITheme.BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.BORDER_COLOR),
            new EmptyBorder(16, 16, 16, 16)
        ));

        JLabel title = new JLabel("📊 Thống Kê Nhanh");
        title.setFont(UITheme.FONT_SUBTITLE);
        title.setForeground(UITheme.PRIMARY);
        card.add(title, BorderLayout.NORTH);

        JPanel rows = new JPanel();
        rows.setLayout(new BoxLayout(rows, BoxLayout.Y_AXIS));
        rows.setBackground(UITheme.BG_CARD);

        // Tổng tiền hóa đơn
        double tongDoanhThu = getSum("SELECT IFNULL(SUM(ct.SoLuong * ct.DonGia), 0) FROM CT_HoaDonBanHang ct");
        int soNhaCungCap  = getCount("SELECT COUNT(*) FROM NhaCungCap");
        int soCuaHang     = getCount("SELECT COUNT(*) FROM CuaHang");
        int soKho         = getCount("SELECT COUNT(*) FROM DanhMucKho");

        addStatRow(rows, "💰", "Tổng doanh thu", String.format("%,.0f đ", tongDoanhThu));
        addStatRow(rows, "🏪", "Số cửa hàng",   String.valueOf(soCuaHang));
        addStatRow(rows, "🏭", "Nhà cung cấp",  String.valueOf(soNhaCungCap));
        addStatRow(rows, "🏗️", "Kho hàng",      String.valueOf(soKho));

        card.add(rows, BorderLayout.CENTER);
        return card;
    }

    private JPanel createGuidePanel() {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(UITheme.BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.BORDER_COLOR),
            new EmptyBorder(16, 16, 16, 16)
        ));

        JLabel title = new JLabel("📋 Hướng Dẫn Sử Dụng");
        title.setFont(UITheme.FONT_SUBTITLE);
        title.setForeground(UITheme.PRIMARY);
        card.add(title, BorderLayout.NORTH);

        JPanel rows = new JPanel();
        rows.setLayout(new BoxLayout(rows, BoxLayout.Y_AXIS));
        rows.setBackground(UITheme.BG_CARD);

        addGuideRow(rows, "👥 Khách Hàng",  "Quản lý danh sách, thêm/sửa/xóa, tìm kiếm");
        addGuideRow(rows, "👔 Nhân Viên",   "Xem nhân viên theo cửa hàng, cập nhật thông tin");
        addGuideRow(rows, "📦 Hàng Hóa",   "Danh mục VLXD, xem tồn kho theo từng sản phẩm");
        addGuideRow(rows, "🧾 Hóa Đơn",    "Xem hóa đơn, tìm kiếm, double-click để xem chi tiết");

        card.add(rows, BorderLayout.CENTER);
        return card;
    }

    private void addStatRow(JPanel parent, String icon, String label, String value) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setBackground(UITheme.BG_CARD);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER_COLOR));

        JLabel lIcon = new JLabel(icon + "  " + label);
        lIcon.setFont(UITheme.FONT_BODY);
        lIcon.setForeground(UITheme.TEXT_MAIN);

        JLabel lVal = new JLabel(value);
        lVal.setFont(UITheme.FONT_HEADER);
        lVal.setForeground(UITheme.PRIMARY);

        row.add(lIcon, BorderLayout.WEST);
        row.add(lVal, BorderLayout.EAST);

        parent.add(row);
        parent.add(Box.createVerticalStrut(8));
    }

    private void addGuideRow(JPanel parent, String title, String desc) {
        JPanel row = new JPanel(new BorderLayout(0, 2));
        row.setBackground(new Color(0xF8F9FB));
        row.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.BORDER_COLOR),
            new EmptyBorder(8, 12, 8, 12)
        ));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JLabel lTitle = new JLabel(title);
        lTitle.setFont(UITheme.FONT_HEADER);
        lTitle.setForeground(UITheme.PRIMARY);

        JLabel lDesc = new JLabel(desc);
        lDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lDesc.setForeground(UITheme.TEXT_MUTED);

        row.add(lTitle, BorderLayout.NORTH);
        row.add(lDesc, BorderLayout.CENTER);

        parent.add(row);
        parent.add(Box.createVerticalStrut(6));
    }

    private int getCount(String sql) {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { /* DB chưa kết nối */ }
        return 0;
    }

    private double getSum(String sql) {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble(1);
        } catch (Exception e) { /* ignore */ }
        return 0;
    }
}
