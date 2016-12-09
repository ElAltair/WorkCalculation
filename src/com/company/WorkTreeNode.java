package com.company;

import java.util.ArrayList;


public class WorkTreeNode {

    public enum NodeType {
        StartNode, EndNode, WorkNode
    }

    private NodeType type;
    private Work nodeWork;
    private ArrayList<WorkTreeNode> nodes;
    private WorkTreeNode prevNode;
    private WorkTreeNode endNode;

    public  WorkTreeNode(NodeType iType, Work iWork)
    {

        nodes = new ArrayList<>();
        type = iType;
        prevNode = null;
        endNode = null;
        if(type == NodeType.WorkNode)
            nodeWork = iWork;
    }
    public boolean isStartNode()
    {
        return type == NodeType.StartNode;
    }

    public boolean isEndNode()
    {
        return endNode == null && type == NodeType.EndNode;
    }

    public void setPrevNode(WorkTreeNode iNode)
    {
        prevNode = iNode;
    }

    public void addEndNode(WorkTreeNode treeNode)
    {
        endNode = treeNode;
    }

    public void addChild(WorkTreeNode treeNode)
    {
        nodes.add(treeNode);
        if(endNode != null)
            endNode = null;
    }

    public WorkTreeNode getEndNode()
    {
        return endNode;
    }

    public WorkTreeNode getPrevNode()
    {
        return prevNode;
    }

    public ArrayList<WorkTreeNode> getChilds()
    {
        return nodes;
    }

    public Work getWork()
    {
        if (type != NodeType.StartNode || type != NodeType.EndNode)
        {
            return nodeWork;
        }
        return null;
    }
}
