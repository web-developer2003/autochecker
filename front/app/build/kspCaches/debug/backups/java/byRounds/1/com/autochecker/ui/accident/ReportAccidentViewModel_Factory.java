package com.autochecker.ui.accident;

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
public final class ReportAccidentViewModel_Factory implements Factory<ReportAccidentViewModel> {
  private final Provider<VehicleRepository> vehicleRepositoryProvider;

  public ReportAccidentViewModel_Factory(Provider<VehicleRepository> vehicleRepositoryProvider) {
    this.vehicleRepositoryProvider = vehicleRepositoryProvider;
  }

  @Override
  public ReportAccidentViewModel get() {
    return newInstance(vehicleRepositoryProvider.get());
  }

  public static ReportAccidentViewModel_Factory create(
      Provider<VehicleRepository> vehicleRepositoryProvider) {
    return new ReportAccidentViewModel_Factory(vehicleRepositoryProvider);
  }

  public static ReportAccidentViewModel newInstance(VehicleRepository vehicleRepository) {
    return new ReportAccidentViewModel(vehicleRepository);
  }
}
