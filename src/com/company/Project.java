package com.company;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.DataFormatException;

public class Project {

    private Double startDate;
    private Double endDate;
    private String name;
    private File xmlFile;

    private WorkTree projectTree;
    private Map<Integer, Work> workMap;

    private PrintWriter getProjectWriter(String fileName) throws IOException
    {
        File file = new File(fileName);
        //OutputStream outS = new FileOutputStream(file);
        //outS = System.out;
        OutputStream fOStream = new FileOutputStream(file);
        OutputStreamWriter outSWriter = new OutputStreamWriter(fOStream);
        PrintWriter writer = new PrintWriter(outSWriter);
        return writer;

    }

    private PrintWriter getProjectWriter()
    {
            OutputStream fOStream = System.out;
            OutputStreamWriter outSWriter = new OutputStreamWriter(fOStream);
            PrintWriter writer = new PrintWriter(outSWriter);
            return writer;
    }

    public Project(String iName)
    {
        projectTree = new WorkTree();
        workMap = new LinkedHashMap<>();
        name = iName;
    }
    public Project(String iName, String ixmlFile) throws DataFormatException
    {
        this(iName);
        parseXML(ixmlFile);
        projectTree.generate(workMap);
    }

    public Project(String iName, Double iStartDate, Double iEndDate)
    {
        this(iName);
        startDate = iStartDate;
        endDate = iEndDate;
    }

    public void setStartDate(Double iStartDate)
    {
        startDate = iStartDate;
    }

    public void setEndDate(Double iEndDate)
    {
        endDate = iEndDate;
    }

    public void setName(String iName)
    {
        name = iName;
    }

    public void parseXML(String fileName) throws DataFormatException
    {
        xmlFile = new File(fileName);
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(fileName);

            NodeList projectParams = document.getElementsByTagName("project");
            for(int i = 0; i < projectParams.getLength(); ++i)
            {
                Node node = projectParams.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element el = (Element)node;
                    System.out.println("Element " + el.getNodeName());
                    System.out.println(el.getElementsByTagName("name").item(0).getTextContent());
                    setName(el.getElementsByTagName("name").item(0).getTextContent());
                    System.out.println(el.getElementsByTagName("startDate").item(0).getTextContent());
                    setStartDate(Double.parseDouble(el.getElementsByTagName("startDate").item(0).getTextContent()));
                    System.out.println(el.getElementsByTagName("endDate").item(0).getTextContent());
                    setEndDate(Double.parseDouble(el.getElementsByTagName("endDate").item(0).getTextContent()));

                }
            }
            System.out.println();

            NodeList workList = document.getElementsByTagName("work");
            for(int i =0; i < workList.getLength(); ++i)
            {

                Node node = workList.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element el = (Element)node;
                    System.out.println("Element " + el.getNodeName());

                    Work work = new Work();

                    String name = el.getElementsByTagName("name").item(0).getTextContent();
                    if(name != null && !name.isEmpty())
                    {
                        System.out.println(el.getElementsByTagName("name").item(0).getTextContent());
                        work.setName(name);
                    }

                    String id = el.getAttribute("id");
                    if(id != null && !id.isEmpty())
                    {
                        System.out.println("Attrib = " +el.getAttribute("id"));
                        work.setId(Integer.parseInt(id));

                    }

                    String startDate = el.getElementsByTagName("startDate").item(0).getTextContent();
                    if(startDate != null && !startDate.isEmpty())
                    {
                        System.out.println(el.getElementsByTagName("startDate").item(0).getTextContent());
                        work.setStartDate(Double.parseDouble(startDate));

                    }

                    String duration =el.getElementsByTagName("duration").item(0).getTextContent();
                    if(duration != null && !duration.isEmpty())
                    {
                        System.out.println(el.getElementsByTagName("duration").item(0).getTextContent());
                        work.setDuration(Double.parseDouble(duration));
                    }

                    String endDate =el.getElementsByTagName("endDate").item(0).getTextContent();
                    if(endDate != null && !endDate.isEmpty())
                    {
                        System.out.println(el.getElementsByTagName("endDate").item(0).getTextContent());
                        work.setEndDate(Double.parseDouble(endDate));
                    }

                    String params = el.getElementsByTagName("params").item(0).getTextContent();
                    if(params != null && !params.isEmpty())
                    {
                        System.out.println(el.getElementsByTagName("params").item(0).getTextContent());
                        work.setParam(Work.WorkParam.valueOf(params));
                    }

                    String after = el.getElementsByTagName("after").item(0).getTextContent();
                    if(after != null && !after.isEmpty())
                    {
                        System.out.println(el.getElementsByTagName("after").item(0).getTextContent());
                        Integer workId = Integer.parseInt(after);

                        Work temp;
                        if((temp = workMap.get(workId)) != null)
                            work.setAfter(temp);
                        else
                            throw new DataFormatException("Error in '" + fileName + "'. Work with id = " + workId + " is not created");

                    }

                    if(workMap.get(work.getId()) == null)
                        workMap.put(work.getId(), work);
                    else
                        throw new DataFormatException("Error in '" + fileName + "'. Work id = " + work.getId() + " is already used");

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
    }

