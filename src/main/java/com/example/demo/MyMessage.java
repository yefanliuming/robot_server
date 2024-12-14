package com.example.demo;

import java.util.List;

public class MyMessage {
    private String actionType;  // 操作类型
    private String floor;       // 楼层号
    private String room;        // 房间号
    private String operationType;  // 操作类型（按按钮/搬开关）
    private String buttonAction;   // 按钮操作（合闸/分闸）
    private String switchNumber;   // 开关号
    private String actionDirection; // 上抬/下按
    private String robotId;     // 机器人编号
    private List<String> selectedTables;
    private String switchGroup;

    @Override
    public String toString() {
        return "MyMessage{" +
                "actionType='" + actionType + '\'' +
                ", floor='" + floor + '\'' +
                ", room='" + room + '\'' +
                ", operationType='" + operationType + '\'' +
                ", buttonAction='" + buttonAction + '\'' +
                ", switchNumber='" + switchNumber + '\'' +
                ", actionDirection='" + actionDirection + '\'' +
                ", robotId='" + robotId + '\'' +
                ", selectedTables=" + selectedTables +
                ", switchGroup='" + switchGroup + '\'' +
                '}';
    }

    public void setSwitchGroup(String switchGroup) {
        this.switchGroup = switchGroup;
    }

    public String getSwitchGroup() {
        return switchGroup;
    }

    public void setSelectedTables(List<String> selectedTables) {
        this.selectedTables = selectedTables;
    }

    public List<String> getSelectedTables() {
        return selectedTables;
    }

    // Getters and Setters
    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getButtonAction() {
        return buttonAction;
    }

    public void setButtonAction(String buttonAction) {
        this.buttonAction = buttonAction;
    }

    public String getSwitchNumber() {
        return switchNumber;
    }

    public void setSwitchNumber(String switchNumber) {
        this.switchNumber = switchNumber;
    }

    public String getActionDirection() {
        return actionDirection;
    }

    public void setActionDirection(String actionDirection) {
        this.actionDirection = actionDirection;
    }

    public String getRobotId() {
        return robotId;
    }

    public void setRobotId(String robotId) {
        this.robotId = robotId;
    }

}
