package org.burningokr.model.revision;

import org.hibernate.envers.RevisionType;

public class TypedRevisionEntry<T>
{
  public final T revision;
  public final RevisionInformation revisionInformation;
  public final RevisionType revisionType;

  public TypedRevisionEntry(T revision, RevisionInformation revisionInformation, RevisionType revisionType) {
    this.revision = revision;
    this.revisionInformation = revisionInformation;
    this.revisionType = revisionType;
  }
}
