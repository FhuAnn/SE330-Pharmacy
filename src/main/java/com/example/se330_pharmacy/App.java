package com.example.se330_pharmacy;

import com.example.se330_pharmacy.Models.Employee;
import com.example.se330_pharmacy.Models.Model;


import javafx.application.Application;


import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
<<<<<<< HEAD
//       Model.getInstance().getViewFactory().showLoginWindow();
=======
       //Model.getInstance().getViewFactory().showLoginWindow();
>>>>>>> 98015c16936249fb9e106daf1a55005f661f3f30
        Model.getInstance().getViewFactory().showMenuWindow(new Employee());
    }
}
