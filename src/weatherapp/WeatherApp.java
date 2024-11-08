package weatherapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.json.JSONObject;

public class WeatherApp {
    private JFrame frame;
    private JTextField cityField;
    private JLabel weatherIconLabel;
    private JLabel tempLabel, descriptionLabel;
    private JComboBox<String> locationComboBox;
    private JButton checkWeatherButton;
    
    // API Key OpenWeatherMap
    private final String API_KEY = "593b96a162ff9dabbf6dd230f586bca9";
    
    // URL API
    private final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
    
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                WeatherApp window = new WeatherApp();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    
    public WeatherApp() {
        initialize();
    }
    
    private void initialize() {
        frame = new JFrame("Aplikasi Cek Cuaca");
        frame.setBounds(100, 100, 600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());
        
        // Panel utama
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        
        // Kombobox untuk memilih lokasi
        locationComboBox = new JComboBox<>();
        locationComboBox.addItem("Jakarta");
        locationComboBox.addItem("Surabaya");
        locationComboBox.addItem("Bandung");
        locationComboBox.addItem("Yogyakarta");
        locationComboBox.addItem("Medan");
        
        // TextField untuk masukkan kota
        cityField = new JTextField(15);
        
        // Tombol untuk cek cuaca
        checkWeatherButton = new JButton("Cek Cuaca");
        checkWeatherButton.addActionListener(e -> fetchWeatherData());
        
        // Menambahkan komponen ke panel
        panel.add(new JLabel("Pilih Kota:"));
        panel.add(locationComboBox);
        panel.add(new JLabel("Atau Masukkan Kota:"));
        panel.add(cityField);
        panel.add(checkWeatherButton);
        
        // Panel untuk hasil cuaca
        JPanel weatherPanel = new JPanel();
        weatherPanel.setLayout(new GridLayout(2, 2));
        
        // Label untuk cuaca
        weatherIconLabel = new JLabel("Cuaca: ");
        tempLabel = new JLabel("Suhu: ");
        descriptionLabel = new JLabel("Deskripsi: ");
        
        weatherPanel.add(weatherIconLabel);
        weatherPanel.add(tempLabel);
        weatherPanel.add(descriptionLabel);
        
        // Menambahkan panel ke frame
        frame.getContentPane().add(panel, BorderLayout.NORTH);
        frame.getContentPane().add(weatherPanel, BorderLayout.CENTER);
    }
    
    private void fetchWeatherData() {
        String location = (String) locationComboBox.getSelectedItem();
        if (!cityField.getText().trim().isEmpty()) {
            location = cityField.getText().trim();
        }
        
        try {
            String urlString = BASE_URL + location + "&appid=" + API_KEY + "&units=metric";
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            
            // Membaca respon dari API
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            
            // Parse JSON dari API
            JSONObject myResponse = new JSONObject(response.toString());
            
            // Mendapatkan informasi cuaca
            double temp = myResponse.getJSONObject("main").getDouble("temp");
            String description = myResponse.getJSONArray("weather").getJSONObject(0).getString("description");
            String icon = myResponse.getJSONArray("weather").getJSONObject(0).getString("icon");
            
            // Mengupdate UI dengan data cuaca
            tempLabel.setText("Suhu: " + temp + " °C");
            descriptionLabel.setText("Deskripsi: " + description);
            
            // Menampilkan ikon cuaca
            String iconUrl = "http://openweathermap.org/img/wn/" + icon + "@2x.png";
            weatherIconLabel.setText("Cuaca: " + description);
            weatherIconLabel.setIcon(new ImageIcon(new URL(iconUrl)));
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Terjadi kesalahan saat mengambil data cuaca.");
            e.printStackTrace();
        }
    }
}