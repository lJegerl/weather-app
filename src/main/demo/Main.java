package main.demo;

import main.prod.builders.PopulationTypeBuilder;
import main.prod.builders.RegionBuilder;
import main.prod.builders.WeatherBuilder;
import main.prod.component.PopulationType;
import main.prod.component.Region;
import main.prod.component.Weather;
import main.prod.connect.sql.JdbcConnector;
import main.prod.connect.sql.PopulationTypeDAO;
import main.prod.connect.sql.RegionDAO;
import main.prod.connect.sql.WeatherDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Connection connection = null;

        Scanner scanner = new Scanner(System.in);
        try {
            connection = JdbcConnector.getConnection();

            showMenu();
            while(true) {
                int choice = scanner.nextInt();
                switch (choice) {
                    case (1):
                        System.out.println("Enter NAME, LANGUAGE, COUNT");
                        String name = scanner.nextLine();
                        String language = scanner.nextLine();
                        int count = scanner.nextInt();

                        PopulationType newPopulationType = new PopulationTypeBuilder()
                                .setName(name)
                                .setLanguage(language)
                                .setCount(count)
                                .getPopulationType();

                        break;

                    case (2):
                        System.out.println("Enter NAME, SQUARE, TYPE(ID)");

                }
            }



            PopulationTypeDAO populationTypeDAO = new PopulationTypeDAO(connection);
            RegionDAO regionDAO = new RegionDAO(connection);
            WeatherDAO weatherDAO = new WeatherDAO(connection);


            PopulationType newPopulationType = new PopulationTypeBuilder()
                    .setName("Type X")
                    .setLanguage("English")
                    .setCount(5000)
                    .getPopulationType();


            PopulationType retrievedPopulationType = populationTypeDAO.getPopulationTypeById(4);
            System.out.println("Type: " + retrievedPopulationType.getName());


            Region newRegion = new RegionBuilder()
                    .setName("Minsk")
                    .setSquare(100500)
                    .setType(retrievedPopulationType)
                    .getRegion();


            Region retrievedRegion = regionDAO.getRegionById(3);
            System.out.println("Region: " + retrievedRegion.getName());


            Weather newWeather = new WeatherBuilder()
                    .setDate(LocalDate.parse("2017-01-13"))
                    .setPrecipitation("Snow")
                    .setTemperature(-14.2)
                    .setRegion(retrievedRegion)
                    .getWeather();


            Weather retrievedWeather = weatherDAO.getWeatherById(1);
            System.out.println("Weather: " + retrievedWeather.getPrecipitation());


            weatherDAO.deleteWeather(1);

            System.out.println(populationTypeDAO.getAllPopulationTypes());
            System.out.println(regionDAO.getAllRegions());
            System.out.println(weatherDAO.getAllWeather());

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public static void showMenu() {
        System.out.println("Menu:" +
                "1)Add population type" +
                "2)Add region" +
                "3)Add weather" +
                "4)Delete population type (Enter ID)" +
                "5)Delete region (Enter ID)" +
                "6)Delete weather (Enter ID)" +
                "7)Show all population types" +
                "8)Shpw all regions" +
                "9)Show all weathers" +
                "0)show menu");
    }
}