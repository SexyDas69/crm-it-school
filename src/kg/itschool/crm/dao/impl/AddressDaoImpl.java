package kg.itschool.crm.dao.impl;

import kg.itschool.crm.dao.AddressDao;
import kg.itschool.crm.dao.daoutil.Log;
import kg.itschool.crm.models.Address;
import kg.itschool.crm.models.Address;

import java.sql.*;

public class AddressDaoImpl implements AddressDao {

    public AddressDaoImpl() {
        Connection connection = null;
        Statement statement = null;

        try {
            Log.info(this.getClass().getSimpleName(), Connection.class.getSimpleName(), "Establishing connection");
            connection = getConnection();
            Log.info(this.getClass().getSimpleName(), connection.getClass().getSimpleName(), "Connection established");

            String ddlQuery = "CREATE TABLE IF NOT EXISTS tb_address(" +
                    "id           BIGSERIAL, " +
                    "state        VARCHAR(50)  NOT NULL, " +
                    "city         VARCHAR(50)  NOT NULL, " +
                    "region       VARCHAR(50)  NOT NULL, " +
                    "district     VARCHAR(50)  NOT NULL," +
                    "street       VARCHAR(50)  NOT NULL, " +
                    "apartment    VARCHAR(50)  NOT NULL, " +
                    "date_created TIMESTAMP    NOT NULL DEFAULT NOW()," +
                    " " +
                    "CONSTRAINT pk_address_id PRIMARY KEY(id))";


            System.out.println("Creating statement...");
            statement = connection.createStatement();
            System.out.println("Executing create table statement...");
            statement.execute(ddlQuery);

        } catch (SQLException e) {
            Log.error(this.getClass().getSimpleName(), e.getStackTrace()[0].getClass().getSimpleName(), e.getMessage());
            e.printStackTrace();
        } finally {
            close(statement);
            close(connection);
        }
    }


    @Override
    public Address save(Address address) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Address savedAddress = null;

        try {
            System.out.println("Connecting to database...");
            connection = getConnection();
            System.out.println("Connection succeeded.");

            String createQuery = "INSERT INTO tb_address(" +
                    "state, city, region, district, street, apartment) " +

                    "VALUES(?, ?, ?, ?, ?, ?)";

            preparedStatement = connection.prepareStatement(createQuery);
            preparedStatement.setString(1, address.getState());
            preparedStatement.setString(2, address.getCity());
            preparedStatement.setString(3, address.getRegion());
            preparedStatement.setString(4, address.getDistrict());
            preparedStatement.setString(5, address.getStreet());
            preparedStatement.setString(6, address.getApartment());


            preparedStatement.execute();
            close(preparedStatement);

            String readQuery = "SELECT * FROM tb_address ORDER BY id DESC LIMIT 1";

            preparedStatement = connection.prepareStatement(readQuery);
            resultSet.next();

            savedAddress = new Address();
            savedAddress.setState(resultSet.getString("state"));
            savedAddress.setCity(resultSet.getString("city"));
            savedAddress.setRegion(resultSet.getString("region"));
            savedAddress.setDistrict(resultSet.getString("district"));
            savedAddress.setStreet(resultSet.getString("street"));
            savedAddress.setApartment(resultSet.getString("apartment"));

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(resultSet);
            close(preparedStatement);
            close(connection);
        }
        return savedAddress;
    }

    @Override
    public Address findById(Long id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Address address = null;

        try {
            connection = getConnection();

            String readQuery = "SELECT * FROM tb_address WHERE id = ?";

            preparedStatement = connection.prepareStatement(readQuery);
            preparedStatement.setLong(1, id);

            resultSet = preparedStatement.executeQuery();
            resultSet.next();

            address = new Address();
            address.setState(resultSet.getString("state"));
            address.setCity(resultSet.getString("city"));
            address.setRegion(resultSet.getString("region"));
            address.setDistrict(resultSet.getString("district"));
            address.setStreet(resultSet.getString("street"));
            address.setApartment(resultSet.getString("apartment"));

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(resultSet);
            close(preparedStatement);
            close(connection);
        }
        return address;
    }

    private void close(AutoCloseable closeable) {
        try {
            System.out.println(closeable.getClass().getSimpleName() + " closing...");
            closeable.close();
            System.out.println(closeable.getClass().getSimpleName() + " closed.");
        } catch (Exception e) {
            System.out.println("Could not close " + closeable.getClass().getSimpleName());
            e.printStackTrace();
        }
    }
}
