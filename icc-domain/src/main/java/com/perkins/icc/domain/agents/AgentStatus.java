package com.perkins.icc.domain.agents;

import lombok.Getter;

@Getter
public enum AgentStatus {
    Available("Available"), Logged_Out("Logged Out"), On_Break("On Break");

    private String status;

    AgentStatus(String status) {
        this.status = status;
    }
}
