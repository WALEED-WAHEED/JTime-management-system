package it.unicam.cs.mpgc.jtime126294.controller;

import it.unicam.cs.mpgc.jtime126294.model.Task;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;

public class ReportController extends BaseController {

    @FXML
    private DatePicker startDate;

    @FXML
    private DatePicker endDate;

    @FXML
    private TreeView<String> reportTreeView;

    @Override
    protected void onModelSet() {
        startDate.setValue(LocalDate.now().minusWeeks(1));
        endDate.setValue(LocalDate.now());
        generateReport();
    }

    @FXML
    private void generateReport() {
        TreeItem<String> root = new TreeItem<>("Reports");
        root.setExpanded(true);

        // Report by Project
        TreeItem<String> projectRoot = new TreeItem<>("By Project");
        Map<String, Collection<? extends Task>> byProject = model.getReportByProject();
        for (Map.Entry<String, Collection<? extends Task>> entry : byProject.entrySet()) {
            TreeItem<String> pNode = new TreeItem<>(entry.getKey());
            for (Task t : entry.getValue()) {
                pNode.getChildren().add(new TreeItem<>(formatTask(t)));
            }
            projectRoot.getChildren().add(pNode);
        }
        root.getChildren().add(projectRoot);

        // Report by Interval
        TreeItem<String> intervalRoot = new TreeItem<>("By Interval");
        Collection<? extends Task> byInterval = model.getReportByInterval(startDate.getValue(), endDate.getValue());
        for (Task t : byInterval) {
            intervalRoot.getChildren().add(new TreeItem<>(formatTask(t)));
        }
        root.getChildren().add(intervalRoot);

        reportTreeView.setRoot(root);
    }

    private String formatTask(Task t) {
        return String.format("[%s] %s (Est: %s, Act: %s)", 
                t.getState().getName(), t.getDescription(), 
                formatDuration(t.getEstimatedTime()), formatDuration(t.getActualTime()));
    }

    private String formatDuration(java.time.Duration d) {
        return d.toHours() + "h " + d.toMinutesPart() + "m";
    }
}
