package org.benetech.servicenet.cucumber.stepdefs;

import org.benetech.servicenet.ServiceNetApp;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = ServiceNetApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
