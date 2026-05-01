package view.panel;

import dao.HangHoaDAO;
import model.HangHoa;
import util.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class HangHoaPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private HangHoaDAO dao = new HangHoaDAO();

    private JTextField txtMaHH, txtTenHH, txtDonVi, txtGiaBan, txtMoTa;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnTonKho;

    private static final NumberFormat currencyFmt = NumberFormat.getNumberInstance(new Locale("vi", "VN"));

    public HangHoaPanel() {
        setLayout(new BorderLayout(0, 0));
        setBackground(UITheme.BG_MAIN);
        initUI();
        loadData();
    }

    private void initUI() {
        add(UITheme.createTitlePanel("📦 Quản Lý Hàng Hóa",
                "Danh mục vật liệu xây dựng"), BorderLayout.NORTH);

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
        txtSearch = UITheme.createSearchField("🔍 Tìm theo tên hàng hóa, đơn vị..."); txtSearch.addActionListener(e -> searchData()); txtSearch.addActionListener(e -> searchData()); txtSearch.addActionListener(e -> searchData());
	txtSearch.addActionListener(e -> searchData());
        JButton btnSearch = UITheme.createButton("Tìm kiếm", UITheme.PRIMARY);
        JButton btnAll    = UITheme.createButton("Tất cả", UITheme.TEXT_MUTED);
        btnTonKho         = UITheme.createButton("📊 Tồn Kho", new Color(0x8E44AD));

        btnSearch.addActionListener(e -> searchData());
        btnAll.addActionListener(e -> loadData());
        btnTonKho.addActionListener(e -> showTonKho());

        searchBar.add(txtSearch); searchBar.add(btnSearch);
        searchBar.add(btnAll); searchBar.add(btnTonKho);
        card.add(searchBar, BorderLayout.NORTH);

        String[] cols = {"Mã HH", "Tên Hàng Hóa", "Đơn Vị", "Giá Bán (VNĐ)", "Mô Tả"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        UITheme.styleTable(table);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) fillFormFromTable();
        });

        int[] widths = {60, 200, 80, 130, 200};
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

        JLabel lblTitle = new JLabel("Chi tiết hàng hóa");
        lblTitle.setFont(UITheme.FONT_SUBTITLE);
        lblTitle.setForeground(UITheme.PRIMARY);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblTitle);
        card.add(Box.createVerticalStrut(14));

        txtMaHH  = addField(card, "Mã HH");
        txtMaHH.setEditable(false);
        txtMaHH.setBackground(new Color(0xF0F3F6));
        txtTenHH = addField(card, "Tên Hàng Hóa *");
        txtDonVi = addField(card, "Đơn Vị (kg/bao/m²...)");
        txtGiaBan= addField(card, "Giá Bán (VNĐ)");
        txtMoTa  = addField(card, "Mô Tả");

        card.add(Box.createVerticalStrut(20));

        JPanel btnPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        btnPanel.setBackground(UITheme.BG_CARD);
        btnPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        btnPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnThem   = UITheme.createButton("➕ Thêm",    UITheme.SUCCESS);
        btnSua    = UITheme.createButton("✏️ Sửa",    UITheme.ACCENT);
        btnXoa    = UITheme.createButton("🗑️ Xóa",    UITheme.DANGER);
        btnLamMoi = UITheme.createButton("🔄 Làm mới", UITheme.PRIMARY);

        btnThem.addActionListener(e -> insertHH());
        btnSua.addActionListener(e -> updateHH());
        btnXoa.addActionListener(e -> deleteHH());
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
        for (HangHoa hh : dao.getAll()) {
            tableModel.addRow(new Object[]{
                hh.getMaHangHoa(), hh.getTenHangHoa(), hh.getDonVi(),
                currencyFmt.format(hh.getGiaBan()), hh.getMoTa()
            });
        }
    }

    private void searchData() {
        String kw = txtSearch.getText().trim();
        if (kw.isEmpty() || kw.startsWith("🔍")) { loadData(); return; }
        tableModel.setRowCount(0);
        for (HangHoa hh : dao.search(kw)) {
            tableModel.addRow(new Object[]{
                hh.getMaHangHoa(), hh.getTenHangHoa(), hh.getDonVi(),
                currencyFmt.format(hh.getGiaBan()), hh.getMoTa()
            });
        }
    }

    private void showTonKho() {
        List<Object[]> data = dao.getTonKho();

        // Tạo dialog hiện thị tồn kho
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "📊 Tình Trạng Tồn Kho", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);

        String[] cols = {"Tên Hàng Hóa", "Tổng Tồn Kho"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        for (Object[] row : data) model.addRow(row);

        JTable tbl = new JTable(model);
        UITheme.styleTable(tbl);

        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));
        panel.setBackground(UITheme.BG_MAIN);

        JLabel lbl = new JLabel("Tổng tồn kho theo từng sản phẩm");
        lbl.setFont(UITheme.FONT_SUBTITLE);
        lbl.setForeground(UITheme.PRIMARY);
        panel.add(lbl, BorderLayout.NORTH);
        panel.add(new JScrollPane(tbl), BorderLayout.CENTER);

        JButton btnClose = UITheme.createButton("Đóng", UITheme.PRIMARY);
        btnClose.addActionListener(e -> dialog.dispose());
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(UITheme.BG_MAIN);
        bottom.add(btnClose);
        panel.add(bottom, BorderLayout.SOUTH);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void fillFormFromTable() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        txtMaHH.setText(tableModel.getValueAt(row, 0).toString());
        txtTenHH.setText(tableModel.getValueAt(row, 1).toString());
        txtDonVi.setText(tableModel.getValueAt(row, 2) != null ? tableModel.getValueAt(row, 2).toString() : "");
        // Giá bán được format, cần lấy lại từ DB — đơn giản để trống hoặc lấy lại từ danh sách
        txtGiaBan.setText("");
        txtMoTa.setText(tableModel.getValueAt(row, 4) != null ? tableModel.getValueAt(row, 4).toString() : "");
    }

    private void insertHH() {
        if (txtTenHH.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Tên hàng hóa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            double gia = txtGiaBan.getText().trim().isEmpty() ? 0 : Double.parseDouble(txtGiaBan.getText().trim());
            HangHoa hh = new HangHoa(0, txtTenHH.getText().trim(), txtDonVi.getText().trim(), gia, txtMoTa.getText().trim());
            if (dao.insert(hh)) { JOptionPane.showMessageDialog(this, "✅ Thêm thành công!"); loadData(); clearForm(); }
            else JOptionPane.showMessageDialog(this, "❌ Thêm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Giá bán phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateHH() {
        if (txtMaHH.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hàng hóa từ bảng!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            double gia = txtGiaBan.getText().trim().isEmpty() ? 0 : Double.parseDouble(txtGiaBan.getText().trim());
            HangHoa hh = new HangHoa(Integer.parseInt(txtMaHH.getText()), txtTenHH.getText().trim(), txtDonVi.getText().trim(), gia, txtMoTa.getText().trim());
            if (dao.update(hh)) { JOptionPane.showMessageDialog(this, "✅ Cập nhật thành công!"); loadData(); clearForm(); }
            else JOptionPane.showMessageDialog(this, "❌ Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Giá bán phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteHH() {
        if (txtMaHH.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hàng hóa từ bảng!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa hàng hóa này?",
            "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.delete(Integer.parseInt(txtMaHH.getText()))) {
                JOptionPane.showMessageDialog(this, "✅ Xóa thành công!"); loadData(); clearForm();
            } else JOptionPane.showMessageDialog(this, "❌ Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        txtMaHH.setText(""); txtTenHH.setText(""); txtDonVi.setText("");
        txtGiaBan.setText(""); txtMoTa.setText("");
        table.clearSelection();
    }
}
