
package Controller;

import javafx.beans.NamedArg;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PaneCustom extends Pane{
    
    private List<Label> labels;
    
    public PaneCustom(@NamedArg("numLabels") int numLabels) {
        labels = new ArrayList<>();
        for(int i = 1 ; i <= numLabels ; i++) {
            Label label = new Label("Label "+i);
            labels.add(label);
        }
        getChildren().addAll(labels);
    }
    
    public List<Label> getLabels() {
        return Collections.unmodifiableList(labels);
    }
}
