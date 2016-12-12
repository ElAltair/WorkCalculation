package com.company;

import java.util.ArrayList;

public class Work {

    public enum WorkParam { ASAP, ALAP}

    private WorkParam workParam;
    private Integer id;
    private Double startDate;
    private Double endDate;
    private Double duration;
    private Boolean isOnCrititcalPath;
    private ArrayList<Work> prevWorks;
    private String name;

    private void updateEndDate() {
        endDate = startDate + duration;
    }
    private void updateStartDate()
    {
        startDate = endDate - duration;
    }

    public Work()
    {
        name = null;
        startDate = 0.0;
        duration = 0.0;
        endDate = 0.0;
        isOnCrititcalPath = false;
        prevWorks = new ArrayList<>();
    }

    public Work(String iName) {
        name = iName;
        startDate = 0.0;
        duration = 0.0;
        endDate = 0.0;
        prevWorks = new ArrayList<>();
        updateEndDate();
    }

    public Work(String iName, Integer iId)
    {
        this(iName);
        id = iId;
    }


    public Work(String iName, Double iStartDate, Double iDuration) {
        this(iName);
        startDate = iStartDate;
        duration = iDuration;
        prevWorks = new ArrayList<>();
        updateEndDate();
    }


    public void setAfter(Work iWork)
    {
        prevWorks.add(iWork);
    }

    public void setStartDate(Double iStartDate) {
        startDate = iStartDate;
        //updateEndDate();
    }

    public void setDuration(Double iDuration) {
        duration = iDuration;
        //updateEndDate();
    }

    public void setEndDate(Double iEndDate)
    {
        endDate = iEndDate;
    }

    public void setName(String iName)
    {
        name = iName;
    }

    public void setId(Integer iId)
    {
        id = iId;
    }

    public void setParam(WorkParam param)
    {
        workParam = param;
    }

    public void setCriticalWork()
    {
        isOnCrititcalPath = true;
    }

    public void unsetCriticalWork()
    {
        isOnCrititcalPath = false;
    }

    public void updateWorkLimits()
    {
        if(workParam == WorkParam.ASAP || workParam == null)
        {
            updateEndDate();
        }
        else if (workParam == WorkParam.ALAP)
        {
            updateStartDate();
        }
    }

    public  Double getStart() { return startDate;}

    public Double getEnd() {
        return endDate;
    }

    public ArrayList<Work> getPrevWork()
    {
        return prevWorks;
    }

    public String getName()
    {
        return name;
    }

    public Integer getId()
    {
        return id;
    }

    public Double getDuration(){ return duration;}

    public Boolean isOnCrititcalPath()
    {
        return  isOnCrititcalPath;
    }

    @Override
    public String toString() {
        String criticalPath = " ";
        if(isOnCrititcalPath)
            criticalPath = "@";
        String params = "";
        if(workParam != null)
        {
            params = "| Params: " +  workParam.toString();
        }

        String prevWork ="";
        if(prevWorks.size() != 0) {
            prevWork = " | Prev works: ";
            for (Work w : prevWorks) {
                prevWork += w.getId().toString() + " ";
            }
        }
            return criticalPath + "Id: " + id.toString() + " | Name: " + name +  " | StartDate: "
                    + startDate.toString() + " | Duration: " + duration + " | End: " + endDate.toString() + prevWork + params;
    }

}
