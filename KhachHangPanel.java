package view.panel;

import dao.KhachHangDAO;
import model.KhachHang;
import util.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class KhachHangPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private KhachHangDAO dao = new KhachHangDAO();

    // Form fields
    private JTextField txtMaKH, txtHoTen, txtSDT, txtDiaChi, txtEmail;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi;

    public KhachHangPanel() {
        setLayout(new BorderLayout(0, 0));
        setBackground(UITheme.BG_MAIN);
        initUI();
        loadData();
    }

    private void initUI() {
        // ===== TOP: Tiêu đề =====
        add(UITheme.createTitlePanel("👥 Quản Lý Khách Hàng",
                "Danh sách khách hàng của chuỗi đại lý VLXD"), BorderLayout.NORTH);

        // ===== CENTER: Table + Form =====
        JPanel centerPanel = new JPanel(new BorderLayout(12, 0));
        centerPanel.setBackground(UITheme.BG_MAIN);
        centerPanel.setBorder(new EmptyBorder(16, 16, 16, 16));

        // --- Bảng dữ liệu ---
        JPanel tableCard = createTableSection();
        centerPanel.add(tableCard, BorderLayout.CENTER);

        // --- Form nhập liệu ---
        JPanel formCard = createFormSection();
        centerPanel.add(formCard, BorderLayout.EAST);

        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createTableSection() {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(UITheme.BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.BORDER_COLOR),
            new EmptyBorder(12, 12, 12, 12)
        ));

        // Search bar
        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        searchBar.setBackground(UITheme.BG_CARD);
        txtSearch = UITheme.createSearchField("🔍 Tìm theo tên, SĐT, địa chỉ..."); txtSearch.addActionListener(e -> searchData()); txtSearch.addActionListener(e -> searchData()); txtSearch.addActionListener(e -> searchData());
        JButton btnSearch = UITheme.createButton("Tìm kiếm", UITheme.PRIMARY);
        JButton btnAll    = UITheme.createButton("Tất cả", UITheme.TEXT_MUTED);
        btnSearch.addActionListener(e -> searchData());
        btnAll.addActionListener(e -> loadData());
        searchBar.add(txtSearch);
        searchBar.add(btnSearch);
        searchBar.add(btnAll);
        card.add(searchBar, BorderLayout.NORTH);

        // Table
        String[] cols = {"Mã KH", "Họ Tên", "Số Điện Thoại", "Địa Chỉ", "Email"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        UITheme.styleTable(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) fillFormFromTable();
        });

        // Column widths
        int[] widths = {60, 160, 120, 200, 150};
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

        JLabel lblTitle = new JLabel("Chi tiết khách hàng");
        lblTitle.setFont(UITheme.FONT_SUBTITLE);
        lblTitle.setForeground(UITheme.PRIMARY);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblTitle);
        card.add(Box.createVerticalStrut(14));

        // Fields
        txtMaKH  = addField(card, "Mã KH");
        txtMaKH.setEditable(false);
        txtMaKH.setBackground(new Color(0xF0F3F6));
        txtHoTen = addField(card, "Họ Tên *");
        txtSDT   = addField(card, "Số Điện Thoại *");
        txtDiaChi= addField(card, "Địa Chỉ");
        txtEmail = addField(card, "Email");

        card.add(Box.createVerticalStrut(20));

        // Buttons
        JPanel btnPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        btnPanel.setBackground(UITheme.BG_CARD);
        btnPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        btnPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnThem   = UITheme.createButton("➕ Thêm",    UITheme.SUCCESS);
        btnSua    = UITheme.createButton("✏️ Sửa",    UITheme.ACCENT);
        btnXoa    = UITheme.createButton("🗑️ Xóa",    UITheme.DANGER);
        btnLamMoi = UITheme.createButton("🔄 Làm mới", UITheme.PRIMARY);

        btnThem.addActionListener(e -> insertKhachHang());
        btnSua.addActionListener(e -> updateKhachHang());
        btnXoa.addActionListener(e -> deleteKhachHang());
        btnLamMoi.addActionListener(e -> clearForm());

        btnPanel.add(btnThem);
        btnPanel.add(btnSua);
        btnPanel.add(btnXoa);
        btnPanel.add(btnLamMoi);
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

    // ===== LOGIC =====
    private void loadData() {
        tableModel.setRowCount(0);
        for (KhachHang kh : dao.getAll()) {
            tableModel.addRow(new Object[]{
                kh.getMaKH(), kh.getHoTen(), kh.getSoDienThoai(), kh.getDiaChi(), kh.getEmail()
            });
        }
    }

    private void searchData() {
        String kw = txtSearch.getText().trim();
        if (kw.isEmpty() || kw.startsWith("🔍")) { loadData(); return; }
        tableModel.setRowCount(0);
        for (KhachHang kh : dao.search(kw)) {
            tableModel.addRow(new Object[]{
                kh.getMaKH(), kh.getHoTen(), kh.getSoDienThoai(), kh.getDiaChi(), kh.getEmail()
            });
        }
    }

    private void fillFormFromTable() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        txtMaKH.setText(tableModel.getValueAt(row, 0).toString());
        txtHoTen.setText(tableModel.getValueAt(row, 1).toString());
        txtSDT.setText(tableModel.getValueAt(row, 2) != null ? tableModel.getValueAt(row, 2).toString() : "");
        txtDiaChi.setText(tableModel.getValueAt(row, 3) != null ? tableModel.getValueAt(row, 3).toString() : "");
        txtEmail.setText(tableModel.getValueAt(row, 4) != null ? tableModel.getValueAt(row, 4).toString() : "");
    }

    private void insertKhachHang() {
        if (txtHoTen.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Họ Tên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        KhachHang kh = new KhachHang(0, txtHoTen.getText().trim(),
            txtSDT.getText().trim(), txtDiaChi.getText().trim(), txtEmail.getText().trim());
        if (dao.insert(kh)) {
            JOptionPane.showMessageDialog(this, "✅ Thêm thành công!");
            loadData(); clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Thêm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateKhachHang() {
        if (txtMaKH.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một khách hàng từ bảng!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        KhachHang kh = new KhachHang(Integer.parseInt(txtMaKH.getText()),
            txtHoTen.getText().trim(), txtSDT.getText().trim(),
            txtDiaChi.getText().trim(), txtEmail.getText().trim());
        if (dao.update(kh)) {
            JOptionPane.showMessageDialog(this, "✅ Cập nhật thành công!");
            loadData(); clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteKhachHang() {
        if (txtMaKH.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một khách hàng từ bảng!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc muốn xóa khách hàng này?", "Xác nhận xóa",
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.delete(Integer.parseInt(txtMaKH.getText()))) {
                JOptionPane.showMessageDialog(this, "✅ Xóa thành công!");
                loadData(); clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        txtMaKH.setText(""); txtHoTen.setText("");
        txtSDT.setText(""); txtDiaChi.setText(""); txtEmail.setText("");
        table.clearSelection();
    }
}
