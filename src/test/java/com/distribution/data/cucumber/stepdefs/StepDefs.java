package com.distribution.data.cucumber.stepdefs;

import com.distribution.data.EmCloudDataApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = EmCloudDataApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
