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
        return new PrintWriter(outSWriter);

    }

    private PrintWriter getProjectWriter()
    {
            OutputStream fOStream = System.out;
            OutputStreamWriter outSWriter = new OutputStreamWriter(fOStream);
            return new PrintWriter(outSWriter);
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

                    NodeList afterList = el.getElementsByTagName("after");
                    //String after = el.getElementsByTagName("after")
                    //        .item(0).getTextContent();
                    for(int j = 0; j < afterList.getLength(); ++j)
                    {

                        Node afterNode =  afterList.item(j);
                        if(afterNode.getNodeType() == Node.ELEMENT_NODE)
                        {
                            Element after = (Element) afterNode;
                            String afterValue = after.getTextContent();
                            if(afterValue != null && !afterValue.isEmpty())
                            {
                                Integer workId = Integer.parseInt(afterValue);

                                Work temp;
                                if((temp = workMap.get(workId)) != null)
                                    work.setAfter(temp);
                                else
                                    throw new DataFormatException("Error in '" + fileName + "'. Work with id = " + workId + " is not created");

                            }
                        }
                    }

                    if(workMap.get(work.getId()) == null)
                        workMap.put(work.getId(), work);
                    else
                        throw new DataFormatException("Error in '" + fileName + "'. Work id = " + work.getId() + " is already used");

                }
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
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
            drawWorkDiagramFinal(writer, workMap);
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
            drawWorkDiagramFinal(writer, workMap);
            //projectTree.printTree(writer);
            writer.println(partDelimeter);
            writer.close();
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
    }

    public void forwardTreeMoving(WorkTreeNode node)
    {

        if(node.isEndNode()) {
            return;
        }

        Double maxStart = 0.0;
        if(node.getPrevNodes().size() != 0 && !node.isStartNode())
        {
            ArrayList<WorkTreeNode> prevNodes = node.getPrevNodes();
            for(WorkTreeNode i: prevNodes)
            {
                if(i.getWork().getMinStarDate() + i.getWork().getDuration() > maxStart)
                    maxStart = i.getWork().getMinStarDate() + i.getWork().getDuration();
            }

            node.getWork().setMinStartDate(maxStart);

        }

        ArrayList<WorkTreeNode> childs = node.getNextNodes();

        for (WorkTreeNode it: childs) {
            forwardTreeMoving(it);
        }
    }

    public void updateChildLimits(WorkTreeNode node, LinkedHashMap<Integer, Work> onOptimizedWorks)
    {
       //if(node.getWork() != null && !node.isEndNode())
       //    return;

        for(WorkTreeNode it: node.getNextNodes())
        {
            if(onOptimizedWorks.get(it.getWork().getId())!= null)
            {
                it.getWork().setStartDate(node.getWork().getEnd());
                it.getWork().updateWorkLimits();
                onOptimizedWorks.remove(it.getWork().getId());
                updateChildLimits(it, onOptimizedWorks);

            }
        }
    }

    public void finalTreeMoving(WorkTreeNode node, LinkedHashMap<Integer, Work> unOptimizedWorks)
    {

        if(node.isEndNode()) {
            return;
        }

        if(node.getWork() != null && unOptimizedWorks.get(node.getWork().getId()) != null)
        {
            double delta = node.getWork().getMaxEndDate() - node.getWork().getMinStarDate();
            node.getWork().setStartDate(node.getWork().getMinStarDate());
            node.getWork().setEndDate(node.getWork().getMaxEndDate());

            if(delta != node.getWork().getDuration())
            {
                    node.getWork().unsetCriticalWork();
                    node.getWork().updateWorkLimits();
                    updateChildLimits(node, unOptimizedWorks);
                    unOptimizedWorks.remove(node.getWork().getId());
            }
            else
            {
                node.getWork().setCriticalWork();
                unOptimizedWorks.remove(node.getWork().getId());

            }
        }

        ArrayList<WorkTreeNode> childs = node.getNextNodes();

        for (WorkTreeNode it: childs) {
            finalTreeMoving(it, unOptimizedWorks);
        }
    }

    public void backwardTreeMoving(WorkTreeNode node)
    {
        if(node.isStartNode())
            return;

        if(node.getWork() != null && node.getWork().getEnd() == 0)
            node.getWork().setMaxEndDate(endDate);
        Double minStart = Double.MAX_VALUE;
        if(node.getNextNodes().size() != 0 && !node.isEndNode())
        {
            ArrayList<WorkTreeNode> nextNodes = node.getNextNodes();
            for(WorkTreeNode it: nextNodes){
                if(it.getWork().getMaxEndDate() - it.getWork().getDuration() < minStart)
                    minStart = it.getWork().getMaxEndDate() - it.getWork().getDuration();
            }
            node.getWork().setMaxEndDate(minStart);
        }

        ArrayList<WorkTreeNode> prevNodes = node.getPrevNodes();
        for(WorkTreeNode it: prevNodes) {
            backwardTreeMoving(it);
        }


    }

    public void analyzeWorks()
    {
            PrintWriter writer = getProjectWriter();
            analyzeForwardWork();
            drawWorkDiagramForward(writer, workMap);
            writer.println("---------------------------------");
            Double maxDate = Double.MIN_VALUE;
            for(Work i : workMap.values())
            {
                if(i.getMinStarDate() + i.getDuration() > maxDate)
                    maxDate = i.getMinStarDate() + i.getDuration();
            }
            System.out.println(maxDate);
            endDate = maxDate;
            analyzeBackwardWork();
            drawWorkDiagramBackward(writer, workMap);
            writer.println("---------------------------------");
            LinkedHashMap<Integer, Work> unOptimizedWorks = new LinkedHashMap<>();
            unOptimizedWorks.putAll(workMap);
            for(Work i: unOptimizedWorks.values())
            {
                System.out.println(i);
            }
            finalAnalyzeWorks(unOptimizedWorks);
            drawWorkDiagramFinal(writer, workMap);
            writer.close();
    }

    public void analyzeWorks(String fileName)
    {
        //try{
            //PrintWriter writer = getProjectWriter(fileName);
            analyzeForwardWork();
            //drawWorkDiagramForward(writer, workMap);
            analyzeBackwardWork();
            //drawWorkDiagramBackward(writer, workMap);
            LinkedHashMap<Integer, Work> unOptimizedWorks = new LinkedHashMap<>();
            unOptimizedWorks.putAll(workMap);
            finalAnalyzeWorks(unOptimizedWorks);
            //drawWorkDiagramForward(writer, workMap);
            //writer.close();

        /*
        }
        catch (IOException e){
            System.err.println(e.getStackTrace());
            System.err.println(e.getMessage());
        }
        */
    }

    public void analyzeForwardWork()
    {
        WorkTreeNode startTreeNode = projectTree.getStartTreeNode();
        forwardTreeMoving(startTreeNode);
    }

    public void finalAnalyzeWorks(LinkedHashMap<Integer, Work> unOptimizedWorks)
    {
        WorkTreeNode startTreeNode = projectTree.getStartTreeNode();
        finalTreeMoving(startTreeNode, unOptimizedWorks);

        /*
        for(Work w: workMap.values())
        {
            Double duration = w.getMaxEndDate() - w.getMinStarDate();
            w.setStartDate(w.getMinStarDate());
            w.setEndDate(w.getMaxEndDate());
            if(!duration.equals(w.getDuration()))
            {
                w.unsetCriticalWork();
                w.updateWorkLimits();
            }
            else
                w.setCriticalWork();
        }
        */
    }

    public void analyzeBackwardWork()
    {
        WorkTreeNode endTreeNode = projectTree.getEndTreeNode();
        for(Work w: workMap.values())
        {
            w.setMaxEndDate(endDate);
        }
        for(Work w: workMap.values()) {
            System.out.println(w);
        }
        backwardTreeMoving(endTreeNode);
    }

    void drawWorkDiagramForward(PrintWriter writer, Map<Integer, Work> workList)
    {
        for(Work it: workList.values())
        {
            String spacer = "";
            String length = "";
            String criticalPath = " ";
            Double duration = it.getDuration();
            for(int i = 0; i < it.getMinStarDate(); ++i)
                spacer += " ";

           for(int i =0; i < duration.intValue(); ++i)
               length += "#";
            if(it.isOnCrititcalPath())
                criticalPath = "@";

            writer.print(criticalPath + " ");
            writer.format("%1$3s", it.getId().toString());
            writer.println(spacer + length);
        }
    }

    void drawWorkDiagramForward(Map<Integer, Work> workList)
    {
        for(Work it: workList.values())
        {
            String spacer = "";
            String length = "";
            String criticalPath = " ";
            Double duration = it.getDuration();
            for(int i = 0; i < it.getMinStarDate(); ++i)
                spacer += " ";

            for(int i =0; i < duration.intValue(); ++i)
                length += "#";

            if(it.isOnCrititcalPath())
                criticalPath = "@";
            System.out.print(criticalPath + " ");
            System.out.format("%1$3s", it.getId().toString());
            System.out.println(criticalPath + spacer + length);
        }
    }

    void drawWorkDiagramFinal(PrintWriter writer, Map<Integer, Work> workList)
    {
        for(Work it: workList.values())
        {
            String spacer = "";
            String length = "";
            String criticalPath = " ";
            Double duration = it.getDuration();
            for(int i = 0; i < it.getStart(); ++i)
                spacer += " ";

            for(int i =0; i < duration.intValue(); ++i)
                length += "#";
            if(it.isOnCrititcalPath())
                criticalPath = "@";

            writer.print(criticalPath + " ");
            writer.format("%1$3s", it.getId().toString());
            writer.println(spacer + length);
        }
    }

    void drawWorkDiagramFinal(Map<Integer, Work> workList)
    {
        for(Work it: workList.values())
        {
            String spacer = "";
            String length = "";
            String criticalPath = " ";
            Double duration = it.getDuration();
            for(int i = 0; i < it.getStart(); ++i)
                spacer += " ";

            for(int i =0; i < duration.intValue(); ++i)
                length += "#";

            if(it.isOnCrititcalPath())
                criticalPath = "@";
            System.out.print(criticalPath + " ");
            System.out.format("%1$3s", it.getId().toString());
            System.out.println(criticalPath + spacer + length);
        }
    }

    void drawWorkDiagramBackward(PrintWriter writer, Map<Integer, Work> workList)
    {
        for(Work it: workList.values())
        {
            String spacer = "";
            String length = "";
            String criticalPath = " ";
            Double duration = it.getDuration();
            for(int i = 0; i < it.getMaxEndDate() - it.getDuration(); ++i)
                spacer += " ";

            for(int i =0; i < duration.intValue(); ++i)
                length += "#";

            if(it.isOnCrititcalPath())
                criticalPath = "@";
            writer.print(criticalPath + " ");
            writer.format("%1$3s", it.getId().toString());
            writer.println(spacer + length);
        }
    }

    void drawWorkDiagramBackward(Map<Integer, Work> workList)
    {
        for(Work it: workList.values())
        {
            String spacer = "";
            String length = "";
            String criticalPath = " ";
            Double duration = it.getDuration();
            for(int i = 0; i < it.getMaxEndDate() - it.getDuration(); ++i)
                spacer += " ";

            for(int i =0; i < duration.intValue(); ++i)
                length += "#";
            if(it.isOnCrititcalPath())
                criticalPath = "@";
            System.out.print(criticalPath + " ");
            System.out.format("%1$3s", it.getId().toString());
            System.out.println(spacer + length);
        }
    }
}
