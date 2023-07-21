/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.model.deprecated;

import static java.util.Optional.ofNullable;

import org.mule.runtime.api.meta.model.deprecated.DeprecationModel;

import java.util.Optional;

/**
 * Immutable implementation of {@link DeprecationModel}
 * 
 * @since 1.2
 */
public class ImmutableDeprecationModel implements DeprecationModel {

  private final String message;
  private final String deprecatedSince;
  private final String toRemoveIn;

  public ImmutableDeprecationModel(String message, String deprecatedSince, String toRemoveIn) {
    this.message = message;
    this.deprecatedSince = deprecatedSince;
    this.toRemoveIn = toRemoveIn;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getMessage() {
    return message;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDeprecatedSince() {
    return deprecatedSince;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<String> getToRemoveIn() {
    return ofNullable(toRemoveIn);
  }
}
