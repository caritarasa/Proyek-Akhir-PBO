package model;

import java.util.ArrayList;
import java.util.List;

public class WhyReason {
    private List<String> whyLayers;
    private String finalMotivation;
    
    public WhyReason() {
        this.whyLayers = new ArrayList<>();
    }
    
    public void addWhy(String why) {
        whyLayers.add(why);
    }
    
    public List<String> getWhyLayers() {
        return whyLayers;
    }
    
    public void setFinalMotivation(String motivation) {
        this.finalMotivation = motivation;
    }
    
    public String getFinalMotivation() {
        if (finalMotivation != null && !finalMotivation.trim().isEmpty()) {
            return finalMotivation;
        }
        return whyLayers.isEmpty() ? "No motivation provided" : 
               String.join(" → ", whyLayers);
    }
}
