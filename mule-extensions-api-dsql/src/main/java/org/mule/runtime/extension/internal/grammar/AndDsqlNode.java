/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.grammar;

import org.antlr.runtime.Token;

/**
 * Represents the AND token in a Dsql Query.
 *
 * @since 1.0
 */
final class AndDsqlNode extends BaseDsqlNode {

  AndDsqlNode(Token t) {
    super(t);
  }

  @Override
  public void accept(DsqlGrammarVisitor visitor) {
    visitor.visit(this);
  }
}
