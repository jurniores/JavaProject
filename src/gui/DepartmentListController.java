/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import application.Main;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

/**
 *
 * @author Antonio
 */
public class DepartmentListController implements Initializable{
    
    private DepartmentService service;
    
    @FXML
    private TableView<Department> tableViewDepartments;
    @FXML
    private TableColumn<Department, Integer> tableColumnId;
    @FXML
    private TableColumn<Department, String> tableColumnName;
    @FXML
    private Button btNew;
    
    private ObservableList<Department> obsList;
    @FXML
    public void onBtNewAction(){
        System.out.println("onBtNewAction");
    }
    public void setDepartmentService(DepartmentService service){
        this.service = service;
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeNodes();
    }

    private void initializeNodes() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        Stage stage = (Stage)Main.getMainScene().getWindow();
        
        tableViewDepartments.prefHeightProperty().bind(stage.heightProperty());
    }
    public void updateTableView(){
        if (service == null){
            throw new IllegalStateException("Service was null");
        }
        List<Department> list = service.FindAll();
        
        
        obsList = FXCollections.observableArrayList(list);
        tableViewDepartments.setItems(obsList);
    }
    
}
