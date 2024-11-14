import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject; // Для работы с JSON
import org.json.JSONArray; // Для работы с JSON
public class WeatherService {

    private static final String API_KEY = "a680f57a-8176-474b-bba0-a6eed2de0627"; // API ключ
    private static final String BASE_URL = "https://api.weather.yandex.ru/v2/forecast";

    public static void main(String[] args) {
        // координаты (широта, долгота) места
        double lat = 55.75;
        double lon = 37.62;

        try {
            // данные с API Яндекса
            String response = getWeatherData(lat, lon);

            JSONObject jsonData = new JSONObject(response);

            System.out.println("Все данные: " + jsonData.toString(2)); // toString(2) для форматирования

            int temperature = jsonData.getJSONObject("fact").getInt("temp");
            System.out.println("Температура: " + temperature);

            int limit = 5;
            int totalTemperature = 0;
            JSONArray forecasts = jsonData.getJSONArray("forecasts");
            for (int i = 0; i < limit; i++) {
                JSONObject dayForecast = forecasts.getJSONObject(i);
                JSONObject parts = dayForecast.getJSONObject("parts");
                int dayTemp = parts.getJSONObject("day_short").getInt("temp");
                totalTemperature += dayTemp;
            }
            double averageTemperature = (double) totalTemperature / limit;
            System.out.println("Средняя температура за " + limit + " дней: " + averageTemperature);
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }
        private static String getWeatherData(double lat, double lon) throws Exception {
        URL url = new URL(BASE_URL + "?lat=" + lat + "&lon=" + lon);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("X-Yandex-Weather-Key", API_KEY);
        connection.connect();

        if (connection.getResponseCode() == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            return response.toString();
        } else {
            return "";
        }
    }
}
