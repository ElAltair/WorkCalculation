package com.company;

import java.util.ArrayList;


public class WorkTreeNode {

    public enum NodeType {
        StartNode, EndNode, WorkNode
    }

    private NodeType type;
    private Work nodeWork;
    private ArrayList<WorkTreeNode> next;
    private ArrayList<WorkTreeNode> prev;
    private WorkTreeNode endNode;

    public  WorkTreeNode(NodeType iType, Work iWork)
    {

        next = new ArrayList<>();
        type = iType;
        prev = new ArrayList<>();
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
        prev.add(iNode);
    }

    public  void removePrevNode(WorkTreeNode iNode)
    {
        Integer deleteIndex = -1;
        for(int i = 0; i < prev.size(); ++i)
        {
            if(prev.get(i) == iNode)
                deleteIndex = i;
        }
        if(deleteIndex != -1)
        prev.remove(deleteIndex);
    }

    public void addEndNode(WorkTreeNode treeNode)
    {
        endNode = treeNode;
        endNode.setPrevNode(this);
    }

    public void addChild(WorkTreeNode treeNode)
    {
        next.add(treeNode);
        if(endNode != null) {
            endNode.removePrevNode(treeNode);
            endNode = null;
        }
    }

    public WorkTreeNode getEndNode()
    {
        return endNode;
    }

    public ArrayList<WorkTreeNode> getPrevNodes()
    {
        return prev;
    }

    public ArrayList<WorkTreeNode> getNextNodes()
    {
        return next;
    }

    public Work getWork()
    {
        if (type == NodeType.WorkNode)
        {
            return nodeWork;
        }
        return null;
    }
}
