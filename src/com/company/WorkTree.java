package com.company;

import java.util.ArrayList;
import java.util.HashMap;

public class WorkTree {
    private WorkTreeNode startTreeNode;
    private WorkTreeNode endTreeNode;

    public WorkTree()
    {
        startTreeNode = new WorkTreeNode(WorkTreeNode.NodeType.StartNode, null);
        endTreeNode = new WorkTreeNode(WorkTreeNode.NodeType.EndNode,null);
    }

    public void generate(HashMap<Integer, Work> workList)
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
               findedNode.addChild(newNode);
           }

        }
        endTreeNode = currentNode;

    }


    void printTree()
    {
        String destination = "| -> ";
        WorkTreeNode node = startTreeNode;
        printTreeRecursively(node,destination);

    }
    void printTreeRecursivelyStart()
    {
        String destination = "| -> ";
        WorkTreeNode node = startTreeNode;
        printTreeRecursively(node,destination);
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


            tempDest = treeDestination + it.getWork().getName() + " -> ";
            printTreeRecursively(it, tempDest);
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
