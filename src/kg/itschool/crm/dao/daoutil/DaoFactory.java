package kg.itschool.crm.dao.daoutil;

import kg.itschool.crm.dao.*;
import kg.itschool.crm.dao.impl.*;

public abstract class DaoFactory {
    static {
        try {
            System.out.println("Loading driver...");
            Class.forName("org.postgresql.Driver");
            System.out.println("Driver loaded.");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver loading failed.");
            e.printStackTrace();
        }
    }

    public static ManagerDao getManagerDaoSql() {
        return new ManagerDaoImpl();
    }

    public static StudentsDao getStudentsDaoSql() {
        return new StudentsDaoImpl();
    }

    public static CourseDao getCoursesDaoSql() {
        return new CourseDaoImpl();
    }

    public static MentorDao getMentorDaoSql() {
        return new MentorDaoImpl();
    }

    public static AddressDao getAddressDaoSql() {
        return new AddressDaoImpl();
    }
}
