package kg.itschool.crm.dao.impl;

import kg.itschool.crm.dao.CourseDao;
import kg.itschool.crm.models.Course;
import kg.itschool.crm.models.CourseFormat;

import java.sql.*;

public class CourseDaoImpl implements CourseDao {

    public CourseDaoImpl() {
        Connection connection = null;
        Statement statement = null;

        try {
            System.out.println("Connecting to database...");
            connection = getConnection();
            System.out.println("Connection succeeded.");

            String ddlQuery = "CREATE TABLE IF NOT EXISTS tb_courses(" +
                    "id           BIGSERIAL, " +
                    "name   VARCHAR(50)  NOT NULL, " +
                    "price       MONEY        NOT NULL, " +
                    "duration    INTEGER         NOT NULL, " +
                    "date_created TIMESTAMP    NOT NULL DEFAULT NOW(), " +
                    "" +
                    "CONSTRAINT pk_course_id PRIMARY KEY(id))";

            System.out.println("Creating statement...");
            statement = connection.createStatement();
            System.out.println("Executing create table statement...");
            statement.execute(ddlQuery);
            System.out.println(ddlQuery);

        } catch (SQLException e) {
            System.out.println("Some error occurred");
            e.printStackTrace();
        } finally {
            close(statement);
            close(connection);
        }
    }

    @Override
    public Course save(Course course) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Course savedCourse = null;
       // CourseFormat courseFormat = null;

        try {
            System.out.println("Connecting to database...");
            connection = getConnection();
            System.out.println("Connection succeeded.");

            String createQuery = "INSERT INTO tb_courses(" +
                    "name, price, duration, date_created ) " +

                    "VALUES(?, ?, ?, ?)";

            preparedStatement = connection.prepareStatement(createQuery);
            preparedStatement.setString(1, course.getName());
            preparedStatement.setString(2, String.valueOf(course.getPrice()));
//            preparedStatement.setString(3, String.valueOf(course.getDuration()));
            preparedStatement.setTimestamp(4, Timestamp.valueOf(course.getDateCreated()));

            preparedStatement.execute();
            close(preparedStatement);

            String readQuery = "SELECT * FROM tb_courses ORDER BY id DESC LIMIT 1";

            preparedStatement = connection.prepareStatement(readQuery);
            resultSet = preparedStatement.executeQuery();

            resultSet.next();

            CourseFormat courseFormat = new CourseFormat();
            courseFormat.setId(resultSet.getLong("f.id"));
            courseFormat.setFormat(resultSet.getString("course_format"));
            courseFormat.setOnline(resultSet.getBoolean("is_online"));
            courseFormat.setLessonDuration(resultSet.getTime("lesson_duration").toLocalTime());
            courseFormat.setCourseDurationWeeks(resultSet.getInt("course_duration_weeks"));
            courseFormat.setDateCreated(resultSet.getTimestamp("date_created").toLocalDateTime());
            courseFormat.setLessonPerWeek(resultSet.getInt("lessons_per_week"));


            savedCourse = new Course();
            savedCourse.setId(resultSet.getLong("id"));
            savedCourse.setName(resultSet.getString("name"));
            savedCourse.setPrice(Double.parseDouble(resultSet.getString("price")));
            savedCourse.setDateCreated(resultSet.getTimestamp("date_created").toLocalDateTime());
            savedCourse.setCourseFormat(courseFormat);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(resultSet);
            close(preparedStatement);
            close(connection);
        }
        return savedCourse;
    }

    @Override
    public Course findById(Long id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Course course = null;

        try {
            connection = getConnection();

            String readQuery = "SELECT c.id, c.name, c.price, c.date_created, f.* " +
                    "FROM tb_course AS c " +
                    "JOIN tb _course_format AS f " +
                    "On c.course_format_id = f.id " +
                    "WHERE c.id = ?;";

            preparedStatement = connection.prepareStatement(readQuery);
            preparedStatement.setLong(1, id);

            resultSet = preparedStatement.executeQuery();
            resultSet.next();

            course = new Course();
            course.setId(resultSet.getLong("id"));
            course.setName(resultSet.getString("name"));
            course.setPrice(Double.parseDouble(resultSet.getString("price")));
//            course.setDuration(Integer.parseInt(resultSet.getString("duration")));
            course.setDateCreated(resultSet.getTimestamp("date_created").toLocalDateTime());

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(resultSet);
            close(preparedStatement);
            close(connection);
        }
        return course;
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
