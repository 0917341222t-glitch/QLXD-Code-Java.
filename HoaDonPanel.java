package view.panel;

import dao.HoaDonDAO;
import model.HoaDon;
import util.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class HoaDonPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private HoaDonDAO dao = new HoaDonDAO();

    private static final NumberFormat currencyFmt = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
    private static final SimpleDateFormat dateFmt = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public HoaDonPanel() {
        setLayout(new BorderLayout(0, 0));
        setBackground(UITheme.BG_MAIN);
        initUI();
        loadData();
    }

    private void initUI() {
        add(UITheme.createTitlePanel("🧾 Hóa Đơn Bán Hàng",
                "Danh sách hóa đơn toàn bộ chuỗi cửa hàng"), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 0));
        centerPanel.setBackground(UITheme.BG_MAIN);
        centerPanel.setBorder(new EmptyBorder(16, 16, 16, 16));

        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(UITheme.BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.BORDER_COLOR),
            new EmptyBorder(12, 12, 12, 12)
        ));

        // Search bar
        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        searchBar.setBackground(UITheme.BG_CARD);
        txtSearch = UITheme.createSearchField("🔍 Tìm theo tên khách hàng, nhân viên...");
        txtSearch.addActionListener(e -> searchData());
        JButton btnSearch = UITheme.createButton("Tìm kiếm", UITheme.PRIMARY);
        JButton btnAll    = UITheme.createButton("Tất cả", UITheme.TEXT_MUTED);
        JButton btnRefresh = UITheme.createButton("🔄 Làm mới", UITheme.SUCCESS);

        btnSearch.addActionListener(e -> searchData());
        btnAll.addActionListener(e -> loadData());
        btnRefresh.addActionListener(e -> loadData());

        searchBar.add(txtSearch);
        searchBar.add(btnSearch);
        searchBar.add(btnAll);
        searchBar.add(btnRefresh);
        card.add(searchBar, BorderLayout.NORTH);

        // Table
        String[] cols = {"Mã HD", "Thời Gian", "Khách Hàng", "Nhân Viên", "Tổng Tiền (VNĐ)"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        UITheme.styleTable(table);

        int[] widths = {70, 130, 180, 160, 160};
        for (int i = 0; i < widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        // Highlight tổng tiền màu xanh
        table.getColumnModel().getColumn(4).setCellRenderer(
            new javax.swing.table.DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable t, Object v,
                        boolean sel, boolean foc, int row, int col) {
                    super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                    setForeground(sel ? UITheme.TEXT_MAIN : UITheme.SUCCESS);
                    setFont(UITheme.FONT_HEADER);
                    setHorizontalAlignment(SwingConstants.RIGHT);
                    setBorder(new EmptyBorder(0, 8, 0, 12));
                    setBackground(sel ? UITheme.ROW_SELECTED : (row % 2 == 0 ? UITheme.ROW_EVEN : UITheme.ROW_ODD));
                    return this;
                }
            }
        );

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER_COLOR));
        card.add(scroll, BorderLayout.CENTER);

        // Summary bar
        JPanel summaryBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 4));
        summaryBar.setBackground(UITheme.BG_CARD);
        summaryBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UITheme.BORDER_COLOR));
        JLabel lblSummary = new JLabel("Double-click vào hóa đơn để xem chi tiết");
        lblSummary.setFont(UITheme.FONT_BODY);
        lblSummary.setForeground(UITheme.TEXT_MUTED);
        summaryBar.add(lblSummary);
        card.add(summaryBar, BorderLayout.SOUTH);

        centerPanel.add(card, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // Double click xem chi tiết
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) showDetailDialog();
            }
        });
    }

    private void loadData() {
        tableModel.setRowCount(0);
        for (HoaDon hd : dao.getAll()) {
            tableModel.addRow(new Object[]{
                hd.getMaHoaDon(),
                hd.getNgayLap() != null ? dateFmt.format(hd.getNgayLap()) : "",
                hd.getTenKH() != null ? hd.getTenKH() : "",
                hd.getTenNV() != null ? hd.getTenNV() : "",
                currencyFmt.format(hd.getTongTien()) + " đ"
            });
        }
    }

    private void searchData() {
        String kw = txtSearch.getText().trim();
        if (kw.isEmpty() || kw.startsWith("🔍")) { loadData(); return; }
        tableModel.setRowCount(0);
        for (HoaDon hd : dao.search(kw)) {
            tableModel.addRow(new Object[]{
                hd.getMaHoaDon(),
                hd.getNgayLap() != null ? dateFmt.format(hd.getNgayLap()) : "",
                hd.getTenKH() != null ? hd.getTenKH() : "",
                hd.getTenNV() != null ? hd.getTenNV() : "",
                currencyFmt.format(hd.getTongTien()) + " đ"
            });
        }
    }

    private void showDetailDialog() {
        int row = table.getSelectedRow();
        if (row < 0) return;

        String maHD      = tableModel.getValueAt(row, 0).toString();
        String thoiGian  = tableModel.getValueAt(row, 1).toString();
        String khachHang = tableModel.getValueAt(row, 2).toString();
        String nhanVien  = tableModel.getValueAt(row, 3).toString();
        String tongTien  = tableModel.getValueAt(row, 4).toString();
        String giamGia   = dao.getChiTietGiamGia(Integer.parseInt(maHD));

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                "🧾 Chi Tiết Hóa Đơn #" + maHD, true);
        dialog.setSize(440, 360);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 24, 20, 24));
        panel.setBackground(UITheme.BG_CARD);

        JLabel title = new JLabel("HÓA ĐƠN BÁN HÀNG");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(UITheme.PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalStrut(16));

        addInfoRow(panel, "Mã hóa đơn", "#" + maHD);
        addInfoRow(panel, "Thời gian", thoiGian);
        addInfoRow(panel, "Khách hàng", khachHang);
        addInfoRow(panel, "Nhân viên", nhanVien);
        addInfoRow(panel, "Giảm giá", giamGia);

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setForeground(UITheme.BORDER_COLOR);
        panel.add(Box.createVerticalStrut(8));
        panel.add(sep);
        panel.add(Box.createVerticalStrut(8));

        JPanel totalRow = new JPanel(new BorderLayout());
        totalRow.setBackground(UITheme.BG_CARD);
        totalRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        JLabel lTong = new JLabel("TỔNG TIỀN (sau giảm)");
        lTong.setFont(UITheme.FONT_HEADER);
        lTong.setForeground(UITheme.TEXT_MAIN);
        JLabel vTong = new JLabel(tongTien);
        vTong.setFont(new Font("Segoe UI", Font.BOLD, 16));
        vTong.setForeground(UITheme.SUCCESS);
        totalRow.add(lTong, BorderLayout.WEST);
        totalRow.add(vTong, BorderLayout.EAST);
        panel.add(totalRow);
        panel.add(Box.createVerticalStrut(20));

        JButton btnClose = UITheme.createButton("Đóng", UITheme.PRIMARY);
        btnClose.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnClose.addActionListener(e -> dialog.dispose());
        panel.add(btnClose);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void addInfoRow(JPanel parent, String label, String value) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setBackground(UITheme.BG_CARD);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
        JLabel lbl = UITheme.createLabel(label + ":");
        lbl.setForeground(UITheme.TEXT_MUTED);
        lbl.setPreferredSize(new Dimension(130, 24));
        JLabel val = UITheme.createLabel(value != null ? value : "—");
        val.setForeground(UITheme.TEXT_MAIN);
        row.add(lbl, BorderLayout.WEST);
        row.add(val, BorderLayout.CENTER);
        parent.add(row);
        parent.add(Box.createVerticalStrut(4));
    }
}