    public void printProject(String fileName)
    {
        try {

            PrintWriter writer = getProjectWriter(fileName);

            String partDelimeterUnit = "------------------------";
            String partDelimeter =  "";
            for(int i = 0; i < 3; ++i)
                partDelimeter += partDelimeterUnit;

            writer.println("------- Project -------");
            writer.println("Name: " + name);
            writer.println("StartDate: " + startDate);
            writer.println("EndDate: " + endDate);
            writer.println(partDelimeter);
            writer.println("Work list:");
            for (Work w: workMap.values()) {
                writer.println(w);
            }
            writer.println(partDelimeter);
            printWorks(writer, workMap);
            //projectTree.printTree(writer);
            writer.println(partDelimeter);
            writer.close();
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());

        }
    }
    public void printProject()
    {
        try{
            PrintWriter writer = getProjectWriter();

            String partDelimeterUnit = "------------------------";
            String partDelimeter = "";
            for (int i = 0; i < 3; ++i)
                partDelimeter += partDelimeterUnit;

            writer.println("------- Project -------");
            writer.println("Name: " + name);
            writer.println("StartDate: " + startDate);
            writer.println("EndDate: " + endDate);
            writer.println(partDelimeter);
            writer.println("Work list:");
            for (Work w : workMap.values()) {
                writer.println(w);
            }
            writer.println(partDelimeter);
            printWorks(writer, workMap);
            //projectTree.printTree(writer);
            writer.println(partDelimeter);
            writer.close();
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
    }

    public void forwardTreeMoving(WorkTreeNode node, Double startValue)
    {

        if(node.isEndNode()) {
            return;
        }

        ArrayList<WorkTreeNode> childs = node.getChilds();

        for (WorkTreeNode it: childs) {
            Double duration = it.getWork().getDuration();
            it.getWork().setStartDate(startValue);
            forwardTreeMoving(it, it.getWork().getEnd());
        }
    }

    public void backwardTreeMoving(WorkTreeNode node, Double endValue)
    {

    }

    public void analyzeWorks()
    {
        analyzeForwardWork();
        //analyzeBackwardWork();
    }

    public void analyzeForwardWork()
    {
        WorkTreeNode startTreeNode = projectTree.getStartTreeNode();
        forwardTreeMoving(startTreeNode, startDate);
    }

    public void analyzeBackwardWork()
    {
        WorkTreeNode endTreeNode = projectTree.getEndTreeNode();
        forwardTreeMoving(endTreeNode, endDate);
    }

    void printWorks(PrintWriter writer, Map<Integer, Work> workList)
    {
        for(Work it: workList.values())
        {
            String spacer = "";
            String length = "";
            Double duration = it.getDuration();
            Double start = it.getStart();
            for(int i = 0; i < it.getStart(); ++i)
                spacer += " ";

           for(int i =0; i < duration.intValue(); ++i)
               length += "#";
            writer.println(it.getId().toString() + ": " + spacer + length);
        }
    }
}
