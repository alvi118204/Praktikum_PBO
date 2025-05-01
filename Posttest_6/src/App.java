import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

interface Promo {
    void tampilkanPromo();
    int diskonTambahan();
}

abstract class TiketF1 {
    protected String kategori;
    protected int harga;
    protected int stok;

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

    public String getDeskripsi() {
        return "Tiket Formula 1 standar";
    }

    public int hitungHargaAkhir() {
        return harga;
    }

    public abstract String getTipeTiket(); 
}

class TiketVIP extends TiketF1 implements Promo {
    private boolean aksesPaddock;
    private boolean meetAndGreet;

    public TiketVIP(int harga, int stok, boolean aksesPaddock, boolean meetAndGreet) {
        super("VIP", harga, stok);
        this.aksesPaddock = aksesPaddock;
        this.meetAndGreet = meetAndGreet;
    }

    public boolean hasAksesPaddock() {
        return aksesPaddock;
    }

    public boolean hasMeetAndGreet() {
        return meetAndGreet;
    }

    @Override
    public String getDeskripsi() {
        String deskripsi = "Tiket VIP dengan akses tribun premium";
        if (aksesPaddock) deskripsi += ", akses paddock";
        if (meetAndGreet) deskripsi += ", dan kesempatan meet & greet";
        return deskripsi;
    }

    @Override
    public int hitungHargaAkhir() {
        int hargaAkhir = harga;
        if (aksesPaddock) hargaAkhir += 1000000;
        if (meetAndGreet) hargaAkhir += 2000000;
        hargaAkhir -= diskonTambahan(); 
        return hargaAkhir;
    }

    @Override
    public String getTipeTiket() {
        return "VIP";
    }

    @Override
    public void tampilkanPromo() {
        System.out.println("🎉 Promo VIP: Diskon tambahan Rp " + diskonTambahan());
    }

    @Override
    public int diskonTambahan() {
        return 500000; 
    }
}

class TiketGrandstand extends TiketF1 {
    private String tribun;
    private boolean termasukSouvenir;

    public TiketGrandstand(String kategori, int harga, int stok, String tribun, boolean termasukSouvenir) {
        super(kategori, harga, stok);
        this.tribun = tribun;
        this.termasukSouvenir = termasukSouvenir;
    }

    public String getTribun() {
        return tribun;
    }

    public boolean hasSouvenir() {
        return termasukSouvenir;
    }

    @Override
    public String getDeskripsi() {
        String deskripsi = "Tiket Grandstand di tribun " + tribun;
        if (termasukSouvenir) deskripsi += " (termasuk souvenir eksklusif)";
        return deskripsi;
    }

    @Override
    public int hitungHargaAkhir() {
        int hargaAkhir = harga;
        if (termasukSouvenir) hargaAkhir += 250000;
        return hargaAkhir;
    }

    @Override
    public String getTipeTiket() {
        return "Grandstand";
    }
}

class Pembeli {
    private String nama;
    private ArrayList<TiketF1> tiketDibeli;

    public Pembeli(String nama) {
        this.nama = nama;
        this.tiketDibeli = new ArrayList<>();
    }

