package com.ht.connected.home.backend.config;

import javax.servlet.Filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilterWrapper {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Returns a wrapper around the specified filter.
     */
    static FilterWrapper wrap(Filter filter) {
        return new FilterWrapper(filter);
    }

    private final Filter filter;

    FilterWrapper(Filter filter) {
        logger.debug("filter:::::::" + filter.toString());
        this.filter = filter;
    }

    /**
     * Un-wraps the filter.
     */
    Filter unwrap() {
        return filter;
    }
}
