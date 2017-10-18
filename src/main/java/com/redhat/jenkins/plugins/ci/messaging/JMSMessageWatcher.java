package com.redhat.jenkins.plugins.ci.messaging;

import com.redhat.jenkins.plugins.ci.messaging.checks.MsgCheck;
import hudson.model.TaskListener;
import org.jenkinsci.plugins.workflow.steps.EnvironmentExpander;

import java.util.List;

public abstract class JMSMessageWatcher {

    protected int timeout;
    protected MessagingProviderOverrides overrides;
    protected String selector;
    protected List<MsgCheck> checks;
    protected JMSMessagingProvider provider;
    protected EnvironmentExpander environmentExpander;
    protected TaskListener taskListener;

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setOverrides(MessagingProviderOverrides overrides) {
        this.overrides = overrides;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public void setChecks(List<MsgCheck> checks) {
        this.checks = checks;
    }

    public abstract String watch();

    public static String getTopic(MessagingProviderOverrides overrides, String providerTopic, String defaultTopic) {
        if (overrides != null && overrides.getTopic() != null && !overrides.getTopic().isEmpty()) {
            return overrides.getTopic();
        } else if (providerTopic != null && !providerTopic.isEmpty()) {
            return providerTopic;
        } else {
            return defaultTopic;
        }
    }

    public void setProvider(JMSMessagingProvider messagingProvider) {
        this.provider = messagingProvider;
    }

    public abstract void interrupt();

    public void setEnvironmentExpander(EnvironmentExpander environmentExpander) {
        this.environmentExpander = environmentExpander;
    }

    public void setTaskListener(TaskListener taskListener) {
        this.taskListener = taskListener;
    }
}