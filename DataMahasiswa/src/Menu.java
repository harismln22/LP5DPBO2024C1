import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Menu extends JFrame{
    public static void main(String[] args) {
        // buat object window
        Menu window = new Menu();

        // atur ukuran window
        window.setSize(400, 300);
        // letakkan window di tengah layar
        window.setLocationRelativeTo(null);
        // isi window
        window.setContentPane(window.mainPanel);
        // ubah warna background
        window.getContentPane().setBackground(Color.white);
        // tampilkan window
        window.setVisible(true);
        // agar program ikut berhenti saat window diclose
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // index baris yang diklik
    private int selectedIndex = -1;
    // list untuk menampung semua mahasiswa
    private ArrayList<Mahasiswa> listMahasiswa;
    private String RadioButtonDipiih = ""; // untuk menentukan RadioButton

    private JPanel mainPanel;
    private JTextField nimField;
    private JTextField namaField;
    private JTable mahasiswaTable;
    private JButton addUpdateButton;
    private JButton cancelButton;
    private JComboBox jenisKelaminComboBox;
    private JButton deleteButton;
    private JLabel titleLabel;
    private JLabel nimLabel;
    private JLabel namaLabel;
    private JLabel jenisKelaminLabel;
    private JTextField nilaiField;
    private JLabel nilaiLabel;
    private JRadioButton nimRadioButton;
    private JRadioButton namaRadioButton;
    private JLabel filterLabel;
    private JTextField searchField;
    private JTextField searchnamaField;
    private JButton filterButton;

    // constructor
    public Menu() {
        // inisialisasi listMahasiswa
        listMahasiswa = new ArrayList<>();

        /* membuat button agar bisa hanya dipilih satu */
        // Buat objek ButtonGroup
        ButtonGroup buttonGroup = new ButtonGroup();
        // Tambahkan RadioButton ke ButtonGroup
        buttonGroup.add(nimRadioButton);
        buttonGroup.add(namaRadioButton);

        // isi listMahasiswa
        populateList();

        // isi tabel mahasiswa
        mahasiswaTable.setModel(setTable());

        // ubah styling title
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD,20f));

        // atur isi combo box
        String[] jenisKelaminData ={"Laki-Laki", "Perempuan"};
        jenisKelaminComboBox.setModel(new DefaultComboBoxModel(jenisKelaminData));

        // sembunyikan button delete
        deleteButton.setVisible(false);

        // menambah filter
        filterButton.setVisible(false);
        searchField.setVisible(false);

        // saat tombol add/update ditekan
        addUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(selectedIndex == -1)
                {
                    insertData();
                }
                else
                {
                    updateData();
                }
            }
        });
        // saat tombol delete ditekan
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(selectedIndex >=0)
                {
                    deleteData();
                }
            }
        });

        // ketika Nim Filter di klik
        nimRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(nimRadioButton.isSelected()) // jika memang memang Nim radio button dipilih
                {
                    searchField.setVisible(true); // untuk pengisian input akan ditampilkan
                    filterButton.setVisible(true); // tampilan button filternya juga ditampilkan
                    RadioButtonDipiih = "NIM"; // yang dipilih sekarang adalah NIM
                }
                else // jika bukan nim button yang dipilih
                {
                    searchField.setVisible(false); // input akan disembunyikan
                }
                mainPanel.revalidate(); // 2 fungsi ini aku tidak ngerti tapi agar bisa menampilkan fieldnya (dari chatgpt awkoawkaok)
                mainPanel.repaint();
            }
        });

        // ketika button nama filter di klik
        namaRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(namaRadioButton.isSelected()) // jika memang dipilih
                {
                    searchField.setVisible(true); // input field akan ditampilkan
                    filterButton.setVisible(true); // filter tombol akan ditampilkan juga
                    RadioButtonDipiih = "Nama"; // yang dpilih sekarang adalah Nama
                }
                else // jika tidak dipilih
                {
                    searchField.setVisible(false); // input bagian nama akan disembunyikan
                }
                mainPanel.revalidate(); // intinya agar bisa menampilkan input field radioButton dari kedua fungsi ini
                mainPanel.repaint();
            }
        });

        // ketika button filter di klik
        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String search = searchField.getText(); // input untuk mencari
                int tidakKetemu = 1; // mark jika tidak ketemu
                if(!search.isEmpty()) // jika memang terdapat data
                {
                    ArrayList<Mahasiswa> newList = new ArrayList<>(); // list untuk data yang akan ditemukan

                    for(Mahasiswa m : listMahasiswa) // mencari list dengan for
                    {
                        // jika memang nama/nim yang dicari ketemu
                        if(m.getNim().equals(search) && RadioButtonDipiih.equals("NIM") || m.getNama().equals(search) && RadioButtonDipiih.equals("Nama"))
                        {
                            tidakKetemu = 0; // ketemu
                            newList.add(0, m); // memasukkan data yang dicari tersebut ke paling atas
                        }
                        else
                        {
                            newList.add(m); // jika tidak ketemu, maka akan seperti list sebelumnya saja
                        }
                    }
                    if(tidakKetemu == 1) // jika tidak ketemu
                    {
                        // akan menampilkan kalau data tersebut tidak ditemukan
                        JOptionPane.showMessageDialog(null, "Data tersebut tidak ditemukan!");
                    }
                    listMahasiswa = newList; // memasukan list yang ketemu tersebut ke listMahasiswa

                    mahasiswaTable.setModel(setTable()); // update table

                    buttonGroup.clearSelection(); // atur ulang RadioButton
                    searchField.setText(""); // kosongkan field pencarian
                    searchField.setVisible(false); // sembunyikan field pencarian
                    filterButton.setVisible(false); // sembunyikan tombol filter
                    RadioButtonDipiih = ""; // kosongkan button yang dipilih
                }
                else // jika data list kosong
                {
                    JOptionPane.showMessageDialog(null, "Data Kosong!");
                }
            }
        });


        // saat tombol cancel ditekan
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });

        // saat salah satu baris tabel ditekan
        mahasiswaTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // ubah selectedIndex menjadi baris tabel yang diklik
                selectedIndex = mahasiswaTable.getSelectedRow();

                // simpan value textfield dan combo box
                String selectedNim = mahasiswaTable.getModel().getValueAt(selectedIndex,1).toString();
                String selectedNama = mahasiswaTable.getModel().getValueAt(selectedIndex,2).toString();
                String selectedJenisKelamin = mahasiswaTable.getModel().getValueAt(selectedIndex,3).toString();
                String selectedNilai  = mahasiswaTable.getModel().getValueAt(selectedIndex,4).toString();

                // ubah isi textfield dan combo box
                nimField.setText(selectedNim);
                namaField.setText(selectedNama);
                jenisKelaminComboBox.setSelectedItem(selectedJenisKelamin);
                nilaiField.setText(selectedNilai);

                // ubah button "Add" menjadi "Update"
                addUpdateButton.setText("Update");
                // tampilkan button delete
                deleteButton.setVisible(true);
            }
        });
    }

    public final DefaultTableModel setTable() {
        // tentukan kolom tabel
        Object[] column = {"No", "Nim","Nama", "Jenis Kelamin", "Nilai"};

        // buat objek tabel dengan kolom yang sudah dibuat
        DefaultTableModel temp = new DefaultTableModel(null, column);

        // isi tabel dengan listMahasiswa
        for(int i = 0; i< listMahasiswa.size(); i++)
        {
            Object[] row = new Object[5];
            row[0] = i + 1;
            row[1]= listMahasiswa.get(i).getNim();
            row[2]= listMahasiswa.get(i).getNama();
            row[3]= listMahasiswa.get(i).getJenisKelamin();
            row[4]= listMahasiswa.get(i).getNilai();

            temp.addRow(row);
        }

        return temp;// return juga harus diganti
    }

    public void insertData() {
        // ambil value dari textfield dan combobox
        String nim= nimField.getText();
        String nama = namaField.getText();
        String jenisKelamin = jenisKelaminComboBox.getSelectedItem().toString();
        String nilai = nilaiField.getText();

        // tambahkan data ke dalam list
        listMahasiswa.add(new Mahasiswa(nim,nama,jenisKelamin, nilai));

        // update tabel
        mahasiswaTable.setModel(setTable());

        // bersihkan form
        clearForm();

        // feedback
        System.out.println("Insert Berhasil");
        JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan");
    }

    public void updateData() {
        // ambil data dari form
        String nim = nimField.getText();
        String nama = namaField.getText();
        String jenisKelamin = jenisKelaminComboBox.getSelectedItem().toString();
        String nilai = nilaiField.getText();

        // konfirmasi dengan JOptionPane confirm
        int confirm = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin mengupdate data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);

        // jika memilih 'Ya'
        if(confirm == JOptionPane.YES_OPTION){
            // ubah data mahasiswa di list
            listMahasiswa.get(selectedIndex).setNim(nim);
            listMahasiswa.get(selectedIndex).setNama(nama);
            listMahasiswa.get(selectedIndex).setJenisKelamin(jenisKelamin);
            listMahasiswa.get(selectedIndex).setNilai(nilai);

            // update tabel
            mahasiswaTable.setModel(setTable());

            // bersihkan form
            clearForm();

            // feedback
            System.out.println("Update Berhasil");
            JOptionPane.showMessageDialog(null, "Data berhasil diubah!");
        }
    }

    public void deleteData() {
        // konfirmasi dengan JOptionPane confirm
        int confirm = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menghapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);

        // jika  memilih 'Ya'
        if(confirm == JOptionPane.YES_OPTION){
            // hapus data dari list
            listMahasiswa.remove(selectedIndex);

            // update tabel
            mahasiswaTable.setModel(setTable());

            // bersihkan form
            clearForm();

            // feedback
            System.out.println("Delete Berhasil");
            JOptionPane.showMessageDialog(null, "Data berhasil dihapus");
        }
    }

    public void clearForm() {
        // kosongkan semua texfield dan combo box
        nimField.setText("");
        namaField.setText("");
        jenisKelaminComboBox.setSelectedItem("");
        nilaiField.setText("");

        // ubah button "Update" menjadi "Add"
        addUpdateButton.setText("Add");
        // sembunyikan button delete
        deleteButton.setVisible(false);
        // ubah selectedIndex menjadi -1 (tidak ada baris yang dipilih)
        selectedIndex = -1;

        searchField.setText(""); // kosongkan field pencarian
        searchField.setVisible(false); // sembunyikan field pencarian
        filterButton.setVisible(false); // sembunyikan tombol filter
        RadioButtonDipiih = "";

    }

    private void populateList() {
        listMahasiswa.add(new Mahasiswa("2203999", "Amelia Zalfa Julianti", "Perempuan", "A"));
        listMahasiswa.add(new Mahasiswa("2202292", "Muhammad Iqbal Fadhilah", "Laki-laki", "A"));
        listMahasiswa.add(new Mahasiswa("2202346", "Muhammad Rifky Afandi", "Laki-laki", "B"));
        listMahasiswa.add(new Mahasiswa("2210239", "Muhammad Hanif Abdillah", "Laki-laki", " B+"));
        listMahasiswa.add(new Mahasiswa("2202046", "Nurainun", "Perempuan", "B+"));
        listMahasiswa.add(new Mahasiswa("2205101", "Kelvin Julian Putra", "Laki-laki", "A+"));
        listMahasiswa.add(new Mahasiswa("2200163", "Rifanny Lysara Annastasya", "Perempuan", "A"));
        listMahasiswa.add(new Mahasiswa("2202869", "Revana Faliha Salma", "Perempuan", "C+"));
        listMahasiswa.add(new Mahasiswa("2209489", "Rakha Dhifiargo Hariadi", "Laki-laki", "A"));
        listMahasiswa.add(new Mahasiswa("2203142", "Roshan Syalwan Nurilham", "Laki-laki", "A"));
        listMahasiswa.add(new Mahasiswa("2200311", "Raden Rahman Ismail", "Laki-laki", "B"));
        listMahasiswa.add(new Mahasiswa("2200978", "Ratu Syahirah Khairunnisa", "Perempuan", "C+"));
        listMahasiswa.add(new Mahasiswa("2204509", "Muhammad Fahreza Fauzan", "Laki-laki", "A"));
        listMahasiswa.add(new Mahasiswa("2205027", "Muhammad Rizki Revandi", "Laki-laki", "A"));
        listMahasiswa.add(new Mahasiswa("2203484", "Arya Aydin Margono", "Laki-laki", "A"));
        listMahasiswa.add(new Mahasiswa("2200481", "Marvel Ravindra Dioputra", "Laki-laki", "A"));
        listMahasiswa.add(new Mahasiswa("2209889", "Muhammad Fadlul Hafiizh", "Laki-laki", "A"));
        listMahasiswa.add(new Mahasiswa("2206697", "Rifa Sania", "Perempuan", "A"));
        listMahasiswa.add(new Mahasiswa("2207260", "Imam Chalish Rafidhul Haque", "Laki-laki", "A"));
        listMahasiswa.add(new Mahasiswa("2204343", "Meiva Labibah Putri", "Perempuan", "A"));
    }
}
