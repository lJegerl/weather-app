package main.prod.connect.sql;

import main.prod.component.Region;
import main.prod.component.Weather;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WeatherDAO {
    private Connection connection;

    public WeatherDAO(Connection connection) {
        this.connection = connection;
    }

    public void createWeather(Weather weather) throws SQLException {
        String sql = "INSERT INTO Weather (region_id, date, temperature, precipitation) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, weather.getRegion().getId());
            preparedStatement.setDate(2, java.sql.Date.valueOf(weather.getDate()));
            preparedStatement.setDouble(3, weather.getTemperature());
            preparedStatement.setString(4, weather.getPrecipitation());
            preparedStatement.executeUpdate();
        }
    }

    public Weather getWeatherById(int weatherId) throws SQLException {
        String sql = "SELECT * FROM Weather WHERE weather_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, weatherId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return extractWeatherFromResultSet(resultSet);
                }
            }
        }
        return null;
    }

    public List<Weather> getAllWeather() throws SQLException {
        List<Weather> weatherList = new ArrayList<>();
        String sql = "SELECT * FROM Weather";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Weather weather = extractWeatherFromResultSet(resultSet);
                System.out.println("Weather ID: " + weather.getId());
                weatherList.add(weather);
            }
        }
        return weatherList;
    }

    public void updateWeather(Weather weather) throws SQLException {
        String sql = "UPDATE Weather SET region_id = ?, date = ?, temperature = ?, precipitation = ? WHERE weather_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, weather.getRegion().getId());
            preparedStatement.setDate(2, java.sql.Date.valueOf(weather.getDate()));
            preparedStatement.setDouble(3, weather.getTemperature());
            preparedStatement.setString(4, weather.getPrecipitation());
            preparedStatement.setInt(5, weather.getId());
            preparedStatement.executeUpdate();
        }
    }

    public void deleteWeather(int weatherId) throws SQLException {
        String sql = "DELETE FROM Weather WHERE weather_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, weatherId);
            preparedStatement.executeUpdate();
        }
    }

    private Weather extractWeatherFromResultSet(ResultSet resultSet) throws SQLException {
        int weatherId = resultSet.getInt("weather_id");
        int regionId = resultSet.getInt("region_id");
        Region region = new RegionDAO(connection).getRegionById(regionId);
        java.sql.Date date = resultSet.getDate("date");
        LocalDate localDate = date.toLocalDate();
        double temperature = resultSet.getDouble("temperature");
        String precipitation = resultSet.getString("precipitation");
        return new Weather(weatherId, region, localDate, temperature, precipitation);
    }
}
