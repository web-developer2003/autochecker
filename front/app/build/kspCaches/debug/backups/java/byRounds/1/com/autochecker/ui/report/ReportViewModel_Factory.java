package com.autochecker.ui.report;

import androidx.lifecycle.SavedStateHandle;
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
public final class ReportViewModel_Factory implements Factory<ReportViewModel> {
  private final Provider<VehicleRepository> vehicleRepositoryProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public ReportViewModel_Factory(Provider<VehicleRepository> vehicleRepositoryProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    this.vehicleRepositoryProvider = vehicleRepositoryProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public ReportViewModel get() {
    return newInstance(vehicleRepositoryProvider.get(), savedStateHandleProvider.get());
  }

  public static ReportViewModel_Factory create(
      Provider<VehicleRepository> vehicleRepositoryProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    return new ReportViewModel_Factory(vehicleRepositoryProvider, savedStateHandleProvider);
  }

  public static ReportViewModel newInstance(VehicleRepository vehicleRepository,
      SavedStateHandle savedStateHandle) {
    return new ReportViewModel(vehicleRepository, savedStateHandle);
  }
}
