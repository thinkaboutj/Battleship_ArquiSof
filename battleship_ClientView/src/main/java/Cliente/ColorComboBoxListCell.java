/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Cliente;

import javafx.scene.control.ListCell;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author jesus
 */
public class ColorComboBoxListCell extends ListCell<String> {
    @Override
    protected void updateItem(String color, boolean empty) {
        super.updateItem(color, empty);
        
        if (empty || color == null) {
            setText(null);
            setGraphic(null);
        } else {
            Rectangle colorRect = new Rectangle(30, 20);
            
            switch(color.toLowerCase()) {
                case "azul":
                    colorRect.setFill(Color.BLUE);
                    break;
                case "rojo":
                    colorRect.setFill(Color.RED);
                    break;
            }
            
            colorRect.setStroke(Color.BLACK);
            colorRect.setStrokeWidth(1);
            
            setText(color);
            setGraphic(colorRect);
        }
    }
}
