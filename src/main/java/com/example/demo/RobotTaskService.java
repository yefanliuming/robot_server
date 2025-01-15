package com.example.demo;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class RobotTaskService {
    private Set<String> activeRobots = new HashSet<>();

    public void addActiveRobot(String robotId) {
        activeRobots.add(robotId);
    }

    public void removeActiveRobot(String robotId) {
        activeRobots.remove(robotId);
    }

    public Set<String> getActiveRobots() {
        return new HashSet<>(activeRobots);
    }
}