package kg.itschool.crm;

import kg.itschool.crm.dao.*;
import kg.itschool.crm.dao.daoutil.DaoContext;
import kg.itschool.crm.dao.impl.ManagerDaoImpl;
import kg.itschool.crm.model.Manager;

import java.time.LocalDate;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Manager manager = new Manager();
        ManagerDao managerDao = (ManagerDao) DaoContext.autowired("ManagerDao", "singleton");


        System.out.println("First name:");
        manager.setFirstName(scan.nextLine());

        System.out.println("LN");
        manager.setLastName(scan.nextLine());

        System.out.println("PN");
        manager.setPhoneNumber(scan.nextLine());

         System.out.println("dob ");
        manager.setDob(LocalDate.parse(scan.nextLine()));

        System.out.println("Sal");
        manager.setSalary(scan.nextDouble());
      //  scan.nextLine();



        System.out.println("Input: " + manager);

        System.out.println("From database: " + managerDao.save(manager));
    }
}
