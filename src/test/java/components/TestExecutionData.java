package components;

import cucumber.api.Scenario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;


import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class TestExecutionData {

    @Autowired
    public Environment environment;

    public Scenario scenario;
    public String build;

    public String getBuild(){
        if(build == null)
        {
            String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
//build = environment.getActiveProfiles()[0].toUpperCase() + " " + timeStamp;
            build = timeStamp;
        }
        return build;
    }

    public String getScenarioName(){
        String featureAndScneanrioNames=scenario.getId();
        String[] featureAndScenarioSplit=featureAndScneanrioNames.split(";");
        String featureName=featureAndScenarioSplit[0];
        return featureName + "/" + scenario.getName();
    }
}
