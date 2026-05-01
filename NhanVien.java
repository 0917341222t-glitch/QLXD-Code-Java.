package model;

public class NhanVien {
    private int maNhanVien;
    private String hoTen;
    private String soDT;
    private String ngaySinh;
    private String diaChi;
    private int maCuaHang;
    private String tenCuaHang;

    public NhanVien() {}

    public NhanVien(int maNhanVien, String hoTen, String soDT, String diaChi, int maCuaHang) {
        this.maNhanVien = maNhanVien;
        this.hoTen = hoTen;
        this.soDT = soDT;
        this.diaChi = diaChi;
        this.maCuaHang = maCuaHang;
    }

    public int getMaNhanVien() { return maNhanVien; }
    public void setMaNhanVien(int maNhanVien) { this.maNhanVien = maNhanVien; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getSoDT() { return soDT; }
    public void setSoDT(String soDT) { this.soDT = soDT; }

    public String getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(String ngaySinh) { this.ngaySinh = ngaySinh; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public int getMaCuaHang() { return maCuaHang; }
    public void setMaCuaHang(int maCuaHang) { this.maCuaHang = maCuaHang; }

    public String getTenCuaHang() { return tenCuaHang; }
    public void setTenCuaHang(String tenCuaHang) { this.tenCuaHang = tenCuaHang; }

    @Override
    public String toString() { return hoTen; }
}
