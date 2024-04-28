package com.example.registration;

public class TaskHelper {
    String taskLabel, taskDate;

    public TaskHelper(String taskLabel, String taskDate) {
        this.taskLabel = taskLabel;
        this.taskDate = taskDate;
    }

    public String getTaskLabel() { return taskLabel; }

    public void setTaskLabel(String taskLabel) { this.taskLabel = taskLabel; }

    public String getTaskDate() { return taskDate; }

    public void setTaskDate(String taskDate) { this.taskDate = taskDate; }
}
