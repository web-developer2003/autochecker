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
public final class MockVehicleRepository_Factory implements Factory<MockVehicleRepository> {
  @Override
  public MockVehicleRepository get() {
    return newInstance();
  }

  public static MockVehicleRepository_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static MockVehicleRepository newInstance() {
    return new MockVehicleRepository();
  }

  private static final class InstanceHolder {
    private static final MockVehicleRepository_Factory INSTANCE = new MockVehicleRepository_Factory();
  }
}
