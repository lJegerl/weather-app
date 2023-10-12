package main.prod.connect.sql;

import main.prod.component.PopulationType;
import main.prod.component.Region;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RegionDAO {
    private Connection connection;

    public RegionDAO(Connection connection) {
        this.connection = connection;
    }

    public void createRegion(Region region) throws SQLException {
        String sql = "INSERT INTO Region (name, square, population_type_id) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, region.getName());
            preparedStatement.setInt(2, region.getSquare());
            preparedStatement.setInt(3, region.getType().getId());
            preparedStatement.executeUpdate();
        }
    }

    public Region getRegionById(int regionId) throws SQLException {
        String sql = "SELECT * FROM Region WHERE region_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, regionId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return extractRegionFromResultSet(resultSet);
                }
            }
        }
        return null;
    }

    public List<Region> getAllRegions() throws SQLException {
        List<Region> regions = new ArrayList<>();
        String sql = "SELECT * FROM Region";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Region region = extractRegionFromResultSet(resultSet);
                System.out.println("Region ID: " + region.getId());
                regions.add(region);
            }
        }
        return regions;
    }

    public void updateRegion(Region region) throws SQLException {
        String sql = "UPDATE Region SET name = ?, square = ?, population_type_id = ? WHERE region_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, region.getName());
            preparedStatement.setInt(2, region.getSquare());
            preparedStatement.setInt(3, region.getType().getId());
            preparedStatement.setInt(4, region.getId());
            preparedStatement.executeUpdate();
        }
    }

    public void deleteRegion(int regionId) throws SQLException {
        String sql = "DELETE FROM Region WHERE region_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, regionId);
            preparedStatement.executeUpdate();
        }
    }

    private Region extractRegionFromResultSet(ResultSet resultSet) throws SQLException {
        int regionId = resultSet.getInt("region_id");
        String name = resultSet.getString("name");
        int square = resultSet.getInt("square");
        int populationTypeId = resultSet.getInt("population_type_id");
        PopulationType type = new PopulationTypeDAO(connection).getPopulationTypeById(populationTypeId);
        return new Region(regionId, name, square, type);
    }
}
