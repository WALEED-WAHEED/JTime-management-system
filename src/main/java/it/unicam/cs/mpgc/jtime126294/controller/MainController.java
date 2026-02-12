package it.unicam.cs.mpgc.jtime126294.controller;

import it.unicam.cs.mpgc.jtime126294.model.impl.JTimeManager;
import it.unicam.cs.mpgc.jtime126294.persistence.PersistenceService;
import it.unicam.cs.mpgc.jtime126294.util.HibernateUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MainController {

    @FXML
    private BorderPane mainContainer;

    @FXML
    private VBox sidebar;

    private JTimeManager model;
    private PersistenceService persistenceService;

    @FXML
    public void initialize() {
        persistenceService = new PersistenceService();
        model = new JTimeManager(persistenceService.loadAll());
        
        // Show projects by default
        showProjects();
    }

    @FXML
    public void showProjects() {
        loadView("/view/projectView.fxml");
    }

    @FXML
    public void showPlanning() {
        loadView("/view/planningView.fxml");
    }

    @FXML
    public void showReports() {
        loadView("/view/reportView.fxml");
    }

    @FXML
    public void saveAndExit() {
        persistenceService.saveAll(model.getProjects());
        HibernateUtil.shutdown();
        System.exit(0);
    }

    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node node = loader.load();
            
            // Pass model to sub-controllers
            Object controller = loader.getController();
            if (controller instanceof BaseController) {
                ((BaseController) controller).setModel(model);
            }
            
            mainContainer.setCenter(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
