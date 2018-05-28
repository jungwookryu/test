/*
 *******************************************************************************
 * Copyright (c) 2013 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.ht.connected.home.backend.category.zwave.constants.commandclass;

/**
 * Abstract base class for all Command classes
 *
 * @author Dan Noguerol
 */
abstract public class CommandClass {
    private Integer version; // always assume version 1 unless told otherwise

    /**
     * Returns the command class version
     *
     * @return the version
     */
    public int getVersion() {
        return (version != null) ? version : 1;
    }

    /**
     * Indicates whether the version has been set explicitly for this command class.
     *
     * @return a boolean
     */
    public boolean hasExplicitVersion() {
        return (version != null);
    }

    /**
     * Returns the highest supported command class version. This is determines whether the command class
     * version needs to be obtained as part of the node interview.
     *
     * @return the version
     */
    public int getMaxSupportedVersion() {
        return 1;
    }

    /**
     * Sets the command class version
     *
     * @param version the version
     */
    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * Returns the command class ID
     *
     * @return the ID as a byte
     */
    abstract public byte getId();

    /**
     * Returns the command class name. This is primarily for logging purposes.
     *
     * @return the name
     */
    abstract public String getName();

    @Override
    public String toString() {
        return "CommandClass{" +
                "version=" + version +
                '}';
    }
}
