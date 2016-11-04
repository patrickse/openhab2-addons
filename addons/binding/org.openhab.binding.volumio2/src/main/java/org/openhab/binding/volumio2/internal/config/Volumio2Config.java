package org.openhab.binding.volumio2.internal.config;

/**
 * Volumio2 Configuration objects
 *
 *
 * @author Patrick Sernetz <patrick@sernetz.com>
 *
 */
public class Volumio2Config {

    private String hostname;

    /** GETTER/SETTER **/

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
