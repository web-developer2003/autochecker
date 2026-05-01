package com.autochecker.ui.history;

import com.autochecker.data.repository.VehicleRepository;
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
public final class HistoryViewModel_Factory implements Factory<HistoryViewModel> {
  private final Provider<VehicleRepository> vehicleRepositoryProvider;

  public HistoryViewModel_Factory(Provider<VehicleRepository> vehicleRepositoryProvider) {
    this.vehicleRepositoryProvider = vehicleRepositoryProvider;
  }

  @Override
  public HistoryViewModel get() {
    return newInstance(vehicleRepositoryProvider.get());
  }

  public static HistoryViewModel_Factory create(
      Provider<VehicleRepository> vehicleRepositoryProvider) {
    return new HistoryViewModel_Factory(vehicleRepositoryProvider);
  }

  public static HistoryViewModel newInstance(VehicleRepository vehicleRepository) {
    return new HistoryViewModel(vehicleRepository);
  }
}
