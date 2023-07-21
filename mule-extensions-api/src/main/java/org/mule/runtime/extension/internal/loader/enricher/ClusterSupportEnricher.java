/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.loader.enricher;

import static java.util.Optional.of;
import static org.mule.runtime.extension.api.loader.DeclarationEnricherPhase.STRUCTURE;
import static org.mule.runtime.extension.internal.loader.util.InfrastructureParameterBuilder.addPrimaryNodeParameter;

import org.mule.runtime.api.meta.model.declaration.fluent.SourceDeclaration;
import org.mule.runtime.api.meta.model.source.SourceModel;
import org.mule.runtime.extension.api.ExtensionConstants;
import org.mule.runtime.extension.api.loader.DeclarationEnricherPhase;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;
import org.mule.runtime.extension.api.loader.IdempotentDeclarationEnricherWalkDelegate;
import org.mule.runtime.extension.api.loader.WalkingDeclarationEnricher;
import org.mule.runtime.extension.api.property.SourceClusterSupportModelProperty;
import org.mule.sdk.api.annotation.source.SourceClusterSupport;

import java.util.Optional;

/**
 * Adds a #{@link ExtensionConstants#PRIMARY_NODE_ONLY_PARAMETER_NAME} parameter on all sources for which
 * {@link SourceModel#runsOnPrimaryNodeOnly()} is {@code false}
 *
 * @since 1.1
 */
public class ClusterSupportEnricher implements WalkingDeclarationEnricher {

  @Override
  public DeclarationEnricherPhase getExecutionPhase() {
    return STRUCTURE;
  }

  @Override
  public Optional<DeclarationEnricherWalkDelegate> getWalkDelegate(ExtensionLoadingContext extensionLoadingContext) {
    return of(new IdempotentDeclarationEnricherWalkDelegate() {

      @Override
      protected void onSource(SourceDeclaration declaration) {
        Optional<SourceClusterSupport> optionalSourceClusterSupport =
            declaration.getModelProperty(SourceClusterSupportModelProperty.class)
                .map(SourceClusterSupportModelProperty::getSourceClusterSupport);

        if (optionalSourceClusterSupport.isPresent()) {
          SourceClusterSupport sourceClusterSupport = optionalSourceClusterSupport.get();
          switch (sourceClusterSupport) {
            case DEFAULT_PRIMARY_NODE_ONLY:
              addPrimaryNodeParameter(declaration, true);
              declaration.setRunsOnPrimaryNodeOnly(false);
              break;
            case DEFAULT_ALL_NODES:
              addPrimaryNodeParameter(declaration, false);
              declaration.setRunsOnPrimaryNodeOnly(false);
              break;
            case NOT_SUPPORTED:
            default:
              declaration.setRunsOnPrimaryNodeOnly(true);
          }
        }
      }
    });
  }
}
