package com.automation.utils;

import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.TestCaseFinished;
import io.cucumber.plugin.event.TestRunFinished;
import io.cucumber.plugin.event.Status;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

public class ExtentReportManager implements ConcurrentEventListener {

    private final AtomicInteger passed = new AtomicInteger();
    private final AtomicInteger failed = new AtomicInteger();
    private final AtomicInteger skipped = new AtomicInteger();
    private final AtomicInteger pending = new AtomicInteger();
    private final AtomicInteger undefined = new AtomicInteger();

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestCaseFinished.class, this::handleTestCaseFinished);
        publisher.registerHandlerFor(TestRunFinished.class, this::handleTestRunFinished);
    }

    private void handleTestCaseFinished(TestCaseFinished event) {
        Status status = event.getResult().getStatus();
        switch (status) {
            case PASSED:
                passed.incrementAndGet();
                break;
            case FAILED:
                failed.incrementAndGet();
                break;
            case SKIPPED:
                skipped.incrementAndGet();
                break;
            case PENDING:
                pending.incrementAndGet();
                break;
            case UNDEFINED:
                undefined.incrementAndGet();
                break;
            default:
                break;
        }
    }

    private void handleTestRunFinished(TestRunFinished event) {
        Properties props = new Properties();
        int total = passed.get() + failed.get() + skipped.get() + pending.get() + undefined.get();

        props.setProperty("PASSED", String.valueOf(passed.get()));
        props.setProperty("FAILED", String.valueOf(failed.get()));
        props.setProperty("SKIPPED", String.valueOf(skipped.get()));
        props.setProperty("TOTAL", String.valueOf(total));

        try (FileOutputStream out = new FileOutputStream("reports/summary.properties")) {
            props.store(out, "Test Execution Summary");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
