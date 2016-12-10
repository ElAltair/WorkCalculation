package com.company;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
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

    public Project(String iName)
    {
        projectTree = new WorkTree();
        workMap = new LinkedHashMap<>();
        name = iName;
    }
    public Project(String iName, String ixmlFile) throws DataFormatException
    {
        this(iName);
        try{
            parseXML(ixmlFile);
            projectTree.generate(workMap);
        }
        catch (DataFormatException e)
        {
            throw e;
        }
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
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace(System.out);
        } catch (SAXException ex) {
            ex.printStackTrace(System.out);
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        }
    }

    public void printProject()
    {
        System.out.println("------- Project -------");
        System.out.println("Name: " + name);
        System.out.println("StartDate: " + startDate);
        System.out.println("EndDate: " + endDate);
        projectTree.printTree();
    }
}
