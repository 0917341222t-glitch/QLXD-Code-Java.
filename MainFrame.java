package view;

import util.UITheme;
import view.panel.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainFrame extends JFrame {

    private JPanel contentArea;
    private CardLayout cardLayout;
    private JButton activeBtn = null;

    public MainFrame() {
        setTitle("🏗️ Hệ Thống Quản Lý VLXD");
        setSize(1280, 780);
        setMinimumSize(new Dimension(960, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(UITheme.BG_MAIN);

      
        JPanel sidebar = createSidebar();
        root.add(sidebar, BorderLayout.WEST);

       
        cardLayout = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setBackground(UITheme.BG_MAIN);

        contentArea.add(new DashboardPanel(), "dashboard");
        contentArea.add(new KhachHangPanel(), "khachhang");
        contentArea.add(new NhanVienPanel(), "nhanvien");
        contentArea.add(new HangHoaPanel(), "hanghoa");
        contentArea.add(new HoaDonPanel(), "hoadon");

        root.add(contentArea, BorderLayout.CENTER);
        setContentPane(root);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(UITheme.PRIMARY_DARK);
        sidebar.setPreferredSize(new Dimension(220, 0));

        // Logo
        JPanel logoPanel = new JPanel(new BorderLayout());
        logoPanel.setBackground(UITheme.PRIMARY_DARK);
        logoPanel.setBorder(new EmptyBorder(20, 16, 20, 16));
        logoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        JLabel lblLogo = new JLabel("🏗️");
        lblLogo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));

        JPanel logoText = new JPanel(new GridLayout(2, 1, 0, 2));
        logoText.setOpaque(false);
        JLabel lblName = new JLabel("VLXD Manager");
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblName.setForeground(Color.WHITE);
        JLabel lblSub = new JLabel("Chuỗi đại lý VLXD");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblSub.setForeground(new Color(0xAED6F1));
        logoText.add(lblName);
        logoText.add(lblSub);

        logoPanel.add(lblLogo, BorderLayout.WEST);
        logoPanel.add(logoText, BorderLayout.CENTER);
        sidebar.add(logoPanel);

        // Separator
        sidebar.add(createSeparator());

        // Nav label
        JLabel lblNav = new JLabel("  MENU CHÍNH");
        lblNav.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblNav.setForeground(new Color(0x5D8AA8));
        lblNav.setBorder(new EmptyBorder(12, 16, 6, 0));
        lblNav.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        sidebar.add(lblNav);

        // Nav buttons
        JButton btnDashboard = createNavButton("🏠  Tổng Quan",  "dashboard");
        JButton btnKhachHang = createNavButton("👥  Khách Hàng", "khachhang");
        JButton btnNhanVien  = createNavButton("👔  Nhân Viên",  "nhanvien");
        JButton btnHangHoa   = createNavButton("📦  Hàng Hóa",   "hanghoa");
        JButton btnHoaDon    = createNavButton("🧾  Hóa Đơn",    "hoadon");

        sidebar.add(btnDashboard);
        sidebar.add(btnKhachHang);
        sidebar.add(btnNhanVien);
        sidebar.add(btnHangHoa);
        sidebar.add(btnHoaDon);

        sidebar.add(Box.createVerticalGlue());

        // Bottom info
        sidebar.add(createSeparator());
        JLabel lblVersion = new JLabel("  v1.0 — Java Swing + MySQL");
        lblVersion.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblVersion.setForeground(new Color(0x4A6D85));
        lblVersion.setBorder(new EmptyBorder(10, 16, 16, 0));
        lblVersion.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        sidebar.add(lblVersion);

        // Activate dashboard by default
        setActive(btnDashboard);
        return sidebar;
    }

    private JButton createNavButton(String text, String cardName) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(new Color(0xBDC8D4));
        btn.setBackground(UITheme.PRIMARY_DARK);
        btn.setOpaque(true);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(12, 20, 12, 16));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (btn != activeBtn) btn.setBackground(new Color(0x163A52));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (btn != activeBtn) btn.setBackground(UITheme.PRIMARY_DARK);
            }
        });

        btn.addActionListener(e -> {
            setActive(btn);
            cardLayout.show(contentArea, cardName);
        });

        return btn;
    }

    private void setActive(JButton btn) {
        if (activeBtn != null) {
            activeBtn.setBackground(UITheme.PRIMARY_DARK);
            activeBtn.setForeground(new Color(0xBDC8D4));
            activeBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        }
        activeBtn = btn;
        btn.setBackground(UITheme.PRIMARY);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
    }

    private JSeparator createSeparator() {
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(0x1F5F8B));
        sep.setBackground(new Color(0x1F5F8B));
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return sep;
    }
}
