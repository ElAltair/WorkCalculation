package com.company;

public class Main {


    public static void main(String[] args) {

        try{
            Project project = new Project("Test","Project.xml");
            project.analyzeWorks();
            project.printProject();
            project.printProject("ProjectReport.txt");
        }
        catch (Exception e)
        {
            System.out.println(e.getStackTrace());
            System.out.println(e.getMessage());
        }
    }
}
