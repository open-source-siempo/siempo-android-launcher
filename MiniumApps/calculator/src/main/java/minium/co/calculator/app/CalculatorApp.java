package minium.co.calculator.app;

import org.androidannotations.annotations.EApplication;
import org.androidannotations.annotations.Trace;

import minium.co.core.BuildConfig;
import minium.co.core.app.CoreApplication;
import minium.co.core.log.LogConfig;
import minium.co.core.log.Tracer;

/**
 * Concrete implementation of {@link CoreApplication}
 * This class will implement specific behaviors that differs with core library
 * <p>
 * Created by shahab on 3/17/16.
 */
@EApplication
public class CalculatorApp extends CoreApplication {

    private final String TRACE_TAG = LogConfig.TRACE_TAG + "CalculatorApp";

    @Trace(tag = TRACE_TAG)
    @Override
    public void onCreate() {
        super.onCreate();

        Tracer.i("Application Id: " + minium.co.calculator.BuildConfig.APPLICATION_ID
                + " || Version code: " + minium.co.calculator.BuildConfig.VERSION_CODE
                + " || Version name: " + minium.co.calculator.BuildConfig.VERSION_NAME
                + " || Git Sha: " + BuildConfig.GIT_SHA
                + " || Build time:  " + BuildConfig.BUILD_TIME
                + " || Build flavor: " + minium.co.calculator.BuildConfig.FLAVOR
                + " || Build type: " + minium.co.calculator.BuildConfig.BUILD_TYPE);
    }
}