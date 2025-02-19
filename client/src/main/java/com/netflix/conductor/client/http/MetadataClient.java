/*
 * Copyright 2020 Netflix, Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.netflix.conductor.client.http;

import java.util.List;

import org.apache.commons.lang3.Validate;

import com.netflix.conductor.client.config.ConductorClientConfiguration;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;

public class MetadataClient extends ClientBase {

    /** Creates a default metadata client */
    public MetadataClient() {
        this(null);
    }

    public MetadataClient(RequestHandler requestHandler) {
        this(requestHandler, null);
    }

    public MetadataClient(
            RequestHandler requestHandler, ConductorClientConfiguration clientConfiguration) {
        super(requestHandler, clientConfiguration);
    }

    // Workflow Metadata Operations

    /**
     * Register a workflow definition with the server.
     *
     * @param workflowDef the workflow definition
     */
    public void registerWorkflowDef(WorkflowDef workflowDef) {
        Validate.notNull(workflowDef, "Workflow definition cannot be null");
        post("metadata/workflow", workflowDef);
    }

    /**
     * Validates a workflow definition with the server.
     *
     * @param workflowDef the workflow definition
     */
    public void validateWorkflowDef(WorkflowDef workflowDef) {
        Validate.notNull(workflowDef, "Workflow definition cannot be null");
        post("metadata/workflow/validate", workflowDef);
    }

    /**
     * Updates a list of existing workflow definitions
     *
     * @param workflowDefs List of workflow definitions to be updated
     */
    public void updateWorkflowDefs(List<WorkflowDef> workflowDefs) {
        Validate.notNull(workflowDefs, "Worfklow defs list cannot be null");
        put("metadata/workflow", null, workflowDefs);
    }

    /**
     * Retrieve the workflow definition
     *
     * @param name the name of the workflow
     * @param version the version of the workflow def
     * @return Workflow definition for the given workflow and version
     */
    public WorkflowDef getWorkflowDef(String name, Integer version) {
        Validate.notBlank(name, "name cannot be blank");
        return getForEntity(
                "metadata/workflow/{name}",
                new Object[] {"version", version},
                WorkflowDef.class,
                name);
    }

    /**
     * Removes the workflow definition of a workflow from the conductor server. It does not remove
     * associated workflows. Use with caution.
     *
     * @param name Name of the workflow to be unregistered.
     * @param version Version of the workflow definition to be unregistered.
     */
    public void unregisterWorkflowDef(String name, Integer version) {
        Validate.notBlank(name, "Workflow name cannot be blank");
        Validate.notNull(version, "Version cannot be null");
        delete("metadata/workflow/{name}/{version}", name, version);
    }

    // Task Metadata Operations

    /**
     * Registers a list of task types with the conductor server
     *
     * @param taskDefs List of task types to be registered.
     */
    public void registerTaskDefs(List<TaskDef> taskDefs) {
        Validate.notNull(taskDefs, "Task defs list cannot be null");
        post("metadata/taskdefs", taskDefs);
    }

    /**
     * Updates an existing task definition
     *
     * @param taskDef the task definition to be updated
     */
    public void updateTaskDef(TaskDef taskDef) {
        Validate.notNull(taskDef, "Task definition cannot be null");
        put("metadata/taskdefs", null, taskDef);
    }

    /**
     * Retrieve the task definition of a given task type
     *
     * @param taskType type of task for which to retrieve the definition
     * @return Task Definition for the given task type
     */
    public TaskDef getTaskDef(String taskType) {
        Validate.notBlank(taskType, "Task type cannot be blank");
        return getForEntity("metadata/taskdefs/{tasktype}", null, TaskDef.class, taskType);
    }

    /**
     * Removes the task definition of a task type from the conductor server. Use with caution.
     *
     * @param taskType Task type to be unregistered.
     */
    public void unregisterTaskDef(String taskType) {
        Validate.notBlank(taskType, "Task type cannot be blank");
        delete("metadata/taskdefs/{tasktype}", taskType);
    }
}
