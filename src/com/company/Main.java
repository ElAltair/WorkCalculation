package com.company;

public class Main {


    public static void main(String[] args) {

        try{
            Project project = new Project("Test","Project.xml");
            project.printProject("ProjectReport.txt");
            project.printProject();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
