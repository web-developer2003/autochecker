package com.autochecker.data.repository.impl;

import android.content.Context;
import com.autochecker.core.network.ApiService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class RealVehicleRepository_Factory implements Factory<RealVehicleRepository> {
  private final Provider<ApiService> apiServiceProvider;

  private final Provider<Context> contextProvider;

  public RealVehicleRepository_Factory(Provider<ApiService> apiServiceProvider,
      Provider<Context> contextProvider) {
    this.apiServiceProvider = apiServiceProvider;
    this.contextProvider = contextProvider;
  }

  @Override
  public RealVehicleRepository get() {
    return newInstance(apiServiceProvider.get(), contextProvider.get());
  }

  public static RealVehicleRepository_Factory create(Provider<ApiService> apiServiceProvider,
      Provider<Context> contextProvider) {
    return new RealVehicleRepository_Factory(apiServiceProvider, contextProvider);
  }

  public static RealVehicleRepository newInstance(ApiService apiService, Context context) {
    return new RealVehicleRepository(apiService, context);
  }
}
