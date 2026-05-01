package util;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class UITheme {


    public static final Color PRIMARY       = new Color(0x1A4F72); 
    public static final Color PRIMARY_DARK  = new Color(0x0D2D42);
    public static final Color ACCENT        = new Color(0xE67E22);   
    public static final Color ACCENT_HOVER  = new Color(0xCA6F1E);
    public static final Color SUCCESS       = new Color(0x27AE60);
    public static final Color DANGER        = new Color(0xC0392B);
    public static final Color BG_MAIN       = new Color(0xF5F6FA);
    public static final Color BG_CARD       = Color.WHITE;
    public static final Color TEXT_MAIN     = new Color(0x2C3E50);
    public static final Color TEXT_MUTED    = new Color(0x7F8C8D);
    public static final Color BORDER_COLOR  = new Color(0xDDE1E7);
    public static final Color ROW_EVEN      = new Color(0xF8F9FB);
    public static final Color ROW_ODD       = Color.WHITE;
    public static final Color ROW_SELECTED  = new Color(0xD6EAF8);

    public static final Font FONT_TITLE     = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font FONT_SUBTITLE  = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_LABEL     = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BODY      = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BTN       = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_TABLE     = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_HEADER    = new Font("Segoe UI", Font.BOLD, 13);

  
    public static JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BTN);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(120, 36));
        btn.setBorder(new EmptyBorder(6, 16, 6, 16));

      
        Color hoverColor = bg.darker();
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(hoverColor); }
            public void mouseExited(java.awt.event.MouseEvent e) { btn.setBackground(bg); }
        });
        return btn;
    }

    public static JTextField createSearchField(String placeholder) {
        JTextField field = new JTextField(placeholder, 20) {
            boolean placeholderVisible = true;
            {
                setForeground(TEXT_MUTED);
                addFocusListener(new java.awt.event.FocusAdapter() {
                    public void focusGained(java.awt.event.FocusEvent e) {
                        if (placeholderVisible) { setText(""); setForeground(TEXT_MAIN); placeholderVisible = false; }
                    }
                    public void focusLost(java.awt.event.FocusEvent e) {
                        if (getText().isEmpty()) { setText(placeholder); setForeground(TEXT_MUTED); placeholderVisible = true; }
                    }
                });
            }
        };
        field.setFont(FONT_BODY);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(6, 10, 6, 10)
        ));
        field.setPreferredSize(new Dimension(260, 36));
        return field;
    }

    // ===== STYLE TABLE =====
    public static void styleTable(JTable table) {
        table.setFont(FONT_TABLE);
        table.setRowHeight(32);
        table.setGridColor(BORDER_COLOR);
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));
        table.setSelectionBackground(ROW_SELECTED);
        table.setSelectionForeground(TEXT_MAIN);
        table.setBackground(Color.WHITE);
        table.setFillsViewportHeight(true);

        // Alternate row colors
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setBorder(new EmptyBorder(0, 8, 0, 8));
                if (sel) {
                    setBackground(ROW_SELECTED);
                } else {
                    setBackground(row % 2 == 0 ? ROW_EVEN : ROW_ODD);
                }
                setForeground(TEXT_MAIN);
                return this;
            }
        });

   
        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_HEADER);
        header.setBackground(PRIMARY);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 38));
        header.setBorder(BorderFactory.createEmptyBorder());
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
    }

    public static JPanel createTitlePanel(String title, String subtitle) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PRIMARY);
        panel.setBorder(new EmptyBorder(18, 24, 18, 24));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(FONT_TITLE);
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel(subtitle);
        lblSub.setFont(FONT_LABEL);
        lblSub.setForeground(new Color(0xAED6F1));

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 2));
        textPanel.setOpaque(false);
        textPanel.add(lblTitle);
        textPanel.add(lblSub);

        panel.add(textPanel, BorderLayout.WEST);
        return panel;
    }

    // ===== LABEL FORM =====
    public static JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_LABEL);
        lbl.setForeground(TEXT_MAIN);
        return lbl;
    }

    // ===== INPUT FIELD =====
    public static JTextField createInput() {
        JTextField field = new JTextField();
        field.setFont(FONT_BODY);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(6, 10, 6, 10)
        ));
        field.setPreferredSize(new Dimension(200, 36));
        return field;
    }
}
