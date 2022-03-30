package kg.itschool.crm.dao.impl;

import kg.itschool.crm.dao.CourseFormatDao;
import kg.itschool.crm.dao.daoutil.Log;
import kg.itschool.crm.models.CourseFormat;

import java.sql.*;
import java.time.LocalTime;

public class CourseFormatDaoImpl implements CourseFormatDao {

    public CourseFormatDaoImpl() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            Log.info(this.getClass().getSimpleName(), Connection.class.getSimpleName(), " establishing connection");
            connection = getConnection();
            Log.info(this.getClass().getSimpleName(), connection.getClass().getSimpleName(), " connection established");

            String ddlQuery = "CREATE TABLE IF NOT EXISTS tb_course_format(" +
                    "id                     BIGSERIAL,             " +
                    "course_format          VARCHAR(50)  NOT NULL, " +
                    "course_duration_weeks  INTEGER      NOT NULL, " +
                    "lesson_duration        TIME         NOT NULL, " +
                    "lessons_per_week       INTEGER      NOT NULL, " +
                    "is_online              BOOLEAN      NOT NULL, " +
                    "date_created           TIMESTAMP    NOT NULL DEFAULT NOW(), " +
                    "" +
                    "CONSTRAINT pk_course_id PRIMARY KEY(id), " +
                    "CONSTRAINT course_duration_weeks_negative_or_zero CHECK (course_duration_weeks > 0), " +
                    "CONSTRAINT lesson_per_week_negative_or_zero CHECK (lessons_per_week > 0) " +
                    ");";

            Log.info(this.getClass().getSimpleName(), PreparedStatement.class.getSimpleName(), " creating preparedStatement...");
            preparedStatement = connection.prepareStatement(ddlQuery);
            Log.info(this.getClass().getSimpleName(), Statement.class.getSimpleName(), " executing create table preparedStatement...");
            preparedStatement.execute();

        } catch (SQLException e) {
            Log.error(this.getClass().getSimpleName(), e.getStackTrace()[0].getClass().getSimpleName(), e.getMessage());
            e.printStackTrace();
        } finally {
            close(preparedStatement);
            close(connection);
        }
    }


    @Override
    public CourseFormat save(CourseFormat courseFormat) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        CourseFormat savedCourseFormat = null;

        try {
            Log.info(this.getClass().getSimpleName(), Connection.class.getSimpleName(), " connecting to database...");
            connection = getConnection();
            Log.info(this.getClass().getSimpleName(), Connection.class.getSimpleName(), " connection succeeded.");

            String createQuery = "INSERT INTO tb_course_format(" +
                    "format, courseDurationWeeks, lessonDuration, lessonPerWeek, isOnline, date_created ) " +

                    "VALUES(?, ?, ?, ?, ?, ?)";

            preparedStatement = connection.prepareStatement(createQuery);
            preparedStatement.setString(1, courseFormat.getFormat());
            preparedStatement.setInt(2, courseFormat.getCourseDurationWeeks());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(String.valueOf(courseFormat.getLessonDuration())));
            preparedStatement.setInt(4, courseFormat.getLessonPerWeek());
            preparedStatement.setBoolean(5, courseFormat.isOnline());
            preparedStatement.setTimestamp(6, Timestamp.valueOf(courseFormat.getDateCreated()));

            preparedStatement.execute();
            close(preparedStatement);

            String readQuery = "SELECT * FROM tb_course_format ORDER BY id DESC LIMIT 1";

            preparedStatement = connection.prepareStatement(readQuery);

            resultSet = preparedStatement.executeQuery();
            resultSet.next();

            savedCourseFormat = new CourseFormat();
            savedCourseFormat.setFormat(resultSet.getString("format"));
            savedCourseFormat.setCourseDurationWeeks(resultSet.getInt("course_duration_weeks"));
            savedCourseFormat.setLessonDuration(LocalTime.parse(resultSet.getString("lesson_duration")));
            savedCourseFormat.setLessonPerWeek(resultSet.getInt("lesson_per_week"));
            savedCourseFormat.setOnline(resultSet.getBoolean("is_online"));
            savedCourseFormat.setDateCreated(resultSet.getTimestamp("date_created").toLocalDateTime());

        } catch (SQLException e) {
            Log.error(this.getClass().getSimpleName(), e.getStackTrace()[0].getClass().getSimpleName(), e.getMessage());
            e.printStackTrace();
        } finally {
            close(resultSet);
            close(preparedStatement);
            close(connection);
        }
        return savedCourseFormat;
    }

    @Override
    public CourseFormat findById(Long id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        CourseFormat courseFormat = null;

        try {
            Log.info(this.getClass().getSimpleName(), Connection.class.getSimpleName(), " connecting to database...");
            connection = getConnection();
            Log.info(this.getClass().getSimpleName(), Connection.class.getSimpleName(), " connection succeeded.");

            String readQuery = "SELECT * FROM tb_course_format WHERE id = ?";

            preparedStatement = connection.prepareStatement(readQuery);
            preparedStatement.setLong(1, id);

            resultSet = preparedStatement.executeQuery();
            resultSet.next();

            courseFormat = new CourseFormat();
            courseFormat.setFormat(resultSet.getString("format"));
            courseFormat.setCourseDurationWeeks(resultSet.getInt("course_duration_weeks"));
            courseFormat.setLessonDuration(LocalTime.parse(resultSet.getString("lesson_duration")));
            courseFormat.setLessonPerWeek(resultSet.getInt("lesson_per_week"));
            courseFormat.setOnline(resultSet.getBoolean("is_online"));
            courseFormat.setDateCreated(resultSet.getTimestamp("date_created").toLocalDateTime());

        } catch (SQLException e) {
            Log.error(this.getClass().getSimpleName(), e.getStackTrace()[0].getClass().getSimpleName(), e.getMessage());
            e.printStackTrace();
        } finally {
            close(resultSet);
            close(preparedStatement);
            close(connection);
        }
            return courseFormat;
    }

    private void close(AutoCloseable closeable) {
        try {
            Log.info(this.getClass().getSimpleName(), closeable.getClass().getSimpleName(), " closing...");
            closeable.close();
            Log.info(this.getClass().getSimpleName(), closeable.getClass().getSimpleName(), " closed...");
        } catch (Exception e) {
            Log.error(this.getClass().getSimpleName(), e.getStackTrace()[0].getClass().getSimpleName(), e.getMessage());
            e.printStackTrace();
        }
    }
}
