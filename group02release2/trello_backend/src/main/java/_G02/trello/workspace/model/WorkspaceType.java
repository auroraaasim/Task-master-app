package _G02.trello.workspace.model;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum WorkspaceType {
    OPERATION,
    ENGINEERING,
    EDUCATION,
    MARKETING,
    HUMAN_RESOURCE,
    CRM,
    OTHER,
    @JsonEnumDefaultValue
    UNDEFINED
}
