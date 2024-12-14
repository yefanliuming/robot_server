package com.example.demo;

import java.util.List;

public class TaskRequest {
    private String operationType;
    private String buttonAction;
    private String switchNumber;
    private String actionDirection;
    private String robotId;
    private List<String> selectedTables;

    public String getActionType() {
        return actionType;
    }

    private String actionType;

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public void setButtonAction(String buttonAction) {
        this.buttonAction = buttonAction;
    }

    public void setSwitchNumber(String switchNumber) {
        this.switchNumber = switchNumber;
    }

    public void setActionDirection(String actionDirection) {
        this.actionDirection = actionDirection;
    }

    public void setRobotId(String robotId) {
        this.robotId = robotId;
    }

    public void setSelectedTables(List<String> selectedTables) {
        this.selectedTables = selectedTables;
    }

    public String getButtonAction() {
        return buttonAction;
    }

    public String getOperationType() {
        return operationType;
    }

    public String getSwitchNumber() {
        return switchNumber;
    }

    public String getActionDirection() {
        return actionDirection;
    }

    public String getRobotId() {
        return robotId;
    }

    public List<String> getSelectedTables() {
        return selectedTables;
    }
// Getters and setters
}
