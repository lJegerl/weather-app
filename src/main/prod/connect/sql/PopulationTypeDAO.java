package main.prod.connect.sql;

import main.prod.component.PopulationType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PopulationTypeDAO {
    private Connection connection;

    public PopulationTypeDAO(Connection connection) {
        this.connection = connection;
    }

    public void createPopulationType(PopulationType populationType) throws SQLException {
        String sql = "INSERT INTO PopulationType (name, language, count) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, populationType.getName());
            preparedStatement.setString(2, populationType.getLanguage());
            preparedStatement.setInt(3, populationType.getCount());
            preparedStatement.executeUpdate();
        }
    }

    public PopulationType getPopulationTypeById(int populationTypeId) throws SQLException {
        String sql = "SELECT * FROM PopulationType WHERE population_type_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, populationTypeId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return extractPopulationTypeFromResultSet(resultSet);
                }
            }
        }
        return null;
    }

    public List<PopulationType> getAllPopulationTypes() throws SQLException {
        List<PopulationType> populationTypes = new ArrayList<>();
        String sql = "SELECT * FROM PopulationType";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                PopulationType type = extractPopulationTypeFromResultSet(resultSet);
                System.out.println("Type ID: " + type.getId());
                populationTypes.add(type);
            }
        }
        return populationTypes;
    }

    public void updatePopulationType(PopulationType populationType) throws SQLException {
        String sql = "UPDATE PopulationType SET name = ?, language = ?, count = ? WHERE population_type_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, populationType.getName());
            preparedStatement.setString(2, populationType.getLanguage());
            preparedStatement.setInt(3, populationType.getCount());
            preparedStatement.setInt(4, populationType.getId());
            preparedStatement.executeUpdate();
        }
    }

    public void deletePopulationType(int populationTypeId) throws SQLException {
        String sql = "DELETE FROM PopulationType WHERE population_type_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, populationTypeId);
            preparedStatement.executeUpdate();
        }
    }

    private PopulationType extractPopulationTypeFromResultSet(ResultSet resultSet) throws SQLException {
        return new PopulationType(
                resultSet.getInt("population_type_id"),
                resultSet.getString("name"),
                resultSet.getString("language"),
                resultSet.getInt("count")
        );
    }
}
