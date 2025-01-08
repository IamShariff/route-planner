package com.metro.routeplanner.model;

public enum RoutePreference {
	
	/**
     * Represents the admin role.
     */
    MINIMUM_INTERCHANGES("minimum_interchanges"),

    /**
     * Represents the user role.
     */
    MINIMUM_STATIONS("minimum_stations");

    private String preferenceType;

    private RoutePreference(String preferenceType) {
        this.preferenceType = preferenceType;
    }

    /**
     * Get the user type associated with the role.
     *
     * @return The user type.
     */
    public String getPreferenceType() {
        return this.preferenceType;
    }

}
