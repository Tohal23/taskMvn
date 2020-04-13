package org.task.example.model;

public class SharedTaskResult {
    private SharedTaskState status;
    private String message;

    public SharedTaskResult() {
    }

    public SharedTaskResult(SharedTaskState status, String message) {
        this.status = status;
        this.message = message;
    }

    public SharedTaskState getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}

