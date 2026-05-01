package com.autochecker.data.mock;

import com.autochecker.data.local.DataStoreManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class MockSettingsRepository_Factory implements Factory<MockSettingsRepository> {
  private final Provider<DataStoreManager> dataStoreManagerProvider;

  public MockSettingsRepository_Factory(Provider<DataStoreManager> dataStoreManagerProvider) {
    this.dataStoreManagerProvider = dataStoreManagerProvider;
  }

  @Override
  public MockSettingsRepository get() {
    return newInstance(dataStoreManagerProvider.get());
  }

  public static MockSettingsRepository_Factory create(
      Provider<DataStoreManager> dataStoreManagerProvider) {
    return new MockSettingsRepository_Factory(dataStoreManagerProvider);
  }

  public static MockSettingsRepository newInstance(DataStoreManager dataStoreManager) {
    return new MockSettingsRepository(dataStoreManager);
  }
}
