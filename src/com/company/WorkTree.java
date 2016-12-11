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

    public WorkTreeNode getStartTreeNode() { return startTreeNode;}

    public WorkTreeNode getEndTreeNode() { return endTreeNode;}

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
               newNode.setPrevNode(findedNode);
           }

        }
        endTreeNode = currentNode;

    }

    void printTree()
    {
        try{
            WorkTreeNode node = startTreeNode;
            Integer treeDepth = 0;
            String itemSpacer = "";
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
            String itemSpacer = "";
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

    void printTreeRecursivelyBeauty(PrintWriter w, WorkTreeNode node, Integer depth, AtomicBoolean printFlag, String itemSpacer)
    {
            depth++;
            if (node.getEndNode() != null) {
                w.println(" -->|");
                itemSpacer = "";
                if (depth >= 2)
                    printFlag.getAndSet(true);
                return;
            }
            ArrayList<WorkTreeNode> childs = node.getChilds();

            for (WorkTreeNode it : childs) {

                if (depth == 1)
                {
                    w.print("|");
                    itemSpacer = "";

                }

                // Print work size
                String filler = "";
                for(int i =0; i < it.getWork().getDuration().intValue(); ++i)
                    filler += "#";

                // spacer for child works
                String nodeItemSpacer = "";
                for(int i =0; i < it.getWork().getDuration().intValue(); ++i)
                    itemSpacer += " ";
                nodeItemSpacer = itemSpacer;


                /*
                if(it.getPrevNode()!= null)
                {
                    for(int i =0; i< it.getPrevNode().getWork().getDuration().intValue() ; ++i)
                        spacer += "^";
                }
                */


                if (depth >= 2 && printFlag.get()) {
                    /*
                    for (int i = 0; i < (depth - 1) * (5 + itemSpacer); ++i) {
                        spacer += " ";
                    }
                    */
                    itemSpacer += "\\--> ";
                    w.print(itemSpacer);
                    w.print(it.getWork().getId().toString());
                    w.print("/");
                    w.print(filler);
                    w.print("\\");
                    //w.format("%1$" + new Integer(it.getWork().getDuration().intValue() + itemSpacer).toString()+ "s", it.getWork().getId().toString());
                    //w.print("#");
                } else {
                    w.print("--> ");
                    //w.format("%1$" + new Integer(it.getWork().getDuration().intValue() + itemSpacer).toString()+ "s", it.getWork().getId().toString());
                    w.print(it.getWork().getId().toString());
                    w.print("/");
                    w.print(filler);
                    w.print("\\");
                }
                printFlag.getAndSet(false);
                printTreeRecursivelyBeauty(w, it, depth, printFlag, nodeItemSpacer);
                String temp = itemSpacer;
                itemSpacer = "";
                for(int i = 0; i < temp.length(); ++i)
                {
                    itemSpacer += " ";
                }
                //w.println(itemSpacer);
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
