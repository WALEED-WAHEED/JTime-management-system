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

    @FXML
    private javafx.scene.chart.PieChart statusChart;

    @FXML
    private javafx.scene.chart.BarChart<String, Number> effortChart;

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

        // Clear charts
        statusChart.getData().clear();
        effortChart.getData().clear();

        // Data structures for charts
        Map<String, Integer> statusCount = new java.util.HashMap<>();
        javafx.scene.chart.XYChart.Series<String, Number> estSeries = new javafx.scene.chart.XYChart.Series<>();
        estSeries.setName("Estimated");
        javafx.scene.chart.XYChart.Series<String, Number> actSeries = new javafx.scene.chart.XYChart.Series<>();
        actSeries.setName("Actual");

        // Report by Project
        TreeItem<String> projectRoot = new TreeItem<>("By Project");
        Map<String, Collection<? extends Task>> byProject = model.getReportByProject();
        
        for (Map.Entry<String, Collection<? extends Task>> entry : byProject.entrySet()) {
            String projectName = entry.getKey();
            TreeItem<String> pNode = new TreeItem<>(projectName);
            
            double totalEst = 0;
            double totalAct = 0;

            for (Task t : entry.getValue()) {
                pNode.getChildren().add(new TreeItem<>(formatTask(t)));
                
                // Chart aggregation
                String status = t.getState().getName();
                statusCount.put(status, statusCount.getOrDefault(status, 0) + 1);
                
                totalEst += t.getEstimatedTime().toMinutes() / 60.0;
                totalAct += t.getActualTime().toMinutes() / 60.0;
            }
            projectRoot.getChildren().add(pNode);
            
            estSeries.getData().add(new javafx.scene.chart.XYChart.Data<>(projectName, totalEst));
            actSeries.getData().add(new javafx.scene.chart.XYChart.Data<>(projectName, totalAct));
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
        
        // Populate Charts
        statusCount.forEach((status, count) -> 
            statusChart.getData().add(new javafx.scene.chart.PieChart.Data(status, count)));
            
        effortChart.getData().addAll(estSeries, actSeries);
    }

    private String formatTask(Task t) {
        return String.format("[%s] %s (Est: %s, Act: %s)", 
                t.getState().getName(), t.getDescription(), 
                formatDuration(t.getEstimatedTime()), formatDuration(t.getActualTime()));
    }

    private String formatDuration(java.time.Duration d) {
        if (d == null) return "0h";
        return d.toHours() + "h " + d.toMinutesPart() + "m";
    }
}
