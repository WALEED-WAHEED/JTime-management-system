package it.unicam.cs.mpgc.jtime126294.controller;

import it.unicam.cs.mpgc.jtime126294.model.impl.TaskImpl;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.Duration;
import java.time.LocalDate;
import java.util.stream.Collectors;

public class PlanningController extends BaseController {

    @FXML
    private DatePicker datePicker;

    @FXML
    private ListView<TaskImpl> dayTasksList;

    @FXML
    private ListView<TaskImpl> unassignedTasksList;

    @FXML
    private Label totalEffortLabel;

    @Override
    protected void onModelSet() {
        datePicker.setValue(LocalDate.now());
        datePicker.valueProperty().addListener((obs, oldVal, newVal) -> refresh());
        refresh();
    }

    @FXML
    private void refresh() {
        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate == null) return;

        dayTasksList.setItems(FXCollections.observableArrayList(model.getTasksForDay(selectedDate)));
        
        // Find tasks not planned for any date
        unassignedTasksList.setItems(FXCollections.observableArrayList(
                model.getProjects().stream()
                        .flatMap(p -> p.getTasks().stream())
                        .filter(t -> t.getPlannedDate() == null)
                        .map(t -> (TaskImpl) t)
                        .collect(Collectors.toList())
        ));

        Duration total = model.getTotalEffortForDay(selectedDate);
        totalEffortLabel.setText(String.format("Total Effort: %dh %dm", total.toHours(), total.toMinutesPart()));
    }

    @FXML
    private void handleAssignTask() {
        TaskImpl selected = unassignedTasksList.getSelectionModel().getSelectedItem();
        LocalDate date = datePicker.getValue();
        if (selected != null && date != null) {
            model.planTask(selected, date);
            refresh();
        }
    }

    @FXML
    private void handleUnassignTask() {
        TaskImpl selected = dayTasksList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            model.planTask(selected, null);
            refresh();
        }
    }
}
