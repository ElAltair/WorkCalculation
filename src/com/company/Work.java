package com.company;

public class Work {

    public enum WorkParam { ASAP, AEAP}

    private WorkParam workParam;
    private Integer id;
    private Double startDate;
    private Double endDate;
    private Double duration;
    private Work prevWork;
    private String name;

    private void updateEndDate() {
        endDate = startDate + duration;
    }

    public Work()
    {
        name = null;
        startDate = 0.0;
        duration = 0.0;
        prevWork = null;
    }

    public Work(String iName) {
        name = iName;
        startDate = 0.0;
        duration = 0.0;
        prevWork = null;
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
        prevWork = null;
        updateEndDate();
    }


    public void setAfter(Work iWork)
    {
        prevWork = iWork;
    }

    public void setStartDate(Double iStartDate) {
        startDate = iStartDate;
        updateEndDate();
    }

    public void setDuration(Double iDuration) {
        duration = iDuration;
        updateEndDate();
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

    public Double getEnd() {
        return endDate;
    }

    public Work getPrevWork()
    {
        return prevWork;
    }

    public String getName()
    {
        return name;
    }

    public Integer getId()
    {
        return id;
    }

    @Override
    public String toString() {
        return "Id: " + id.toString() + " StartDate: " + startDate.toString() + " End: " + endDate.toString();
    }

}
