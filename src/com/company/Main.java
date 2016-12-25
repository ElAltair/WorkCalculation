package com.company;

import java.util.Map;

public class Main {


    public static void main(String[] args) {

        try{
            Project project = new Project("Test","Project.xml");
            Map<Integer, Work> map = project.analyzeWorks();
            project.printProject();
            project.printProject("ProjectReport.txt");
            JavafxPostProcessor.map = map;
            JavafxPostProcessor.main(new String[0]);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
