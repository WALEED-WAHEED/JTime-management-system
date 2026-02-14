package it.unicam.cs.mpgc.jtime126294.controller;

import it.unicam.cs.mpgc.jtime126294.model.Task;
import it.unicam.cs.mpgc.jtime126294.model.impl.ProjectImpl;
import it.unicam.cs.mpgc.jtime126294.model.impl.TaskImpl;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportController extends BaseController {

    @FXML private RadioButton rbSingleProject;
    @FXML private RadioButton rbTimePeriod;
    
    @FXML private HBox projectControls;
    @FXML private HBox periodControls;
    
    @FXML private ComboBox<ProjectImpl> projectSelector;
    @FXML private DatePicker startDate;
    @FXML private DatePicker endDate;
    
    @FXML private VBox singleProjectView;
    @FXML private VBox timePeriodView;
    
    // Single Project Components
    @FXML private PieChart projectStatusChart;
    @FXML private Label lblTotalEstimated;
    @FXML private Label lblTotalActual;
    
    // Time Period Components
    @FXML private TableView<TaskImpl> scheduledTasksTable;
    @FXML private TableColumn<TaskImpl, String> colDate;
    @FXML private TableColumn<TaskImpl, String> colTask;
    @FXML private TableColumn<TaskImpl, String> colStatus;
    
    @FXML private BarChart<String, Number> completedTasksChart;

    @Override
    protected void onModelSet() {
        setupToggleGroup();
        setupProjectSelector();
        setupTable();
        
        startDate.setValue(LocalDate.now().minusWeeks(1));
        endDate.setValue(LocalDate.now());
        
        // Initial state
        updateVisibility();
        
        // Listeners for auto-update
        projectSelector.valueProperty().addListener((obs, oldVal, newVal) -> generateReport());
    }

    private void setupToggleGroup() {
        ToggleGroup group = new ToggleGroup();
        rbSingleProject.setToggleGroup(group);
        rbTimePeriod.setToggleGroup(group);
        
        group.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            updateVisibility();
            generateReport();
        });
    }
    
    private void setupProjectSelector() {
        projectSelector.setConverter(new StringConverter<ProjectImpl>() {
            @Override
            public String toString(ProjectImpl object) {
                return object == null ? "" : object.getName();
            }

            @Override
            public ProjectImpl fromString(String string) {
                return null;
            }
        });
        projectSelector.setItems(FXCollections.observableArrayList(model.getProjects()));
    }
    
    private void setupTable() {
        colDate.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                cell.getValue().getPlannedDate() != null ? cell.getValue().getPlannedDate().toString() : "Not Planned"));
        colTask.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getName()));
        colStatus.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getState().getName()));
    }

    private void updateVisibility() {
        boolean isSingle = rbSingleProject.isSelected();
        
        projectControls.setVisible(isSingle);
        projectControls.setManaged(isSingle);
        singleProjectView.setVisible(isSingle);
        singleProjectView.setManaged(isSingle);
        
        periodControls.setVisible(!isSingle);
        periodControls.setManaged(!isSingle);
        timePeriodView.setVisible(!isSingle);
        timePeriodView.setManaged(!isSingle);
    }

    @FXML
    private void generateReport() {
        if (rbSingleProject.isSelected()) {
            generateProjectReport();
        } else {
            generatePeriodReport();
        }
    }

    private void generateProjectReport() {
        ProjectImpl project = projectSelector.getValue();
        if (project == null) {
            projectStatusChart.getData().clear();
            lblTotalActual.setText("Total Actual: 0h");
            lblTotalEstimated.setText("Total Estimated: 0h");
            return;
        }

        // Pie Chart
        Map<String, Long> statusCounts = project.getTasks().stream()
                .collect(Collectors.groupingBy(t -> t.getState().getName(), Collectors.counting()));
        
        projectStatusChart.setData(FXCollections.observableArrayList(
                statusCounts.entrySet().stream()
                        .map(e -> new PieChart.Data(e.getKey(), e.getValue()))
                        .collect(Collectors.toList())
        ));

        // Time Stats
        long totalEst = project.getTasks().stream()
                .map(Task::getEstimatedTime)
                .mapToLong(Duration::toMinutes)
                .sum();
        
        long totalAct = project.getTasks().stream()
                .map(Task::getActualTime)
                .mapToLong(Duration::toMinutes)
                .sum();

        lblTotalEstimated.setText(String.format("Total Estimated: %dh %dm", totalEst / 60, totalEst % 60));
        lblTotalActual.setText(String.format("Total Actual: %dh %dm", totalAct / 60, totalAct % 60));
    }

    private void generatePeriodReport() {
        LocalDate start = startDate.getValue();
        LocalDate end = endDate.getValue();
        
        if (start == null || end == null) return;

        // Scheduled Tasks
        Collection<? extends Task> scheduled = model.getReportByInterval(start, end);
        scheduledTasksTable.setItems(FXCollections.observableArrayList(
                scheduled.stream()
                        .filter(t -> t instanceof TaskImpl)
                        .map(t -> (TaskImpl) t)
                        .collect(Collectors.toList())
        ));

        // Completed Tasks Chart
        Collection<? extends Task> completed = model.getCompletedTasks(start, end);
        
        Map<LocalDate, Long> completedByDate = completed.stream()
                .collect(Collectors.groupingBy(Task::getCompletionDate, Collectors.counting()));
        
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Completed Tasks");
        
        // Fill gaps if needed, but for simplicity just showing days with data
        // Sort by date
        completedByDate.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(e -> series.getData().add(new XYChart.Data<>(e.getKey().toString(), e.getValue())));
        
        completedTasksChart.getData().clear();
        completedTasksChart.getData().add(series);
    }
}
