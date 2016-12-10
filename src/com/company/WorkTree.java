package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class WorkTree {
    private WorkTreeNode startTreeNode;
    private WorkTreeNode endTreeNode;

    public WorkTree()
    {
        startTreeNode = new WorkTreeNode(WorkTreeNode.NodeType.StartNode, null);
        endTreeNode = new WorkTreeNode(WorkTreeNode.NodeType.EndNode,null);
    }

    public void generate(Map<Integer, Work> workList)
    {
        WorkTreeNode currentNode = startTreeNode;

        for (Work w: workList.values()) {

            Work prevWork = w.getPrevWork();

           if(prevWork == null)
           {
               WorkTreeNode newNode = new WorkTreeNode(WorkTreeNode.NodeType.WorkNode, w);
               newNode.addEndNode(endTreeNode);
               startTreeNode.addChild(newNode);
           }
           else
           {
               WorkTreeNode newNode = new WorkTreeNode(WorkTreeNode.NodeType.WorkNode, w);
               newNode.addEndNode(endTreeNode);
               WorkTreeNode findedNode = findWork(prevWork);
               if(findedNode != null)
                   findedNode.addChild(newNode);
           }

        }
        endTreeNode = currentNode;

    }


    void printTree()
    {
        try{
            WorkTreeNode node = startTreeNode;
            Integer treeDepth = 0;
            Integer itemSpacer = 12;
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
            AtomicBoolean printFlag = new AtomicBoolean(false);
            printTreeRecursivelyBeauty(writer, node, treeDepth, printFlag, itemSpacer);
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }

    }

    void printTree(PrintWriter writer)
    {
            WorkTreeNode node = startTreeNode;
            Integer treeDepth = 0;
            Integer itemSpacer = 12;
            AtomicBoolean printFlag = new AtomicBoolean(false);
            printTreeRecursivelyBeauty(writer, node, treeDepth, printFlag, itemSpacer);

    }

    void printTreeRecursively(WorkTreeNode node, String treeDestination)
    {
        String tempDest;
        if(node.getEndNode() != null) {
            System.out.println(treeDestination + "|");
            return;
        }
        ArrayList<WorkTreeNode> childs = node.getChilds();

        for (WorkTreeNode it: childs) {


            //tempDest = treeDestination + it.getWork().getName() + " -> ";
            tempDest = it.getWork().getName();
            printTreeRecursively(it, tempDest);
        }
    }

    void printTreeRecursivelyBeauty(PrintWriter w, WorkTreeNode node, Integer depth, AtomicBoolean printFlag, Integer itemSpacer)
    {
            depth++;
            if (node.getEndNode() != null) {
                w.println(" --> |");
                if (depth >= 2)
                    printFlag.getAndSet(true);
                return;
            }
            ArrayList<WorkTreeNode> childs = node.getChilds();

            for (WorkTreeNode it : childs) {

                if (depth == 1)
                    w.print("|");

                if (depth >= 2 && printFlag.get()) {
                    String spacer = "";
                    for (int i = 0; i < 6 + itemSpacer; ++i) {
                        spacer += " ";
                    }
                    spacer += "\\--> ";
                    w.print(spacer);
                    w.format("%1$" + itemSpacer.toString() + "s", it.getWork().getName());
                } else {
                    w.print(" --> ");
                    w.format("%1$" + itemSpacer.toString()+ "s", it.getWork().getName());
                }
                printFlag.getAndSet(false);
                printTreeRecursivelyBeauty(w, it, depth, printFlag, itemSpacer);
            }
    }
    private WorkTreeNode findWorkRecursively(WorkTreeNode node, Work toFind)
    {
        WorkTreeNode returnNode = null;

        if(node.isEndNode()) {
            return null;
        }

        if(node.getWork() == toFind)
        {
            return node;
        }

        ArrayList<WorkTreeNode> childs = node.getChilds();

        for (WorkTreeNode it: childs) {
            returnNode = findWorkRecursively(it, toFind);
            if(returnNode != null)
                break;
        }
        return returnNode;
    }

    private WorkTreeNode findWorkRecursivelyByName(WorkTreeNode node, String workName)
    {
        WorkTreeNode returnNode = null;
        if(node.isEndNode()) {
            return null;
        }

        if(!node.isStartNode() && workName.equals(node.getWork().getName()))
        {
            return node;
        }

        ArrayList<WorkTreeNode> childs = node.getChilds();

        for (WorkTreeNode it: childs) {
            returnNode = findWorkRecursivelyByName(it, workName);
            if(returnNode != null)
                break;
        }
        return returnNode;
    }

    private WorkTreeNode findWorkRecursivelyById(WorkTreeNode node, Integer id)
    {
        WorkTreeNode returnNode = null;
        if(node.isEndNode()) {
            return null;
        }

        if(!node.isStartNode() && id.equals(node.getWork().getId()))
        {
            return node;
        }

        ArrayList<WorkTreeNode> childs = node.getChilds();

        for (WorkTreeNode it: childs) {
            returnNode = findWorkRecursivelyById(it, id);
            if(returnNode != null)
                break;
        }
        return returnNode;
    }


    public WorkTreeNode findWork(Work toFind)
    {
        return findWorkRecursively(startTreeNode, toFind);
    }

    public WorkTreeNode findWork(Integer id)
    {
        return findWorkRecursivelyById(startTreeNode, id);
    }

    public WorkTreeNode findWork(String workName)
    {
        return findWorkRecursivelyByName(startTreeNode, workName);
    }
}
