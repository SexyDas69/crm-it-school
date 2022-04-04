package kg.itschool.crm.dao.impl;

import kg.itschool.crm.dao.StudentDao;
import kg.itschool.crm.dao.daoutil.Log;
import kg.itschool.crm.model.Student;

import java.sql.*;
import java.util.List;

public class StudentDaoImpl implements StudentDao {

    public StudentDaoImpl() {
        Connection connection = null;
        Statement statement = null;

        try {  // api:driver://host:port/database_name
            System.out.println("Connecting to database...");
            connection = getConnection();
            System.out.println("Connection succeeded.");

            String ddlQuery = "CREATE TABLE IF NOT EXISTS tb_students(" +
                    "id           BIGSERIAL, " +
                    "first_name   VARCHAR(50)  NOT NULL, " +
                    "last_name     VARCHAR(50)  NOT NULL, " +
                    "email        VARCHAR(100) NOT NULL UNIQUE, " +
                    "phone_number CHAR(13)     NOT NULL, " +
                    "dob          DATE         NOT NULL CHECK(dob < NOW()), " +
                    "date_created TIMESTAMP    NOT NULL DEFAULT NOW(), " +
                    "" +
                    "CONSTRAINT pk_manager_id PRIMARY KEY(id), " +
                    "CONSTRAINT chk_manager_first_name CHECK(LENGTH(first_name) > 2));";

            System.out.println("Creating statement...");
            statement = connection.createStatement();
            System.out.println("Executing create table statement...");
            statement.execute(ddlQuery);

        } catch (SQLException e) {
            System.out.println("Some error occurred");
            e.printStackTrace();
        } finally {
            close(statement);
            close(connection);
        }
    }

    @Override
    public Student save(Student student) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Student savedStudent = null;

        try {
            System.out.println("Connecting to database...");
            connection = getConnection();
            System.out.println("Connection succeeded.");

            String createQuery = "INSERT INTO tb_students(" +
                    "last_name, first_name, phone_number, date_created, dob, email) " +

                    "VALUES(?, ?, ?, ?, ?, ?)";

            preparedStatement = connection.prepareStatement(createQuery);
            preparedStatement.setString(1, student.getLastName());
            preparedStatement.setString(2, student.getFirstName());
            preparedStatement.setString(3, student.getPhoneNumber());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(student.getDateCreated()));
            preparedStatement.setDate(5, Date.valueOf(student.getDob()));
            preparedStatement.setString(6, student.getEmail());

            preparedStatement.execute();
            close(preparedStatement);

            String readQuery = "SELECT * FROM tb_students ORDER BY id DESC LIMIT 1";

            preparedStatement = connection.prepareStatement(readQuery);
            resultSet = preparedStatement.executeQuery();

            resultSet.next();

            savedStudent = new Student();
            savedStudent.setId(resultSet.getLong("id"));
            savedStudent.setFirstName(resultSet.getString("first_name"));
            savedStudent.setLastName(resultSet.getString("last_name"));
            savedStudent.setEmail(resultSet.getString("email"));
            savedStudent.setPhoneNumber(resultSet.getString("phone_number"));
            savedStudent.setDob(resultSet.getDate("dob").toLocalDate());
            savedStudent.setDateCreated(resultSet.getTimestamp("date_created").toLocalDateTime());

        } catch (SQLException e) {
            Log.error(this.getClass().getSimpleName() + " MentorDaoImpl()", e.getStackTrace()[0].getClass().getSimpleName(), e.getMessage());
            e.printStackTrace();
        } finally {
            close(resultSet);
            close(preparedStatement);
            close(connection);
        }
        return savedStudent;
    }


    @Override
    public Student findById(Long id) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Student student = null;

        try {
            Log.info(this.getClass().getSimpleName() + " findById(" + id + ")", Connection.class.getSimpleName(), "Establishing connection");
            connection = getConnection();

            String readQuery = "SELECT * FROM tb_students WHERE id = ?";

            preparedStatement = connection.prepareStatement(readQuery);
            preparedStatement.setLong(1, id);

            resultSet = preparedStatement.executeQuery();
            resultSet.next();

            student = new Student();
            student.setId(resultSet.getLong("id"));
            student.setFirstName(resultSet.getString("first_name"));
            student.setLastName(resultSet.getString("last_name"));
            student.setEmail(resultSet.getString("email"));
            student.setPhoneNumber(resultSet.getString("phone_number"));
            student.setDob(resultSet.getDate("dob").toLocalDate());
            student.setDateCreated(resultSet.getTimestamp("date_created").toLocalDateTime());


        } catch (SQLException e) {
            Log.error(this.getClass().getSimpleName(), e.getStackTrace()[0].getClass().getSimpleName(), e.getMessage());
            e.printStackTrace();
        } finally {
            close(resultSet);
            close(preparedStatement);
            close(connection);
        }
        return student;
    }

    @Override
    public List<Student> findAll() {
        return null;
    }
}