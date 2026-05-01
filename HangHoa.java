package model;

public class HangHoa {
    private int maHangHoa;
    private String tenHangHoa;
    private String donVi;
    private double giaBan;
    private String moTa;

    public HangHoa() {}

    public HangHoa(int maHangHoa, String tenHangHoa, String donVi, double giaBan, String moTa) {
        this.maHangHoa = maHangHoa;
        this.tenHangHoa = tenHangHoa;
        this.donVi = donVi;
        this.giaBan = giaBan;
        this.moTa = moTa;
    }

    public int getMaHangHoa() { return maHangHoa; }
    public void setMaHangHoa(int maHangHoa) { this.maHangHoa = maHangHoa; }

    public String getTenHangHoa() { return tenHangHoa; }
    public void setTenHangHoa(String tenHangHoa) { this.tenHangHoa = tenHangHoa; }

    public String getDonVi() { return donVi; }
    public void setDonVi(String donVi) { this.donVi = donVi; }

    public double getGiaBan() { return giaBan; }
    public void setGiaBan(double giaBan) { this.giaBan = giaBan; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    @Override
    public String toString() { return tenHangHoa; }
}
