package view.panel;

import dao.NhanVienDAO;
import model.NhanVien;
import util.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class NhanVienPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private NhanVienDAO dao = new NhanVienDAO();

    private JTextField txtMaNV, txtHoTen, txtSoDT, txtNgaySinh, txtDiaChi, txtMaCuaHang;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi;

    public NhanVienPanel() {
        setLayout(new BorderLayout(0, 0));
        setBackground(UITheme.BG_MAIN);
        initUI();
        loadData();
    }

    private void initUI() {
        add(UITheme.createTitlePanel("👔 Quản Lý Nhân Viên",
                "Danh sách nhân viên các cửa hàng"), BorderLayout.NORTH);
        JPanel centerPanel = new JPanel(new BorderLayout(12, 0));
        centerPanel.setBackground(UITheme.BG_MAIN);
        centerPanel.setBorder(new EmptyBorder(16, 16, 16, 16));
        centerPanel.add(createTableSection(), BorderLayout.CENTER);
        centerPanel.add(createFormSection(), BorderLayout.EAST);
        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createTableSection() {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(UITheme.BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.BORDER_COLOR),
            new EmptyBorder(12, 12, 12, 12)
        ));

        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        searchBar.setBackground(UITheme.BG_CARD);
        txtSearch = UITheme.createSearchField("🔍 Tìm theo tên, số điện thoại..."); txtSearch.addActionListener(e -> searchData()); txtSearch.addActionListener(e -> searchData());
        JButton btnSearch = UITheme.createButton("Tìm kiếm", UITheme.PRIMARY);
        JButton btnAll    = UITheme.createButton("Tất cả", UITheme.TEXT_MUTED);
        btnSearch.addActionListener(e -> searchData());
        btnAll.addActionListener(e -> loadData());
        searchBar.add(txtSearch); searchBar.add(btnSearch); searchBar.add(btnAll);
        card.add(searchBar, BorderLayout.NORTH);

        // Thêm cột NgaySinh
        String[] cols = {"Mã NV", "Họ Tên", "Số ĐT", "Ngày Sinh", "Địa Chỉ", "Cửa Hàng"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        UITheme.styleTable(table);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) fillFormFromTable();
        });

        int[] widths = {60, 150, 110, 100, 160, 130};
        for (int i = 0; i < widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER_COLOR));
        card.add(scroll, BorderLayout.CENTER);
        return card;
    }

    private JPanel createFormSection() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UITheme.BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.BORDER_COLOR),
            new EmptyBorder(16, 16, 16, 16)
        ));
        card.setPreferredSize(new Dimension(280, 0));

        JLabel lblTitle = new JLabel("Chi tiết nhân viên");
        lblTitle.setFont(UITheme.FONT_SUBTITLE);
        lblTitle.setForeground(UITheme.PRIMARY);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblTitle);
        card.add(Box.createVerticalStrut(14));

        txtMaNV      = addField(card, "Mã NV");
        txtMaNV.setEditable(false);
        txtMaNV.setBackground(new Color(0xF0F3F6));
        txtHoTen     = addField(card, "Họ Tên *");
        txtSoDT      = addField(card, "Số Điện Thoại *");
        txtNgaySinh  = addField(card, "Ngày Sinh (YYYY-MM-DD)");
        txtDiaChi    = addField(card, "Địa Chỉ");
        txtMaCuaHang = addField(card, "Mã Cửa Hàng");

        card.add(Box.createVerticalStrut(20));

        JPanel btnPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        btnPanel.setBackground(UITheme.BG_CARD);
        btnPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        btnPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnThem   = UITheme.createButton("➕ Thêm",    UITheme.SUCCESS);
        btnSua    = UITheme.createButton("✏️ Sửa",    UITheme.ACCENT);
        btnXoa    = UITheme.createButton("🗑️ Xóa",    UITheme.DANGER);
        btnLamMoi = UITheme.createButton("🔄 Làm mới", UITheme.PRIMARY);

        btnThem.addActionListener(e -> insertNV());
        btnSua.addActionListener(e -> updateNV());
        btnXoa.addActionListener(e -> deleteNV());
        btnLamMoi.addActionListener(e -> clearForm());

        btnPanel.add(btnThem); btnPanel.add(btnSua);
        btnPanel.add(btnXoa); btnPanel.add(btnLamMoi);
        card.add(btnPanel);
        return card;
    }

    private JTextField addField(JPanel parent, String label) {
        JLabel lbl = UITheme.createLabel(label);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        parent.add(lbl);
        parent.add(Box.createVerticalStrut(4));
        JTextField field = UITheme.createInput();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        parent.add(field);
        parent.add(Box.createVerticalStrut(10));
        return field;
    }

    private void loadData() {
        tableModel.setRowCount(0);
        for (NhanVien nv : dao.getAll()) {
            tableModel.addRow(new Object[]{
                nv.getMaNhanVien(), nv.getHoTen(), nv.getSoDT(),
                nv.getNgaySinh(), nv.getDiaChi(), nv.getTenCuaHang()
            });
        }
    }

    private void searchData() {
        String kw = txtSearch.getText().trim();
        if (kw.isEmpty() || kw.startsWith("🔍")) { loadData(); return; }
        tableModel.setRowCount(0);
        for (NhanVien nv : dao.search(kw)) {
            tableModel.addRow(new Object[]{
                nv.getMaNhanVien(), nv.getHoTen(), nv.getSoDT(),
                nv.getNgaySinh(), nv.getDiaChi(), nv.getTenCuaHang()
            });
        }
    }

    private void fillFormFromTable() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        txtMaNV.setText(tableModel.getValueAt(row, 0).toString());
        txtHoTen.setText(tableModel.getValueAt(row, 1).toString());
        txtSoDT.setText(tableModel.getValueAt(row, 2) != null ? tableModel.getValueAt(row, 2).toString() : "");
        txtNgaySinh.setText(tableModel.getValueAt(row, 3) != null ? tableModel.getValueAt(row, 3).toString() : "");
        txtDiaChi.setText(tableModel.getValueAt(row, 4) != null ? tableModel.getValueAt(row, 4).toString() : "");
        txtMaCuaHang.setText("");
    }

    private void insertNV() {
        if (txtHoTen.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Họ Tên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            int maCH = txtMaCuaHang.getText().trim().isEmpty() ? 0 : Integer.parseInt(txtMaCuaHang.getText().trim());
            NhanVien nv = new NhanVien(0, txtHoTen.getText().trim(), txtSoDT.getText().trim(), txtDiaChi.getText().trim(), maCH);
            nv.setNgaySinh(txtNgaySinh.getText().trim());
            if (dao.insert(nv)) { JOptionPane.showMessageDialog(this, "✅ Thêm thành công!"); loadData(); clearForm(); }
            else JOptionPane.showMessageDialog(this, "❌ Thêm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Mã cửa hàng phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateNV() {
        if (txtMaNV.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên từ bảng!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int maCH = txtMaCuaHang.getText().trim().isEmpty() ? 0 : Integer.parseInt(txtMaCuaHang.getText().trim());
            NhanVien nv = new NhanVien(Integer.parseInt(txtMaNV.getText()), txtHoTen.getText().trim(), txtSoDT.getText().trim(), txtDiaChi.getText().trim(), maCH);
            nv.setNgaySinh(txtNgaySinh.getText().trim());
            if (dao.update(nv)) { JOptionPane.showMessageDialog(this, "✅ Cập nhật thành công!"); loadData(); clearForm(); }
            else JOptionPane.showMessageDialog(this, "❌ Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Mã cửa hàng phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteNV() {
        if (txtMaNV.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên từ bảng!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa nhân viên này?",
            "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.delete(Integer.parseInt(txtMaNV.getText()))) {
                JOptionPane.showMessageDialog(this, "✅ Xóa thành công!"); loadData(); clearForm();
            } else JOptionPane.showMessageDialog(this, "❌ Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        txtMaNV.setText(""); txtHoTen.setText(""); txtSoDT.setText("");
        txtNgaySinh.setText(""); txtDiaChi.setText(""); txtMaCuaHang.setText("");
        table.clearSelection();
    }
}
