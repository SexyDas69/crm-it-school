package kg.itschool.crm;

import kg.itschool.crm.dao.*;
import kg.itschool.crm.dao.daoutil.DaoFactory;
import kg.itschool.crm.models.Manager;

import java.time.LocalDate;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        Manager manager = new Manager();

        System.out.print("First name: ");
        manager.setFirstName(scan.nextLine());

        System.out.print("Last name: ");
        manager.setLastName(scan.nextLine());

        System.out.print("Email: ");
        manager.setEmail(scan.nextLine());

        System.out.print("Phone number: ");
        manager.setPhoneNumber(scan.nextLine());

        System.out.print("Date of birth: ");
        manager.setDob(LocalDate.parse(scan.nextLine()));// yyyy-mm-dd

        System.out.print("Salary: ");
        manager.setSalary(scan.nextDouble());

        System.out.println(manager);

        ManagerDao managerDao = DaoFactory.getManagerDaoSql();
        managerDao.save(manager);


    }
}
