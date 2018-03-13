package com.ht.connected.home.backend.config;

import javax.servlet.Filter;


public class FilterWrapper {
	/**
     * Returns a wrapper around the specified filter.
     */
    static FilterWrapper wrap(Filter filter){
        return new FilterWrapper(filter);
    }

    private final Filter filter;

    FilterWrapper(Filter filter) {
        this.filter = filter;
    }

    /**
     * Un-wraps the filter.
     */
    Filter unwrap() {
        return filter;
    }
}