    public final String getNama() {
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

public final class App {
    private static HashMap<String, TiketF1> daftarTiket = new HashMap<>();
    private static HashMap<String, Pembeli> daftarPembeli = new HashMap<>();
    private static final Scanner scanner = new Scanner(System.in);
    public static int totalTiketTerjual = 0; // STATIC variable

    public static void main(String[] args) {
        daftarTiket.put("VIP", new TiketVIP(5000000, 10, true, true));
        daftarTiket.put("Regular", new TiketGrandstand("Regular", 2000000, 20, "Main Straight", true));
        daftarTiket.put("Ekonomi", new TiketGrandstand("Ekonomi", 1000000, 30, "Backstraight", false));

        while (true) {
            System.out.println("\n===== MENU UTAMA =====");
            System.out.println("1. Lihat Tiket Tersedia");
            System.out.println("2. Beli Tiket");
            System.out.println("3. Lihat Tiket Saya");
            System.out.println("4. Batalkan Tiket");
            System.out.println("5. Login Admin");
            System.out.println("6. Keluar");
            System.out.print("Pilih menu: ");

            try {
                int pilihan = scanner.nextInt();
                scanner.nextLine();

                switch (pilihan) {
                    case 1 -> lihatTiket();
                    case 2 -> beliTiket();
                    case 3 -> lihatTiketSaya();
                    case 4 -> batalTiket();
                    case 5 -> loginAdmin();
                    case 6 -> {
                        System.out.println("Total Tiket Terjual: " + totalTiketTerjual);
                        System.out.println("Terima kasih telah menggunakan layanan kami!");
                        System.exit(0);
                    }
                    default -> System.out.println("Pilihan tidak valid!");
                }
            } catch (InputMismatchException e) {
                System.out.println("⚠️ Masukkan angka yang valid!");
                scanner.nextLine(); // clear buffer
            }
        }
    }

    public static void lihatTiket() {
        System.out.println("\n=== DAFTAR TIKET TERSEDIA ===");
        for (TiketF1 tiket : daftarTiket.values()) {
            System.out.println("Kategori: " + tiket.getKategori() +
                    ", Harga: Rp " + tiket.hitungHargaAkhir() +
                    ", Stok: " + tiket.getStok() +
                    "\nDeskripsi: " + tiket.getDeskripsi());

            if (tiket instanceof TiketVIP vip) {
                vip.tampilkanPromo(); // panggil dari interface Promo
                System.out.println("- Akses Paddock: " + (vip.hasAksesPaddock() ? "Ya" : "Tidak"));
                System.out.println("- Meet & Greet: " + (vip.hasMeetAndGreet() ? "Ya" : "Tidak"));
            } else if (tiket instanceof TiketGrandstand gs) {
                System.out.println("- Tribun: " + gs.getTribun());
                System.out.println("- Souvenir: " + (gs.hasSouvenir() ? "Ya" : "Tidak"));
            }
            System.out.println();
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
            totalTiketTerjual++; 
            System.out.println("Tiket berhasil dibeli!");
            System.out.println("Harga akhir: Rp " + tiket.hitungHargaAkhir());
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
            System.out.println("Kategori: " + tiket.getKategori() +
                    " | Harga: Rp " + tiket.hitungHargaAkhir() +
                    "\nDeskripsi: " + tiket.getDeskripsi());

            if (tiket instanceof TiketVIP vip) {
                System.out.println("- Akses Paddock: " + (vip.hasAksesPaddock() ? "Ya" : "Tidak"));
                System.out.println("- Meet & Greet: " + (vip.hasMeetAndGreet() ? "Ya" : "Tidak"));
            } else if (tiket instanceof TiketGrandstand gs) {
                System.out.println("- Tribun: " + gs.getTribun());
                System.out.println("- Souvenir: " + (gs.hasSouvenir() ? "Ya" : "Tidak"));
            }
            System.out.println();
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

        TiketF1 tiketToCancel = null;
        for (TiketF1 t : pembeli.getTiketDibeli()) {
            if (t.getKategori().equals(kategori)) {
                tiketToCancel = t;
                break;
            }
        }

        if (tiketToCancel != null) {
            System.out.print("Apakah Anda yakin ingin membatalkan tiket? (ya/tidak): ");
            String konfirmasi = scanner.nextLine();
            if (konfirmasi.equalsIgnoreCase("ya")) {
                pembeli.batalTiket(tiketToCancel);

                TiketF1 originalTiket = daftarTiket.get(kategori);
                if (originalTiket != null) {
                    originalTiket.setStok(originalTiket.getStok() + 1);
                    totalTiketTerjual--; // kurangi tiket yang batal
                }

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

    private static void menuAdmin() {
        while (true) {
            System.out.println("\n===== MENU ADMIN =====");
            System.out.println("1. Tambah Tiket VIP");
            System.out.println("2. Tambah Tiket Grandstand");
            System.out.println("3. Perbarui Stok Tiket");
            System.out.println("4. Kembali ke Menu Utama");
            System.out.print("Pilih menu: ");

            try {
                int pilihan = scanner.nextInt();
                scanner.nextLine();

                switch (pilihan) {
                    case 1 -> tambahTiketVIP();
                    case 2 -> tambahTiketGrandstand();
                    case 3 -> perbaruiStok();
                    case 4 -> { return; }
                    default -> System.out.println("Pilihan tidak valid!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Input tidak valid. Masukkan angka!");
                scanner.nextLine(); 
            }
        }
    }

    public static void tambahTiketVIP() {
        System.out.print("Masukkan nama kategori VIP: ");
        String kategori = scanner.nextLine();

        if (daftarTiket.containsKey(kategori)) {
            System.out.println("Kategori tiket sudah ada!");
            return;
        }

        try {
            System.out.print("Masukkan harga dasar tiket: ");
            int harga = scanner.nextInt();
            System.out.print("Masukkan stok tiket: ");
            int stok = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Apakah termasuk akses paddock? (ya/tidak): ");
            boolean aksesPaddock = scanner.nextLine().equalsIgnoreCase("ya");

            System.out.print("Apakah termasuk meet & greet? (ya/tidak): ");
            boolean meetAndGreet = scanner.nextLine().equalsIgnoreCase("ya");

            daftarTiket.put(kategori, new TiketVIP(harga, stok, aksesPaddock, meetAndGreet));
            System.out.println("Tiket VIP berhasil ditambahkan!");
        } catch (InputMismatchException e) {
            System.out.println("Input angka tidak valid!");
            scanner.nextLine();
        }
    }

    public static void tambahTiketGrandstand() {
        System.out.print("Masukkan nama kategori Grandstand: ");
        String kategori = scanner.nextLine();

        if (daftarTiket.containsKey(kategori)) {
            System.out.println("Kategori tiket sudah ada!");
            return;
        }

        try {
            System.out.print("Masukkan harga tiket: ");
            int harga = scanner.nextInt();
            System.out.print("Masukkan stok tiket: ");
            int stok = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Masukkan nama tribun: ");
            String tribun = scanner.nextLine();

            System.out.print("Apakah termasuk souvenir? (ya/tidak): ");
            boolean termasukSouvenir = scanner.nextLine().equalsIgnoreCase("ya");

            daftarTiket.put(kategori, new TiketGrandstand(kategori, harga, stok, tribun, termasukSouvenir));
            System.out.println("Tiket Grandstand berhasil ditambahkan!");
        } catch (InputMismatchException e) {
            System.out.println("Input angka tidak valid!");
            scanner.nextLine();
        }
    }

    public static void perbaruiStok() {
        lihatTiket();
        System.out.print("Masukkan kategori tiket yang ingin diperbarui: ");
        String kategori = scanner.nextLine();

        TiketF1 tiket = daftarTiket.get(kategori);
        if (tiket != null) {
            System.out.println("Stok saat ini: " + tiket.getStok());

            try {
                System.out.print("Masukkan stok baru: ");
                int stokBaru = scanner.nextInt();
                scanner.nextLine();
                tiket.setStok(stokBaru);
                System.out.println("Stok tiket berhasil diperbarui!");
            } catch (InputMismatchException e) {
                System.out.println("Masukkan angka yang valid!");
                scanner.nextLine();
            }
        } else {
            System.out.println("Tiket tidak ditemukan!");
        }
    }
}
