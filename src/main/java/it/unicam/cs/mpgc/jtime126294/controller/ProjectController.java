package it.unicam.cs.mpgc.jtime126294.controller;

import it.unicam.cs.mpgc.jtime126294.model.ProjectState;
import it.unicam.cs.mpgc.jtime126294.model.Task;
import it.unicam.cs.mpgc.jtime126294.model.impl.ProjectImpl;
import it.unicam.cs.mpgc.jtime126294.model.impl.TaskImpl;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.time.Duration;
import java.util.Optional;

public class ProjectController extends BaseController {

    @FXML
    private ListView<ProjectImpl> projectList;

    @FXML
    private TableView<TaskImpl> taskTable;

    @FXML
    private TableColumn<TaskImpl, String> colDescription;

    @FXML
    private TableColumn<TaskImpl, String> colEstimate;

    @FXML
    private TableColumn<TaskImpl, String> colActual;

    @FXML
    private TableColumn<TaskImpl, String> colStatus;

    @FXML
    private Label projectTitleLabel;

    @FXML
    private Button btnCloseProject;

    @Override
    protected void onModelSet() {
        refreshProjectList();
        
        projectList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                showProjectDetails(newVal);
            }
        });
        
        setupTaskTable();
    }

    private void setupTaskTable() {
        colDescription.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getDescription()));
        colEstimate.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(formatDuration(cell.getValue().getEstimatedTime())));
        colActual.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(formatDuration(cell.getValue().getActualTime())));
        colStatus.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getState().getName()));
        
        // Context Menu
        taskTable.setRowFactory(tv -> {
            TableRow<TaskImpl> row = new TableRow<>();
            ContextMenu menu = new ContextMenu();
            MenuItem cancelItem = new MenuItem("Cancel Task");
            cancelItem.setOnAction(event -> {
                TaskImpl task = row.getItem();
                if (task != null) {
                    task.setState(it.unicam.cs.mpgc.jtime126294.model.TaskState.CANCELLED);
                    taskTable.refresh();
                }
            });
            menu.getItems().add(cancelItem);
            
            row.contextMenuProperty().bind(
                javafx.beans.binding.Bindings.when(row.emptyProperty())
                .then((ContextMenu)null)
                .otherwise(menu)
            );
            return row;
        });
    }

    private String formatDuration(Duration duration) {
        if (duration == null) return "0h";
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        return String.format("%dh %dm", hours, minutes);
    }

    private void refreshProjectList() {
        projectList.setItems(FXCollections.observableArrayList(model.getProjects()));
    }

    private void showProjectDetails(ProjectImpl project) {
        projectTitleLabel.setText(project.getName());
        taskTable.setItems(FXCollections.observableArrayList(
                project.getTasks().stream().map(t -> (TaskImpl) t).toList()
        ));
        btnCloseProject.setDisable(project.getState() == ProjectState.COMPLETED);
        btnDeleteProject.setDisable(false);
    }

    @FXML
    private void handleNewProject() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New Project");
        dialog.setHeaderText("Create a new project");
        dialog.setContentText("Project Name:");
        
        dialog.showAndWait().ifPresent(name -> {
            model.createProject(name);
            refreshProjectList();
        });
    }

    @FXML
    private void handleNewTask() {
        ProjectImpl selected = projectList.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        
        // Input for name, description and hours
        TextInputDialog nameDialog = new TextInputDialog();
        nameDialog.setTitle("New Task");
        nameDialog.setHeaderText("Task Name");
        
        nameDialog.showAndWait().ifPresent(name -> {
            TextInputDialog descDialog = new TextInputDialog();
            descDialog.setTitle("New Task");
            descDialog.setHeaderText("Description");
            
            descDialog.showAndWait().ifPresent(desc -> {
                TextInputDialog hoursDialog = new TextInputDialog("1");
                hoursDialog.setTitle("New Task");
                hoursDialog.setHeaderText("Estimated Hours");
                
                hoursDialog.showAndWait().ifPresent(hours -> {
                    try {
                        Duration duration = Duration.ofHours(Long.parseLong(hours));
                        model.addTaskToProject(selected, name, desc, duration);
                        showProjectDetails(selected);
                    } catch (NumberFormatException e) {
                        new Alert(Alert.AlertType.ERROR, "Invalid hours format").show();
                    }
                });
            });
        });
    }

    @FXML
    private void handleCompleteTask() {
        TaskImpl selectedTask = taskTable.getSelectionModel().getSelectedItem();
        if (selectedTask == null) return;
        
        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Complete Task");
        dialog.setHeaderText("Actual duration (hours)");
        
        dialog.showAndWait().ifPresent(hours -> {
            try {
                Duration duration = Duration.ofHours(Long.parseLong(hours));
                model.completeTask(selectedTask, duration);
                taskTable.refresh();
                showProjectDetails(projectList.getSelectionModel().getSelectedItem());
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.ERROR, "Invalid hours format").show();
            }
        });
    }

    @FXML
    private Button btnDeleteProject;

    @FXML
    private void handleDeleteProject() {
        ProjectImpl selected = projectList.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Project");
        alert.setHeaderText("Delete Project '" + selected.getName() + "'?");
        alert.setContentText("Are you sure you want to delete this project? This action cannot be undone.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                model.deleteProject(selected);
                refreshProjectList();
                // Clear details
                projectTitleLabel.setText("Select a project");
                taskTable.getItems().clear();
                btnCloseProject.setDisable(true);
                btnDeleteProject.setDisable(true);
            }
        });
    }

    @FXML
    private void handleCloseProject() {
        ProjectImpl selected = projectList.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        
        if (model.closeProject(selected)) {
            refreshProjectList();
            showProjectDetails(selected);
        } else {
            new Alert(Alert.AlertType.WARNING, "Cannot close project with pending tasks.").show();
        }
    }
}
