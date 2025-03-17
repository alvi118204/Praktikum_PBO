import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

class TiketF1 {
    private String kategori;
    private int harga;
    private int stok;

    public TiketF1(String kategori, int harga, int stok) {
        this.kategori = kategori;
        this.harga = harga;
        this.stok = stok;
    }

    public String getKategori() {
        return kategori;
    }

    public int getHarga() {
        return harga;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }
}

class Pembeli {
    private String nama;
    private ArrayList<TiketF1> tiketDibeli;

    public Pembeli(String nama) {
        this.nama = nama;
        this.tiketDibeli = new ArrayList<>();
    }

    public String getNama() {
        return nama;
    }

    public ArrayList<TiketF1> getTiketDibeli() {
        return tiketDibeli;
    }

    public void beliTiket(TiketF1 tiket) {
        tiketDibeli.add(tiket);
    }

    public void batalTiket(TiketF1 tiket) {
        tiketDibeli.remove(tiket);
    }
}

public class Main {
    static HashMap<String, TiketF1> daftarTiket = new HashMap<>();
    static HashMap<String, Pembeli> daftarPembeli = new HashMap<>();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        daftarTiket.put("VIP", new TiketF1("VIP", 5000000, 10));
        daftarTiket.put("Regular", new TiketF1("Regular", 2000000, 20));
        daftarTiket.put("Ekonomi", new TiketF1("Ekonomi", 1000000, 30));

        while (true) {
            System.out.println("\n===== MENU UTAMA =====");
            System.out.println("1. Lihat Tiket Tersedia");
            System.out.println("2. Beli Tiket");
            System.out.println("3. Lihat Tiket Saya");
            System.out.println("4. Batalkan Tiket");
            System.out.println("5. Login Admin");
            System.out.println("6. Keluar");
            System.out.print("Pilih menu: ");
            int pilihan = scanner.nextInt();
            scanner.nextLine();

            switch (pilihan) {
                case 1:
                    lihatTiket();
                    break;
                case 2:
                    beliTiket();
                    break;
                case 3:
                    lihatTiketSaya();
                    break;
                case 4:
                    batalTiket();
                    break;
                case 5:
                    loginAdmin();
                    break;
                case 6:
                    System.out.println("Terima kasih telah menggunakan layanan kami!");
                    System.exit(0);
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }

    public static void lihatTiket() {
        System.out.println("\n=== DAFTAR TIKET TERSEDIA ===");
        for (TiketF1 tiket : daftarTiket.values()) {
            System.out.println("Kategori: " + tiket.getKategori() + ", Harga: Rp " + tiket.getHarga() + ", Stok: " + tiket.getStok());
        }
    }

    public static void beliTiket() {
        System.out.print("Masukkan nama Anda: ");
        String nama = scanner.nextLine();
        Pembeli pembeli = daftarPembeli.getOrDefault(nama, new Pembeli(nama));
        daftarPembeli.put(nama, pembeli);

        lihatTiket();
        System.out.print("Pilih kategori tiket: ");
        String kategori = scanner.nextLine();
        TiketF1 tiket = daftarTiket.get(kategori);

        if (tiket != null && tiket.getStok() > 0) {
            tiket.setStok(tiket.getStok() - 1);
            pembeli.beliTiket(tiket);
            System.out.println("Tiket berhasil dibeli!");
        } else {
            System.out.println("Tiket tidak tersedia!");
        }
    }

    public static void lihatTiketSaya() {
        System.out.print("Masukkan nama Anda: ");
        String nama = scanner.nextLine();
        Pembeli pembeli = daftarPembeli.get(nama);

        if (pembeli == null || pembeli.getTiketDibeli().isEmpty()) {
            System.out.println("Anda belum membeli tiket!");
            return;
        }

        System.out.println("\n=== TIKET YANG TELAH DIBELI ===");
        for (TiketF1 tiket : pembeli.getTiketDibeli()) {
            System.out.println("Kategori: " + tiket.getKategori() + " | Harga: Rp " + tiket.getHarga());
        }
    }

    public static void batalTiket() {
        System.out.print("Masukkan nama Anda: ");
        String nama = scanner.nextLine();
        Pembeli pembeli = daftarPembeli.get(nama);

        if (pembeli == null || pembeli.getTiketDibeli().isEmpty()) {
            System.out.println("Anda belum membeli tiket!");
            return;
        }

        lihatTiketSaya();
        System.out.print("Masukkan kategori tiket yang ingin dibatalkan: ");
        String kategori = scanner.nextLine();
        TiketF1 tiket = daftarTiket.get(kategori);

        if (tiket != null && pembeli.getTiketDibeli().contains(tiket)) {
            System.out.print("Apakah Anda yakin ingin membatalkan tiket? (ya/tidak): ");
            String konfirmasi = scanner.nextLine();
            if (konfirmasi.equalsIgnoreCase("ya")) {
                pembeli.batalTiket(tiket);
                tiket.setStok(tiket.getStok() + 1);
                System.out.println("Tiket berhasil dibatalkan!");
            } else {
                System.out.println("Pembatalan tiket dibatalkan.");
            }
        } else {
            System.out.println("Tiket tidak ditemukan!");
        }
    }

    public static void loginAdmin() {
        System.out.print("Masukkan username admin: ");
        String username = scanner.nextLine();
        System.out.print("Masukkan password admin: ");
        String password = scanner.nextLine();

        if (username.equals("admin") && password.equals("admin123")) {
            System.out.println("Login admin berhasil!");
            menuAdmin();
        } else {
            System.out.println("Username atau password salah!");
        }
    }

    public static void menuAdmin() {
        while (true) {
            System.out.println("\n===== MENU ADMIN =====");
            System.out.println("1. Tambah Tiket Baru");
            System.out.println("2. Kembali ke Menu Utama");
            System.out.print("Pilih menu: ");
            int pilihan = scanner.nextInt();
            scanner.nextLine();

            switch (pilihan) {
                case 1:
                    tambahTiketBaru();
                    break;
                case 2:
                    return;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }

    public static void tambahTiketBaru() {
        System.out.print("Masukkan kategori tiket: ");
        String kategori = scanner.nextLine();
        System.out.print("Masukkan harga tiket: ");
        int harga = scanner.nextInt();
        System.out.print("Masukkan stok tiket: ");
        int stok = scanner.nextInt();
        scanner.nextLine();

        if (daftarTiket.containsKey(kategori)) {
            System.out.println("Kategori tiket sudah ada!");
        } else {
            daftarTiket.put(kategori, new TiketF1(kategori, harga, stok));
            System.out.println("Tiket berhasil ditambahkan!");
        }
    }
}