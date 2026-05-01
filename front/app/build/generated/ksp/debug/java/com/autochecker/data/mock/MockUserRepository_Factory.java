package com.autochecker.data.mock;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class MockUserRepository_Factory implements Factory<MockUserRepository> {
  @Override
  public MockUserRepository get() {
    return newInstance();
  }

  public static MockUserRepository_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static MockUserRepository newInstance() {
    return new MockUserRepository();
  }

  private static final class InstanceHolder {
    private static final MockUserRepository_Factory INSTANCE = new MockUserRepository_Factory();
  }
}
