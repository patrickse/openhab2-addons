package org.openhab.binding.volumio2.internal.config;

public class VolumioConfig {

    private String hostname;

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    @Override
    public String toString() {
        return String.format("hostname: %s", this.getHostname());
    }

}
